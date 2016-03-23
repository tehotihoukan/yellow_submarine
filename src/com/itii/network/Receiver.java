package com.itii.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.itii.data.Coordinates;
import com.itii.data.State.GamePhase;
import com.itii.data.State.StateEnum;
import com.itii.gui.MainWindow;
import com.itii.gui.Square;
import com.itii.manager.TurnManager;

public class Receiver
    extends Thread
{
    
    private int mPort;

    private static Receiver instance;
    
    private Receiver() {}

    public static Receiver getInstance ()
    {
        if (instance == null)
        {
            instance= new Receiver();
        }
        return instance;
    }
    
    @Override
    public void run ()
    {
        
        Socket socket;
        ServerSocket serverSocket;
        
        try
        {
            serverSocket=  new ServerSocket( mPort );
            socket=  serverSocket.accept();
            /**
             * Vos explications ici
             */
             boolean ended=  false;
             while ( ! ended )
             {        
                 try
                 {
                     BufferedReader in= new BufferedReader(
                          new InputStreamReader(socket.getInputStream()) );
                 
                     String msg=  in.readLine();
                     System.out.println("start readMessageFromOpponent");        
                     readMessageFromOpponent( msg );
                     System.out.println("end readMessageFromOpponent");
                 }
                 catch (IOException e)
                 {
                     e.printStackTrace();
                     ended= true;
                 }            
             }
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public synchronized void start ()
    {
        Thread thread= new Thread(this);    
        thread.start();
    }

    public int getPort ()
    {
        return mPort;
    }

    public void setPort ( int mPort )
    {
        this.mPort= mPort;
    }
    
    
    
    public void readMessageFromOpponent( final String pMessage )
    {
            
        final Message             msg=  MessageType.getMessage( pMessage );  
        final TurnManager turnManager=  TurnManager.getInstance();
        
        System.out.println("Message reçu par Receiver : " + msg );
        switch ( msg.getId() )
        {
            case MessageType.PLAYER_JOINED_ID :
            {
                final Sender sender=   Sender.getInstance();
                sender.setHost( msg.getMessage() );
                sender.setPort( 8888 );
                sender.initialize();
                if ( ! TurnManager.getInstance().isCurrentPlayerAvailable() )
                {
                    sender.sendPlayerMessage( new Message( MessageType.PLAYER_JOINED_ID, 
                                                          "192.168.1.11" ) );
                    TurnManager.getInstance().setCurrentPlayerAvailable( true );
                }
                // Opponent joined
                turnManager.setGamePhase( 
                        TurnManager.getInstance().isCurrentPlayerAvailable() 
                            ? GamePhase.DEPLOYMENT
                            : GamePhase.WAITING_FOR_YOU
                        );
                
                break;
            }
            case MessageType.READY_ID :
            {
//                turnManager.setStartValueForOpponent( Double.parseDouble( msg.getMessage() ) );
                
                GamePhase realGamePhase=  ( TurnManager.getInstance().getGamePhase() == GamePhase.PLAYER_READY ) 
                                               ? GamePhase.READY
                                               : GamePhase.OPPONENT_READY; 
                        
//                if ( realGamePhase == GamePhase.OPPONENT_READY )
//                {
//                    TurnManager.getInstance().setPlayerTurn( false );
//                }
                
//                else 
                if ( realGamePhase == GamePhase.READY )
                {
                    realGamePhase=  TurnManager.getInstance().isPlayerTurn()
                                        ? GamePhase.PLAYER_TURN 
                                        : GamePhase.OPPONENT_TURN;
                }
                    
                turnManager.setGamePhase(realGamePhase);
                
//                if ( realGamePhase == GamePhase.READY )
//                {
//                    realGamePhase =  turnManager.isCurrentPlayerFirstToStart() 
//                                         ? GamePhase.PLAYER_TURN 
//                                         : GamePhase.OPPONENT_TURN;
//                }
//                turnManager.setGamePhase( realGamePhase );

                break;
                //ajoutez ici tous les cas, et n’oubliez pas les "break"
            }
            
            case MessageType.SQUARE_HIT_ID :
            {
                // We update the opponent player grid
                final Coordinates coordinates= Coordinates.getCoordinatesFromString( pMessage );
                
                final Square square=   MainWindow.getInstance()
                                                 .getDesk()
                                                 .getJoueurGrid()
                                                 .getSquareAt( coordinates.getX(),  coordinates.getY() );
                
                square.updateState( StateEnum.HIT );
                // Then we send back the square status.
                Sender.getInstance().sendPlayerMessage( new Message( MessageType.SQUARE_UPDATE_ID, square.getCoordinates().toString() +" "+ square.getState().toString() ) );
                
                if ( square.getState() != StateEnum.BOAT_HIT )  // !SM!
                {
                    turnManager.setGamePhase( GamePhase.PLAYER_TURN );
                }
                
                break;
            }   
            case MessageType.SQUARE_UPDATE_ID :
            {
                final Coordinates coordinates=  Coordinates.getCoordinatesFromString( pMessage );
                final Square square=     MainWindow.getInstance()
                                                   .getDesk()
                                                   .getAdversaireGrid()
                                                   .getSquareAt( coordinates.getX(),  coordinates.getY() );
                final StateEnum state=           StateEnum.valueOf( pMessage.substring( 8 ) );
                square.setState( state );
                
                
                if ( square.getState() != StateEnum.BOAT_HIT ) // !SM!
                {
                    TurnManager.getInstance().setGamePhase( GamePhase.OPPONENT_TURN );
                    
                }
                
                // Verify if all boats aren't sunk
                if ( MainWindow.getInstance().getDesk().isOpponentSunk() )
                {
                    turnManager.setGamePhase( GamePhase.ENDED );
                    Sender.getInstance().sendPlayerMessage( new Message( MessageType.END_ID,
                                                                         null    ) );
                }

                break;
            }
            case MessageType.END_ID :
            {
                turnManager.setGamePhase( GamePhase.ENDED );
            }
                
        }
        
        MainWindow.getInstance().getDesk().repaint();
        
    }

    
}
