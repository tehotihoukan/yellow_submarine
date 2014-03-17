package com.itii.network;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import javax.swing.SwingUtilities;

/**
 * @author SebastienM
 * 
 */
public class Sender
{

    private boolean mInitialize=  false;
    private static Sender instance = new Sender();

    private Socket mSocket;
    private OutputStreamWriter mOutputStreamWriter;
    private String mHost;
    private int mPort;

    private Sender()
    {
        // TODO Auto-generated constructor stub
    }

    public void initialize()
    {
        if ( ! mInitialize )
        {
            try
            {
                mSocket = new Socket(mHost, mPort);
                mOutputStreamWriter = new OutputStreamWriter(
                        mSocket.getOutputStream());
                mInitialize=  true;
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * PAGE 8 (2° TP)
     * 
     * 
     * 
     * @param pMessage
     */

    public void sendPlayerMessage(Message pMessage)
    {
        try
        {
            mOutputStreamWriter
                    .write(MessageType.parseMessage(pMessage) + '\n');
            mOutputStreamWriter.flush();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getHost()
    {
        return mHost;
    }

    public void setHost(String pHost)
    {
        this.mHost = pHost;
    }

    public int getPort()
    {
        return mPort;
    }

    public void setPort(int pPort)
    {
        this.mPort = pPort;
    }

    public static Sender getInstance()
    {
        return instance;
    }

}
