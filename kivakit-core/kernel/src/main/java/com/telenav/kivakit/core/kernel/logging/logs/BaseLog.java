////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.logs;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.core.kernel.language.collections.map.count.CountMap;
import com.telenav.kivakit.core.kernel.language.primitives.Booleans;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.Plural;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.threading.RepeatingThread;
import com.telenav.kivakit.core.kernel.language.threading.conditions.ValueWatcher;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.language.vm.KivaKitShutdownHook;
import com.telenav.kivakit.core.kernel.logging.Log;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.filters.LogEntriesWithSeverityGreaterThanOrEqualTo;
import com.telenav.kivakit.core.kernel.messaging.listeners.ConsoleWriter;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLoggingLogs;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.telenav.kivakit.core.kernel.language.vm.KivaKitShutdownHook.Order.LAST;

/**
 * Base class for log implementations. Handles background queueing of log entries.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
@UmlClassDiagram(diagram = DiagramLoggingLogs.class)
public abstract class BaseLog implements Startable, Stoppable, Log
{
    private static boolean isAsynchronous;

    private static final List<BaseLog> logs = new ArrayList<>();

    static
    {
        isAsynchronous = !Booleans.isTrue(System.getProperty("KIVAKIT_LOG_SYNCHRONOUS"));
    }

    public static void asynchronous(final boolean asynchronous)
    {
        isAsynchronous = asynchronous;
    }

    public static boolean isAsynchronous()
    {
        return isAsynchronous;
    }

    public static List<BaseLog> logs()
    {
        return logs;
    }

    private final ArrayBlockingQueue<LogEntry> queue = new ArrayBlockingQueue<>(queueSize());

    final ValueWatcher<Boolean> queueEmpty = new ValueWatcher<>(queue::isEmpty);

    @KivaKitIncludeProperty
    private final List<Filter<LogEntry>> filters = new ArrayList<>();

    private RepeatingThread thread;

    private volatile boolean closed;

    @KivaKitIncludeProperty
    private final AtomicBoolean started = new AtomicBoolean();

    private final CountMap<String> messageCounts = new CountMap<>();

    protected BaseLog()
    {
        logs.add(this);

        // If we are asynchronous,
        if (isAsynchronous())
        {
            // when the VM shuts down
            new KivaKitShutdownHook(LAST, () ->
            {
                // flush asynchronous entries for up to one minute
                stop(Duration.ONE_MINUTE);
            });
        }
    }

    public void addFilter(final Filter<LogEntry> filter)
    {
        filters.add(filter);
    }

    public void clear()
    {
    }

    /**
     * Closes the queue to new entries
     */
    @Override
    public void close()
    {
        closed = true;
    }

    public void closeOutput()
    {
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof BaseLog)
        {
            final var that = (BaseLog) object;
            return name().equals(that.name());
        }
        return false;
    }

    @Override
    public List<Filter<LogEntry>> filters()
    {
        return filters;
    }

    @Override
    public void flush(final Duration maximumWaitTime)
    {
        queueEmpty.waitForValue(true, maximumWaitTime, () -> thread.interrupt());
    }

    @Override
    public int hashCode()
    {
        return name().hashCode();
    }

    public boolean isClosed()
    {
        return closed;
    }

    @Override
    public boolean isRunning()
    {
        return thread != null && thread.isRunning();
    }

    @Override
    public void level(final Severity minimum)
    {
        if (minimum != null)
        {
            addFilter(new LogEntriesWithSeverityGreaterThanOrEqualTo(minimum));
        }
    }

    @Override
    public final void log(final LogEntry entry)
    {
        assert entry.context() != null;
        if (!closed && accept(entry))
        {
            JavaVirtualMachine.local().health().logEntry(entry);

            if (isAsynchronous())
            {
                if (!started.getAndSet(true))
                {
                    start();
                }
                try
                {
                    queue.put(entry);
                }
                catch (final InterruptedException ignored)
                {
                }
            }
            else
            {
                if (!dispatch(entry))
                {
                    onLogFailure(entry);
                }
            }
        }
    }

    /**
     * @return The number of times each type of message has been logged so far
     */
    public CountMap<String> messageCounts()
    {
        synchronized (messageCounts)
        {
            return new CountMap<>(messageCounts);
        }
    }

    @Override
    @KivaKitIncludeProperty
    public String name()
    {
        return Classes.simpleName(getClass());
    }

    @Override
    public final boolean start()
    {
        thread = new RepeatingThread(name() + "-Log")
        {
            @Override
            protected void onRun()
            {
                try
                {
                    final var entry = queue.take();
                    if (!dispatch(entry))
                    {
                        retry(entry);
                    }
                    checkForEmptyQueue();
                }
                catch (final InterruptedException ignored)
                {
                    checkForEmptyQueue();
                }
            }

            private void retry(final LogEntry entry)
            {
                // Try a few times to write the failed log entry
                var success = false;
                for (var i = 0; i < retries(); i++)
                {
                    if (dispatch(entry))
                    {
                        success = true;
                        break;
                    }
                }

                // If we are unable to write in several tries,
                if (!success)
                {
                    // log the entry as a failure
                    onLogFailure(entry);

                    // and then drain the rest of the queue as failures to
                    // prevent the queue from blocking
                    final List<LogEntry> failures = new ArrayList<>();
                    queue.drainTo(failures);
                    checkForEmptyQueue();
                    for (final var failure : failures)
                    {
                        onLogFailure(failure);
                    }
                }
            }

            {
                addListener(new ConsoleWriter());
            }
        };
        return thread.start();
    }

    @Override
    public void stop(final Duration wait)
    {
        close();
        flush(wait);
        if (thread != null)
        {
            thread.stop(wait);
        }
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    protected final boolean accept(final LogEntry entry)
    {
        for (final var filter : filters)
        {
            if (filter != null && !filter.accepts(entry))
            {
                return false;
            }
        }
        return true;
    }

    protected abstract void onLog(LogEntry entry);

    protected void onLogFailure(final LogEntry entry)
    {
        System.out.println("Failed to log: " + entry);
    }

    @SuppressWarnings("SameReturnValue")
    @KivaKitIncludeProperty
    protected int queueSize()
    {
        return 20_000;
    }

    @SuppressWarnings("SameReturnValue")
    @KivaKitIncludeProperty
    protected int retries()
    {
        return 3;
    }

    private void checkForEmptyQueue()
    {
        if (queue.isEmpty())
        {
            queueEmpty.valueChanged();
        }
    }

    private boolean dispatch(final LogEntry entry)
    {
        if (entry.severity().isGreaterThan(Severity.NONE))
        {
            synchronized (messageCounts)
            {
                messageCounts.increment(Plural.pluralize(entry.messageType()));
            }
        }
        var success = true;
        try
        {
            onLog(entry);
        }
        catch (final Exception e)
        {
            System.err.println(new Problem(e, "Failed to write log entry").asString());
            success = false;
        }
        return success;
    }
}
