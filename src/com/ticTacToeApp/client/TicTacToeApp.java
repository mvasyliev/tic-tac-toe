package com.ticTacToeApp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;

public class TicTacToeApp implements EntryPoint {

    public void onModuleLoad() {
        GameUI gameUI = GWT.create(GameUI.class);

        RootPanel.get("game-widget").add(gameUI);

        gameUI.loadGameState();
    }
}
