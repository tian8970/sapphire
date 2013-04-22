/******************************************************************************
 * Copyright (c) 2013 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.modeling;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.PropertyDef;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public abstract class BindingImpl
{
    private Element element;
    private PropertyDef property;

    public void init( final Element element,
                      final PropertyDef property,
                      final String[] params )
    {
        if( element == null )
        {
            throw new IllegalArgumentException();
        }
        
        if( property == null )
        {
            throw new IllegalArgumentException();
        }
        
        this.element = element;
        this.property = property;
    }
    
    public final Element element()
    {
        return this.element;
    }
    
    public PropertyDef property()
    {
        return this.property;
    }
    
    public void dispose()
    {
        // The default implementation doesn't do anything.
    }

}
