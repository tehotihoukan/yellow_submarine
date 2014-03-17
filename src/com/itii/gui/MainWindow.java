package com.itii.gui;

import javax.swing.JFrame;

public class MainWindow extends JFrame
{

    Desk mDesk;
    private static MainWindow instance = new MainWindow();

    private MainWindow()
    {
        initialize();
    }

    public static MainWindow getInstance()
    {
        return instance;
    }

    public void initialize()
    {
        setVisible(true);
        setSize(1000, 300);

        getContentPane().add(getDesk());

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//        addWindowListener(new WindowListener()
//        {
//
//            @Override
//            public void windowActivated(WindowEvent e)
//            {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void windowClosed(WindowEvent e)
//            {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void windowClosing(WindowEvent e)
//            {
//                System.out.println("closing");
//                System.exit(0);
//            }
//
//            @Override
//            public void windowDeactivated(WindowEvent e)
//            {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void windowDeiconified(WindowEvent e)
//            {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void windowIconified(WindowEvent e)
//            {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void windowOpened(WindowEvent e)
//            {
//                // TODO Auto-generated method stub
//
//            }
//
//        }
//
//        );

        
        validate();
        repaint();
    }

    public Desk getDesk()
    {
        if (mDesk == null)
        {
            mDesk = new Desk();
        }
        return mDesk;
    }

}
