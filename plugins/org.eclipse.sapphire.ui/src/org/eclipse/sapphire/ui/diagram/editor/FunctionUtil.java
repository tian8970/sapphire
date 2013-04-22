/******************************************************************************
 * Copyright (c) 2013 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shenxue Zhou - initial implementation and ongoing maintenance
 *    Konstantin Komissarchik - [348751] Diagram connection labels should check for derived and readonly properties
 ******************************************************************************/

package org.eclipse.sapphire.ui.diagram.editor;

import java.util.List;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.Property;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.modeling.el.FunctionResult;
import org.eclipse.sapphire.modeling.el.PropertyAccessFunction;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 */

public class FunctionUtil 
{
    public static Value<?> getFunctionProperty(Element element, FunctionResult functionResult)
    {
        if (functionResult.function() instanceof PropertyAccessFunction)
        {
            if (functionResult.operand(0) instanceof String)
            {
                final Property property = element.property( (String)functionResult.operand(0) );
                
                if( property != null && property instanceof Value && ! property.definition().isReadOnly() )
                {
                    return (Value<?>)property;
                }
            }
        }
        else 
        {
            List<FunctionResult> subFuncs = functionResult.operands();
            for (FunctionResult subFunc : subFuncs)
            {
                Value<?> property = getFunctionProperty(element, subFunc);
                
                if( property != null )
                {
                    return property;
                }
            }
        }
        return null;
    }

}
