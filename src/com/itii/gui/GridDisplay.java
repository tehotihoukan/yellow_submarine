package com.itii.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
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

public class GridDisplay 
    extends JPanel
    implements MouseListener,
               MouseMotionListener
{

    int     mGridSize;
    final boolean mOpponent;
    
    final Square[][] mSquares;
    
    public GridDisplay ( final int      pGridSize,
                         final boolean  pOpponent )
    {
        mOpponent=  pOpponent;
        mGridSize=  pGridSize+1;
        
        mSquares=  new Square[pGridSize + 1][ pGridSize + 1];
        
        setMinimumSize( new Dimension( pGridSize * Square.DEFAULT_SIZE,  pGridSize * Square.DEFAULT_SIZE) );
        setSize( new Dimension( pGridSize * Square.DEFAULT_SIZE,  pGridSize * Square.DEFAULT_SIZE)  );
        initializeGrid();
        
        setGridDisplayEnabled( ! pOpponent );
    }
    
    private  void initializeGrid()
    {
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
            
            Boat boat_being_added=   (Boat) MainWindow.getInstance()
                                               .getDesk()
                                               .getGameMenu()
                                               .getBoatComboBox()
                                               .getSelectedItem();
            if ( boat_being_added != null )
            {
                boolean has_been_added=  updateSquareDependingOnBoatSelected( square_selected, 
                                                                              boat_being_added, 
                                                                              State.StateEnum.BOAT,
                                                                              false );
               
                if ( has_been_added )
                {
                    // Once the boat has been placed we delete it from the combo box.
                    JComboBox boat_combo_box=   MainWindow.getInstance()
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
//                if ( square_displaying_boat.getState() != StateEnum.BOAT_HIT ) // !SM!
//                    TurnManager.getInstance().setGamePhase( GamePhase.OPPONENT_TURN );
            }
        }

        
        repaint();
    }

    @Override
    public void mouseEntered ( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited ( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed ( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased ( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged ( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved ( MouseEvent pMouseEvent )
    {
        GridDisplay grid_display=        (GridDisplay) pMouseEvent.getSource();
        
        Boat boat_being_added=           (Boat) MainWindow.getInstance().getDesk().getGameMenu()
                                                                           .getBoatComboBox()
                                                                           .getSelectedItem();
        if ( boat_being_added != null )
        {
            Square  square_displaying_boat=  (Square)(grid_display.getComponentAt( pMouseEvent.getPoint() ));
            
            // If it remeains some boat to add we display them on the grid. Else we jump to next step.
            updateSquareDependingOnBoatSelected( square_displaying_boat, 
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
        
        final Color prev_color=  g.getColor();
        
        Color bgColor=  new Color(100, 100, 200);
        g.setColor( bgColor );
        g.fillRect( 0, 0, getWidth(), getHeight() );
        g.setColor( prev_color );

        // Display the grid itself.
        final int squareEdgeSize=  Math.min( getWidth() / mSquares.length ,
                                             getHeight() / mSquares[0].length );
//        final float gridSize=  Math.min( getWidth(), getHeight() );
        
        mGridSize=  squareEdgeSize * mSquares.length;

        //        System.out.println("squareEdgeSize" + squareEdgeSize);
        for (int y = 0; y < mSquares.length ; y ++ )
        {
            for (int x = 0; x < mSquares[y].length ; x ++ )
            {
                mSquares[y][x].paintSquare( g, squareEdgeSize );                
            }
        }
        
        g.setColor( prev_color );
    }

    
    
    
    /**
     * Get the component at the given coordinate (in pixel on the panel)
     */
    @Override
    public Component getComponentAt ( final int x, final int y )
    {
        int xLocation=  (int) ( ( (float)x / (float)mGridSize ) * mSquares[0].length );
        int yLocation=  (int) ( ( (float)y / (float)mGridSize ) * mSquares.length );
        
        xLocation =  Math.min( xLocation, mSquares[0].length - 1 );
        yLocation =  Math.min( yLocation, mSquares.length - 1 ); 

        return mSquares[yLocation][xLocation];
    }    
    
    
    
    
    
    
    /**
     * Update the drawing of the specified boat on the grid.
     * @param pCurrentSquare is the current square where a mouse is pointing
     * @param pCurrentBoatBeingAdded is the current boat to be displayed on the grid
     * @return a boolean representing the update status. If it cannot be update return false.
     */
    public boolean updateSquareDependingOnBoatSelected ( final Square     pCurrentSquare,
                                                         final Boat       pCurrentBoatBeingAdded,
                                                         final StateEnum  pState,
                                                         final boolean    pIsTemporary)
    {
        freeAllTemporarySquareState();
        
        final ArrayList< Square > listOfSquareToUpdate=  new ArrayList< Square >();
        boolean isFreeSquare=  true;

        final int boatSize=                 pCurrentBoatBeingAdded.getBoatType().getLength();
        final int boatCenter=               pCurrentBoatBeingAdded.getCenter();
        
        switch( pCurrentBoatBeingAdded.getOrientation() ) 
        {
    
        case HORIZONTAL :
         // Verify if we cross the boarders by comparing boat size, current position and grid size
            final int numberOfSquaresOnY=       mSquares.length;
            final int currentYPosition=         pCurrentSquare.getY() - 1;

            
            
            final int boatUpEdgePosition=  currentYPosition + ( boatSize - boatCenter );
            final int boatBottomEdgePosition=   currentYPosition - ( boatCenter - 1 ); 
            
            int startingYPosition=  boatBottomEdgePosition;
            
            if (   boatUpEdgePosition >= numberOfSquaresOnY
                || boatBottomEdgePosition < 1 )
            {
                // As the boat is crossing the limit of the grid we shift it inside the grid
                startingYPosition=    boatUpEdgePosition >= numberOfSquaresOnY 
                                                ? ( numberOfSquaresOnY - boatSize ) 
                                                : 1; 
            }
            
            for (int y=0; y < boatSize ; y++)
            {
                final Square  square_for_tmp_boat_placing=  mSquares[startingYPosition + y][pCurrentSquare.getX()];
                if ( ! square_for_tmp_boat_placing.isFree() )
                {
                    isFreeSquare=  false;
                }
                listOfSquareToUpdate.add( square_for_tmp_boat_placing );
            }
            break;
        case VERTICAL :
            
         // Verify if we cross the boarders by comparing boat size, current position and grid size
            final int numberOfSquaresOnX=       mSquares[0].length;
            final int currentXPosition=         pCurrentSquare.getX() - 1;
            
            final int boatRightEdgePosition=  currentXPosition + ( boatSize - boatCenter );
            final int boatLeftEdgePosition=   currentXPosition - ( boatCenter - 1 ); 
            
            int startingXPosition=  boatLeftEdgePosition;
            
            if (   boatRightEdgePosition >= numberOfSquaresOnX
                || boatLeftEdgePosition < 1 )
            {
                // As the boat is crossing the limit of the grid we shift it inside the grid
                startingXPosition=    boatRightEdgePosition >= numberOfSquaresOnX 
                                                ? ( numberOfSquaresOnX - boatSize ) 
                                                : 1; 
            }
            
            for (int x=0; x < boatSize ; x++)
            {
                final Square  square_for_tmp_boat_placing=  mSquares[pCurrentSquare.getY()][startingXPosition + x];
                if ( ! square_for_tmp_boat_placing.isFree() )
                {
                    isFreeSquare=  false;
                }
                listOfSquareToUpdate.add( square_for_tmp_boat_placing );
            }
            break;
            
        default :
                
        }
        
        // Verify if the boat could be added to the current position. 
        // It must not have any boat already on this square
        StateEnum state=  isFreeSquare
                ? pState
                : State.StateEnum.FORBIDDEN;

        for (Square square : listOfSquareToUpdate)
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
        
        return isFreeSquare;
    }
    
    
    public void freeGrid()
    {
        for ( int y=0; y < mSquares.length; y++ )
        {
            for ( int x=0; x < mSquares[y].length; x++ )
            {
                mSquares[y][x].setState(StateEnum.EMPTY);
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
            }
        }
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
     * @param x
     * @param y
     */
    public Square getSquareAt ( final short x, final short y )
    {
        return mSquares[y][x];
    }
    
    
    
    
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
