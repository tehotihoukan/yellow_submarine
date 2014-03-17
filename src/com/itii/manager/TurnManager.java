package com.itii.manager;

import com.itii.data.State.GamePhase;
import com.itii.gui.GameMenu;
import com.itii.gui.MainWindow;

public class TurnManager
{

    private static TurnManager instance = new TurnManager();
    private GamePhase mGamePhase = GamePhase.STARTING;

    private boolean mOpponentReady = false;
    private boolean mCurrentPlayerAvailable = false;
    private boolean mOpponentPlayerAvailable = false;

    private boolean mCurrentPlayerTurn = true;

    private TurnManager()
    {

    }

    public boolean isOpponentReady()
    {
        return mOpponentReady;
    }

    public void setCurrentPlayerTurn(boolean pCurrentPlayerTurn)
    {
        this.mCurrentPlayerTurn = pCurrentPlayerTurn;
    }

    public boolean isCurrentPlayerTurn()
    {
        return mCurrentPlayerTurn;
    }

    public void setCurrentPlayerAvailable(boolean pCurrentPlayerAvailable)
    {
        mCurrentPlayerAvailable = pCurrentPlayerAvailable;
    }

    public boolean isCurrentPlayerAvailable()
    {
        return mCurrentPlayerAvailable;
    }

    public boolean isOpponentPlayerAvailable()
    {
        return mOpponentPlayerAvailable;
    }

    public static TurnManager getInstance()
    {
        return instance;
    }

    public GamePhase getGamePhase()
    {
        return mGamePhase;
    }

    public void setGamePhase(GamePhase pGamePhase)
    {
        mGamePhase = pGamePhase;
        updateCurrentPhase();
    }

    public void updateCurrentPhase()
    {
        GameMenu gameMenu = MainWindow.getInstance().getDesk().getGameMenu();
        switch (mGamePhase)
        {
        case STARTING:

            gameMenu.getQuitButton().setEnabled(true);
            gameMenu.getJoinButton().setEnabled(true);
            gameMenu.getRestartButton().setEnabled(false);
            gameMenu.getRotateBoatButton().setEnabled(false);
            gameMenu.getReadyButton().setEnabled(false);
            gameMenu.getHitButton().setEnabled(false);
            gameMenu.getSurrenderButton().setEnabled(false);
            MainWindow.getInstance().getDesk().getOpponentGrid()
                    .setGridDisplayEnabled(false);
            MainWindow.getInstance().getDesk().getPlayerGrid()
                    .setGridDisplayEnabled(false);
            break;
        case DEPLOYMENT:

            mCurrentPlayerAvailable = true;
            gameMenu.getJoinButton().setEnabled(false);
            gameMenu.getRotateBoatButton().setEnabled(true);
            gameMenu.getBoatComboBox().setEnabled(true);
            MainWindow.getInstance().getDesk().getPlayerGrid()
                    .setGridDisplayEnabled(true);
            gameMenu.getJoinButton().setText("Joined");
            break;
        case DEPLOYMENT_ENDED:
            mOpponentPlayerAvailable = true;
            gameMenu.getBoatComboBox().setEnabled(false);
            gameMenu.getRotateBoatButton().setEnabled(false);
            gameMenu.getReadyButton().setEnabled(true);
            MainWindow.getInstance().getDesk().getPlayerGrid()
                    .setGridDisplayEnabled(false);
            MainWindow.getInstance().getDesk().getOpponentGrid()
                    .setGridDisplayEnabled(true);
            break;
        case ENDED:
            MainWindow.getInstance().getDesk().getGameMenu().getRestartButton()
                    .setEnabled(true);
            MainWindow.getInstance().getDesk().getGameMenu().getHitButton()
                    .setEnabled(false);
            break;
        case OPPONENT_READY:
            break;
        case OPPONENT_TURN:
            gameMenu.getHitButton().setEnabled(false);
            MainWindow.getInstance().getDesk().getOpponentGrid()
                    .setGridDisplayEnabled(false);
            MainWindow.getInstance().getDesk().getPlayerGrid()
                    .setGridDisplayEnabled(true);
            break;
        case PLAYER_LEFT:
            break;
        case PLAYER_READY:
            break;
        case PLAYER_TURN:
            gameMenu.getHitButton().setEnabled(true);
            MainWindow.getInstance().getDesk().getOpponentGrid()
                    .setGridDisplayEnabled(true);
            MainWindow.getInstance().getDesk().getPlayerGrid()
                    .setGridDisplayEnabled(false);

            break;
        case READY:
            break;
        case STARTER:
            break;
        case WAITING_FOR_OPPONENT:
            mCurrentPlayerAvailable = true;
            break;
        case WAITING_FOR_YOU:
            mOpponentPlayerAvailable = true;
            break;
        }
    }
}
