package com.ticTacToeApp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("TicTacToeAppService")
public interface TicTacToeAppService extends RemoteService {
    // Sample interface method of remote interface

    String getMessage(String msg);

    /**
     * Utility/Convenience class.
     * Use TicTacToeAppService.App.getInstance() to access static instance of TicTacToeAppServiceAsync
     */
    public static class App {
        private static TicTacToeAppServiceAsync ourInstance = GWT.create(TicTacToeAppService.class);

        public static synchronized TicTacToeAppServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
