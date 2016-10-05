package com.rabbile.santajump.free;

import com.rabbile.santajump.free.Resources.FontResources;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.Util.FrameworkHelper;

public class GameStatisticsHud extends HUD{
    private final boolean _isDebugMode;
    private IGameSceneServices _gameSceneServices;

    private Text _scoreText;
    private Text _remainingTimeText;
    private Text _objectCountText;
    float _leftMargin = 5;
    float _gap = 5;
    float _hudTextHeight = 30;

    public GameStatisticsHud(IGameSceneServices gameSceneServices){
        super();
        _gameSceneServices = gameSceneServices;
        _isDebugMode = FrameworkHelper.isDebugMode();

        float backgroundRectHeight = 100;
        Rectangle backgroundRect = new Rectangle(_gameSceneServices.getCameraWidth() / 2, _gameSceneServices.getCameraHeight() - backgroundRectHeight / 2,
                _gameSceneServices.getCameraWidth(), backgroundRectHeight, ResourceManager.Get().Vbo);
        backgroundRect.setColor(Color.BLUE);
        backgroundRect.setAlpha(.5f);
        attachChild(backgroundRect);

        if (_isDebugMode){
            _scoreText = new Text(0, 0, FontResources.Get().CourierNewRegularFont, "Score: 00000", 12, ResourceManager.Get().Vbo);
            float textScale = _hudTextHeight / _scoreText.getHeight();
            _scoreText.setScale(textScale);
            _scoreText.setPosition(_leftMargin + _scoreText.getWidth() * textScale / 2, _gameSceneServices.getCameraHeight());
            attachChild(_scoreText);

            _objectCountText = new Text(0, 0, FontResources.Get().CourierNewRegularFont, "Objects: 000", 12, ResourceManager.Get().Vbo);
            _objectCountText.setScale(textScale);
            _objectCountText.setPosition(_leftMargin + _scoreText.getWidth() * textScale / 2, _gameSceneServices.getCameraHeight());
            attachChild(_objectCountText);

            _remainingTimeText = new Text(0, 0, FontResources.Get().CourierNewRegularFont, "Remaining time: 000", 19, ResourceManager.Get().Vbo);
            _remainingTimeText.setScale(textScale);
            _remainingTimeText.setPosition(_leftMargin + _scoreText.getWidth() * textScale / 2, _gameSceneServices.getCameraHeight());
            attachChild(_remainingTimeText);

        } else {
            // Create using fancy fonts
        }
    }

    public void setScore(int score){
        _scoreText.setText("Score: " + score);
        float textScale = _hudTextHeight / _scoreText.getHeight();
        _scoreText.setPosition(_leftMargin + _scoreText.getWidth() * textScale / 2, _gameSceneServices.getCameraHeight() - _hudTextHeight / 2);
    }

    public void setObjectCount(int objectCount){
        _objectCountText.setText("Objects: " + objectCount);
        float textScale = _hudTextHeight / _objectCountText.getHeight();
        _objectCountText.setPosition(_leftMargin + _objectCountText.getWidth() * textScale / 2, _gameSceneServices.getCameraHeight() - 3 * _hudTextHeight / 2);
    }

    public void setRemainingTime(int remainingTimeInSeconds){
        _remainingTimeText.setText("Remaining time: " + remainingTimeInSeconds);
        float textScale = _hudTextHeight / _remainingTimeText.getHeight();
        _remainingTimeText.setPosition(_leftMargin + _remainingTimeText.getWidth() * textScale / 2, _gameSceneServices.getCameraHeight() - 5 * _hudTextHeight / 2);
    }
}
