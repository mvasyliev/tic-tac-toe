package com.ticTacToeApp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TicTacToeAppServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);
}
