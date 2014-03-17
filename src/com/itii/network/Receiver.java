package com.itii.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingUtilities;

import com.itii.data.Coordinates;
import com.itii.data.State.GamePhase;
import com.itii.data.State.StateEnum;
import com.itii.gui.MainWindow;
import com.itii.gui.Square;
import com.itii.manager.TurnManager;

public class Receiver extends Thread
{

    private final static Receiver instance = new Receiver();

    private int mPortNumber;

    private Receiver()
    {
        // nothing
    }

    public void setPortNumber(final int pPortNumber)
    {
        mPortNumber = pPortNumber;
    }

    public static Receiver getInstance()
    {
        return instance;
    }

    @Override
    public void run()
    {

        try
        {
            ServerSocket serverSocket = new ServerSocket(mPortNumber);
            Socket socket = serverSocket.accept();

            boolean ended = false;

            while (!ended)
            {

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));

                String msg = in.readLine();

                readMessageFromOpponent(msg);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void readMessageFromOpponent(final String pMessage)
    {
        final Message msg = MessageType.getMessage(pMessage);
        final TurnManager turnManager = TurnManager.getInstance();

        Coordinates coordinates = null;
        Square square = null;
        System.out.println("message reçu par Receiver : " + msg);

        switch (msg.getID())
        {
        case MessageType.PLAYER_JOINED_ID:
        {
            final Sender sender = Sender.getInstance();
            sender.setPort(8888);
            sender.setHost(msg.getMessage());
            sender.initialize();
            if (!TurnManager.getInstance().isCurrentPlayerAvailable())
            {
                SwingUtilities.invokeLater(
                        new Runnable()
                        {
                            public void run()
                            {
                            sender.sendPlayerMessage(new Message( MessageType.PLAYER_JOINED_ID,
                                                                  "127.0.0.1" ) );
                            }
                        }
                        );
                TurnManager.getInstance()
                           .setCurrentPlayerAvailable(true);
            }
            turnManager.setGamePhase(TurnManager.getInstance()
                                                .isCurrentPlayerAvailable()
                                                    ? GamePhase.DEPLOYMENT
                                                    : GamePhase.WAITING_FOR_YOU);

            break;
        }
        case MessageType.END_ID:
        {
            TurnManager.getInstance().setGamePhase(GamePhase.ENDED);
            break;
        }
        case MessageType.NEXT_ID:
        {
            break;
        }
        case MessageType.PLAYER_LEFT_ID:
        {
            break;
        }
        case MessageType.READY_ID:
        {
            GamePhase updatedGamePhase = TurnManager.getInstance()
                    .getGamePhase() == GamePhase.PLAYER_READY ? GamePhase.READY
                    : GamePhase.OPPONENT_READY;
            if (updatedGamePhase == GamePhase.OPPONENT_READY)
            {
                TurnManager.getInstance().setCurrentPlayerTurn(false);
            } else if (updatedGamePhase == GamePhase.READY)
            {
                updatedGamePhase = TurnManager.getInstance()
                        .isCurrentPlayerTurn() ? GamePhase.PLAYER_TURN
                        : GamePhase.OPPONENT_TURN;
            }
            turnManager.setGamePhase(updatedGamePhase);
            break;
        }
        case MessageType.SQUARE_HIT_ID:
        {
            coordinates = Coordinates.readCoordinatesFromString(pMessage);
            final Square innerSquare = MainWindow.getInstance()
                                                 .getDesk()
                                                 .getPlayerGrid()
                                                 .getSquareAt( coordinates.getmX(), coordinates.getmY() );
            innerSquare.updateState( StateEnum.HIT );
            if (innerSquare.getState() != StateEnum.BOAT_HIT)
            {
                turnManager.setGamePhase(GamePhase.PLAYER_TURN);
            }
            // TODO !SM! Add a Java
            SwingUtilities.invokeLater(
                    new Runnable()
                    {
                        public void run()
                        {
                Sender.getInstance()
                      .sendPlayerMessage( new Message( MessageType.SQUARE_UPDATE_ID,
                                                       innerSquare.getCoordinates().toString()
                                                           + " "
                                                           + innerSquare.getState().toString()));
                        }
                    });
            break;
        }
        case MessageType.SUNK_ID:
        {
            break;
        }
        case MessageType.SQUARE_UPDATE_ID:
        {
            coordinates = Coordinates.readCoordinatesFromString(pMessage);
            square = MainWindow.getInstance()
                               .getDesk()
                               .getOpponentGrid()
                               .getSquareAt(coordinates.getmX(), coordinates.getmY());

            final StateEnum state = StateEnum.valueOf(pMessage.substring(7));

            square.setState(state);
            if (square.getState() != StateEnum.BOAT_HIT)
            {
                TurnManager.getInstance().setGamePhase(GamePhase.OPPONENT_TURN);
            }
            if (MainWindow.getInstance().getDesk().isOpponentSunk())
            {
                turnManager.setGamePhase(GamePhase.ENDED);
                SwingUtilities.invokeLater(
                        new Runnable()
                        {
                            public void run()
                            {
                                Sender.getInstance().sendPlayerMessage(
                                        new Message(MessageType.END_ID) );
                            }
                        } );
            }
            break;
        }
        default:
            break;
        }

        MainWindow.getInstance().getDesk().repaint();
    }
}
