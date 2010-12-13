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

package org.eclipse.sapphire.modeling.serialization.internal;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.sapphire.modeling.serialization.ValueSerializerImpl;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class UrlSerializer

    extends ValueSerializerImpl<URL>
    
{
    @Override
    protected URL decodeFromString( final String value )
    {
        URL result = null;
        
        try
        {
            result = new URL( value );
        }
        catch( MalformedURLException e )
        {
            // Intentionally ignored. It is not the job of serializer to report these
            // problems. That's handled by validators.
        }
        
        return result;
    }
    
}
