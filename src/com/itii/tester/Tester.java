package com.itii.tester;

import com.itii.data.Coordinates;
import com.itii.network.Message;
import com.itii.network.MessageType;
import com.itii.network.Sender;

public class Tester
{

    public static void main(final String[] args)
    {
        Sender.getInstance().setPort(8888);
        Sender.getInstance().setHost("127.0.0.1");
        Sender.getInstance().initialize();

        while (true)
        {
            Sender.getInstance().sendPlayerMessage(
                    new Message(MessageType.SQUARE_HIT_ID, new Coordinates(
                            (short) 4, (short) 5).toString()));

            try
            {
                Thread.sleep(10000);

            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }
}