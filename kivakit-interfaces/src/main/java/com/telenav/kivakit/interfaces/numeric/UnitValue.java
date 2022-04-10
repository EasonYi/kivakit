package com.telenav.kivakit.interfaces.numeric;

public interface UnitValue
{
    /**
     * Returns a unit value scaled to the range 0 to 1, inclusive. The unit value may exceed one, as in the case of the
     * unit value for 110%, or it may be less than zero, as in -110%.
     */
    double unitValue();
}
