/******************************************************************************
 * Copyright (c) 2012 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.tests.modeling.xml.xsd.t0005;

import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@GenerateImpl
@XmlBinding( path = "circle" )

public interface TestXmlXsd0005Circle extends TestXmlXsd0005Shape
{
    ModelElementType TYPE = new ModelElementType( TestXmlXsd0005Circle.class );
    
    // *** Radius ***
    
    @Type( base = Integer.class )
    @XmlBinding( path = "radius" )

    ValueProperty PROP_RADIUS = new ValueProperty( TYPE, "Radius" );
    
    Value<Integer> getRadius();
    void setRadius( String value );
    void setRadius( Integer value );
    
}
