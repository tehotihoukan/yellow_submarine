package com.itii.network;

public class MessageType
{

    
    public static final int    UNKNOWN_ID=          0x00;
    
    
    public static final int    PLAYER_JOINED_ID=    0x10;
    public static final int    PLAYER_LEFT_ID=      0x11;
    public static final int    READY_ID=            0x05;
    
    public static final int    SQUARE_HIT_ID=          0x01;
    public static final int    SQUARE_UPDATE_ID=       0x12;
    
    
    public static final int    SUNK_ID=             0x02;        
    public static final int    NEXT_ID=             0x03;        
    public static final int    END_ID=              0x04;
    
    
    public static Message getMessage( final String pMessage )
    {
        final int    messageId=     
                Integer.parseInt( pMessage.substring( 0,  2 ) );
    
        final String messageValue=  pMessage.length() > 2
                ? pMessage.substring( 3 )
                : null;

        return new Message( messageId, messageValue );
    }
}
