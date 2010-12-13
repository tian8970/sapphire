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

package org.eclipse.sapphire.ui.assist;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public abstract class PropertyEditorAssistContributor
{
    public static final String ID_INFO_SECTION_CONTRIBUTOR = "System.InfoSectionContributor";
    public static final String ID_DEFAULT_VALUE_CONTRIBUTOR = "System.DefaultValueContributor";
    public static final String ID_MIN_MAX_INFO_CONTRIBUTOR = "System.MinMaxInfoContributor";
    public static final String ID_PROBLEMS_SECTION_CONTRIBUTOR = "System.ProblemsSectionContributor";
    public static final String ID_PROBLEMS_CONTRIBUTOR = "System.ProblemsContributor";
    public static final String ID_ACTIONS_SECTION_CONTRIBUTOR = "System.ActionsSectionContributor";
    public static final String ID_RESET_ACTIONS_CONTRIBUTOR = "System.ResetActionsContributor";
    public static final String ID_SHOW_IN_SOURCE_ACTION_CONTRIBUTOR = "System.ShowInSourceActionContributor";

    public static final int PRIORITY_INFO_SECTION_CONTRIBUTOR = 100;
    public static final int PRIORITY_DEFAULT_VALUE_CONTRIBUTOR = 110;
    public static final int PRIORITY_MIN_MAX_INFO_CONTRIBUTOR = 115;
    public static final int PRIORITY_PROBLEMS_SECTION_CONTRIBUTOR = 200;
    public static final int PRIORITY_PROBLEMS_CONTRIBUTOR = 210;
    public static final int PRIORITY_ACTIONS_SECTION_CONTRIBUTOR = 300;
    public static final int PRIORITY_RESET_ACTIONS_CONTRIBUTOR = 310;
    public static final int PRIORITY_SHOW_IN_SOURCE_ACTION_CONTRIBUTOR = 320;
    
    public static final String SECTION_ID_INFO = "info";
    public static final String SECTION_ID_PROBLEMS = "problems";
    public static final String SECTION_ID_ACTIONS = "actions";

    private String id = getClass().getName();
    private int priority = 1000;
    
    public String getId()
    {
        return this.id;
    }
    
    public void setId( final String id )
    {
        this.id = id;
    }
    
    public int getPriority()
    {
        return this.priority;
    }
    
    public void setPriority( final int priority )
    {
        this.priority = priority;
    }
    
    public abstract void contribute( PropertyEditorAssistContext context );
    
    protected static String escapeForXml( final String string )
    {
        final StringBuilder result = new StringBuilder();
        
        for( int i = 0, n = string.length(); i < n; i++ )
        {
            final char ch = string.charAt( i );
            
            if( ch == '<' )
            {
                result.append( "&lt;" );
            }
            else if( ch == '>' )
            {
                result.append( "&gt;" );
            }
            else if( ch == '&' )
            {
                result.append( "&amp;" );
            }
            else if( ch == '"' )
            {
                result.append( "&quot;" );
            }
            else if( ch == '\'' )
            {
                result.append( "&apos;" );
            }
            else
            {
                result.append( ch );
            }
        }
        
        return result.toString();
    }
    
}
