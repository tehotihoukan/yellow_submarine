package com.itii.network;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender
{

    private static Sender       mSender=  new Sender();

    private Socket              mSocket;
    private OutputStreamWriter  mOutputStreamWriter;
    private int                 mPort;
    private String              mHost;
    
    private boolean             mInitialized= false;
    
    private Sender()
    {
        
    }
    
    public static Sender getInstance()
    {
        return mSender;
    }

    public int getPort ()
    {
        return mPort;
    }

    public void setPort ( int mPort )
    {
        this.mPort= mPort;
    }

    public String getHost ()
    {
        return mHost;
    }

    public void setHost ( String mHost )
    {
        this.mHost= mHost;
    }
    
    
    public void initialize()
    {
        if ( ! mInitialized )
        {
            try
            {
                mSocket=  new Socket(mHost, mPort);
                mOutputStreamWriter = new OutputStreamWriter( mSocket.getOutputStream() );
                mInitialized=  true;
            } catch (UnknownHostException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void sendPlayerMessage( Message message )
    {
        try
        {
            System.out.println("message ? "+ message);
            mOutputStreamWriter.write( message + "\n" );
            mOutputStreamWriter.flush();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    
    }
    
}
