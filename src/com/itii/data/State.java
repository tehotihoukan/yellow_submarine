package com.itii.data;

public class State
{
    public enum StateEnum
    {
        PLACING_BOAT,
        FORBIDDEN,
        
        EMPTY,
        BOAT,
        MISSED,
        BOAT_HIT,
        HIT,

        BOAT_SUNK,
        SURROUND
    }
    
    public enum BoatOrientation
    {
        HORIZONTAL,
        VERTICAL
    }
    
    public enum GamePhase
    {
        STARTING, // Before the game starts we wait for the opponent to come in the game
        WAITING_FOR_OPPONENT,
        WAITING_FOR_YOU,
        DEPLOYMENT, // Phase where both players add their boats on the grid
        DEPLOYMENT_ENDED,
        OPPONENT_READY, // If the opponent is ready before the current player
        PLAYER_READY,
        READY, // If both players are ready (OPPONENT_READY & PLAYER_READY)
        STARTED,
        PLAYER_TURN,
        OPPONENT_TURN,
        ENDED,
        PLAYER_LEFT
    }
}
