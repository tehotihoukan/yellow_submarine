package com.itii.data.boats;

import com.itii.data.Boat;
import com.itii.data.BoatList;

public class Submarine extends Boat
{

    @Override
    public BoatList getBoatType()
    {
        return BoatList.SUBMARINE;
    }

}
