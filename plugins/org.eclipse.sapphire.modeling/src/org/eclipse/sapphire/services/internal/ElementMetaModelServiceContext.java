/******************************************************************************
 * Copyright (c) 2016 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.services.internal;

import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.Sapphire;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class ElementMetaModelServiceContext extends ElementServiceContext
{
    public ElementMetaModelServiceContext( final ElementType elementMetaModel )
    {
        super( ID_ELEMENT_METAMODEL, Sapphire.services(), elementMetaModel, null, null );
    }
    
}
