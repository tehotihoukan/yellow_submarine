package com.itii.data;

public class Coordinates
{
    private final short mX, mY;
    
    public Coordinates ( final short pX, final short pY )
    {
        mX=  pX;
        mY=  pY;
    }
    
    public short getX ()
    {
        return mX;
    }
    
    public short getY ()
    {
        return mY;
    }
    
    public String toString ()
    {
        return String.format("%02d", mX)+ String.format("%02d", mY);
    }


    public static final Coordinates getCoordinatesFromString ( final String pString )
    {
        final String coordinates=  pString.substring( 3 );
        final Short  x=            Short.parseShort( coordinates.substring( 0, 2 ) );
        final Short  y=            Short.parseShort( coordinates.substring( 2, 4 ) );
        
        return new Coordinates( x, y );
    }
}
