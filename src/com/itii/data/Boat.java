package com.itii.data;

import java.awt.Graphics2D;

import com.itii.data.State.BoatOrientation;

/**
 * 
 * @author Sebastien MARTAGEX
 *
 */
public abstract class Boat
{
    public static int TOTAL_NUMBER_OF_SQUARE_OCCUPIED;

    private boolean mIsOnTheGrid;
    private BoatOrientation mOrientation = BoatOrientation.HORIZONTAL;

    public abstract BoatList getBoatType();

    @Override
    public String toString()
    {
        return getBoatType().getDisplayName() + " [" + getLength() + "]";
    }

    public static int countTotalNumberOfBoatSquares(final Boat[] pBoats)
    {
        TOTAL_NUMBER_OF_SQUARE_OCCUPIED = 0;

        for (int i = 0; i < pBoats.length; i++)
        {
            TOTAL_NUMBER_OF_SQUARE_OCCUPIED += pBoats[i].getLength();
        }
        return TOTAL_NUMBER_OF_SQUARE_OCCUPIED;
    }

    public int getCenter()
    {
        return (getLength() / 2);
    }

    /**
     * @return the length of the boat in number of square occupied.
     */
    public int getLength()
    {
        return getBoatType().getLength();
    }

    public BoatOrientation getOrientation()
    {
        return mOrientation;
    }

    public void setOrientation(final BoatOrientation pOrientation)
    {
        mOrientation = pOrientation;
    }

    public void flipOrientation()
    {
        setOrientation(getOrientation() == BoatOrientation.HORIZONTAL ? BoatOrientation.VERTICAL
                : BoatOrientation.HORIZONTAL);
    }

    /**
     * Method to display the image representing the boat on the grid.
     */
    public void paintBoat( final Graphics2D  pGraphics,
                           final int         pSquareSize )
    {
        
    }

}