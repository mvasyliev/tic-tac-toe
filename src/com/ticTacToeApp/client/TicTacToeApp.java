package com.ticTacToeApp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class TicTacToeApp implements EntryPoint {
    private static int DIM = 3;

    private TicTacToeAppServiceAsync _gameService = (TicTacToeAppServiceAsync) GWT.create(TicTacToeAppService.class);
    private boolean _processingRequest;

    private Button[][] _gameButtons = new Button[DIM][DIM];
    private CheckBox _computerPlaysFirst;
    private Label _winnerMessage = new Label();

    public void onModuleLoad() {
        _gameService.getGameState(new AsyncCallback<int[][]>() {

            public void onFailure(Throwable caught) {
                showAlert("Unable to initialize game: " + caught.getMessage());
            }

            public void onSuccess(int[][] result) {
                for (int i = 0; i < DIM; i++) {
                    for (int k = 0; k < DIM; k++) {
                        if (result[i][k] == 0) {
                            _gameButtons[i][k].setEnabled(true);
                        } else {
                            String style = result[i][k] == 1 ? "crossImgBtn" : "circleImgBtn";
                            _gameButtons[i][k].setStyleName(style);
                        }
                    }
                }
            }
        });
        VerticalPanel vPanel = new VerticalPanel();

        for (int i = 0; i < DIM; i++) {
            HorizontalPanel hPanel = new HorizontalPanel();

            for (int k = 0; k < DIM; k++) {
                Button button = new Button("");
                button.setEnabled(false);
                _gameButtons[i][k] = button;
                hPanel.add(button);

                button.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        if (!_processingRequest) {
                            _processingRequest = true;
                            Button button = ((Button)event.getSource());
                            button.setStyleName("crossImgBtn");
                            button.setEnabled(false);

                            for (int i = 0; i < DIM; i++) {
                                for (int k = 0; k < DIM; k++) {
                                    if (_gameButtons[i][k].equals(button)) {
                                        computerMove(i, k);
                                        checkWinner();
                                        return;
                                    }
                                }
                            }
                        }

                    }
                });
            }

            vPanel.add(hPanel);
        }

        final Button resetButton = new Button("Reset game");
        resetButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                for (int i = 0; i < DIM; i++) {
                    for (int k = 0; k < DIM; k++) {
                        _gameButtons[i][k].setEnabled(true);
                        _gameButtons[i][k].setStyleName("");
                        _winnerMessage.setText("");
                    }
                }

                _gameService.resetGame(new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                        showAlert("Unable to reset the game: " + caught.getMessage());
                    }

                    public void onSuccess(Void result) {
                        // no op
                    }
                });

                if (_computerPlaysFirst.getValue()) {
                    computerMove(-1, -1);
                }
            }
        });

        _computerPlaysFirst = new CheckBox("Computer plays first");

        RootPanel.get("settingsID").add(_computerPlaysFirst);
        RootPanel.get("winnerMessageID").add(_winnerMessage);
        RootPanel.get("gameFieldID").add(vPanel);
        RootPanel.get("resetButtonID").add(resetButton);
    }

    private void checkWinner() {
        _gameService.checkWinner(new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                showAlert("Unable to check who won: " + caught.getMessage());
            }

            public void onSuccess(String result) {
                if (result != null) {
                    _winnerMessage.setText(result);

                    for (int i = 0; i < DIM; i++) {
                        for (int k = 0; k < DIM; k++) {
                            _gameButtons[i][k].setEnabled(false);
                        }
                    }
                }

            }
        });
    }

    private void computerMove(int x, int y) {
        _gameService.getNextMove(x, y, new AsyncCallback<int[]>() {

            public void onFailure(Throwable caught) {
                showAlert("Unable to make move: " + caught.getMessage());
            }

            public void onSuccess(int[] result) {
                int x = result[0];
                int y = result[1];

                if (x + y >= 0) {
                    _gameButtons[x][y].setStyleName("circleImgBtn");
                    _gameButtons[x][y].setEnabled(false);
                }

                _processingRequest = false;
            }
        });
    }

    private void showAlert(String message) {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText(message);
        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setSpacing(4);
        dialogBox.setWidth("100px");
        dialogBox.setWidget(dialogContents);

        Button closeButton = new Button("Close", new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });

        dialogContents.add(closeButton);

        dialogBox.center();
        dialogBox.show();
    }
}
