////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem;

import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.core.filesystem.spi.FileService;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
public class FileList implements List<File>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    public static FileList forServices(final List<? extends FileService> virtualFiles)
    {
        final var files = new FileList();
        for (final FileService file : virtualFiles)
        {
            files.add(new File(file));
        }
        return files;
    }

    public static class Converter extends BaseStringConverter<FileList>
    {
        private final Extension extension;

        public Converter(final Listener listener, final Extension extension)
        {
            super(listener);
            this.extension = extension;
        }

        @Override
        protected FileList onConvertToObject(final String value)
        {
            final var files = new FileList();
            for (final var path : value.split(","))
            {
                final var file = File.parse(path);
                if (file.isFolder())
                {
                    files.addAll(file.asFolder().nestedFiles(extension.matcher()));
                }
                else
                {
                    if (file.fileName().endsWith(extension))
                    {
                        files.add(file);
                    }
                    else
                    {
                        LOGGER.warning("$ is not a $ file", file, extension);
                    }
                }
            }
            return files;
        }
    }

    @UmlAggregation
    private final ObjectList<File> files = new ObjectList<>();

    public FileList()
    {
    }

    FileList(final FileList that)
    {
        files.addAll(that.files);
    }

    @Override
    public boolean add(final File file)
    {
        return files.add(file);
    }

    @Override
    public void add(final int index, final File file)
    {
        files.add(index, file);
    }

    @Override
    public boolean addAll(@NotNull final Collection<? extends File> c)
    {
        return files.addAll(c);
    }

    @Override
    public boolean addAll(final int index, @NotNull final Collection<? extends File> c)
    {
        return files.addAll(index, c);
    }

    @UmlExcludeMember
    public List<java.io.File> asJavaFiles()
    {
        final var files = new ArrayList<java.io.File>();
        forEach(file -> files.add(file.asJavaFile()));
        return files;
    }

    public Set<File> asSet()
    {
        return new HashSet<>(files);
    }

    @Override
    public void clear()
    {
        files.clear();
    }

    @Override
    public boolean contains(final Object o)
    {
        return false;
    }

    @Override
    public boolean containsAll(@NotNull final Collection<?> c)
    {
        return files.containsAll(c);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof FileList)
        {
            final FileList that = (FileList) object;
            return files.equals(that.files);
        }
        return false;
    }

    public File first()
    {
        return files.first();
    }

    @Override
    public File get(final int index)
    {
        return files.get(index);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(files);
    }

    @Override
    public int indexOf(final Object o)
    {
        return files.indexOf(o);
    }

    @Override
    public boolean isEmpty()
    {
        return files.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<File> iterator()
    {
        return files.iterator();
    }

    public File largest()
    {
        File largest = null;
        for (final var file : this)
        {
            if (largest == null || file.isLargerThan(largest))
            {
                largest = file;
            }
        }
        return largest;
    }

    @Override
    public int lastIndexOf(final Object o)
    {
        return files.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<File> listIterator()
    {
        return files.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<File> listIterator(final int index)
    {
        return files.listIterator(index);
    }

    public FileList matching(final Matcher<File> matcher)
    {
        final var files = new FileList();
        for (final var file : this)
        {
            if (matcher.matches(file))
            {
                files.add(file);
            }
        }
        return files;
    }

    @Override
    public boolean remove(final Object o)
    {
        return files.remove(o);
    }

    @Override
    public File remove(final int index)
    {
        return files.remove(index);
    }

    @Override
    public boolean removeAll(@NotNull final Collection<?> c)
    {
        return files.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull final Collection<?> c)
    {
        return files.retainAll(c);
    }

    @Override
    public File set(final int index, final File file)
    {
        return files.set(index, file);
    }

    @Override
    public int size()
    {
        return files.size();
    }

    public File smallest()
    {
        File smallest = null;
        for (final var file : this)
        {
            if (smallest == null || file.isSmallerThan(smallest))
            {
                smallest = file;
            }
        }
        return smallest;
    }

    public FileList sortedLargestToSmallest()
    {
        final var sorted = new FileList(this);
        sorted.files.sort((a, b) ->
        {
            if (a.isLargerThan(b))
            {
                return -1;
            }
            if (b.isLargerThan(a))
            {
                return 1;
            }
            return 0;
        });
        return sorted;
    }

    @SuppressWarnings("UnusedReturnValue")
    public FileList sortedOldestToNewest()
    {
        final var sorted = new FileList(this);
        sorted.files.sort((a, b) ->
        {
            if (a.isOlderThan(b))
            {
                return -1;
            }
            if (b.isOlderThan(a))
            {
                return 1;
            }
            return 0;
        });
        return sorted;
    }

    @NotNull
    @Override
    public List<File> subList(final int fromIndex, final int toIndex)
    {
        return files.subList(fromIndex, toIndex);
    }

    @NotNull
    @Override
    public Object[] toArray()
    {
        return Ensure.unsupported();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull final T[] a)
    {
        return Ensure.unsupported();
    }

    public Bytes totalSize()
    {
        var bytes = Bytes._0;
        for (final var file : this)
        {
            bytes = bytes.add(file.bytes());
        }
        return bytes;
    }
}
