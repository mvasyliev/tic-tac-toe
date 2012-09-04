package com.ticTacToeApp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.ticTacToeApp.client.resourses.ClientResourses;
import com.ticTacToeApp.client.resourses.TicTacCssResourse;

public class GameUI extends Composite {
    interface GameUIBinder extends UiBinder<Widget, GameUI> {}
    private static final GameUIBinder uiBinder = GWT.create(GameUIBinder.class);

    private static int DIM = 3;
    private TicTacToeAppServiceAsync _gameService = (TicTacToeAppServiceAsync) GWT.create(TicTacToeAppService.class);

    private boolean _processingRequest;
    private TicTacCssResourse _css = ClientResourses.INSTANCE.css();

    private Button[][] _gameButtons = new Button[DIM][DIM];
    
    @UiField
    CheckBox _computerPlaysFirst;

    @UiField
    Button _resetButton;

    @UiField
    VerticalPanel _vGamePanel;

    @UiField
    Label _winnerLabel;

    public GameUI() {
        ClientResourses.INSTANCE.css().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));

        _vGamePanel.setStyleName(_css.gameField());

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
                            button.addStyleName(_css.crossImgBtn());
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

            _vGamePanel.add(hPanel);
        }
    }

    public void loadGameState() {
        _gameService.getGameState(new AsyncCallback<int[][]>() {

            public void onFailure(Throwable caught) {
                Window.alert("Unable to initialize game: " + caught.getMessage());
            }

            public void onSuccess(int[][] result) {
                for (int i = 0; i < DIM; i++) {
                    for (int k = 0; k < DIM; k++) {
                        if (result[i][k] == 0) {
                            _gameButtons[i][k].setEnabled(true);
                        } else {
                            String style = result[i][k] == 1 ? _css.crossImgBtn() : _css.circleImgBtn();
                            _gameButtons[i][k].addStyleName(style);
                        }
                    }
                }
            }
        });
    }

    private void checkWinner() {
        _gameService.checkWinner(new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                Window.alert("Unable to check who won: " + caught.getMessage());
            }

            public void onSuccess(String result) {
                if (result != null) {
                    _winnerLabel.setText(result);

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
                Window.alert("Unable to make move: " + caught.getMessage());
            }

            public void onSuccess(int[] result) {
                int x = result[0];
                int y = result[1];

                if (x + y >= 0) {
                    _gameButtons[x][y].setStyleName(_css.circleImgBtn());
                    _gameButtons[x][y].setEnabled(false);
                }

                _processingRequest = false;
            }
        });
    }

    @UiHandler("_resetButton")
    void onReset(ClickEvent e) {
        for (int i = 0; i < DIM; i++) {
            for (int k = 0; k < DIM; k++) {
                _gameButtons[i][k].setEnabled(true);
                _gameButtons[i][k].setStyleName("");
                _winnerLabel.setText("");
            }
        }

        _gameService.resetGame(new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                Window.alert("Unable to reset the game: " + caught.getMessage());
            }

            public void onSuccess(Void result) {
                // no op
            }
        });

        if (_computerPlaysFirst.getValue()) {
            computerMove(-1, -1);
        }
    }

}
