package com.ticTacToeApp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("TicTacToeAppService")
public interface TicTacToeAppService extends RemoteService {
    int DIM = 3;

    int[] getNextMove(int x, int y);

    String checkWinner();

    void resetGame();

    int[][] getGameState();
}
