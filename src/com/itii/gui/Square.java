package com.itii.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.itii.data.Coordinates;
import com.itii.data.State.StateEnum;

@SuppressWarnings("serial")
public class Square 
    extends JComponent
{
    protected final Coordinates mCoordinates;
    
    private StateEnum mTemporarySquareState;
    
    private       StateEnum mSquareState= StateEnum.EMPTY;

    // Default size of a square given in Pixels
    public static final int DEFAULT_SIZE=  20;
    
    public Square ( final short pXCoordinate,  final short pYCoordinate )
    {
        mCoordinates=  new Coordinates( pXCoordinate, pYCoordinate );
        
        setMinimumSize( new Dimension( 20, 20 ) );
        setPreferredSize( new Dimension(20, 20) );
    }
    
    public final void freeTemporaryState()
    {
        mTemporarySquareState=  null;
    }
    

    public final void setTemporaryState( final StateEnum pTemporarySquareState )
    {
        mTemporarySquareState=  pTemporarySquareState;
    }
    
    public Coordinates getCoordinates()
    {
        return mCoordinates;
    }
    
    
    /**
     * Update this square state depending on its current state
     * @param pSquareState
     */
    public final void updateState( final StateEnum pSquareState )
    {
        if (   pSquareState == StateEnum.HIT
            && getState() == StateEnum.BOAT )
        {
            setState( StateEnum.BOAT_HIT );
        }
        else 
        {
            setState( StateEnum.MISSED );
        }
    }
    
    @Override
    public void paint ( Graphics pGraphics )
    {
        System.out.println("paint square : X: " + this.getCoordinates().getXIndex() 
                + "  Y : "  + this.getCoordinates().getYIndex() );
        Color c=  pGraphics.getColor();

        switch ( getState() )
        {
        case EMPTY :
            pGraphics.setColor( ColorSet.SQUARE_BG );
            pGraphics.fillRect( 0, 0, 
                                getWidth(), 
                                getHeight() );
            
            pGraphics.setColor( ColorSet.SQUARE_EDGE );
            pGraphics.drawRect( 0, 0, 
                    getWidth(), 
                    getHeight() );
            break;
    
        case PLACING_BOAT :
            pGraphics.setColor( ColorSet.SQUARE_PLACING_BOAT_BG );
            pGraphics.fillRect( 0, 0, 
                    getWidth(), 
                    getHeight() );
            break;
        case BOAT :
            pGraphics.setColor( ColorSet.SQUARE_BOAT_BG );
            pGraphics.fillRect( 0, 0, 
                    getWidth(), 
                    getHeight() );
            break;
            
        case BOAT_HIT :
            pGraphics.setColor( ColorSet.SQUARE_BOAT_HIT );
            pGraphics.fillRect( 0, 0, 
                    getWidth(), 
                    getHeight() );
            break;
            
        case BOAT_SUNK :
            pGraphics.setColor( ColorSet.SQUARE_BOAT_SUNK );
            pGraphics.fillRect( 0, 0, 
                    getWidth(), 
                    getHeight() );
            break;
            
        case HIT : 
            pGraphics.setColor( ColorSet.HIT );
            pGraphics.fillRect( 0, 0, 
                    getWidth() -1 , 
                    getHeight() -1 );
            break;
            
        case MISSED :
            pGraphics.setColor( ColorSet.MISSED );
            pGraphics.fillRect( 0, 0, 
                    getWidth() -1, 
                    getHeight() -1 );
            
            break;
            
        case FORBIDDEN :
            pGraphics.setColor( ColorSet.FORBIDDEN );
            pGraphics.fillRect( 0, 0, 
                    getWidth(), 
                    getHeight() );
            break;
            
        default :
            System.out.println(" Square - Error with default ");
        }
        
        pGraphics.setColor( c );
        
    }
    
    
    public final boolean isFree()
    {
        boolean isFree=  false;
        switch (mSquareState)
        {
        case EMPTY :
        case PLACING_BOAT :
            isFree=  true;
            break;
        case BOAT :
        default :
            isFree=  false;
        }
        
        return isFree;
    }
    
    /**
     * 
     * @param pSquareState
     */
    public final void setState( final StateEnum pSquareState)
    {
        mSquareState=  pSquareState;
    }
    
    /**
     * Return the current state of this square, even its temporary state.
     * @return
     */
    public StateEnum getState()
    {
        return mTemporarySquareState != null
                   ? mTemporarySquareState
                   : mSquareState;
    }
    
    public int getXIndex ()
    {
        return mCoordinates.getXIndex();
    }
    
    public int getYIndex ()
    {
     return mCoordinates.getYIndex();
    }
}
