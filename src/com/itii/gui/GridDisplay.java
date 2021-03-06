package com.itii.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.itii.data.Boat;
import com.itii.data.State;
import com.itii.data.State.GamePhase;
import com.itii.data.State.StateEnum;
import com.itii.manager.TurnManager;
import com.itii.network.Message;
import com.itii.network.MessageType;
import com.itii.network.Sender;

@SuppressWarnings("serial")
public class GridDisplay
    extends JPanel
    implements MouseListener,
               MouseMotionListener
{

    private final boolean mOpponent;

    private final Square[][] mSquares;

    private ArrayList<Square> mTemporarySquares;


    public GridDisplay ( final int      pGridSize,
                         final boolean  pOpponent )
    {
        setBackground( pOpponent
                ? new Color(100,100,150)
                        : new Color(200,100,100));
        mOpponent=  pOpponent;
        mSquares=  new Square[pGridSize + 1][ pGridSize + 1];

        setMinimumSize( new Dimension( pGridSize * Square.DEFAULT_SIZE,  pGridSize * Square.DEFAULT_SIZE) );
        setSize( new Dimension( pGridSize * Square.DEFAULT_SIZE,  pGridSize * Square.DEFAULT_SIZE)  );
        initializeGrid();

        setGridDisplayEnabled( ! pOpponent );
    }

    private  void initializeGrid()
    {
        this.setLayout( new GridLayout( mSquares.length,  mSquares.length ) );

        for (int y=  0; y < mSquares.length; y++)
        {
            for (int x=  0; x < mSquares[y].length; x++)
            {
                if ( x == 0 ^ y == 0 )
                {
                    mSquares[y][x]=  new SurroundingSquare( (short)x, (short) y);
                }
                else
                {
                    mSquares[y][x]=  new Square((short)x, (short)y);
                }
                this.add( mSquares[y][x] );
            }
        }
    }

    @Override
    public void mouseClicked ( MouseEvent pMouseEvent )
    {
        if (   TurnManager.getInstance().getGamePhase() == GamePhase.DEPLOYMENT
            || TurnManager.getInstance().getGamePhase() == GamePhase.OPPONENT_READY )
        {
            GridDisplay       grid_display=  (GridDisplay)pMouseEvent.getSource();
            Square         square_selected=  (Square)grid_display.getComponentAt( pMouseEvent.getPoint() );

            final Boat boat_being_added=   (Boat) MainWindow.getInstance()
                                                           .getDesk()
                                                           .getGameMenu()
                                                           .getBoatComboBox()
                                                           .getSelectedItem();
            if ( boat_being_added != null )
            {
                boolean has_been_added=  updateSquareDependingOnSelectedBoat( square_selected,
                                                                              boat_being_added,
                                                                              State.StateEnum.BOAT,
                                                                              false );

                if ( has_been_added )
                {
                    // Once the boat has been placed we delete it from the combo box.
                    JComboBox<Boat> boat_combo_box=   MainWindow.getInstance()
                                                          .getDesk()
                                                          .getGameMenu()
                                                          .getBoatComboBox();

                    boat_combo_box.removeItemAt( boat_combo_box.getSelectedIndex() );

                    if ( boat_combo_box.getItemCount() == 0 )
                    {
                        TurnManager.getInstance().setGamePhase( GamePhase.DEPLOYMENT_ENDED );
                    }

                }
            }
        }
        else if ( TurnManager.getInstance().getGamePhase() == GamePhase.PLAYER_TURN )
        {
            System.out.println( "Mouse Clicked : "
                    + " X: " + pMouseEvent.getX()
                    + " Y: " + pMouseEvent.getY());

            GridDisplay grid_display=        (GridDisplay) pMouseEvent.getSource();

            Square  square_displaying_boat=  (Square)grid_display.getComponentAt( pMouseEvent.getPoint() );

            if (   square_displaying_boat.getState() != State.StateEnum.BOAT_HIT
                && square_displaying_boat.getState() != State.StateEnum.HIT   )
            {
                System.out.println("Sending message with SQUARE_HIT_ID and coordinates");
                Sender.getInstance().sendPlayerMessage( new Message( MessageType.SQUARE_HIT_ID,
                                                                     square_displaying_boat.getCoordinates().toString() ) );

                square_displaying_boat.setState( StateEnum.HIT );
            }
        }


        repaint();
    }

    @Override
    public void mouseEntered ( MouseEvent arg0 )
    {
        // Nothing
    }

    @Override
    public void mouseExited ( MouseEvent arg0 )
    {
        // Nothing
    }

    @Override
    public void mousePressed ( MouseEvent arg0 )
    {
        // Nothing
    }

    @Override
    public void mouseReleased ( MouseEvent arg0 )
    {
        // Nothing
    }

    @Override
    public void mouseDragged ( MouseEvent arg0 )
    {
        // Nothing
    }

    @Override
    public void mouseMoved ( MouseEvent pMouseEvent )
    {
        GridDisplay grid_display=        (GridDisplay) pMouseEvent.getSource();

        Boat boat_being_added=           (Boat) MainWindow.getInstance().getDesk().getGameMenu()
                                                                           .getBoatComboBox()
                                                                           .getSelectedItem();
        if (   ( boat_being_added != null )
            && (grid_display.getComponentAt( pMouseEvent.getPoint() ) instanceof Square ) )
        {
            Square  square_displaying_boat=  (Square) (grid_display.getComponentAt( pMouseEvent.getPoint() ));

            // If it remains some boat to add we display them on the grid. Else we jump to next step.
            updateSquareDependingOnSelectedBoat( square_displaying_boat,
                                                 boat_being_added,
                                                 State.StateEnum.PLACING_BOAT,
                                                 true );
            System.out.println( "Mouse Mouved : "
                    + " X: " + pMouseEvent.getX()
                    + " Y: " + pMouseEvent.getY());
        }
        repaint();

    }

    @Override
    public void paint ( Graphics g )
    {
        super.paint( g );
    }

    /**
     * Update the drawing of the specified boat on the grid.
     * @param pCurrentSquare is the current square where a mouse is pointing
     * @param pCurrentBoatBeingAdded is the current boat to be displayed on the grid
     * @return a boolean representing the update status. If it cannot be updated return false.
     */
    public boolean updateSquareDependingOnSelectedBoat ( final Square     pCurrentSquare,
                                                         final Boat       pCurrentBoatBeingAdded,
                                                         final StateEnum  pState,
                                                         final boolean    pIsTemporary)
    {
        freeTemporarySquareState();

        final ArrayList< Square > listOfSquareToUpdate=  new ArrayList< Square >();
        boolean isFreeSquare=  true;

        final int boatSize=                 pCurrentBoatBeingAdded.getBoatType().getLength();
        final int boatCenter=               pCurrentBoatBeingAdded.getCenter();

        switch( pCurrentBoatBeingAdded.getOrientation() )
        {

        case VERTICAL :
        {
            // Verify if we cross the boarders by comparing boat size, current position and grid size
            final int numberOfSquaresOnY=       mSquares.length;
            final int currentYPosition=         pCurrentSquare.getYIndex() - 1;

            // Upper Edge of the current boat
            final int boatUpEdgePosition=  currentYPosition + ( boatSize - boatCenter );
            // Bottom Edge of the current boat (could be the same as the Upper if boat is HORIZONTALY displayed
            final int boatBottomEdgePosition=   currentYPosition - ( boatCenter - 1 );

            int startingYPosition=  boatBottomEdgePosition;
            int startingXPosition=  pCurrentSquare.getXIndex();

            // Calculate the minimum / maximum diplayable Y Coordinate
            if (   boatUpEdgePosition >= numberOfSquaresOnY
                || boatBottomEdgePosition < 1 )
            {
                // As the boat is crossing the limit of the grid we shift it inside the grid
                startingYPosition=    boatUpEdgePosition >= numberOfSquaresOnY
                                                ? ( numberOfSquaresOnY - boatSize )
                                                : 1;
            }
            if ( startingXPosition < 1 )
            {
                startingXPosition=  1;
            }

            for (int y=0; y < boatSize ; y++)
            {
                final Square  square_for_tmp_boat_placing=  mSquares[startingYPosition + y][startingXPosition];
                // If one square is not currenlty "free", then we don't allow placing the boat.
                if ( ! square_for_tmp_boat_placing.isFree() )
                {
                    isFreeSquare=  false;
                }
                listOfSquareToUpdate.add( square_for_tmp_boat_placing );
            }
            break;
        }
        case HORIZONTAL :
        {
            // Verify if we cross the boarders by comparing boat size, current position and grid size
            final int numberOfSquaresOnX=       mSquares[0].length;
            final int currentXPosition=         pCurrentSquare.getXIndex() - 1;

            final int boatRightEdgePosition=  currentXPosition + ( boatSize - boatCenter );
            final int boatLeftEdgePosition=   currentXPosition - ( boatCenter - 1 );

            int startingXPosition=  boatLeftEdgePosition;
            int startingYPosition=  pCurrentSquare.getYIndex();

            if (   boatRightEdgePosition >= numberOfSquaresOnX
                || boatLeftEdgePosition < 1 )
            {
                // As the boat is crossing the limit of the grid we shift it inside the grid
                startingXPosition=    boatRightEdgePosition >= numberOfSquaresOnX
                                                ? ( numberOfSquaresOnX - boatSize )
                                                : 1;
            }
            if ( startingYPosition < 1 )
            {
                startingYPosition=  1;
            }

            for (int x=0; x < boatSize ; x++)
            {
                final Square  square_for_tmp_boat_placing=  mSquares[startingYPosition][startingXPosition + x];
                // If one square is not currenlty "free", then we don't allow placing the boat.
                if ( ! square_for_tmp_boat_placing.isFree() )
                {
                    isFreeSquare=  false;
                }
                listOfSquareToUpdate.add( square_for_tmp_boat_placing );
            }
            break;
        }

        default :

        }

        // Verify if the boat could be added to the current position.
        // It must not have any boat already on this square
        StateEnum state=  isFreeSquare
                ? pState
                : State.StateEnum.FORBIDDEN;

        for ( final Square square : listOfSquareToUpdate )
        {
            if ( pIsTemporary )
            {
                square.setTemporaryState( state );
            }
            else if (   ! pIsTemporary
                     && isFreeSquare )
            {
                square.setState( state );
            }
        }

        setTemporarySquaresToUpdate( listOfSquareToUpdate );

        return isFreeSquare;
    }


    public void freeGrid()
    {
        for ( int y=0; y < mSquares.length; y++ )
        {
            for ( int x=0; x < mSquares[y].length; x++ )
            {
                mSquares[y][x].setState(StateEnum.EMPTY);
                System.out.println("freeGrid :" + mOpponent);
            }
        }
    }

    /**
     *  Free all temporary status from the grid
     */
    public void freeAllTemporarySquareState()
    {
        for ( int y=0; y < mSquares.length; y++ )
        {
            for ( int x=0; x < mSquares[y].length; x++ )
            {
                mSquares[y][x].freeTemporaryState();
                System.out.println("freeAllTemporarySquareState :" + mOpponent);
            }
        }
    }

    /**
     * Free the Square that were updated.
     */
    public void freeTemporarySquareState()
    {
        if ( mTemporarySquares != null )
        {
            for ( final Square square : mTemporarySquares )
            {
                square.freeTemporaryState();
                System.out.println("freeTemporarySquareState :" + mOpponent);
            }
        }
    }

    public void setTemporarySquaresToUpdate( final ArrayList<Square> pTemporarySquares )
    {
        mTemporarySquares=  pTemporarySquares;
    }

    public void setGridDisplayEnabled( final boolean pIsEnabled )
    {

        if ( pIsEnabled )
        {
            this.addMouseListener( this );
            this.addMouseMotionListener( this );
        }
        else
        {
            this.removeMouseListener( this );
            this.removeMouseMotionListener( this );
        }

    }

    /**
     * Get the square at the specific coordinates
     */
    public Square getSquareAt ( final short x, final short y )
    {
        return mSquares[y][x];
    }

    /**
     * @return the number of free squares of boats unsunk. 
     */
    public final int getNumberOfBoatSquareUnsunk()
    {

        int boatUnsunk=  0;
        for (int y=  0; y < mSquares.length; y++)
        {
            for (int x=  0; x < mSquares[y].length; x++)
            {
                if ( mSquares[y][x].getState() == State.StateEnum.BOAT_HIT )
                {
                    boatUnsunk++;
                }
            }
        }
        return boatUnsunk;
    }
}
