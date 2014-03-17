package com.itii;

import com.itii.gui.MainWindow;
import com.itii.manager.TurnManager;
import com.itii.network.Receiver;

public class Launcher
{
    public static void main(String[] args)
    {
        MainWindow.getInstance();
        TurnManager.getInstance().updateCurrentPhase();

        Receiver.getInstance().setPortNumber(8888);
        Receiver.getInstance().start();
    }
}
