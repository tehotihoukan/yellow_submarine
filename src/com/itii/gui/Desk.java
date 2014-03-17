package com.itii.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.itii.data.Boat;

/**
 * Main desk where all the game takes places.
 * It contains both menu and player grids.
 *
 * @author Sebastien MARTAGEX
 *
 */
public class Desk
    extends JPanel
{

    private GameMenu mGameMenu;
    private GridDisplay mOpponentGrid;
    private final GridDisplay mPlayerGrid;

    public Desk()
    {
        mPlayerGrid = new GridDisplay(10, false);
        initialize();
    }

    private void initialize()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(getOpponentGrid());
        add(getGameMenu());
        add(mPlayerGrid);
    }

    public boolean isOpponentSunk()
    {
        return (mOpponentGrid.getNumberOfBoatSquareNotHit() >= Boat.TOTAL_NUMBER_OF_SQUARE_OCCUPIED)
                    ? true
                    : false;
    }

    public GridDisplay getOpponentGrid()
    {
        if (mOpponentGrid == null)
        {
            mOpponentGrid = new GridDisplay(10, true);
        }

        return mOpponentGrid;
    }

    public GridDisplay getPlayerGrid()
    {
        return mPlayerGrid;
    }

    public GameMenu getGameMenu()
    {
        if (mGameMenu == null)
        {
            mGameMenu = new GameMenu();
        }
        return mGameMenu;
    }
}