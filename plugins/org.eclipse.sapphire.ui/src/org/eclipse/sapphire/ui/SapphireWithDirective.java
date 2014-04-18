/******************************************************************************
 * Copyright (c) 2012 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui;

import static org.eclipse.sapphire.ui.SapphireWithDirectiveHelper.resolvePath;
import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.gd;
import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.gdhfill;
import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.gdhindent;
import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.gdhspan;
import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.gdvalign;
import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.glayout;
import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.glspacing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.eclipse.sapphire.Event;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.modeling.CapitalizationType;
import org.eclipse.sapphire.modeling.EditFailedException;
import org.eclipse.sapphire.modeling.ElementProperty;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementHandle;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.ModelPath;
import org.eclipse.sapphire.modeling.ModelPropertyChangeEvent;
import org.eclipse.sapphire.modeling.ModelPropertyListener;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.modeling.util.NLS;
import org.eclipse.sapphire.services.PossibleTypesService;
import org.eclipse.sapphire.ui.SapphireWithDirectiveHelper.ResolvePathResult;
import org.eclipse.sapphire.ui.assist.internal.PropertyEditorAssistDecorator;
import org.eclipse.sapphire.ui.def.ISapphireCompositeDef;
import org.eclipse.sapphire.ui.def.ISapphireLabelDef;
import org.eclipse.sapphire.ui.def.ISapphireUiDef;
import org.eclipse.sapphire.ui.def.ISapphireWithDirectiveDef;
import org.eclipse.sapphire.ui.internal.SapphireUiFrameworkPlugin;
import org.eclipse.sapphire.ui.internal.binding.RadioButtonsGroup;
import org.eclipse.sapphire.ui.swt.renderer.SapphireActionPresentationManager;
import org.eclipse.sapphire.ui.swt.renderer.SapphireKeyboardActionPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class SapphireWithDirective extends PageBookPart
{
    private ModelPath path;
    private IModelElement element;
    private ElementProperty property;
    private IModelElement elementForChildParts;
    private ModelPropertyListener listener;
    
    @Override
    protected void init()
    {
        final ISapphireWithDirectiveDef def = (ISapphireWithDirectiveDef) this.definition;
        final ResolvePathResult resolvePathResult = resolvePath( getModelElement(), def, this.params );
        
        if( resolvePathResult.property == null )
        {
            throw new IllegalStateException();
        }
        
        this.path = resolvePathResult.path;
        this.element = resolvePathResult.element;
        this.property = resolvePathResult.property;

        super.init();
        
        setExposePageValidationState( true );
        
        this.listener = new ModelPropertyListener()
        {
            @Override
            public void handlePropertyChangedEvent( final ModelPropertyChangeEvent event )
            {
                updateCurrentPage( false );
            }
        };
        
        this.element.addListener( this.listener, this.property.getName() );
        
        updateCurrentPage( true );
    }
    
    @Override
    protected ISapphireCompositeDef initDefaultPageDef()
    {
        final ISapphireUiDef root = ISapphireUiDef.TYPE.instantiate();
        final ISapphireCompositeDef composite = (ISapphireCompositeDef) root.getPartDefs().addNewElement( ISapphireCompositeDef.TYPE );
        final ISapphireLabelDef label = (ISapphireLabelDef) composite.getContent().addNewElement( ISapphireLabelDef.TYPE );
        label.setText( Resources.noAdditionalPropertiesMessage );
        
        return composite;
    }
    
    public ModelPath getPath()
    {
        return this.path;
    }
    
    @Override
    public IModelElement getLocalModelElement()
    {
        return this.element;
    }

    public ElementProperty getProperty()
    {
        return this.property;
    }

    @Override
    public void render( final SapphireRenderingContext context )
    {
        final ISapphireWithDirectiveDef def = (ISapphireWithDirectiveDef) this.definition;
        
        final Composite composite = new Composite( context.getComposite(), SWT.NONE );
        composite.setLayoutData( gdhspan( gdhfill(), 2 ) );
        composite.setLayout( glayout( 1, 0, 0 ) );
        context.adapt( composite );
        
        if( this.property != null )
        {
            final IModelElement element = getLocalModelElement();
            final ElementProperty property = getProperty();
            final PossibleTypesService possibleTypesService = element.service( property, PossibleTypesService.class );

            final Composite typeSelectorComposite = new Composite( composite, SWT.NONE );
            typeSelectorComposite.setLayoutData( gdhfill() );
            typeSelectorComposite.setLayout( glayout( 1, 0, 0 ) );
            context.adapt( typeSelectorComposite );
            
            final Runnable renderTypeSelectorOp = new Runnable()
            {
                public void run()
                {
                    for( Control control : typeSelectorComposite.getChildren() )
                    {
                        control.dispose();
                    }
                    
                    final Composite innerTypeSelectorComposite = new Composite( typeSelectorComposite, SWT.NONE );
                    innerTypeSelectorComposite.setLayoutData( gdvalign( gdhfill(), SWT.CENTER ) );
                    innerTypeSelectorComposite.setLayout( glspacing( glayout( 2, 0, 0 ), 2 ) );
                    context.adapt( innerTypeSelectorComposite );
                    
                    final SortedSet<ModelElementType> allPossibleTypes = possibleTypesService.types();
                    final int allPossibleTypesCount = allPossibleTypes.size();
                    final ModelPropertyListener modelPropertyListener;
                    
                    final Style defaultStyle;
                    
                    if( allPossibleTypesCount == 1 )
                    {
                        defaultStyle = Style.CHECKBOX;
                    }
                    else if( allPossibleTypesCount <= 3 )
                    {
                        defaultStyle = Style.RADIO_BUTTONS;
                    }
                    else
                    {
                        defaultStyle = Style.DROP_DOWN_LIST;
                    }
                    
                    Style style = Style.decode( def.getHint( ISapphireWithDirectiveDef.HINT_STYLE ) );
                    
                    if( style == null || ( style == Style.CHECKBOX && allPossibleTypesCount != 1 ) )
                    {
                        style = defaultStyle;
                    }
                    
                    final SapphireActionGroup actions = getActions( getMainActionContext() );
                    final SapphireActionPresentationManager actionPresentationManager = new SapphireActionPresentationManager( context, actions );
                    final SapphireKeyboardActionPresentation actionPresentationKeyboard = new SapphireKeyboardActionPresentation( actionPresentationManager );
                    
                    final PropertyEditorAssistDecorator decorator = new PropertyEditorAssistDecorator( SapphireWithDirective.this, element, property, context, innerTypeSelectorComposite );
                    decorator.control().setLayoutData( gdvalign( gd(), ( style == Style.DROP_DOWN_LIST ? SWT.TOP : SWT.CENTER ) ) );

                    if( style == Style.CHECKBOX )
                    {
                        final ModelElementType type = allPossibleTypes.first();
                        
                        String masterCheckBoxText = def.getLabel().getLocalizedText( CapitalizationType.FIRST_WORD_ONLY, true );
                        
                        if( masterCheckBoxText == null )
                        {
                            masterCheckBoxText = NLS.bind( Resources.enableElementLabel, type.getLabel( true, CapitalizationType.NO_CAPS, false ) ); 
                        }
                        
                        final Button masterCheckBox = new Button( innerTypeSelectorComposite, SWT.CHECK );
                        masterCheckBox.setLayoutData( gd() );
                        masterCheckBox.setText( masterCheckBoxText );
                        context.adapt( masterCheckBox );
                        decorator.addEditorControl( masterCheckBox );
                        actionPresentationKeyboard.attach( masterCheckBox );
                        context.setHelp( masterCheckBox, element, property );
            
                        modelPropertyListener = new ModelPropertyListener()
                        {
                            @Override
                            public void handlePropertyChangedEvent( final ModelPropertyChangeEvent event )
                            {
                                final IModelElement subModelElement = element.read( property ).element();
                                
                                masterCheckBox.setSelection( subModelElement != null );
                                masterCheckBox.setEnabled( element.isPropertyEnabled( property ) );
                            }
                        };
                        
                        masterCheckBox.addSelectionListener
                        (
                            new SelectionAdapter()
                            {
                                @Override
                                public void widgetSelected( final SelectionEvent event )
                                {
                                    try
                                    {
                                        final ModelElementHandle<?> handle = element.read( property );
                                        
                                        if( masterCheckBox.getSelection() == true )
                                        {
                                            handle.element( true );
                                        }
                                        else
                                        {
                                            handle.remove();
                                        }
                                    }
                                    catch( Exception e )
                                    {
                                        // Note that the EditFailedException is ignored here because the user has already
                                        // been notified and likely has taken action that led to the exception (such as
                                        // declining to make a file writable).
                                        
                                        final EditFailedException editFailedException = EditFailedException.findAsCause( e );
                                        
                                        if( editFailedException == null )
                                        {
                                            SapphireUiFrameworkPlugin.log( e );
                                        }
                                    }
                                }
                            }
                        );
                    }
                    else if( style == Style.RADIO_BUTTONS )
                    {
                        final RadioButtonsGroup radioButtonsGroup = new RadioButtonsGroup( context, innerTypeSelectorComposite, false );
                        radioButtonsGroup.setLayoutData( gdhfill() );
                        context.adapt( radioButtonsGroup );
                        
                        final Button noneButton = radioButtonsGroup.addRadioButton( Resources.noneSelection );
                        decorator.addEditorControl( noneButton );
                        actionPresentationKeyboard.attach( noneButton );
                        context.setHelp( noneButton, element, property );
                        
                        final Map<ModelElementType,Button> typeToButton = new HashMap<ModelElementType,Button>();
                        final Map<Button,ModelElementType> buttonToType = new HashMap<Button,ModelElementType>();
                        
                        for( ModelElementType type : allPossibleTypes )
                        {
                            final String label = type.getLabel( true, CapitalizationType.FIRST_WORD_ONLY, false );
                            final Button button = radioButtonsGroup.addRadioButton( label );
                            typeToButton.put( type, button );
                            buttonToType.put( button, type );
                            decorator.addEditorControl( button );
                            actionPresentationKeyboard.attach( button );
                            context.setHelp( button, element, property );
                        }
                        
                        modelPropertyListener = new ModelPropertyListener()
                        {
                            @Override
                            public void handlePropertyChangedEvent( final ModelPropertyChangeEvent event )
                            {
                                final IModelElement subModelElement = element.read( property ).element();
                                final Button button;
                                
                                if( subModelElement == null )
                                {
                                    button = noneButton;
                                }
                                else
                                {
                                    button = typeToButton.get( subModelElement.getModelElementType() );
                                }
                                
                                if( radioButtonsGroup.getSelection() != button )
                                {
                                    radioButtonsGroup.setSelection( button );
                                }
                                
                                radioButtonsGroup.setEnabled( element.isPropertyEnabled( property ) );
                            }
                        };
                        
                        radioButtonsGroup.addSelectionListener
                        (
                            new SelectionAdapter()
                            {
                                @Override
                                public void widgetSelected( final SelectionEvent event )
                                {
                                    try
                                    {
                                        final ModelElementHandle<?> handle = element.read( property );
                                        final Button button = radioButtonsGroup.getSelection();
                                        
                                        if( button == noneButton )
                                        {
                                            handle.remove();
                                        }
                                        else
                                        {
                                            final ModelElementType type = buttonToType.get( button );
                                            handle.element( true, type );
                                        }
                                    }
                                    catch( Exception e )
                                    {
                                        // Note that the EditFailedException is ignored here because the user has already
                                        // been notified and likely has taken action that led to the exception (such as
                                        // declining to make a file writable).
                                        
                                        final EditFailedException editFailedException = EditFailedException.findAsCause( e );
                                        
                                        if( editFailedException == null )
                                        {
                                            SapphireUiFrameworkPlugin.log( e );
                                        }
                                    }
                                }
                            }
                        );
                    }
                    else if( style == Style.DROP_DOWN_LIST )
                    {
                        final Combo combo = new Combo( innerTypeSelectorComposite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
                        combo.setLayoutData( gdhfill() );
                        context.adapt( combo );
                        decorator.addEditorControl( combo );
                        actionPresentationKeyboard.attach( combo );
                        context.setHelp( combo, element, property );
                        
                        combo.add( Resources.noneSelection );
                        
                        final Map<ModelElementType,Integer> typeToIndex = new HashMap<ModelElementType,Integer>();
                        final Map<Integer,ModelElementType> indexToType = new HashMap<Integer,ModelElementType>();
                        
                        int index = 1;
                        
                        for( ModelElementType type : allPossibleTypes )
                        {
                            final String label = type.getLabel( true, CapitalizationType.FIRST_WORD_ONLY, false );
                            combo.add( label );
                            typeToIndex.put( type, index );
                            indexToType.put( index, type );
                            
                            index++;
                        }
                        
                        modelPropertyListener = new ModelPropertyListener()
                        {
                            @Override
                            public void handlePropertyChangedEvent( final ModelPropertyChangeEvent event )
                            {
                                final IModelElement subModelElement = element.read( property ).element();
                                final int index;
                                
                                if( subModelElement == null )
                                {
                                    index = 0;
                                }
                                else
                                {
                                    index = typeToIndex.get( subModelElement.getModelElementType() );
                                }
                                
                                if( combo.getSelectionIndex() != index )
                                {
                                    combo.select( index );
                                }
                                
                                combo.setEnabled( element.isPropertyEnabled( property ) );
                            }
                        };
                        
                        combo.addSelectionListener
                        (
                            new SelectionAdapter()
                            {
                                @Override
                                public void widgetSelected( final SelectionEvent event )
                                {
                                    try
                                    {
                                        final ModelElementHandle<?> handle = element.read( property );
                                        final int index = combo.getSelectionIndex();
                                        
                                        if( index == 0 )
                                        {
                                            handle.remove();
                                        }
                                        else
                                        {
                                            final ModelElementType type = indexToType.get( index );
                                            handle.element( true, type );
                                        }
                                    }
                                    catch( Exception e )
                                    {
                                        // Note that the EditFailedException is ignored here because the user has already
                                        // been notified and likely has taken action that led to the exception (such as
                                        // declining to make a file writable).
                                        
                                        final EditFailedException editFailedException = EditFailedException.findAsCause( e );
                                        
                                        if( editFailedException == null )
                                        {
                                            SapphireUiFrameworkPlugin.log( e );
                                        }
                                    }
                                }
                            }
                        );
                    }
                    else
                    {
                        throw new IllegalStateException();
                    }
                    
                    actionPresentationKeyboard.render();
                    
                    modelPropertyListener.handlePropertyChangedEvent( null );
                    element.addListener( modelPropertyListener, property.getName() );
                    
                    typeSelectorComposite.layout( true, true );
                    
                    innerTypeSelectorComposite.addDisposeListener
                    (
                        new DisposeListener()
                        {
                            public void widgetDisposed( final DisposeEvent event )
                            {
                                element.removeListener( modelPropertyListener, property.getName() );
                                actionPresentationManager.dispose();
                                actionPresentationKeyboard.dispose();
                            }
                        }
                    );
                }
            };
            
            renderTypeSelectorOp.run();
            
            final Listener possibleTypesServiceListener = new Listener()
            {
                @Override
                public void handle( final Event event )
                {
                    renderTypeSelectorOp.run();
                }
            };
            
            possibleTypesService.attach( possibleTypesServiceListener );
            
            typeSelectorComposite.addDisposeListener
            (
                new DisposeListener()
                {
                    public void widgetDisposed( final DisposeEvent event )
                    {
                        possibleTypesService.detach( possibleTypesServiceListener );
                    }
                }
            );
        
            final Composite separatorComposite = new Composite( composite, SWT.NONE );
            separatorComposite.setLayoutData( gdhindent( gdhspan( gdhfill(), 2 ), 9 ) );
            separatorComposite.setLayout( glayout( 1, 0, 5 ) );
            context.adapt( separatorComposite );
            
            final Label separator = new Label( separatorComposite, SWT.SEPARATOR | SWT.HORIZONTAL );
            separator.setLayoutData( gdhfill() );
        }
        
        super.render( new SapphireRenderingContext( this, context, composite ) );
    }

    @Override
    protected Object parsePageKey( final String pageKeyString )
    {
        final ISapphireUiDef rootdef = this.definition.nearest( ISapphireUiDef.class );
        final Class<?> cl = rootdef.resolveClass( pageKeyString );
        return ClassBasedKey.create( cl );
    }
    
    @Override
    protected Status computeValidationState()
    {
        Status state = super.computeValidationState();
        
        if( this.property != null )
        {
            final Status.CompositeStatusFactory factory = Status.factoryForComposite();
            factory.merge( this.element.read( this.property ).validate( false ) );
            factory.merge( state );
            
            state = factory.create();
        }
        
        return state;
    }

    private void updateCurrentPage( final boolean force )
    {
        final IModelElement child = this.element.read( this.property ).element();
        
        if( force == true || this.elementForChildParts != child )
        {
            this.elementForChildParts = child;

            if( this.elementForChildParts == null )
            {
                changePage( this.element, null );
            }
            else
            {
                changePage( this.elementForChildParts, ClassBasedKey.create( this.elementForChildParts ) );
            }
        }
    }
    
    @Override
    public boolean setFocus( final ModelPath path )
    {
        if( this.path.isPrefixOf( path ) )
        {
            final ModelPath tail = path.makeRelativeTo( this.path );
            
            if( this.property == null || ( this.element.isPropertyEnabled( this.property ) && this.element.read( this.property ) != null ) )
            {
                return super.setFocus( tail );
            }
        }
        
        return false;
    }
    
    @Override
    public Set<String> getActionContexts()
    {
        return Collections.singleton( SapphireActionSystem.CONTEXT_WITH_DIRECTIVE );
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if( this.listener != null )
        {
            this.element.removeListener( this.listener, this.property.getName() );
        }
    }
    
    private enum Style
    {
        CHECKBOX( "checkbox" ),
        RADIO_BUTTONS( "radio.buttons" ),
        DROP_DOWN_LIST( "drop.down.list" );
        
        public static Style decode( final String text )
        {
            if( text != null )
            {
                for( Style style : Style.values() )
                {
                    if( style.text.equals( text ) )
                    {
                        return style;
                    }
                }
            }
            
            return null;
        }

        private final String text;
        
        private Style( final String text )
        {
            this.text = text;
        }
        
        @Override
        public String toString()
        {
            return this.text;
        }
    }

    private static final class Resources extends NLS
    {
        public static String noneSelection;
        public static String noAdditionalPropertiesMessage;
        public static String enableElementLabel; 
        
        static
        {
            initializeMessages( SapphireWithDirective.class.getName(), Resources.class );
        }
    }

}