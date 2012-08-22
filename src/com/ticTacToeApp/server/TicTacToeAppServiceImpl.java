package com.ticTacToeApp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ticTacToeApp.client.TicTacToeAppService;

public class TicTacToeAppServiceImpl extends RemoteServiceServlet implements TicTacToeAppService {
    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }
}