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

public class GameMenu extends JPanel implements ActionListener
{

    private JComboBox<Boat> mBoatComboBox;
    private JLabel mBoatAvailableLabel = new JLabel("test");

    /** Button used to rotate the current boat **/
    private final String ACTION_ROTATE_BOAT_BUTTON = "rotate";
    private JButton mRotateBoatButton;

    private final String ACTION_READY_BUTTON = "ready";
    private JButton mReadyButton;

    private final String ACTION_JOIN_BUTTON = "join";
    /** Buton used to join the opponent **/
    private JButton mJoinButton;
    /** and button used to hit the opponent **/
    private JButton mHitButton;
    private JButton mSurrenderButton;
    private JButton mRestartButton;

    private final String ACTION_QUIT_BUTTON = "quit";
    private JButton mQuitButton;

    public GameMenu()
    {
        initialize();
    }

    public void initialize()
    {
//        setSize(100, getHeight());
        setBackground(Color.RED);
        setLayout(new GridLayout(10, 0));

        add(getJoinButton());
        add(mBoatAvailableLabel);
        add(getBoatComboBox());
        add(getRotateBoatButton());

        add(getReadyButton());
        add(getHitButton());
        add(getSurrenderButton());

        add(getRestartButton());
        add(getQuitButton());

    }

    public JButton getRestartButton()
    {
        if (mRestartButton == null)
        {
            mRestartButton = new JButton("Restart");
        }
        return mRestartButton;
    }

    public JButton getReadyButton()
    {
        if (mReadyButton == null)
        {
            mReadyButton = new JButton("Ready");
            mReadyButton.setActionCommand(ACTION_READY_BUTTON);
            mReadyButton.addActionListener(this);
        }
        return mReadyButton;
    }

    public JButton getHitButton()
    {
        if (mHitButton == null)
        {
            mHitButton = new JButton("Hit");
        }
        return mHitButton;
    }

    public JButton getSurrenderButton()
    {
        if (mSurrenderButton == null)
        {
            mSurrenderButton = new JButton("Surrender");
        }
        return mSurrenderButton;
    }

    public JButton getJoinButton()
    {
        if (mJoinButton == null)
        {
            mJoinButton = new JButton("Join");
            mJoinButton.setActionCommand(ACTION_JOIN_BUTTON);
            mJoinButton.addActionListener(this);
        }
        return mJoinButton;
    }

    public JButton getQuitButton()
    {
        if (mQuitButton == null)
        {
            mQuitButton = new JButton("Quit");
            mQuitButton.setActionCommand(ACTION_QUIT_BUTTON);
            mQuitButton.addActionListener(this);

        }
        return mQuitButton;
    }

    public JComboBox<Boat> getBoatComboBox()
    {
        if (mBoatComboBox == null)
        {
            Boat[] boats = { new AircraftCarrier(),
//                             new Battleship(),
//                             new Submarine(),
//                             new Submarine(),
//                             new Destroyer(),
//                             new Destroyer(),
                             new Cruiser()
            };
            mBoatComboBox = new JComboBox<Boat>(boats);
            Boat.countTotalNumberOfBoatSquares(boats);
        }
        return mBoatComboBox;
    }

    public JButton getRotateBoatButton()
    {
        if (mRotateBoatButton == null)
        {
            mRotateBoatButton = new JButton("Rotate");
            mRotateBoatButton.setActionCommand(ACTION_ROTATE_BOAT_BUTTON);
            mRotateBoatButton.addActionListener(this);
        }

        return mRotateBoatButton;
    }

    ActionEvent mActionEvent;

    @Override
    public void actionPerformed(ActionEvent pActionEvent)
    {
        mActionEvent = pActionEvent;

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                if ( ACTION_ROTATE_BOAT_BUTTON.equals(mActionEvent.getActionCommand()) )
                {
                    ((Boat) mBoatComboBox.getSelectedItem()).flipOrientation();
                }
                else if ( ACTION_QUIT_BUTTON.equals(mActionEvent.getActionCommand()) )
                {
                    Sender.getInstance()
                          .sendPlayerMessage( new Message(MessageType.PLAYER_LEFT_ID, "") );
                }
                else if ( ACTION_READY_BUTTON.equals(mActionEvent.getActionCommand()) )
                {
                    mReadyButton.setEnabled(false);
                    TurnManager
                            .getInstance()
                            .setGamePhase(
                                    TurnManager.getInstance().isOpponentReady() ? GamePhase.READY
                                            : GamePhase.PLAYER_READY);

                    final Sender sender = Sender.getInstance();
                    sender.sendPlayerMessage(new Message(MessageType.READY_ID));

                }
                else if ( ACTION_JOIN_BUTTON.equals(mActionEvent.getActionCommand()))
                {
                    String ipAddress = JOptionPane.showInputDialog( new JFrame(),
                                                                    "Enter IP Address and Port number in format {255.255.255.255}",
                                                                    "127.0.0.1");

                    if (ipAddress.split("\\.").length == 4)
                    {
                        final Sender sender = Sender.getInstance();
                        {
                            sender.setHost(ipAddress);
                            sender.setPort(8888);
                            sender.initialize();

                            sender.sendPlayerMessage(new Message(
                                    MessageType.PLAYER_JOINED_ID,
                                    "192.168.1.35"));

                            mJoinButton.setEnabled(false);
                            TurnManager
                                    .getInstance()
                                    .setGamePhase(
                                            TurnManager
                                                    .getInstance()
                                                    .isOpponentPlayerAvailable() ? GamePhase.DEPLOYMENT
                                                    : GamePhase.WAITING_FOR_OPPONENT);

                        }
                    }
                }
            }
        });

    }
}
