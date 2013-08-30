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

package org.eclipse.sapphire.samples.contacts.internal;

import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.modeling.PropertyContentEvent;
import org.eclipse.sapphire.samples.contacts.Contact;
import org.eclipse.sapphire.samples.contacts.ContactAddress;
import org.eclipse.sapphire.samples.contacts.PhoneNumber;
import org.eclipse.sapphire.samples.contacts.SendContactOp;
import org.eclipse.sapphire.services.DerivedValueService;
import org.eclipse.sapphire.services.DerivedValueServiceData;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class SendContactOpMessageBodyDerivedValueService extends DerivedValueService
{
    @Override
    protected void initDerivedValueService()
    {
        final Listener listener = new FilteredListener<PropertyContentEvent>()
        {
            @Override
            protected void handleTypedEvent( final PropertyContentEvent event )
            {
                refresh();
            }
        };
        
        context( SendContactOp.class ).attach( listener, SendContactOp.PROP_CONTACT );
    }

    @Override
    protected DerivedValueServiceData compute()
    {
        final SendContactOp op = context( SendContactOp.class );
        final Contact contact = op.getContact().content();
        final StringBuilder buf = new StringBuilder();
        
        buf.append( "<html><body>\n" );
        
        if( contact != null )
        {
            buf.append( "<b>" );
            buf.append( contact.getName().getText() );
            buf.append( "</b>\n" );
            buf.append( "<br/><hr/>\n" );
            
            if( ! contact.getPhoneNumbers().isEmpty() )
            {
                buf.append( "<p><table>\n" );
                
                for( PhoneNumber phone : contact.getPhoneNumbers() )
                {
                    buf.append( "<tr><td><i>" );
                    buf.append( phone.getType().getText() );
                    buf.append( "</i></td><td>" );
                    
                    final String areaCode = phone.getAreaCode().getText();
                    final String localNumber = phone.getLocalNumber().getText();
                    
                    if( areaCode != null )
                    {
                        buf.append( '(' );
                        buf.append( areaCode );
                        buf.append( ") " );
                    }
                    
                    buf.append( localNumber );
                    
                    buf.append( "</td></tr>\n" );
                }
                
                buf.append( "</table></p>\n" );
            }
            
            final ContactAddress address = contact.getAddress();
            
            if( address != null && address.getStreet().getContent() != null )
            {
                buf.append( "<p>" );
                buf.append( address.getStreet().getText() );
                buf.append( "<br/>" );
                buf.append( address.getCity().getText() );
                buf.append( ", " );
                buf.append( address.getState().getText() );
                buf.append( ' ' );
                buf.append( address.getZipCode().getText() );
                buf.append( "</p>\n" );
            }
        }
        
        buf.append( "</body></html>" );
        
        return new DerivedValueServiceData( buf.toString() );
    }

}
