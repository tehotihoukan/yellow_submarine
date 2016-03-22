package com.itii.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.itii.data.Boat;

public class Desk 
    extends JPanel
{

    private GridDisplay mJoueurGrid;
    private GridDisplay mAdversaireGrid;
    private GameMenu  mGameMenu;
    
    public Desk ()
    {
        initialize();
    }
    
    public void initialize()
    {
        
        setBackground( new Color( 80, 80, 180) );
        this.setLayout( new GridBagLayout( ) );
        GridBagConstraints c = new GridBagConstraints();
    
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.4;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        this.add( getJoueurGrid(), c );
        
        
        c.weightx = 0.2;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add( getGameMenu(), c);
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.4;
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 2;
        this.add( getAdversaireGrid(), c );
        
    }
    
    public GridDisplay getJoueurGrid()
    {
        if ( mJoueurGrid == null )
        {
            mJoueurGrid=  new  GridDisplay( 10 , false ); 
        }
        return mJoueurGrid;
    }
    
    public GridDisplay getAdversaireGrid()
    {
        if ( mAdversaireGrid == null  )
        {
            mAdversaireGrid=  new  GridDisplay( 10, true );
        }
        return mAdversaireGrid;
    }
    
    public GameMenu getGameMenu()
    {
        if ( mGameMenu == null )
        {
            mGameMenu=  new GameMenu();
        }
        return mGameMenu;
    }
    
    
    public final boolean isOpponentSunk()
    {
        return mAdversaireGrid.getNumberOfBoatSquareUnsunk() >= Boat.mTotalNumberOfSquare
                    ? true 
                    : false;
    }
}
