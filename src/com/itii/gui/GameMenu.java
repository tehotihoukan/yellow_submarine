package com.itii.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.itii.data.Boat;
import com.itii.data.State.GamePhase;
import com.itii.data.boats.AircraftCarrier;
import com.itii.data.boats.Battleship;
import com.itii.data.boats.Cruiser;
import com.itii.data.boats.Destroyer;
import com.itii.data.boats.Submarine;
import com.itii.manager.TurnManager;
import com.itii.network.Message;
import com.itii.network.MessageType;
import com.itii.network.Sender;

@SuppressWarnings("serial")
public class GameMenu
    extends JPanel
    implements ActionListener
{
    private ActionEvent mActionEvent;

    private final String    ACTION_ROTATE_BOAT_BUTTON=  "ACTION_ROTATE_BOAT_BUTTON";
    private JButton         mRotateBoatButton;

    private final String   ACTION_READY_BUTTON =  "ACTION_READY_BUTTON";
    private JButton   mReadyButton;

    private final String    ACTION_JOIN_BUTTON=  "ACTION_JOIN_BUTTON";
    private JButton   mJoinButton;

    private JComboBox<Boat> mBoatComboBox;

    private JLabel    mBoatAvailableLabel;

    private JButton   mHitButton;

    private JButton   mSurrenderButton;

    private final String    ACTION_RESTART_BUTTON= "ACTION_RESTART_BUTTON";
    private JButton   mRestartButton;

    private final String    ACTION_QUIT_BUTTON=  "ACTION_QUIT_BUTTON";
    private JButton         mQuitButton;

    public GameMenu ()
    {
        initialize();
    }

    public void initialize()
    {


        setBackground( ColorSet.GAME_MENU_BG );
//        setSize( 50, getHeight() );
        validate();
        GridLayout layout=   new GridLayout(10, 0);
        this.setLayout( layout );

        this.add( getJoinButton() );
        this.add( getBoatAvailableLabel() );
        this.add( getBoatComboBox() );
        this.add( getRotateBoatButton() );
        this.add( getReadyButton() );
        this.add( getHitButton() );
        this.add( getSurrenderButton() );
        this.add( getRestartButton() );
        this.add( getQuitButton() );
        this.add( new JPanel()
                {
                    @Override
                    public Color getBackground()
                    {
                        return Color.BLUE;
                    }
                });

    }

    @Override
    public void actionPerformed ( ActionEvent pActionEvent )
    {
        mActionEvent=  pActionEvent;
        if ( ! SwingUtilities.isEventDispatchThread() )
        {
            System.out.println( "not in EDT" );
        }

        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run ()
            {
                if ( ACTION_ROTATE_BOAT_BUTTON.equals( mActionEvent.getActionCommand() ) )
                {
                    Boat boat_being_added=   (Boat) MainWindow.getInstance()
                                                              .getDesk()
                                                              .getGameMenu()
                                                              .getBoatComboBox()
                                                              .getSelectedItem();
                    boat_being_added.flipOrientation( );
                }

                else if ( ACTION_QUIT_BUTTON.equals( mActionEvent.getActionCommand() ))
                {
                    Sender.getInstance().sendPlayerMessage(
                            new Message ( MessageType.PLAYER_LEFT_ID, "" ) );

                    System.exit( 0 );
                }
                else if ( ACTION_READY_BUTTON.equals( mActionEvent.getActionCommand() ) )
                {
                    mReadyButton.setEnabled( false );

                    TurnManager.getInstance().setGamePhase( TurnManager.getInstance().isOpponentReady()
                                                            ? GamePhase.READY
                                                            : GamePhase.PLAYER_READY );

                    final Sender sender=   Sender.getInstance();
//                    final double discriminantValue=  Math.random();
//                    TurnManager.getInstance().setStartValueForCurrentPlayer( discriminantValue );
                    sender.sendPlayerMessage( new Message ( MessageType.READY_ID, null ) );
                }

                else if ( ACTION_JOIN_BUTTON.equals( mActionEvent.getActionCommand() ))
                {
                    String ipAddress=  JOptionPane.showInputDialog( new JFrame(),
                                                                    "Enter IP Address and Port number in format {255.225.225.225}",
                                                                    "127.0.0.1");
//                    int i=  ipAddress.split("\\d+").length;
                    if ( ipAddress.split("\\.").length == 4 )
                    {
                        final Sender sender=   Sender.getInstance();
                        {
                            sender.setHost(ipAddress);
                            sender.setPort( 8888 );
                            sender.initialize();

                            sender.sendPlayerMessage( new Message( MessageType.PLAYER_JOINED_ID,
                                                                   "192.168.0.11" ) ); // Envoi de "mon" IP

                            getJoinButton().setEnabled( false );
                            TurnManager.getInstance().setGamePhase(
                                    TurnManager.getInstance().isOpponentPlayerAvailable()
                                        ? GamePhase.DEPLOYMENT
                                        : GamePhase.WAITING_FOR_OPPONENT
                                    );

                        }

                    }
                }
                else if ( ACTION_RESTART_BUTTON.equals( mActionEvent.getActionCommand() ) )
                {
                    TurnManager.getInstance().setGamePhase( GamePhase.STARTING );
                    MainWindow.getInstance().getDesk().getAdversaireGrid().freeGrid();
                    MainWindow.getInstance().getDesk().getJoueurGrid().freeGrid();
                }
            }
        });
    }





    public JButton getRotateBoatButton()
    {
        if ( mRotateBoatButton == null )
        {
            mRotateBoatButton=       new JButton("Rotate boat");
            mRotateBoatButton.setActionCommand( ACTION_ROTATE_BOAT_BUTTON );
            mRotateBoatButton.addActionListener( this );
        }

        return mRotateBoatButton;

    }


    public JButton getJoinButton()
    {
        if ( mJoinButton == null )
        {
            mJoinButton =            new JButton("Join Server");
            mJoinButton.setActionCommand( ACTION_JOIN_BUTTON );
            mJoinButton.addActionListener( this );
        }
        return mJoinButton ;
    }


    public JButton getReadyButton()
    {
        if ( mReadyButton == null )
        {
            mReadyButton=            new JButton("Ready");
            mReadyButton.setActionCommand( ACTION_READY_BUTTON );
            mReadyButton.addActionListener( this );
        }
        return mReadyButton;
    }

    public JComboBox<Boat> getBoatComboBox()
    {
        if ( mBoatComboBox == null )
        {
            final Boat[] boats= {
                                  new AircraftCarrier(),
                                  new Battleship(),
                                  new Cruiser(),
                                  new Submarine(),
                                  new Destroyer() };
            mBoatComboBox=  new JComboBox<Boat>( boats );
            Boat.countTotalNumberOfBoatSquares( boats );
        }
        return mBoatComboBox;
    }

    public JLabel getBoatAvailableLabel()
    {
        if ( mBoatAvailableLabel == null )
        {
            mBoatAvailableLabel=  new JLabel("Available Boats");
        }
        return mBoatAvailableLabel;
    }

    public JButton getHitButton()
    {
        if ( mHitButton == null )
        {
            mHitButton=              new JButton("Hit");
        }
        return mHitButton;

    }

    public JButton getSurrenderButton()
    {
        if ( mSurrenderButton == null )
        {
            mSurrenderButton=        new JButton("Surrender");
        }
        return mSurrenderButton;
    }

    public JButton getRestartButton()
    {
        if ( mRestartButton == null )
        {
            mRestartButton=          new JButton("Restart");
            mRestartButton.addActionListener(this);
            mRestartButton.setActionCommand(ACTION_RESTART_BUTTON);
        }
        return mRestartButton;
    }


    public JButton getQuitButton()
    {
        if ( mQuitButton == null )
        {
            mQuitButton=             new JButton("Quit");
            mQuitButton.setActionCommand( ACTION_QUIT_BUTTON );
            mQuitButton.addActionListener( this );
        }
        return mQuitButton;
    }

}
