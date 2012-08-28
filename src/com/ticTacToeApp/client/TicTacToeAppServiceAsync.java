package com.ticTacToeApp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TicTacToeAppServiceAsync {
    void getNextMove(int x, int y, AsyncCallback<int[]> async);

    void checkWinner(AsyncCallback<String> async);

    void resetGame(AsyncCallback<Void> async);
}
