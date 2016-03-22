package com.itii.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.itii.data.Coordinates;
import com.itii.data.State.StateEnum;

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
    
    
    public void paintSquare ( Graphics pGraphics, int dimension )
    {
        
        Color c=  pGraphics.getColor();

        switch ( getState() )
        {
        case EMPTY :
            pGraphics.setColor( Color.blue );
            pGraphics.fillRect( mCoordinates.getX() * dimension, 
                                mCoordinates.getY() * dimension, 
                                dimension, 
                                dimension );
            
            pGraphics.setColor( Color.white );
            pGraphics.drawRect( mCoordinates.getX() * dimension, 
                                mCoordinates.getY() * dimension, 
                                dimension, 
                                dimension );
            break;
    
        case PLACING_BOAT :
            pGraphics.setColor( Color.gray );
            pGraphics.fillRect( mCoordinates.getX() * dimension, 
                                mCoordinates.getY() * dimension,
                                dimension, 
                                dimension );
            break;
        case BOAT :
            pGraphics.setColor( Color.black );
            pGraphics.fillRect( mCoordinates.getX() * dimension, 
                               mCoordinates.getY() * dimension,
                               dimension, 
                               dimension );
            break;
            
        case BOAT_HIT :
            pGraphics.setColor( new Color( 220, 0, 0 ) );
            pGraphics.fillRect( mCoordinates.getX() * dimension, 
                               mCoordinates.getY() * dimension,
                               dimension, 
                               dimension );
            break;
            
        case BOAT_SUNK :
            pGraphics.setColor( new Color( 0, 0, 0 ) );
            pGraphics.fillRect( mCoordinates.getX() * dimension, 
                               mCoordinates.getY() * dimension,
                               dimension, 
                               dimension  );
            break;
            
        case HIT : 
            pGraphics.setColor( new Color( 0, 0, 0 ) );
            pGraphics.drawRect( mCoordinates.getX() * dimension, 
                               mCoordinates.getY() * dimension,
                               dimension - 1, 
                               dimension - 1 );
            break;
            
        case MISSED :
            pGraphics.setColor( new Color( 10, 10, 10 ) );
            pGraphics.drawRect( mCoordinates.getX() * dimension, 
                               mCoordinates.getY() * dimension,
                               dimension - 1, 
                               dimension - 1 );
            
            break;
            
        case FORBIDDEN :
            pGraphics.setColor( new Color( 220, 10, 10 ) );
            pGraphics.fillRect( mCoordinates.getX() * dimension, 
                               mCoordinates.getY() * dimension,
                               dimension, 
                               dimension );
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
    
    @Override
    public int getX ()
    {
        return mCoordinates.getX();
    }
    
    @Override
    public int getY ()
    {
     return mCoordinates.getY();
    }
}
