package com.itii.data;


/**
 * All the states of the game, the states for the game turn, the message exchanges etc...
 * @author Sebastien MARTAGEX
 *
 */
public class State
{

    public enum StateEnum
    {
        MISSED(), // Missed fire
        BOAT_HIT(), // Boat hit by fire
        HIT(), // Hit in an unknown state (when user clicks for the first time)
        PLACING_BOAT(), // When placing boat, temporary square state
        FORBIDDEN(), // When a boat is already there, cannot place another boat.
        EMPTY(), // Empty square (mainly sea color)
        BOAT(), // Boat placed there - not specified what kind of boat
        BOAT_SUNK(), // Boat entirely sunk
        SURROUND(); // Square used for surrounding the game.
    
        Boat boat;
        
        StateEnum()
        {
            
        }
        public void setBoat(Boat boat)
        {
            this.boat = boat;
        }
        
    }

    public enum BoatOrientation
    {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * All the phases of the game from Start to End.
     */
    public enum GamePhase
    {
        STARTING,
        WAITING_FOR_OPPONENT,
        WAITING_FOR_YOU,
        DEPLOYMENT,
        DEPLOYMENT_ENDED, 
        OPPONENT_READY,
        PLAYER_READY,
        READY,
        STARTER,
        PLAYER_TURN,
        OPPONENT_TURN,
        ENDED, // When the game ends normally
        PLAYER_LEFT // In case the player "rage quits" the game.
    }
}
