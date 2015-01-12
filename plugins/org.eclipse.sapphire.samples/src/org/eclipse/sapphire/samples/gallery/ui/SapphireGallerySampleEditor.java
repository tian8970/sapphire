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

package org.eclipse.sapphire.samples.gallery.ui;

import org.eclipse.sapphire.modeling.IModel;
import org.eclipse.sapphire.modeling.ModelStore;
import org.eclipse.sapphire.modeling.xml.ModelStoreForXml;
import org.eclipse.sapphire.samples.gallery.GalleryFactory;
import org.eclipse.sapphire.ui.xml.SapphireEditorForXml;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class SapphireGallerySampleEditor

    extends SapphireEditorForXml
    
{
    public SapphireGallerySampleEditor()
    {
        super( "org.eclipse.sapphire.samples" );
        
        setEditorDefinitionPath( "org.eclipse.sapphire.samples/sdef/GalleryEditor.sdef/main" );
    }

    @Override
    protected IModel createModel( final ModelStore modelStore )
    {
        return GalleryFactory.load( (ModelStoreForXml) modelStore );
    }
    
}