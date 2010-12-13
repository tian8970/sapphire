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

package org.eclipse.sapphire.ui.assist.internal;

import org.eclipse.core.runtime.IPath;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.EclipseWorkspacePath;
import org.eclipse.sapphire.ui.assist.BrowseHandler;
import org.eclipse.sapphire.ui.assist.BrowseHandlerFactory;
import org.eclipse.sapphire.ui.assist.EclipseWorkspacePathValueBrowseHandler;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class EclipseWorkspacePathValueBrowseHandlerFactory 

    extends BrowseHandlerFactory
    
{
    @Override
    public boolean isApplicable( final ValueProperty property )
    {
        if( property.isOfType( IPath.class ) && property.hasAnnotation( EclipseWorkspacePath.class ) )
        {
            return true;
        }
        
        return false;
    }

    @Override
    public BrowseHandler create()
    {
        return new EclipseWorkspacePathValueBrowseHandler();
    }
    

}