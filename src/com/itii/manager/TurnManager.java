package com.itii.manager;

import com.itii.data.State.GamePhase;
import com.itii.gui.Desk;
import com.itii.gui.GameMenu;
import com.itii.gui.MainWindow;

public class TurnManager
{

    private static final TurnManager mTurnManager=  new TurnManager();

    private TurnManager() {}
    
    private GamePhase mGamePhase=  GamePhase.STARTING;

    private boolean   mIsCurrentPlayerAvailable=  false;
    private boolean  mIsOpponentPlayerAvailable=  false;
    private boolean            mIsOpponentReady=  false;
    private boolean          mCurrentPlayerTurn=  true;
    
    public static TurnManager getInstance()
    {
        return mTurnManager;
    }
    
    public void setGamePhase( final GamePhase pGamePhase )
    {
        mGamePhase=  pGamePhase;

        updateCurrentPhase();
    }
    
    public GamePhase getGamePhase()
    {
        return mGamePhase;
    }
    
    public boolean isOpponentPlayerAvailable()
    {
        return mIsOpponentPlayerAvailable;
    }
    
    public boolean isCurrentPlayerAvailable()
    {
        return mIsCurrentPlayerAvailable;
    }
    
    public void setCurrentPlayerAvailable( final boolean pCurrentPlayerAvailable )
    {
        mIsCurrentPlayerAvailable=  pCurrentPlayerAvailable;
    }
    
    
    public boolean isOpponentReady ()
    {
        return mIsOpponentReady;
    }
    
    public void setOpponentPlayerAvailable ( final boolean pOpponentPlayerAvailable )
    {
        this.mIsOpponentPlayerAvailable= pOpponentPlayerAvailable;
    }
    
    public void updateCurrentPhase()
    {
        GameMenu gameMenu=  MainWindow.getInstance().getDesk().getGameMenu();

        switch ( mGamePhase ) 
        {
            case STARTING:
            {
                gameMenu.getJoinButton().setEnabled( true );
                gameMenu.getQuitButton().setEnabled( true );
                gameMenu.getReadyButton().setEnabled( false );
                gameMenu.getBoatComboBox().setEnabled( false );
                gameMenu.getRotateBoatButton().setEnabled( false );
                gameMenu.getSurrenderButton().setEnabled( false );
                gameMenu.getHitButton().setEnabled( false );
                gameMenu.getRestartButton().setEnabled( false );
                
                MainWindow.getInstance().getDesk()
                                        .getAdversaireGrid()
                                        .setGridDisplayEnabled( false );
                MainWindow.getInstance().getDesk()
                                        .getJoueurGrid()
                                        .setGridDisplayEnabled( false );
                break;
            }
            
            case WAITING_FOR_OPPONENT : 
            {
                mIsCurrentPlayerAvailable=  true;
                break;
            }
            case WAITING_FOR_YOU :
            {
                mIsOpponentPlayerAvailable=  true;
                break;
            }
            case DEPLOYMENT :
            {
                gameMenu.getJoinButton().setText( "Joined" );
                gameMenu.getJoinButton().setEnabled( false );
                gameMenu.getRotateBoatButton().setEnabled( true );
                gameMenu.getBoatComboBox().setEnabled( true );
                MainWindow.getInstance().getDesk()
                                        .getJoueurGrid()
                                        .setGridDisplayEnabled( true );
                break;
            }
            case DEPLOYMENT_ENDED :
            {
                gameMenu.getRotateBoatButton().setEnabled( false );
                gameMenu.getBoatComboBox().setEnabled( false );
                gameMenu.getReadyButton().setEnabled( true );
                MainWindow.getInstance().getDesk()
                                        .getJoueurGrid()
                                        .setGridDisplayEnabled( false );
                break;
            }
            case STARTED :
            {
//                game_menu.getBoatComboBox().setEnabled( true );
                break;
            }
            case ENDED :
            {
                MainWindow.getInstance()
                          .getDesk()
                          .getGameMenu()
                          .getRestartButton()
                          .setEnabled( true );
                MainWindow.getInstance()
                          .getDesk()
                          .getGameMenu()
                          .getHitButton()
                          .setEnabled(false);
                break;
            }
            case PLAYER_LEFT :
            {
                
                break;
            }
            case OPPONENT_READY :
            { 
//                mIsOpponentReady=  true;
                // Display Ready on opponent player grid              
                break;
            }
            case PLAYER_READY :
            {
//                mFirstPlayerToBegin=  true;
                // Display Ready on current player grid
                break;                
            }
            
            case PLAYER_TURN :
            {
                gameMenu.getHitButton().setEnabled( true );
                MainWindow.getInstance().getDesk()
                                        .getJoueurGrid()
                                        .setGridDisplayEnabled( false );
                MainWindow.getInstance().getDesk()
                                        .getAdversaireGrid()
                                        .setGridDisplayEnabled( true );
                break;
            }
            case OPPONENT_TURN :
            {
                gameMenu.getHitButton().setEnabled( false );
                MainWindow.getInstance().getDesk()
                                        .getAdversaireGrid()
                                        .setGridDisplayEnabled( false );
                break;
            }
            
            case READY :
            {
                break;
            }
        }
    }

    public void setPlayerTurn ( final boolean pCurrentPlayerTurn )
    {
        mCurrentPlayerTurn=  pCurrentPlayerTurn;
    }

    public boolean isPlayerTurn ()
    {
        return mCurrentPlayerTurn;
    }
    
}
