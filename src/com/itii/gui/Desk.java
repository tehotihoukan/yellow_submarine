package com.itii.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.itii.data.Boat;

@SuppressWarnings("serial")
public class Desk 
    extends JPanel
{
    
    private static final int GRID_SIZE=  10;

    private GridDisplay mJoueurGrid;
    private GridDisplay mAdversaireGrid;
    private GameMenu  mGameMenu;
    
    public Desk ()
    {
        initialize();
    }
    
    private void setGridBagLayout( JPanel panel )
    {
        panel.setLayout( new GridBagLayout( ) );
        
        GridBagConstraints c = new GridBagConstraints();
  
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.4;
        c.weighty = 1; 
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        panel.add(getJoueurGrid(), c);

        c.weightx = 0.2;
        c.weighty = 1; 
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        panel.add(getGameMenu(), c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.4;
        c.weighty = 1;
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 3;
        panel.add( getAdversaireGrid(), c );
      
    }
    
    
    public void setBoxLayout( JPanel panel )
    {
        panel.setLayout( new BoxLayout(panel, BoxLayout.X_AXIS)  );
        
        panel.add( getJoueurGrid() );
        panel.add(getGameMenu());
        panel.add( getAdversaireGrid() );
    }
    
    public void initialize()
    {
        setBackground( new Color( 80, 80, 180) );
        this.setGridBagLayout( this );
    }
    
    public GridDisplay getJoueurGrid()
    {
        if ( mJoueurGrid == null )
        {
            mJoueurGrid=  new  GridDisplay( GRID_SIZE , false ); 
        }
        return mJoueurGrid;
    }
    
    public GridDisplay getAdversaireGrid()
    {
        if ( mAdversaireGrid == null  )
        {
            mAdversaireGrid=  new  GridDisplay( GRID_SIZE, true );
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
