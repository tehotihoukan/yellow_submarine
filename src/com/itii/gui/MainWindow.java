package com.itii.gui;

import javax.swing.JFrame;


/**
 * Main Window containing all the game
 */
@SuppressWarnings("serial")
public class MainWindow 
    extends JFrame
{
    private static final MainWindow instance=  new MainWindow();
    
    private Desk mDesk;
    
    private MainWindow()
    {
        initialize();
    }
    
    private void initialize()
    {
        setSize( 600, 265);
        setVisible( true );
        setResizable( false );
        // When closing the window, exit also the program
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        
        // Main Pane
        this.getContentPane().add( getDesk() );

        this.validate();
        this.repaint();
    }
    
    public Desk getDesk()
    {
        if ( mDesk == null )
        {
            mDesk=  new Desk();
        }
        return mDesk;
    }
    
    public static MainWindow getInstance()
    {
        return instance;
    }
    
    
}
