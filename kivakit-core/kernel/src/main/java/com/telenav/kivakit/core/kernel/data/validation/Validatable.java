////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.validation;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitExcludeProperty;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * An object that is {@link Validatable} can create a {@link Validator} for a given kind of {@link Validation}.
 *
 * @author jonathanl (shibo)
 * @see Validator
 * @see Validation
 */
@UmlClassDiagram(diagram = DiagramDataValidation.class)
@UmlRelation(label = "how to validate", referent = Validation.class)
@UmlRelation(label = "provides", referent = Validator.class)
public interface Validatable
{
    /**
     * Determines if this object is valid by using the default validator
     *
     * @see Validator#validate()
     */
    @KivaKitExcludeProperty
    default boolean isValid()
    {
        return validator().validate();
    }

    /**
     * Determines if this object is valid by using the default validator
     *
     * @see Validator#validate(Listener)
     */
    default boolean isValid(final Listener listener)
    {
        return validator().validate(listener);
    }

    /**
     * @return A validator for full validation, if any. Although it cannot be final, this method should not be
     * overridden. Instead, override {@link #validator(Validation)}
     */
    default Validator validator()
    {
        return validator(Validation.VALIDATE_ALL);
    }

    /**
     * @param type The type of validation to perform
     * @return A new {@link Validator} instance
     */
    Validator validator(Validation type);
}
