package com.telenav.kivakit.interfaces.numeric;

/**
 * Interface to an object that performs simple arithmetic.
 *
 * @author jonathanl (shibo)
 */
public interface Arithmetic<Value>
{
    Value dividedBy(Value value);

    Value minus(Value value);

    Value plus(Value value);

    Value times(Value value);
}
