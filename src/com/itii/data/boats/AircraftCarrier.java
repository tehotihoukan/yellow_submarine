package com.itii.data.boats;

import com.itii.data.Boat;
import com.itii.data.BoatList;

public class AircraftCarrier
    extends Boat
{

    @Override
    public BoatList getBoatType()
    {
        return BoatList.AIRCRAFT_CARRIER;
    }

}
