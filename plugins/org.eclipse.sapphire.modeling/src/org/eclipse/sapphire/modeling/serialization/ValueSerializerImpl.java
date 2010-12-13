/******************************************************************************
 * Copyright (c) 2011 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.modeling.serialization;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ValueProperty;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public abstract class ValueSerializerImpl<T>
{
    private IModelElement element;
    private ValueProperty property;
    
    public void init( final IModelElement element,
                      final ValueProperty property,
                      final String[] params )
    {
        this.element = element;
        this.property = property;
    }
    
    public final IModelElement getModelElement()
    {
        return this.element;
    }
    
    public final ValueProperty getProperty()
    {
        return this.property;
    }

    public String encode( final T value )
    {
        String result = null;
        
        if( value != null )
        {
            result = value.toString();
        }
        
        return result;
    }
    
    public T decode( String value )
    {
        if( value != null )
        {
            return decodeFromString( value.trim() );
        }
        
        return null;
    }
    
    
    protected abstract T decodeFromString( String value );
}
