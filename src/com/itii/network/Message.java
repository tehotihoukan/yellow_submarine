package com.itii.network;

public class Message
{

    private String mID;
    private String mMessage = null;

    public Message(int pId)
    {
        mID = String.format("%02d", pId);
    }

    public Message(int pId, String pMessage)
    {
        mID = String.format("%02d", pId);
        mMessage = pMessage;
    }

    public int getID()
    {
        return Integer.parseInt(mID);
    }

    public String getMessage()
    {
        return mMessage;
    }

    @Override
    public String toString()
    {
        return mID + (mMessage == null ? "" : mMessage);
    }

}
