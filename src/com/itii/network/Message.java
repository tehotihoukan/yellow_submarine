package com.itii.network;

public class Message
{

    String mMessage;
    String mId;
    
    
    public Message (int pId, String pMsg )
    {
        mId=       String.format("%02d", pId);
        mMessage=  pMsg;
    }


    public String getMessage ()
    {
        return mMessage;
    }


    public int getId ()
    {
        return Integer.parseInt( mId );
    }
    
    
    @Override
    public String toString ()
    {
        return mMessage == null
                   ? mId
                   : mId + " " + mMessage; 
        
    }
}
