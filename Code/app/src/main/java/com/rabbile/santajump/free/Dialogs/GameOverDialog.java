package com.rabbile.santajump.free.Dialogs;

import com.rabbile.santajump.free.IGameSceneServices;

/**
 * Created by asanka.samarawickram on 11/5/2016.
 */

public class GameOverDialog extends SimpleGameStateDialog{

    public GameOverDialog(IGameSceneServices gameSceneServices, String title) {
        super(gameSceneServices);
        _dialogHeight = 500;
        _dialogWidth = 400;
        _title = title;

        _showScoreLabel = true;
        _score = gameSceneServices.getScore();

        _showRestartButton = true;
        _showHomeButton = true;
        create();
    }
}
