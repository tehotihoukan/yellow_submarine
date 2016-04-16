package com.itii.data;

public class Coordinates
{
    private final short mXIndex, mYIndex;

    /**
     * @param pXIndex is the X Coordinate for a component
     * @param pYIndex is the Y Coordinate for a component
     */
    public Coordinates ( final short pXIndex,
                         final short pYIndex )
    {
        mXIndex=  pXIndex;
        mYIndex=  pYIndex;
    }

    /**
     * @return the X Location in the grid.
     */
    public short getXIndex ()
    {
        return mXIndex;
    }

    /**
     * 
     * @return the Y Location in the grid.
     */
    public short getYIndex ()
    {
        return mYIndex;
    }

    public String toString ()
    {
        return String.format("%02d", mXIndex)+ String.format("%02d", mYIndex);
    }


    public static final Coordinates getCoordinatesFromString ( final String pString )
    {
        final String coordinates=  pString.substring( 3 );
        final Short  x=            Short.parseShort( coordinates.substring( 0, 2 ) );
        final Short  y=            Short.parseShort( coordinates.substring( 2, 4 ) );

        return new Coordinates( x, y );
    }
}
