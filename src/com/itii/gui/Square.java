package com.itii.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.itii.data.Coordinates;
import com.itii.data.State;
import com.itii.data.State.StateEnum;

public class Square
    extends JComponent
{
    int pSquareSize= 40;

    // PAGE 21
    private State.StateEnum mTemporarySquareState;
    private State.StateEnum mSquareState = State.StateEnum.EMPTY;
    protected final Coordinates mCoordinates;

    public Square(short pXCoordinate, short pYCoordinate)
    {
        mCoordinates = new Coordinates(pXCoordinate, pYCoordinate);
        setSize( pSquareSize, pSquareSize );
        setPreferredSize( new Dimension( pSquareSize, pSquareSize ) );
    }

    @Override
    public int getX()
    {
        return mCoordinates.getmX();
    }

    @Override
    public int getY()
    {
        return mCoordinates.getmY();
    }

    public State.StateEnum getState()
    {
        return mTemporarySquareState != null
                   ? mTemporarySquareState
                   : mSquareState;
    }

    @Override
    public void paint( final Graphics  pGraphics )
    {

        Color color = Color.blue;

        switch (getState())
        {
        case BOAT_HIT:
            pGraphics.setColor(new Color(220, 0, 0));
            pGraphics.fillRect( 0 , 0, getWidth(), getHeight() );
            break;
        case BOAT_SUNK:
            pGraphics.setColor(new Color(0, 0, 0));
            pGraphics.fillRect( 0 , 0, getWidth(), getHeight() );
            break;
        case HIT:
            pGraphics.setColor(new Color(0, 0, 0));
            pGraphics.fillRect( 0 , 0, getWidth(), getHeight() );

            break;
        case MISSED:
            pGraphics.fillRect( 0 , 0, getWidth(), getHeight() );

            break;
        case PLACING_BOAT:
            color = Color.white;
            break;
        case FORBIDDEN:
            pGraphics.setColor(Color.red);
            pGraphics.fillRect( 0 , 0, getWidth(), getHeight() );

            break;
        case BOAT:
            color = Color.gray;
            break;
        case EMPTY:
            pGraphics.setColor(color);
            pGraphics.fillRect( 0 , 0, getWidth(), getHeight() );

        default:
            break;
        }

        pGraphics.setColor(Color.white);
//        pGraphics.drawRect(pSquareSize * mCoordinates.getmX(), pSquareSize
//                * mCoordinates.getmY(), pSquareSize, pSquareSize);
        
        pGraphics.fillRect( 0 , 0, getWidth(), getHeight() );

    }
    
//    public void paintSquare( final Graphics  pGraphics,
//                             final int       pSquareSize )
//    {
//
//        Color color = Color.blue;
//
//        switch (getState())
//        {
//        case BOAT_HIT:
//            pGraphics.setColor(new Color(220, 0, 0));
//            pGraphics.fillRect(mCoordinates.getmX() * pSquareSize,
//                    mCoordinates.getmY() * pSquareSize, pSquareSize,
//                    pSquareSize);
//            break;
//        case BOAT_SUNK:
//            pGraphics.setColor(new Color(0, 0, 0));
//            pGraphics.fillRect(mCoordinates.getmX() * pSquareSize,
//                    mCoordinates.getmY() * pSquareSize, pSquareSize,
//                    pSquareSize);
//            break;
//        case HIT:
//            pGraphics.setColor(new Color(0, 0, 0));
//            pGraphics.fillRect(mCoordinates.getmX() * pSquareSize,
//                    mCoordinates.getmY() * pSquareSize, pSquareSize - 1,
//                    pSquareSize - 1);
//            break;
//        case MISSED:
//            pGraphics.setColor(new Color(30, 30, 30));
//            pGraphics.fillRect(mCoordinates.getmX() * pSquareSize,
//                    mCoordinates.getmY() * pSquareSize, pSquareSize - 1,
//                    pSquareSize - 1);
//            break;
//        case PLACING_BOAT:
//            color = Color.white;
//            break;
//        case FORBIDDEN:
//            pGraphics.setColor(Color.red);
//            pGraphics.fillRect(mCoordinates.getmX() * pSquareSize,
//                    mCoordinates.getmY() * pSquareSize, pSquareSize,
//                    pSquareSize);
//            break;
//        case BOAT:
//            color = Color.gray;
//            break;
//        case EMPTY:
//            pGraphics.setColor(color);
//            pGraphics.fillRect(pSquareSize * mCoordinates.getmX(), pSquareSize
//                    * mCoordinates.getmY(), pSquareSize, pSquareSize);
//
//        default:
//            break;
//        }
//
//        pGraphics.setColor(Color.white);
//        pGraphics.drawRect(pSquareSize * mCoordinates.getmX(), pSquareSize
//                * mCoordinates.getmY(), pSquareSize, pSquareSize);
//    }

    public final void setTemporaryState(final StateEnum pTemporarySquareState)
    {
        mTemporarySquareState = pTemporarySquareState;
    }

    public Coordinates getCoordinates()
    {
        return mCoordinates;
    }

    public final void freeTemporaryState()
    {
        mTemporarySquareState = null;
    }

    public void setState(final StateEnum pState)
    {
        mSquareState = pState;
    }

    public boolean isFree()
    {
        switch (mSquareState)
        {
        case BOAT:
            return false;
        case EMPTY:
        case PLACING_BOAT:
        case FORBIDDEN:
            return true;
        default:
            return false;
        }

    }

    public void updateState(StateEnum pSquareState)
    {
        if (pSquareState == StateEnum.HIT && getState() == StateEnum.BOAT)
        {
            setState(StateEnum.BOAT_HIT);
        }
        else
        {
            setState(StateEnum.MISSED);
        }
    }

}