package com.itii.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.itii.data.Boat;
import com.itii.data.Coordinates;
import com.itii.data.State;
import com.itii.data.State.GamePhase;
import com.itii.data.State.StateEnum;
import com.itii.manager.TurnManager;
import com.itii.network.Message;
import com.itii.network.MessageType;
import com.itii.network.Sender;

public class GridDisplay
    extends JPanel
    implements MouseListener,
               MouseMotionListener
{

    Square[][] mSquares;
    int mGridSizeInPxl;

    public GridDisplay(int pSize, boolean pIsOpponent)
    {
        mSquares = new Square[pSize + 1][pSize + 1];
        mGridSizeInPxl = getWidth();
        initializeGrid();
    }

    public int getNumberOfBoatSquareNotHit()
    {
        int ret = 0;

        for (int y = 0; y < mSquares.length; y++)
        {
            for (int x = 0; x < mSquares[y].length; x++)
            {
                if (mSquares[y][x].getState() == State.StateEnum.BOAT_HIT)
                {
                    ret++;
                }
            }
        }
        return ret;
    }

    private void initializeGrid()
    {   
        GridLayout gridLayout=  new GridLayout( mSquares.length, mSquares.length);
        setLayout(gridLayout);

        for (int i = 0; i < mSquares.length; i++)
        {
            for (int j = 0; j < mSquares[i].length; j++)
            {
                if (j == 0 ^ i == 0)
                {
                    mSquares[i][j] = new SurroundingSquare((short) i, (short) j);
                } else
                {
                    mSquares[i][j] = new Square((short) i, (short) j);
                }
                add( mSquares[i][j] );
            }
        }

        setGridDisplayEnabled(true);
    }

    @Override
    public void mouseDragged(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent pMouseEvent)
    {

        GridDisplay gridDisplay = (GridDisplay) pMouseEvent.getSource();

        Boat boatBeingAdded = (Boat) MainWindow.getInstance()
                                               .getDesk()
                                               .getGameMenu()
                                               .getBoatComboBox()
                                               .getSelectedItem();

        Square squareDisplayingBoat = (Square) (gridDisplay.getComponentAt(pMouseEvent.getPoint()));

        if (boatBeingAdded != null)
        {
            updateSquareDependingOnBoatSelected(squareDisplayingBoat,
                    boatBeingAdded, State.StateEnum.PLACING_BOAT, true);
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent pMouseEvent)
    {

        if (TurnManager.getInstance().getGamePhase() == GamePhase.DEPLOYMENT
                || TurnManager.getInstance().getGamePhase() == GamePhase.OPPONENT_READY)
        {
            GridDisplay grid = (GridDisplay) pMouseEvent.getSource();
            // grid.get

            JComboBox<Boat> comboBox = MainWindow.getInstance().getDesk()
                                                 .getGameMenu().getBoatComboBox();
            Square square = (Square) getComponentAt(pMouseEvent.getPoint());

            if (updateSquareDependingOnBoatSelected(square,
                    (Boat) comboBox.getSelectedItem(), State.StateEnum.BOAT,
                    false))
            {
                comboBox.removeItemAt(comboBox.getSelectedIndex());

                if (comboBox.getItemCount() == 0)
                {
                    TurnManager.getInstance().setGamePhase(
                            GamePhase.DEPLOYMENT_ENDED);
                }
            }

        } else if (TurnManager.getInstance().getGamePhase() == GamePhase.PLAYER_TURN)
        {
            GridDisplay grid = (GridDisplay) pMouseEvent.getSource();
            Square squareDisplayingBoat = (Square) grid
                    .getComponentAt(pMouseEvent.getPoint());

            System.out.println("PLAYER_TURN : Mouse Clicked : X= "
                    + pMouseEvent.getX() + " Y= " + pMouseEvent.getY());

            if (squareDisplayingBoat.getState() != State.StateEnum.BOAT_HIT
                    && squareDisplayingBoat.getState() != State.StateEnum.HIT)
            {
                Sender.getInstance().sendPlayerMessage(
                        new Message(MessageType.SQUARE_HIT_ID,
                                squareDisplayingBoat.getCoordinates()
                                        .toString()));
                squareDisplayingBoat.setState(StateEnum.HIT);
                TurnManager.getInstance().setGamePhase(GamePhase.OPPONENT_TURN);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void paint(Graphics pGraphics)
    {
        super.paint(pGraphics);

        pGraphics.setColor(Color.green);
        pGraphics.drawRect(0, 0, getWidth(), getHeight());

        // Size of a square based on the size of the whole grid.
        final int squareEdgeSize = Math.min(getWidth() / mSquares.length,
                getHeight() / mSquares[0].length);

        mGridSizeInPxl = squareEdgeSize * mSquares.length;

        for (int i = 0; i < mSquares.length; i++)
        {
            for (int j = 0; j < mSquares[i].length; j++)
            {
//                mSquares[i][j].paintSquare(pGraphics, squareEdgeSize);
                mSquares[i][j].repaint();
            }
        }
//
//        for ( final Boat boat : listOfBoats )
//        {
//            boat.paintBoat( pGraphics, squareEdgeSize);
//        }

    }

    public Square getSquare(final Coordinates pCoordinates)
    {
        return mSquares[pCoordinates.getmY()][pCoordinates.getmX()];
    }

    public void freeAllTemporarySquareState()
    {
        for (int i = 0; i < mSquares.length; i++)
        {
            for (int j = 0; j < mSquares[i].length; j++)
            {
                mSquares[i][j].freeTemporaryState();
            }
        }
    }

    @Override
    public Component getComponentAt(int x, int y)
    {
        int xLocation = (int) (((float) x / (float) mGridSizeInPxl) * mSquares[0].length);
        int yLocation = (int) (((float) y / (float) mGridSizeInPxl) * mSquares.length);

        xLocation = Math.min(xLocation, mSquares[0].length - 1);
        yLocation = Math.min(yLocation, mSquares.length - 1);

        return mSquares[yLocation][xLocation];
    }

    public Square getSquareAt(int x, int y)
    {
        return mSquares[y][x];
    }

    public boolean updateSquareDependingOnBoatSelected( final Square pCurrentSquare,
                                                        final Boat pCurrentBoatBeingAdded,
                                                        final State.StateEnum pState,
                                                        final boolean pIsTemporary )
    {
        final int boatSize = pCurrentBoatBeingAdded.getLength();
        final int boatCenter = pCurrentBoatBeingAdded.getCenter();

        freeAllTemporarySquareState();
        boolean isFreeSquare = true;

        ArrayList<Square> listOfSquareToUpdate = new ArrayList<Square>();

        switch (pCurrentBoatBeingAdded.getOrientation())
        {
        case HORIZONTAL:
        {
            final int numberOfSquaresOnX = mSquares[0].length;
            final int currentXPosition = pCurrentSquare.getX();
            final int boatRightEdgePosition = currentXPosition
                    + (boatSize - boatCenter);
            final int boatLeftEdgePosition = currentXPosition - boatCenter;
            int startingXPosition = boatLeftEdgePosition;

            if (boatRightEdgePosition >= numberOfSquaresOnX
                    || boatLeftEdgePosition < 1)
            {

                startingXPosition = (boatRightEdgePosition >= numberOfSquaresOnX) ? (numberOfSquaresOnX - boatSize)
                        : 1;
                System.out.println("StartingXPosition" + startingXPosition);
            }
            for (int x = 0; x < boatSize; x++)
            {
                final Square squareForTmpBoatPlacing = mSquares[pCurrentSquare
                        .getY()][startingXPosition + x];

                if (!squareForTmpBoatPlacing.isFree())
                {
                    isFreeSquare = false;
                }
                listOfSquareToUpdate.add(squareForTmpBoatPlacing);
            }

            break;
        }
        case VERTICAL:
        {
            final int numberOfSquaresOnY = mSquares.length;
            final int currentYPosition = pCurrentSquare.getY();
            final int boatUpEdgePosition = currentYPosition - boatCenter;
            int startingYPosition = boatUpEdgePosition;
            final int boatBottomEdgePosition = currentYPosition
                    + (boatSize - boatCenter);

            if (boatBottomEdgePosition >= numberOfSquaresOnY
                    || boatUpEdgePosition < 1)
            {

                startingYPosition = (boatBottomEdgePosition >= numberOfSquaresOnY) ? (numberOfSquaresOnY - boatSize)
                        : 1;
                System.out.println("StartingXPosition" + startingYPosition);
            }
            for (int y = 0; y < boatSize; y++)
            {
                final Square squareForTmpBoatPlacing = mSquares[startingYPosition
                        + y][pCurrentSquare.getX()];

                if (!squareForTmpBoatPlacing.isFree())
                {
                    isFreeSquare = false;
                }
                listOfSquareToUpdate.add(squareForTmpBoatPlacing);
            }
            break;
        }
        default:
            break;
        }
        StateEnum state = isFreeSquare ? pState : State.StateEnum.FORBIDDEN;

        for ( final Square square : listOfSquareToUpdate )
        {
            if (pIsTemporary)
            {
                square.setTemporaryState(state);
            } else if (isFreeSquare)
            {
                square.setState(state);
            }
        }
        return isFreeSquare;
    }

    public void setGridDisplayEnabled(boolean pEnable)
    {
        if (pEnable)
        {
            addMouseListener(this);
            addMouseMotionListener(this);
        } else
        {
            removeMouseListener(this);
            removeMouseMotionListener(this);
        }
    }
}