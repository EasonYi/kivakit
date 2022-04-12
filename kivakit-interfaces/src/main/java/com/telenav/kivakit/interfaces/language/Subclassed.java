package com.telenav.kivakit.interfaces.language;

public interface Subclassed<Subclass extends Subclassed<Subclass>>
{
    @SuppressWarnings("unchecked")
    default Subclass subclass()
    {
        return (Subclass) this;
    }
}
