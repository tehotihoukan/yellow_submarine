package com.itii.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


@SuppressWarnings("serial")
public class SurroundingSquare 
    extends Square
{

    private static final char startingLetter=  'A' - 1 ;
    private static final int  startingNumber=  0;
    
    
    private final String currentName;
    
    public SurroundingSquare ( final short pXCoordinate, final short pYCoordinate )
    {
        super( pXCoordinate, pYCoordinate );
        
        if ( pXCoordinate == 0 )
        {
            currentName=  Integer.toString( startingNumber + pYCoordinate );
        }
        else
        {
            currentName=  String.valueOf( (char)(startingLetter + pXCoordinate) );
        }
    }
    
    
    
    @Override
    public final void paintSquare ( Graphics pGraphics, int pSquareDimension )
    {
        
        final Graphics2D graphics=  (Graphics2D) pGraphics;

        final Color defaultBgColor=  graphics.getBackground();
        final Color defaultFgColor=  graphics.getColor();
        
        graphics.setBackground( Color.BLACK );
        graphics.setColor( Color.BLACK );
        graphics.fillRect( mCoordinates.getXIndex() * pSquareDimension, 
                           mCoordinates.getYIndex() * pSquareDimension,
                           pSquareDimension, 
                           pSquareDimension );

        graphics.setColor( Color.WHITE );
        graphics.drawString( currentName, 
                             mCoordinates.getXIndex() * pSquareDimension + pSquareDimension / 2, 
                             mCoordinates.getYIndex() * pSquareDimension + pSquareDimension - 3 );
        
        
        
        graphics.setColor( defaultFgColor );
        graphics.setBackground( defaultBgColor );
        
        
    }
    
}