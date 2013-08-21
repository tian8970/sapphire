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

package org.eclipse.sapphire.tests.modeling.misc.t0011;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.sapphire.modeling.xml.RootXmlResource;
import org.eclipse.sapphire.modeling.xml.XmlResourceStore;
import org.eclipse.sapphire.tests.SapphireTestCase;

/**
 * Tests the @DependsOn annotation where the dependencies are between model elements held in a list.
 * 
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class TestModelingMisc0011 extends SapphireTestCase
{
    private TestModelingMisc0011( final String name )
    {
        super( name );
    }
    
    public static Test suite()
    {
        final TestSuite suite = new TestSuite();
        
        suite.setName( "TestModelingMisc0011" );

        suite.addTest( new TestModelingMisc0011( "testFromEmptyModel" ) );
        suite.addTest( new TestModelingMisc0011( "testFromExistingModel" ) );
        
        return suite;
    }
    
    public void testFromEmptyModel() throws Exception
    {
        final TestElementRoot root = TestElementRoot.TYPE.instantiate();
        
        final TestElementChild x = root.getChildren().insert();
        x.setId( "x" );
        x.setContent( "123" );
        
        final TestElementChild y = root.getChildren().insert();
        y.setId( "y" );
        y.setReference( "x" );
        
        final TestElementChild z = root.getChildren().insert();
        z.setId( "z" );
        z.setReference( "y" );
        
        assertEquals( "123", y.getContent().text() );
        assertEquals( "123", z.getContent().text() );
        
        x.setContent( "456" );
        
        assertEquals( "456", y.getContent().text() );
        assertEquals( "456", z.getContent().text() );
    }

    public void testFromExistingModel() throws Exception
    {
        final RootXmlResource rootXmlResource = new RootXmlResource( new XmlResourceStore( loadResource( "TestData.xml" ) ) );
        final TestElementRoot root = TestElementRoot.TYPE.instantiate( rootXmlResource );
        
        final TestElementChild x = root.getChildren().get( 0 );
        final TestElementChild y = root.getChildren().get( 1 );
        final TestElementChild z = root.getChildren().get( 2 );
        
        assertEquals( "123", y.getContent().text() );
        assertEquals( "123", z.getContent().text() );
        
        x.setContent( "456" );
        
        assertEquals( "456", y.getContent().text() );
        assertEquals( "456", z.getContent().text() );
    }

}
