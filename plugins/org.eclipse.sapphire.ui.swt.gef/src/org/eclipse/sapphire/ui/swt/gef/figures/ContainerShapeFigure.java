/******************************************************************************
 * Copyright (c) 2013 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shenxue Zhou - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui.swt.gef.figures;

import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.ui.diagram.shape.def.SequenceLayoutDef;
import org.eclipse.sapphire.ui.diagram.shape.def.SequenceLayoutOrientation;
import org.eclipse.sapphire.ui.diagram.shape.def.ShapeLayoutDef;
import org.eclipse.sapphire.ui.swt.gef.DiagramConfigurationManager;
import org.eclipse.sapphire.ui.swt.gef.model.DiagramResourceCache;
import org.eclipse.sapphire.ui.swt.gef.presentation.ContainerShapePresentation;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 */

public class ContainerShapeFigure extends Shape 
{
	private ContainerShapePresentation containerShapePresentation;
	private DiagramResourceCache resourceCache;
	private DiagramConfigurationManager configManager;
	private Element model;
	private int validationMarkerIndex;
	private ShapeLayoutDef layout;
	
	public ContainerShapeFigure(ContainerShapePresentation containerShapePresentation, DiagramResourceCache resourceCache,
			DiagramConfigurationManager configManager)
	{
		this.containerShapePresentation = containerShapePresentation;
		this.resourceCache = resourceCache;
		this.configManager = configManager;
		this.validationMarkerIndex = this.containerShapePresentation.getValidationMarkerIndex();
		this.model = this.containerShapePresentation.getPart().getLocalModelElement();
		this.layout = this.containerShapePresentation.getLayout();
	}
	
	@Override
	protected void fillShape(Graphics graphics) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void outlineShape(Graphics graphics) 
	{
		// TODO Auto-generated method stub

	}

	public boolean showValidationMarker()
	{
		boolean show = false;
		if (this.validationMarkerIndex != -1)
		{
			Status status = this.model.validation();		
			show =  status.severity() != Status.Severity.OK;
		}
		return show;
	}
	
	public boolean isHorizontalSequenceLayout()
	{		
		if (this.layout instanceof SequenceLayoutDef)
		{
			SequenceLayoutDef sequenceLayout = (SequenceLayoutDef)layout;				
			if (sequenceLayout.getOrientation().content() == SequenceLayoutOrientation.HORIZONTAL)	
			{
				return true;
			}
		}
		return false;
		
	}
			
	public TextFigure getTextFigure()
	{
		List children = this.getChildren();
		TextFigure textFigure = null;
		for (Object figureObj : children)
		{
			if (figureObj instanceof TextFigure)
			{
				textFigure = (TextFigure)figureObj;
				break;
			}
		}
		return textFigure;
		
	}
	
}
