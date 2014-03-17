package com.itii.data;

public class Coordinates
{

    short mY;
    short mX;

    public Coordinates(short pX, short pY)
    {
        mY = pY;
        mX = pX;
    }

    public short getmX()
    {
        return mX;
    }

    public short getmY()
    {
        return mY;
    }

    @Override
    public String toString()
    {
        return String.format("%02d", mX) + String.format("%02d", mY);
    }

    public static Coordinates readCoordinatesFromString(String pString)
    {
        final String coordinates = pString.substring(2);
        Short x = Short.parseShort(coordinates.substring(0, 2));
        Short y = Short.parseShort(coordinates.substring(2, 4));

        return new Coordinates(x, y);
    }

}
