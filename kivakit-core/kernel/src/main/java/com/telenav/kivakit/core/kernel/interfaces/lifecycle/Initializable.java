////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.lifecycle;

/**
 * Initialize this object
 *
 * @author jonathanl (shibo)
 */
public interface Initializable<T>
{
    /**
     * Initializes this object.
     */
    T initialize();
}
