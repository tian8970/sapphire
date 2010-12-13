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

package org.eclipse.sapphire.ui.def;

import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.NumericRange;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.GenerateXmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@Label( standard = "spacer" )
@GenerateXmlBinding

public interface ISapphireSpacerDef

    extends ISapphirePartDef
    
{
    ModelElementType TYPE = new ModelElementType( ISapphireSpacerDef.class );
 
    // *** Size ***
    
    @Type( base = Integer.class )
    @Label( standard = "size" )
    @DefaultValue( "5" )
    @NumericRange( min = "1" )
    @XmlBinding( path = "size" )
    
    ValueProperty PROP_SIZE = new ValueProperty( TYPE, "Size" ); //$NON-NLS-1$
    
    Value<Integer> getSize();
    void setSize( String size );
    void setSize( Integer size );
    
}
