package com.rabbile.santajump.free.Dialogs;


import com.rabbile.santajump.free.IGameSceneServices;
import com.rabbile.santajump.free.Resources.FontResources;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.util.modifier.IModifier;
import org.rabbilegames.CommonUi.BaseDialog;
import org.rabbilegames.CommonUi.GrowButton;
import org.rabbilegames.ResourceManager;


public class SimpleGameStateDialog extends BaseDialog {
    final float BORDER_WIDTH = 10;
    //Buttons
    protected boolean _showRestartButton;
    protected boolean _showResumeButton;
    protected boolean _showPlayButton;
    protected boolean _showNextButton;
    protected boolean _showMenuButton;
    protected boolean _showHomeButton;

    protected boolean _showScoreLabel;
    protected boolean _showDescriptionLabel;
    protected boolean _showStarLabel;

    protected String _title;
    protected String _description;
    protected int _stars;
    protected int _score;

    // color theme
    protected int _themeRed = 255;
    protected int _themeGreen;
    protected int _themeBlue;
    protected int _themeAlpha;

    protected float _dialogHeight;
    protected float _dialogWidth;
    private IGameSceneServices _gameSceneServices;

    private Entity _mainLayer;
    private Text _scoreText;

    private int _currentScore = 0;

    public SimpleGameStateDialog(IGameSceneServices gameSceneServices) {
        super();
        _gameSceneServices = gameSceneServices;
    }

    public void create(){
        _mainLayer = new Entity();
        attachBackground();
        attachTitle();
        attachScoreLabel();
        attachDescription();
        attachStars();
        attachButtons();
        _mainLayer.registerEntityModifier(new ScaleModifier(.2f, 1.2f, 1f, new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

            }

            @Override
            public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                // Start score count animation
                float period = 1f / _score;
                registerUpdateHandler(new TimerHandler(period, true, new ITimerCallback() {
                    @Override
                    public void onTimePassed(TimerHandler pTimerHandler) {
                        if (_currentScore <= _score){
                            _scoreText.setText("Score : " + _currentScore);
                            _currentScore++;
                        } else  {
                            unregisterUpdateHandler(pTimerHandler);
                        }
                    }
                }));
            }
        }));
        attachChild(_mainLayer);
    }

    private void attachBackground() {
        // Draw background rect
        Rectangle background = new Rectangle(0, 0, _dialogWidth, _dialogHeight, ResourceManager.Get().Vbo);
        background.setColor(_themeRed / 255f, _themeGreen / 255f, _themeBlue / 255f);
        _mainLayer.attachChild(background);

        // Draw border
        Line left = new Line(-_dialogWidth / 2, -_dialogHeight / 2, -_dialogWidth / 2, _dialogHeight / 2, ResourceManager.Get().Vbo);
        left.setLineWidth(BORDER_WIDTH);
        Line right = new Line(_dialogWidth / 2, -_dialogHeight / 2, _dialogWidth / 2, _dialogHeight / 2, ResourceManager.Get().Vbo);
        right.setLineWidth(BORDER_WIDTH);
        Line top = new Line(-_dialogWidth / 2, _dialogHeight / 2, _dialogWidth / 2, _dialogHeight / 2, ResourceManager.Get().Vbo);
        top.setLineWidth(BORDER_WIDTH);
        Line bottom  = new Line(-_dialogWidth / 2, -_dialogHeight / 2, _dialogWidth / 2, -_dialogHeight / 2, ResourceManager.Get().Vbo);
        bottom.setLineWidth(BORDER_WIDTH);

        _mainLayer.attachChild(left);
        _mainLayer.attachChild(right);
        _mainLayer.attachChild(top);
        _mainLayer.attachChild(bottom);
    }


    private void attachTitle() {
        // add title to the top 1/4 of the total height
        float titleTextFactor = 0.8f;
        float titleHeight = (_dialogHeight / 4);
        float titleTextHeight = titleHeight * titleTextFactor;
        Text title = new Text(0, _dialogHeight / 2 - titleHeight / 2, FontResources.Get().CondensedFont, _title,
                _title.length(), ResourceManager.Get().Vbo);
        title.setScale(titleTextHeight / title.getHeight());
        _mainLayer.attachChild(title);

        Line border  = new Line(-_dialogWidth / 2, _dialogHeight / 2 - titleHeight, _dialogWidth / 2, _dialogHeight / 2 - titleHeight, ResourceManager.Get().Vbo);
        border.setLineWidth(BORDER_WIDTH);

        _mainLayer.attachChild(border);
    }

    private void attachScoreLabel() {
        if (_showScoreLabel){
            int contentColumnsCount = getContentRowsCount();
            float scoreTextFactor = 0.7f;
            float scoreHeight = _dialogHeight / 2 / contentColumnsCount;
            float scoreTextHeight = scoreHeight * scoreTextFactor;
            String scoreText = "Score : " + _score;
            _scoreText = new Text(0, _dialogHeight / 2 - _dialogHeight / 4 - (scoreHeight / 2), FontResources.Get().CondensedFont, scoreText,
                    scoreText.length(), ResourceManager.Get().Vbo);

            _scoreText.setScale(scoreTextHeight / _scoreText.getHeight());
            if (_scoreText.getWidth() > _dialogWidth * scoreTextFactor ){
                // Text exceeds dialog limits, we need to rescale
                _scoreText.setScale(_dialogWidth * scoreTextFactor / _scoreText.getWidth());
            }
            _scoreText.setText("Score : 0");
            _mainLayer.attachChild(_scoreText);
        }
    }

    private void attachDescription() {
        if (_showDescriptionLabel){
            //TODO: add description label logic
        }
    }

    private void attachStars() {
        if (_showStarLabel){
            //TODO: add stars logic
        }
    }

    private void attachButtons() {
        int buttonBarColumnsCount = getButtonBarColumnsCount();
        float buttonFactor = 0.8f;
        float buttonBarColumnWidth = _dialogWidth / buttonBarColumnsCount;
        float buttonHeight = buttonBarColumnWidth * buttonFactor;
        if (buttonHeight > (_dialogHeight / 4 * buttonFactor)){
            //Height exceeds
            buttonHeight *= (_dialogHeight * buttonFactor / 4) / (buttonHeight);
        }
        float y = -_dialogHeight / 2 + _dialogHeight / 8;
        float currentX = -_dialogWidth / 2 + buttonBarColumnWidth / 2;
        if (_showRestartButton){

            GrowButton button = new GrowButton(currentX, y, buttonHeight, buttonHeight, GameResources.Get().SimpleGameStateRestartButtonTR) {
                @Override
                public void onClick() {

                }
            };
            _mainLayer.attachChild(button);
            registerTouchArea(button);
            currentX += buttonBarColumnWidth;
        }
        if (_showResumeButton){
            GrowButton button = new GrowButton(currentX, y, buttonHeight, buttonHeight, GameResources.Get().SimpleGameStateResumeButtonTR) {
                @Override
                public void onClick() {

                }
            };
            _mainLayer.attachChild(button);
            registerTouchArea(button);
            currentX += buttonBarColumnWidth;
        }
        if (_showPlayButton){
            GrowButton button = new GrowButton(currentX, y, buttonHeight, buttonHeight, GameResources.Get().SimpleGameStatePlayButtonTR) {
                @Override
                public void onClick() {

                }
            };
            _mainLayer.attachChild(button);
            registerTouchArea(button);
            currentX += buttonBarColumnWidth;
        }
        if (_showNextButton){
            GrowButton button = new GrowButton(currentX, y, buttonHeight, buttonHeight, GameResources.Get().SimpleGameStateNextButtonTR) {
                @Override
                public void onClick() {

                }
            };
            _mainLayer.attachChild(button);
            registerTouchArea(button);
            currentX += buttonBarColumnWidth;
        }
        if (_showHomeButton){
            GrowButton button = new GrowButton(currentX, y, buttonHeight, buttonHeight, GameResources.Get().SimpleGameStateHomeButtonTR) {
                @Override
                public void onClick() {

                }
            };
            _mainLayer.attachChild(button);
            registerTouchArea(button);
        }
    }

    private int getButtonBarColumnsCount() {
        int buttonBarColumnsCount = 0;
        if (_showRestartButton) {
            buttonBarColumnsCount++;
        }
        if (_showResumeButton) {
            buttonBarColumnsCount++;
        }
        if (_showPlayButton) {
            buttonBarColumnsCount++;
        }
        if (_showNextButton) {
            buttonBarColumnsCount++;
        }
        if (_showMenuButton) {
            buttonBarColumnsCount++;
        }
        if (_showHomeButton) {
            buttonBarColumnsCount++;
        }
        return buttonBarColumnsCount;
    }

    private int getContentRowsCount() {
        int contentColumnsCount = 0;
        if (_showScoreLabel){
            contentColumnsCount++;
        }
        if (_showDescriptionLabel){
            contentColumnsCount++;
        }
        if (_showStarLabel){
            contentColumnsCount++;
        }
        return contentColumnsCount;
    }
}
