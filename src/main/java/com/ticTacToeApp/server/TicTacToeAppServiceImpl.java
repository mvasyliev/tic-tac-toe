package com.ticTacToeApp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ticTacToeApp.client.TicTacToeAppService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Service("gameService")
public class TicTacToeAppServiceImpl extends RemoteServiceServlet implements TicTacToeAppService {
    private static String GAME_SESSION_ATTR = "GAME_STATE"; 
    private static int CROSS_SUM = DIM;
    private static int CIRCLE_SUM = DIM * 10;
    private static int MAX_MOVES = DIM * DIM;

    private int[][] _gameField = new int[DIM][DIM];
    private int _moves;

    public int[] getNextMove(int x, int y) {
        if (x + y >= 0) {
            _gameField[x][y] = 1;
            _moves++;
        }
        
         if (_moves < MAX_MOVES) {
            while (true) {
                int i = new Random().nextInt(DIM);
                int k = new Random().nextInt(DIM);
                if (_gameField[i][k] == 0) {
                    _gameField[i][k] = 10;
                    _moves++;

                    return new int[] {i, k};
                }
            }
        }

        return new int[] {-1, -1};
    }

    public void resetGame() {
        for (int i = 0; i < DIM; i++) {
            for (int k = 0; k < DIM; k++) {
                _gameField[i][k] = 0;
                _moves = 0;
            }
        }
    }

    public int[][] getGameState() {
        return _gameField;
    }

    public String checkWinner() {
        int state = calculateWinner();

        if (state != 0) {
            _moves = MAX_MOVES;
            if (state == CROSS_SUM) {
                return "You Win!";
            } else {
                return "Computer Wins!";
            }
        } else if (_moves == MAX_MOVES) {
            return "Draw";
        }

        return null;
    }

    private int calculateWinner() {
        for (int i = 0; i < DIM; i++) {
            int verticalSum = 0;
            int horizontalSum = 0;
            for (int k = 0; k < DIM; k++) {
                verticalSum += _gameField[i][k];
                horizontalSum += _gameField[k][i];
            }

            if (verticalSum == CROSS_SUM || verticalSum == CIRCLE_SUM) {
                return verticalSum;
            }

            if (horizontalSum == CROSS_SUM || horizontalSum == CIRCLE_SUM) {
                return horizontalSum;
            }
        }

        int leftSum = 0;
        int rightSum = 0;

        for (int i = 0; i < DIM; i++) {
            leftSum += _gameField[i][i];
            rightSum += _gameField[i][DIM - i - 1];
        }

        if (leftSum == CROSS_SUM || leftSum == CIRCLE_SUM) {
            return leftSum;
        }

        if (rightSum == CROSS_SUM || rightSum == CIRCLE_SUM) {
            return rightSum;
        }

        return 0;
    }

    @Override
    protected void onBeforeRequestDeserialized(String serializedRequest) {
        HttpSession session = this.getThreadLocalRequest().getSession();
        GameState gameState = (GameState)session.getAttribute(GAME_SESSION_ATTR);

        if (gameState == null) {
            gameState = new GameState();
            session.setAttribute(GAME_SESSION_ATTR, gameState);
        }
        
        _gameField = gameState.getGameField();
        _moves = gameState.getMoves();
    }

    @Override
    protected void onAfterResponseSerialized(String serializedResponse) {
        HttpSession session = this.getThreadLocalRequest().getSession();
        GameState gameState = (GameState)session.getAttribute(GAME_SESSION_ATTR);

        gameState.setGameField(_gameField);
        gameState.setMoves(_moves);
    }
    
    private class GameState {
        private int[][] _gameField = new int[DIM][DIM];
        private int _moves;

        public int getMoves() {
            return _moves;
        }

        public void setMoves(int moves) {
            _moves = moves;
        }

        public int[][] getGameField() {
            return _gameField;
        }

        public void setGameField(int[][] gameField) {
            _gameField = gameField;
        }
    }
}