package com.ticTacToeApp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ticTacToeApp.client.TicTacToeAppService;

import java.util.Random;

public class TicTacToeAppServiceImpl extends RemoteServiceServlet implements TicTacToeAppService {
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

}