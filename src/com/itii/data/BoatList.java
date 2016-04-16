package com.itii.data;

/**
 * List of all the available boats
 */
public enum BoatList
{
    AIRCRAFT_CARRIER( "Aircraft carrier", 5 ),
    BATTLESHIP( "Battleship", 4 ),
    SUBMARINE( "Submarine" , 3 ),
    CRUISER( "Cruiser", 3 ),
    DESTROYER( "Destroyer", 2 );

    private String mDisplayName;
    private int mLength;

    private BoatList( final String  pDisplayName,
                      final int     pLength )
    {
        mDisplayName= pDisplayName;
        mLength=  pLength;
    }


    public String getDisplayName()
    {
        return mDisplayName;
    }

    public int getLength()
    {
        return mLength;
    }
}
