package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.settings.stores.resource.FolderSettingsStore;
import com.telenav.kivakit.configuration.settings.stores.resource.PackageSettingsStore;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.threading.locks.ReadWriteLock;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.configuration.settings.SettingsStore.Access.ADD;
import static com.telenav.kivakit.configuration.settings.SettingsStore.Access.CLEAR;
import static com.telenav.kivakit.configuration.settings.SettingsStore.Access.LOAD;
import static com.telenav.kivakit.configuration.settings.SettingsStore.Access.SAVE;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * <b>Service Provider API</b>
 *
 * <p>
 * Base class for {@link SettingsStore} provider implementations.
 * </p>
 *
 * <p><b>Provided Stores</b></p>
 *
 * <ul>
 *     <li>{@link Deployment} - Loads settings for a particular application or server deployment</li>
 *     <li>{@link FolderSettingsStore} - Loads settings from <i>.properties</i> files</li>
 *     <li>{@link PackageSettingsStore} - Loads settings from <i>.properties</i> package resources</li>
 * </ul>
 *
 * <p>
 * Deployments are the most commonly used settings store and typically they should be all that is required by most
 * applications. See {@link Deployment} for detailed usage examples.
 * </p>
 *
 * <p><b>Providers</b></p>
 *
 * <p>
 * {@link SettingsStore} providers inherit several useful methods and implementations from this base class:
 * <ul>
 *     <li>{@link #iterator()} - Iterates through each settings {@link Object} in this store</li>
 *     <li>{@link #add(SettingsObject)} - Adds the given object to the store's in-memory index (but not to any persistent storage)</li>
 *     <li>{@link #clear()} - Clears this store's in-memory index</li>
 *     <li>{@link #load()} - Lazy-loads objects from persistent storage by calling {@link #onLoad()} and then adds them to the in-memory index</li>
 *     <li>{@link #lookup(SettingsObject.Identifier)} - Looks up the object for the given identifier in the store's index</li>
 *     <li>{@link #save(SettingsObject)} - Saves the given settings object to persistent storage by calling {@link #onSave(SettingsObject)}</li>
 * </ul>
 * <p>
 * Providers must override these methods (although some may be unsupported methods):
 *
 * <ul>
 *     <li>{@link #access()} - Specifies the kinds of access that the store supports</li>
 *     <li>{@link #onLoad()} - Loads settings objects from persistent storage</li>
 *     <li>{@link #onSave(SettingsObject)} - Saves the settings object to persistent storage</li>
 * </ul>
 * </p>
 *
 * @author jonathanl (shibo)
 * @see SettingsStore
 * @see Deployment
 * @see FolderSettingsStore
 * @see PackageSettingsStore
 */
public abstract class BaseSettingsStore extends BaseRepeater implements SettingsStore
{
    /** Map to get settings entries by identifier */
    private final Map<SettingsObject.Identifier, SettingsObject> objects = new HashMap<>();

    /** Lock for accessing settings entries */
    private final ReadWriteLock lock = new ReadWriteLock();

    /** True if settings have been loaded into this store */
    private boolean loaded;

    /**
     * Adds the given settings object to the in-memory index for this store. The object (from {@link
     * SettingsObject#object()}) is indexed under its class and all implemented interfaces. It is also indexed under all
     * superclasses and superinterfaces.
     */
    @Override
    @UmlExcludeMember
    public boolean add(SettingsObject object)
    {
        ensure(supports(ADD));
        ensureNotNull(object);

        // Obtain a write lock,
        return lock.write(() ->
        {
            // add the object to the global lookup registry
            Registry.of(this).register(object.object(), object.identifier().instance());

            // then walk up the class hierarchy of the object,
            var instance = object.identifier().instance();
            for (var at = (Class<?>) object.object().getClass(); !at.equals(Object.class); at = at.getSuperclass())
            {
                // add the interfaces of the object,
                for (var in : at.getInterfaces())
                {
                    internalAdd(new SettingsObject(in, instance, object));
                }

                // and the class itself.
                internalAdd(new SettingsObject(at, instance, object));
            }
            return true;
        });
    }

    /**
     * Gets a <b>copy</b> of the {@link SettingsObject}s in this store, loading them if need be
     */
    @Override
    public Set<SettingsObject> all()
    {
        maybeLoad();
        return lock.write(() -> new HashSet<>(objects.values()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clear()
    {
        ensure(supports(CLEAR));
        lock.write(objects::clear);
        return true;
    }

    @NotNull
    @Override
    public Iterator<Object> iterator()
    {
        maybeLoad();
        return all()
                .stream()
                .map(SettingsObject::object)
                .iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public final synchronized Set<SettingsObject> load()
    {
        ensure(supports(LOAD));

        // If we are the first thread to call load(),
        if (!loaded)
        {
            // load settings by calling the subclass.
            trace("Loading settings from $", name());
            lock.write(() -> onLoad().forEach(this::add));
            loaded = true;
        }

        return lock.read(() -> new HashSet<>(objects.values()));
    }

    /**
     * Looks up the settings object for the given identifier. First looks in the global object {@link Registry}, then
     * looks at the objects in this store's index.
     *
     * @return Any settings object for the given identifier
     */
    @SuppressWarnings("unchecked")
    public <T> T lookup(SettingsObject.Identifier identifier)
    {
        maybeLoad();
        return lock.read(() ->
        {
            // First try the global object registry,
            T settings = (T) Registry.of(this).lookup(identifier.type(), identifier.instance());
            if (settings == null)
            {
                // and try the index.
                var object = objects.get(identifier);
                if (object != null)
                {
                    settings = object.object();
                }
            }
            return settings;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(SettingsObject object)
    {
        ensure(supports(SAVE));
        add(object);
        return onSave(object);
    }

    @Override
    public String toString()
    {
        return new StringList(all()).titledBox(name());
    }

    /**
     * @return All settings objects in this store
     */
    protected abstract Set<SettingsObject> onLoad();

    /**
     * @return True if the given object was saved to persistent storage
     */
    protected abstract boolean onSave(SettingsObject object);

    private void internalAdd(SettingsObject settings)
    {
        lock.write(() -> objects.put(settings.identifier(), settings));
    }

    private void maybeLoad()
    {
        if (supports(LOAD))
        {
            load();
        }
    }
}
