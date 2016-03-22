package com.itii.gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class MainWindow 
    extends JFrame
{

    
    private static MainWindow instance=  new MainWindow();;
    
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
        
        addWindowListener( 
                new WindowListener() 
                {

                    @Override
                    public void windowActivated ( WindowEvent arg0 )
                    {
                        // TODO Auto-generated method stub
                        
                    }

                    @Override
                    public void windowClosed ( WindowEvent arg0 )
                    {
                        // TODO Auto-generated method stub
                        
                    }

                    @Override
                    public void windowClosing ( WindowEvent arg0 )
                    {
                        System.out.println("closing");
                        System.exit(0);
                    }

                    @Override
                    public void windowDeactivated ( WindowEvent arg0 )
                    {
                        // TODO Auto-generated method stub
                        
                    }

                    @Override
                    public void windowDeiconified ( WindowEvent arg0 )
                    {
                        // TODO Auto-generated method stub
                        
                    }

                    @Override
                    public void windowIconified ( WindowEvent arg0 )
                    {
                        // TODO Auto-generated method stub
                        
                    }

                    @Override
                    public void windowOpened ( WindowEvent arg0 )
                    {
                        // TODO Auto-generated method stub
                        
                    }
            
                }
                
        );
        
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
