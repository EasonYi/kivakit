package com.telenav.kivakit.interfaces.language;

public interface SubClassed<SubClass extends SubClassed<SubClass>>
{
    @SuppressWarnings("unchecked")
    default SubClass subClass()
    {
        return (SubClass) this;
    }
}
