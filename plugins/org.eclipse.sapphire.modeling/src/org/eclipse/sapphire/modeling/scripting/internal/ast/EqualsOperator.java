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

package org.eclipse.sapphire.modeling.scripting.internal.ast;

import org.eclipse.sapphire.modeling.scripting.VariableResolver;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class EqualsOperator extends SyntaxTreeNode
{
    private final SyntaxTreeNode x;
    private final SyntaxTreeNode y;
    
    public EqualsOperator( final SyntaxTreeNode x,
                           final SyntaxTreeNode y )
    {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public Object execute( final VariableResolver variableResolver )
    {
        final String xstr = this.x.execute( variableResolver ).toString();
        final String ystr = this.y.execute( variableResolver ).toString();
        
        return xstr.equals( ystr );
    }
}
