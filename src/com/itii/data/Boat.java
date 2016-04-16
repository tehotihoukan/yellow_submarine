package com.itii.data;

import com.itii.data.State.BoatOrientation;

public abstract class Boat
{

    public static int mTotalNumberOfSquare;

    protected boolean mIsPlacedOnGrid=  false;

    private BoatOrientation mBoatOrientation=  BoatOrientation.VERTICAL;

    public abstract BoatList getBoatType();

    @Override
    public String toString ()
    {
        return getBoatType().getDisplayName()
                + " [" + getBoatType().getLength() + "]";
    }


    public int getCenter()
    {
        return getBoatType().getLength() / 2;
    }

    public BoatOrientation getOrientation()
    {
        return mBoatOrientation;
    }

    public void setOrientation( final BoatOrientation pOrientation )
    {
        mBoatOrientation=  pOrientation;
    }

    public final void flipOrientation()
    {
        setOrientation( getOrientation() == BoatOrientation.HORIZONTAL
                            ? BoatOrientation.VERTICAL
                            : BoatOrientation.HORIZONTAL );

    }



    public static int countTotalNumberOfBoatSquares( final Boat[] boats)
    {
        mTotalNumberOfSquare=  0;

        for (int i= 0 ; i < boats.length ; i++ )
        {
            mTotalNumberOfSquare+=  boats[i].getBoatType().getLength();
        }
        return mTotalNumberOfSquare;
    }

}
