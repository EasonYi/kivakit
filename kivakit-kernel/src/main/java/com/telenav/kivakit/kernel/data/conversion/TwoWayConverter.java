package com.telenav.kivakit.kernel.data.conversion;

import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataConversion;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * The superinterface, {@link Converter}, converts values from type &lt;From&gt; to type &lt;To&gt; while this interface
 * adds the ability to convert in the reverse direction from type &lt;To&gt; to type &lt;From&gt;.
 *
 * @param <From> The original type
 * @param <To> The desired type
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversion.class)
public interface TwoWayConverter<From, To> extends Converter<From, To>
{
    /**
     * Converts from the destination type back to the original type
     */
    From unconvert(To to);

    /**
     * @return A converter that converts from type &lt;To&gt; back to type &lt;From&gt;.
     */
    default Converter<To, From> unconverter(Listener listener)
    {
        return new BaseConverter<>(listener)
        {
            @Override
            protected From onConvert(To to)
            {
                return unconvert(to);
            }
        };
    }
}
