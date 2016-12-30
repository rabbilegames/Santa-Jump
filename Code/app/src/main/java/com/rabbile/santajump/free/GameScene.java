package com.rabbile.santajump.free;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import com.rabbile.santajump.free.Dialogs.SimpleGameStateDialog;
import org.rabbilegames.framework.BaseGameElement;
import org.rabbilegames.framework.BaseGameElementPool;

import com.rabbile.santajump.free.Dialogs.GameOverDialog;
import com.rabbile.santajump.free.Elements.BrokenStep;
import com.rabbile.santajump.free.Elements.Clock;
import com.rabbile.santajump.free.Elements.Cloud;
import com.rabbile.santajump.free.Elements.Fairy;
import com.rabbile.santajump.free.Elements.Gift;
import com.rabbile.santajump.free.Elements.IronStep;
import com.rabbile.santajump.free.Elements.Santa;
import com.rabbile.santajump.free.Elements.SantaInteractor;
import com.rabbile.santajump.free.Elements.SantaJumpGameElementFactory;
import com.rabbile.santajump.free.Elements.SideBorderLeft;
import com.rabbile.santajump.free.Elements.SideBorderRight;
import com.rabbile.santajump.free.Elements.StepBase;
import com.rabbile.santajump.free.Elements.WoodenStep;
import com.rabbile.santajump.free.Resources.FontResources;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;
import org.rabbilegames.BaseScene;
import org.rabbilegames.CommonUi.BaseLayer;
import org.rabbilegames.CommonUi.GrowButton;
import org.rabbilegames.Definitions.GameState;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.SceneType;
import org.rabbilegames.Util.MathUtil;

import java.util.ArrayList;
import java.util.List;


public class GameScene extends BaseScene implements IGameSceneServices, IOnSceneTouchListener {
    private boolean _testBool = false;

    private final float FLOOR_HEIGHT = 70;
    public static boolean ChileSceneOpened = false;

    private static final float _cameraHeight = ResourceManager.Get().camera.getHeight();
    private static final float _cameraWidth = ResourceManager.Get().camera.getWidth();
    boolean _isInTransitionEnabled;

    BaseLayer _mainMenuElementsLayer;
    BaseLayer _staticGameElementsLayer;
    BaseLayer _santaLayer;
    BaseLayer _dynamicGameElementsLayer;
    GameStatisticsHud _gameHud;

    private PhysicsWorld _physicsWorld;

    GrowButton _startButton;
    GrowButton _rateButton;
    GrowButton _scoreButton;

    Santa _santa;

    private BaseGameElementPool _gameElementPool;
    private List<BaseGameElement> _attachedGameElements = new ArrayList<>();

    private float _multipleStepsProbability = .8f;

    float _lastSideBarY;
    float _lastCloudY;
    float _lastStepRowY;
    float _lastStepRowCount;
    boolean _isStepsAttached = false;
    List<Integer> _solidStepIndexes = new ArrayList<>();
    int _score = 0;
    float _remainingTimeInSeconds = 30;
    int _limitToShowMetalSteps = 50;
    private int _limitToShowAdvancedItems = 50;


    GameState _gameState = GameState.NotStarted;
    SimpleGameStateDialog _gameStateDialog;


    public GameScene(boolean isInTransitionEnabled) {
        _isInTransitionEnabled = isInTransitionEnabled;
    }

    @Override
    public void createScene() {

    }

    @Override
    public void OnLoadScene() {
        setBackground(new Background(new Color(112f / 255, 196f / 255, 206f / 255)));
        setBackgroundEnabled(true);

        createPhysicsWorld();
        initializePools();
        createHud();

        _mainMenuElementsLayer = new BaseLayer();
        this.attachChild(_mainMenuElementsLayer);

        _staticGameElementsLayer = new BaseLayer();
        this.attachChild(_staticGameElementsLayer);

        _santaLayer = new BaseLayer();
        this.attachChild(_santaLayer);

        _dynamicGameElementsLayer = new BaseLayer();
        this.attachChild(_dynamicGameElementsLayer);

        //Attach menu contents
        initializeMenuLayer();

        // Attach initial game contents
        initializeStaticGameLayer();

        //Create Santa
        _santa = new Santa(_physicsWorld, this, _cameraWidth / 2, FLOOR_HEIGHT + 5 + Santa.SANTA_HEIGHT / 2);
        _santaLayer.attachChild(_santa.getEntity());
        _camera.set_chaseEntity(_santa.getEntity());

        setTouchAreaBindingOnActionDownEnabled(true);
        setTouchAreaBindingOnActionMoveEnabled(true);
        setOnSceneTouchListener(this);
    }

    private void createHud() {
        _gameHud = new GameStatisticsHud(this);
        _gameHud.setVisible(false);
        _camera.setHUD(_gameHud);
    }

    private void initializePools() {
        _gameElementPool = new BaseGameElementPool(new SantaJumpGameElementFactory(this, _physicsWorld));
    }

    @Override
    public void onBackKeyPressed() {
        System.exit(0);
        //		if (!this.hasChildScene())
        //		{
        //			if (this.mCurrentScreen == MainMenuScreens.WorldSelector)
        //			{
        //				if (this.hasChildScene())
        //				{
        //					//					LevelSelectorScreen levelSelectorScreen = (LevelSelectorScreen) this.getChildScene();
        //					//					levelSelectorScreen.OnBackKeyPressed();
        //				}
        //				else
        //				{
        //					goToTitleScreen();
        //				}
        //			}
        //			else
        //			{
        //				AdManager.ShowAdOnExit();
        //				this.registerUpdateHandler(new IUpdateHandler()
        //				{
        //					@Override
        //					public void onUpdate(float pSecondsElapsed)
        //					{
        //						if (!AdManager.GetIsAdShown())
        //						{
        //							System.exit(0);
        //						}
        //					}
        //
        //					@Override
        //					public void reset()
        //					{
        //					}
        //				});
        //			}
        //		}
        //		else
        //		{
        //			if (this.getChildScene() instanceof GameStatusScreen)
        //			{
        //				((GameStatusScreen) this.getChildScene()).OnBackKeyPressed();
        //			}
        //			else if (this.getChildScene() instanceof Layer)
        //			{
        //				((Layer) this.getChildScene()).OnBackKeyPressed();
        //			}
        //		}
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME;
    }

    public Engine GetEngine() {
        return _engine;
    }

    @Override
    public void RegisterTouchArea(Entity entity) {
        this.registerTouchArea(entity);
    }

    @Override
    public void hideChildScene() {
        this.clearChildScene();
    }

    @Override
    public void onSantaJumpOnFallingStep() {
        _camera.set_chaseEntity(null);
        _gameState = GameState.Failed;
        showGameOverDialog();
    }

    @Override
    public void onSantaJumpOnWoodenStep() {
        _score++;
        _gameHud.setScore(_score);
    }

    @Override
    public float getCameraWidth() {
        return _cameraWidth;
    }

    @Override
    public float getCameraHeight() {
        return _cameraHeight;
    }

    @Override
    public HUD GetHUD() {
        return _gameHud;
    }

    @Override
    public void addTime(int addedTime) {
        _remainingTimeInSeconds += addedTime;
    }

    @Override
    public void addScore(int score) {
        _score += score;
    }

    @Override
    public int getScore() {
        return _score;
    }

    @Override
    public void clearChildScene() {
        GameScene.ChileSceneOpened = false;
        super.clearChildScene();
    }

    @Override
    public void beginTransitionOut() {
        setIsSceneInTransition(false);
//        IEaseFunction easeFunction = EaseExponentialIn.getInstance();
//        float transitionTime = 0.3f;
//        rectTop.registerEntityModifier(new MoveYModifier(transitionTime, _camera.getHeight() - rectTop.getHeight() / 2,
//                _camera.getHeight() + rectTop.getHeight() / 2, easeFunction) {
//            @Override
//            protected void onModifierFinished(IEntity pItem) {
//                super.onModifierFinished(pItem);
//                setIsSceneInTransition(false);
//            }
//        });
//        rectBottom.registerEntityModifier(new MoveYModifier(transitionTime, rectBottom.getHeight() / 2,
//                -rectBottom.getHeight() / 2, easeFunction));
    }

    @Override
    public void beginTransitionIn() {
        //TODO: Add fading effect
        if (_isInTransitionEnabled) {
            setIsSceneInTransition(false);
//            IEaseFunction easeFunction = EaseExponentialIn.getInstance();
//            float transitionTime = 0.3f;
//            rectTop.registerEntityModifier(new MoveYModifier(transitionTime, _camera.getHeight() + rectTop.getHeight()
//                    / 2,
//                    _camera.getHeight() - rectTop.getHeight() / 2, easeFunction) {
//                @Override
//                protected void onModifierFinished(IEntity pItem) {
//                    super.onModifierFinished(pItem);
//                    setIsSceneInTransition(false);
//                }
//            });
//            rectBottom.registerEntityModifier(new MoveYModifier(transitionTime, -rectBottom.getHeight() / 2,
//                    rectBottom.getHeight() / 2, easeFunction));
        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        switch (_gameState) {
            case Started:
                populateGameElementsForVisibleRegion();
                break;
            case Running:
                populateGameElementsForVisibleRegion();
                findNotifyElementInteractions();
                refreshGameTimer(pSecondsElapsed);
                if (_remainingTimeInSeconds <= 0) {
                    _gameState = GameState.Passed;
                    showGameOverDialog();
                }
                break;
            case Passed:
                break;
            case Failed:
                break;
            case Paused:
                break;
        }
    }

    private void showGameOverDialog() {
        final float timeSpan = 1f;
        this.registerUpdateHandler(new TimerHandler(timeSpan, new ITimerCallback()
        {
            @Override
            public void onTimePassed(TimerHandler timerHandler)
            {
                showGameStateDialog();
            }
        }));
    }

    private void showGameStateDialog()
    {
        switch (_gameState)
        {
            case Paused:
                //this.gameStatusScreen = new LevelPausedScreen(this);
                break;
            case Failed:
                _gameStateDialog = new GameOverDialog(this, "Failed");
                break;
            case Passed:
                _gameStateDialog = new GameOverDialog(this, "Time Up");
            case NotStarted:
                break;
            default:
                break;
        }
        _gameStateDialog.setPosition(_camera.getCenterX(), _camera.getCenterY());
        setChildScene(_gameStateDialog, false, true, true);
    }

    private void findNotifyElementInteractions() {
        for (int gameElementIndex = 0; gameElementIndex < _attachedGameElements.size(); gameElementIndex++) {
            BaseGameElement gameElement = _attachedGameElements.get(gameElementIndex);
            if (gameElement instanceof SantaInteractor){
                SantaInteractor santaInteractor = (SantaInteractor)gameElement;
                if (santaInteractor.getEntity().collidesWith(_santa.getEntity())){
                    santaInteractor.onSantaHit();
                }
            }
        }
    }

    private void refreshGameTimer(float secondsElapsed) {
        _remainingTimeInSeconds -= secondsElapsed;
        _gameHud.setRemainingTime(_remainingTimeInSeconds <= 0 ? 0 : (int) Math.ceil(_remainingTimeInSeconds));
    }

    private void populateGameElementsForVisibleRegion() {
        float maxY = _camera.getCenterY() + _camera.getHeight() / 2;
        float minY = Math.max(_camera.getCenterY() - _camera.getHeight() / 2, FLOOR_HEIGHT);

        // Remove Unused game elements
        int attachedGameElementCount = _attachedGameElements.size();
        List<BaseGameElement> detachedGameElements = new ArrayList<>();
        for (int gameElementIndex = 0; gameElementIndex < attachedGameElementCount; gameElementIndex++) {
            BaseGameElement gameElement = _attachedGameElements.get(gameElementIndex);
            if (gameElement.getEntity().getY() + gameElement.getEntity().getHeight() / 2 < minY) {
                detachedGameElements.add(gameElement);
            } else {
                break;
            }
        }
        for (BaseGameElement gameElementToDetach : detachedGameElements) {
            _attachedGameElements.remove(gameElementToDetach);
            gameElementToDetach.getEntity().detachSelf();
            _gameElementPool.handleRecycleItem(gameElementToDetach);
        }

        //Add clouds
        while (_lastCloudY + Cloud.HEIGHT / 2 < maxY) {
            float cloudLowerLimitY = Math.max(_lastCloudY + 3 * Cloud.HEIGHT / 2, _cameraHeight * 2);
            float cloudUpperLimitY = cloudLowerLimitY + Cloud.HEIGHT * 3;
            float cloudLowerLimitX = 0;
            float cloudUpperLimitX = _cameraWidth;

            float y = MathUtil.NextRand(cloudLowerLimitY, cloudUpperLimitY);
            BaseGameElement cloud = _gameElementPool.obtainPoolItem(Cloud.ID, MathUtil.NextRand(cloudLowerLimitX, cloudUpperLimitX), y);
            ((Cloud) cloud).transformRandom();
            _staticGameElementsLayer.attachChild(cloud.getEntity());
            _attachedGameElements.add(cloud);
            _lastCloudY = y;
        }

        //Add side bars
        while (_lastSideBarY + SideBorderLeft.HEIGHT / 2 < maxY) {
            _lastSideBarY = _lastSideBarY + SideBorderLeft.HEIGHT / 2;
            BaseGameElement sideBorderLeft = _gameElementPool.obtainPoolItem(SideBorderLeft.ID, _cameraWidth / 2 - SantaJumpConstants.HORIZONTAL_GAP_BETWEEN_STEPS - _cameraWidth / 2, _lastSideBarY);
            BaseGameElement sideBorderRight = _gameElementPool.obtainPoolItem(SideBorderRight.ID, _cameraWidth / 2 + SantaJumpConstants.HORIZONTAL_GAP_BETWEEN_STEPS + _cameraWidth / 2, _lastSideBarY);
            _staticGameElementsLayer.attachChild(sideBorderLeft.getEntity());
            _staticGameElementsLayer.attachChild(sideBorderRight.getEntity());
            _attachedGameElements.add(sideBorderLeft);
            _attachedGameElements.add(sideBorderRight);
        }

        // Attach game elements
        if (!_isStepsAttached) {
            _isStepsAttached = true;
            _solidStepIndexes.clear();
            _solidStepIndexes.add(2);
            _solidStepIndexes.add(3);
            //Attach the first row of four
            float y = FLOOR_HEIGHT + SantaJumpConstants.VERTICAL_GAP_BETWEEN_STEPS;
            attachStepRowOfFour(SantaJumpConstants.HORIZONTAL_GAP_BETWEEN_STEPS, y, _solidStepIndexes, false);
            _lastStepRowCount = 4;
            _lastStepRowY = y;
        }


        while (_lastStepRowY + StepBase.STEP_BASE_HEIGHT / 2 + SantaJumpConstants.VERTICAL_GAP_BETWEEN_STEPS < maxY) {
            // Top level not reached yet, can attach more
            float y = _lastStepRowY + StepBase.STEP_BASE_HEIGHT / 2 + SantaJumpConstants.VERTICAL_GAP_BETWEEN_STEPS;
            _lastStepRowY = y;
            if (_lastStepRowCount == 4) {
                updateNextSolidStepsForThreeSteps();
                attachStepRowOfThree(SantaJumpConstants.HORIZONTAL_GAP_BETWEEN_STEPS, y, _solidStepIndexes, false);
                _lastStepRowCount = 3;
            } else {
                updateNextSolidStepsForFourSteps();
                attachStepRowOfFour(SantaJumpConstants.HORIZONTAL_GAP_BETWEEN_STEPS, y, _solidStepIndexes, false);
                _lastStepRowCount = 4;
            }
        }

        _gameHud.setObjectCount(_dynamicGameElementsLayer.getChildCount());
    }

    private void attachStepRowOfFour(float gap, float y, List<Integer> solidStepIndexes, boolean forceSolidSteps) {
        boolean stepOneIsSolidStep = solidStepIndexes.contains(1) || forceSolidSteps;
        boolean stepTwoIsSolidStep = solidStepIndexes.contains(2) || forceSolidSteps;
        boolean stepThreeIsSolidStep = solidStepIndexes.contains(3) || forceSolidSteps;
        boolean stepFourIsSolidStep = solidStepIndexes.contains(4) || forceSolidSteps;

        String stepID = _limitToShowMetalSteps >= _score ? WoodenStep.ID : IronStep.ID;

        Vector2 step2Position = new Vector2(_cameraWidth / 2 - gap / 2, y);
        BaseGameElement step2 = stepTwoIsSolidStep ? _gameElementPool.obtainPoolItem(stepID, step2Position.x, step2Position.y)
                : _gameElementPool.obtainPoolItem(BrokenStep.ID, step2Position.x, step2Position.y);

        Vector2 step1Position = new Vector2(_cameraWidth / 2 - 3 * gap / 2, y);
        BaseGameElement step1 = stepOneIsSolidStep ? _gameElementPool.obtainPoolItem(stepID, step1Position.x, step1Position.y)
                : _gameElementPool.obtainPoolItem(BrokenStep.ID, step1Position.x, step1Position.y);

        Vector2 step3Position = new Vector2(_cameraWidth / 2 + gap / 2, y);
        BaseGameElement step3 = stepThreeIsSolidStep ? _gameElementPool.obtainPoolItem(stepID, step3Position.x, step3Position.y)
                : _gameElementPool.obtainPoolItem(BrokenStep.ID, step3Position.x, step3Position.y);

        Vector2 step4Position = new Vector2(_cameraWidth / 2 + 3 * gap / 2, y);
        BaseGameElement step4 = stepFourIsSolidStep ? _gameElementPool.obtainPoolItem(stepID, step4Position.x, step4Position.y)
                : _gameElementPool.obtainPoolItem(BrokenStep.ID, step4Position.x, step4Position.y);
        _dynamicGameElementsLayer.attachChild(step1.getEntity());
        _dynamicGameElementsLayer.attachChild(step2.getEntity());
        _dynamicGameElementsLayer.attachChild(step3.getEntity());
        _dynamicGameElementsLayer.attachChild(step4.getEntity());
        _attachedGameElements.add(step1);
        _attachedGameElements.add(step2);
        _attachedGameElements.add(step3);
        _attachedGameElements.add(step4);

        //Attach special items if two or more solid steps
        tryAttachSantaInteractors(solidStepIndexes, step1Position, step2Position, step3Position, step4Position);
    }

    private void attachStepRowOfThree(float gap, float y, List<Integer> solidStepIndexes, boolean forceSolidSteps) {
        boolean stepOneIsSolidStep = solidStepIndexes.contains(1) || forceSolidSteps;
        boolean stepTwoIsSolidStep = solidStepIndexes.contains(2) || forceSolidSteps;
        boolean stepThreeIsSolidStep = solidStepIndexes.contains(3) || forceSolidSteps;

        String stepID = _limitToShowMetalSteps >= _score ? WoodenStep.ID : IronStep.ID;

        Vector2 step2Position = new Vector2(_cameraWidth / 2, y);
        BaseGameElement step2 = stepTwoIsSolidStep ? _gameElementPool.obtainPoolItem(stepID, step2Position.x, step2Position.y)
                : _gameElementPool.obtainPoolItem(BrokenStep.ID, step2Position.x, step2Position.y);

        Vector2 step1Position = new Vector2(_cameraWidth / 2 - gap, y);
        BaseGameElement step1 = stepOneIsSolidStep ? _gameElementPool.obtainPoolItem(stepID, step1Position.x, step1Position.y)
                : _gameElementPool.obtainPoolItem(BrokenStep.ID, step1Position.x, step1Position.y);

        Vector2 step3Position = new Vector2(_cameraWidth / 2 + gap, y);
        BaseGameElement step3 = stepThreeIsSolidStep ? _gameElementPool.obtainPoolItem(stepID, step3Position.x, step3Position.y)
                : _gameElementPool.obtainPoolItem(BrokenStep.ID, step3Position.x, step3Position.y);
        _dynamicGameElementsLayer.attachChild(step1.getEntity());
        _dynamicGameElementsLayer.attachChild(step2.getEntity());
        _dynamicGameElementsLayer.attachChild(step3.getEntity());
        _attachedGameElements.add(step1);
        _attachedGameElements.add(step2);
        _attachedGameElements.add(step3);

        tryAttachSantaInteractors(solidStepIndexes, step1Position, step2Position, step3Position);
    }

    private void tryAttachSantaInteractors(List<Integer> solidStepIndexes, Vector2... stepLocation) {
        if (solidStepIndexes.size() > 1) {
            String itemId = "";
            // there are more than two solid steps. We can place one special item
            if (canProvideHelp()) {
                // Add clock
                itemId = Clock.ID;
            } else {
                // Add special item in .75 probability
                if (_score > 10) {
                    float selectorProbability = MathUtil.NextRand(0, 1);
                    if (_score < _limitToShowAdvancedItems) {
                        // Can show simple items only
                        if (selectorProbability < .5) {
                            // Add clock
                            itemId = Clock.ID;
                        } else {
                            // Add gift
                            itemId = Gift.ID;
                        }
                    } else {
                        // Can show all items
                        if (selectorProbability < .25) {
                            // Add clock
                            itemId = Clock.ID;
                        } else if (selectorProbability < .5) {
                            // Add gift
                            itemId = Gift.ID;
                        } else if (selectorProbability < .75) {
                            // Add Fairy
                            itemId = Fairy.ID;
                        } else {
                            // Add fire
                        }
                    }
                }
            }
            if (!itemId.isEmpty()) {
                Vector2 selectedStepLocation = stepLocation[solidStepIndexes.get(MathUtil.NextRandInt(0, solidStepIndexes.size() - 1))];
                SantaInteractor santaInteractor = (SantaInteractor)_gameElementPool.obtainPoolItem(itemId, selectedStepLocation.x, selectedStepLocation.y);
                santaInteractor.getBody().setTransform(selectedStepLocation.x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                        (selectedStepLocation.y + StepBase.STEP_BASE_HEIGHT / 2 + santaInteractor.getEntity().getHeight() / 2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
                _dynamicGameElementsLayer.attachChild(santaInteractor.getEntity());
                _attachedGameElements.add(santaInteractor);

                santaInteractor.startAnimation();
            }
        }
    }

    private void updateNextSolidStepsForFourSteps() {
        if (_solidStepIndexes.size() > 1) {
            // Make this will have only one solid step
            int nextSolidStepIndex = _solidStepIndexes.get(1);
            _solidStepIndexes.clear();
            _solidStepIndexes.add(nextSolidStepIndex);
        } else {
            // This will one or more solid steps
            int existingSolidStepIndex = _solidStepIndexes.get(0);
            int nextSolidStep = MathUtil.nextBoolean(_multipleStepsProbability) ? existingSolidStepIndex : existingSolidStepIndex + 1;
            _solidStepIndexes.clear();
            _solidStepIndexes.add(nextSolidStep);
            if (canProvideHelp()) {
                //Add another solid step
                int secondSolidStepIndex = nextSolidStep == existingSolidStepIndex ? existingSolidStepIndex + 1 : existingSolidStepIndex;
                if (_solidStepIndexes.get(0) > secondSolidStepIndex) {
                    _solidStepIndexes.add(0, secondSolidStepIndex);
                } else {
                    _solidStepIndexes.add(secondSolidStepIndex);
                }
            }
        }
    }

    private void updateNextSolidStepsForThreeSteps() {
        if (_solidStepIndexes.size() > 1) {
            // Make this will have only one solid step
            int nextSolidStepIndex = _solidStepIndexes.get(0);
            _solidStepIndexes.clear();
            _solidStepIndexes.add(nextSolidStepIndex);
        } else {
            // This will one or more solid steps
            if (_solidStepIndexes.contains(1)) {
                _solidStepIndexes.clear();
                _solidStepIndexes.add(1);
            } else if (_solidStepIndexes.contains(4)) {
                _solidStepIndexes.clear();
                _solidStepIndexes.add(3);
            } else {
                // if solid step indexes are 2 or three
                int existingSolidStepIndex = _solidStepIndexes.get(0);
                int nextSolidStepIndex = MathUtil.nextBoolean(_multipleStepsProbability) ? existingSolidStepIndex - 1 : existingSolidStepIndex;
                _solidStepIndexes.clear();
                _solidStepIndexes.add(nextSolidStepIndex);

                if (canProvideHelp()) {
                    //Add another solid step
                    int secondSolidStepIndex = nextSolidStepIndex == existingSolidStepIndex - 1 ? existingSolidStepIndex : existingSolidStepIndex - 1;
                    if (_solidStepIndexes.get(0) > secondSolidStepIndex) {
                        _solidStepIndexes.add(0, secondSolidStepIndex);
                    } else {
                        _solidStepIndexes.add(secondSolidStepIndex);
                    }
                }
            }
        }
    }

    private boolean canProvideHelp() {
        //Increase probability when remaining time reaches less than 7 seconds otherwise 20 %
        //Max probability 50%
        if (_remainingTimeInSeconds < 10) {
            int probabilityScore = (int) Math.ceil(_remainingTimeInSeconds);
            probabilityScore = probabilityScore <= 5 ? 5 : probabilityScore;
            return MathUtil.NextRandInt(1, 10) > probabilityScore;
        }
        return MathUtil.NextRandInt(1, 10) > 8;
    }

    private void createPhysicsWorld() {
        _physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0 - (SensorManager.GRAVITY_EARTH) * 50), false);
        _physicsWorld.setContactListener(BaseGameElement.BaseGameElementContactListener);
        registerUpdateHandler(_physicsWorld);
    }

    private void initializeStaticGameLayer() {
        //Attach floor
        float floorWidth = MathUtil.GetScaledWidth(GameResources.Get().MainMenuFloorTR, FLOOR_HEIGHT);
        for (int index = 0; index < 3; index++) {
            Sprite floor = new Sprite(_cameraWidth / 2 + (index - 1) * floorWidth,
                    FLOOR_HEIGHT / 2,
                    floorWidth,
                    FLOOR_HEIGHT,
                    GameResources.Get().MainMenuFloorTR,
                    ResourceManager.Get().Vbo);
            _staticGameElementsLayer.attachChild(floor);
        }
        //Create floor Line
        Line floor = new Line(-_cameraWidth, FLOOR_HEIGHT, 2 * _cameraWidth, FLOOR_HEIGHT, ResourceManager.Get().Vbo);
        floor.setColor(Color.TRANSPARENT);
        PhysicsFactory.createLineBody(_physicsWorld, floor, PhysicsFactory.createFixtureDef(1, 0, 0));
        _staticGameElementsLayer.attachChild(floor);

        //attach mountain
        float mountainHeight = 179;
        float mountainWidth = MathUtil.GetScaledWidth(GameResources.Get().MainMenuMountainTR, mountainHeight);
        for (int index = 0; index < 3; index++) {
            Sprite mountain = new Sprite(_cameraWidth / 2 + (index - 1) * mountainWidth,
                    mountainHeight / 2 + FLOOR_HEIGHT,
                    mountainWidth,
                    mountainHeight,
                    GameResources.Get().MainMenuMountainTR,
                    ResourceManager.Get().Vbo);
            _staticGameElementsLayer.attachChild(mountain);
        }
        // Attach trees
        float treeHeight = 145;
        float treeWidth = MathUtil.GetScaledWidth(GameResources.Get().MainMenuTreeTR, treeHeight);
        for (int index = 0; index < 3; index++) {
            Sprite treeOne = new Sprite(_cameraWidth / 8 + (index - 1) * _cameraWidth,
                    treeHeight / 2 + FLOOR_HEIGHT,
                    treeWidth,
                    treeHeight,
                    GameResources.Get().MainMenuTreeTR,
                    ResourceManager.Get().Vbo);
            treeOne.setScale(0.8f);
            treeOne.setY(treeOne.getY() - (treeHeight * 0.2f / 2));
            Sprite treeTwo = new Sprite(_cameraWidth * 3 / 8 + (index - 1) * _cameraWidth,
                    treeHeight / 2 + FLOOR_HEIGHT,
                    treeWidth,
                    treeHeight,
                    GameResources.Get().MainMenuTreeTR,
                    ResourceManager.Get().Vbo);
            Sprite treeThree = new Sprite(_cameraWidth * 5 / 8 + (index - 1) * _cameraWidth,
                    treeHeight / 2 + FLOOR_HEIGHT,
                    treeWidth,
                    treeHeight,
                    GameResources.Get().MainMenuTreeTR,
                    ResourceManager.Get().Vbo);
            treeThree.setScale(0.8f);
            treeThree.setY(treeThree.getY() - (treeHeight * 0.2f / 2));
            Sprite treeFour = new Sprite(_cameraWidth * 7 / 8 + (index - 1) * _cameraWidth,
                    treeHeight / 2 + FLOOR_HEIGHT,
                    treeWidth,
                    treeHeight,
                    GameResources.Get().MainMenuTreeTR,
                    ResourceManager.Get().Vbo);
            _staticGameElementsLayer.attachChild(treeOne);
            _staticGameElementsLayer.attachChild(treeTwo);
            _staticGameElementsLayer.attachChild(treeThree);
            _staticGameElementsLayer.attachChild(treeFour);
        }
    }

    private void initializeMenuLayer() {
        // Attach title
        final float titleHeight = 260;
        float titleWidth = MathUtil.GetScaledWidth(GameResources.Get().MainMenuTitleTR, titleHeight);
        final Sprite title = new Sprite(_cameraWidth / 2,
                _cameraHeight * 13 / 16,
                titleWidth,
                titleHeight,
                GameResources.Get().MainMenuTitleTR,
                ResourceManager.Get().Vbo);
        _mainMenuElementsLayer.attachChild(title);

        //Attach buttons
        float startButtonHeight = 80;
        _startButton = new GrowButton(_cameraWidth / 2, _cameraHeight * 7 / 12,
                MathUtil.GetScaledWidth(GameResources.Get().MainMenuStartButtonTR, startButtonHeight), startButtonHeight,
                GameResources.Get().MainMenuStartButtonTR) {
            @Override
            public void onClick() {
                // Remove main menu
                AlphaModifier alphaModifier = new AlphaModifier(.3f, 1, 0, new IEntityModifier.IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

                    }

                    @Override
                    public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                        //Reload game
                        reloadGame();
                        ResourceManager.Get().activity.runOnUpdateThread(new Runnable() {
                            @Override
                            public void run() {
                                _mainMenuElementsLayer.detachSelf();
                                unregisterTouchArea(_scoreButton);
                                unregisterTouchArea(_startButton);
                                unregisterTouchArea(_rateButton);
                            }
                        });
                    }
                });
                float dy = 20;
                MoveModifier moveUpModifier = new MoveModifier(.3f, _mainMenuElementsLayer.getX(), _mainMenuElementsLayer.getY(), _mainMenuElementsLayer.getX(), _mainMenuElementsLayer.getY() + dy);
                _mainMenuElementsLayer.registerEntityModifier(new ParallelEntityModifier(alphaModifier, moveUpModifier));
            }
        };
        _startButton.setScales(1, 1.1f);
        _startButton.setSoundFileName("Click.ogg");
        _mainMenuElementsLayer.attachChild(_startButton);
        registerTouchArea(_startButton);

        float utilityButtonHeight = 70;
        _rateButton = new GrowButton(_cameraWidth * 3 / 16, _cameraHeight * 7 / 12,
                MathUtil.GetScaledWidth(GameResources.Get().MainMenuScoreButtonTR, utilityButtonHeight), utilityButtonHeight,
                GameResources.Get().MainMenuScoreButtonTR) {
            @Override
            public void onClick() {
                // Show rate screen
            }
        };
        _rateButton.setScales(1, 1.1f);
        _rateButton.setSoundFileName("Click.ogg");
        _mainMenuElementsLayer.attachChild(_rateButton);
        registerTouchArea(_rateButton);

        _scoreButton = new GrowButton(_cameraWidth * 13 / 16, _cameraHeight * 7 / 12,
                MathUtil.GetScaledWidth(GameResources.Get().MainMenuScoreButtonTR, utilityButtonHeight), utilityButtonHeight,
                GameResources.Get().MainMenuScoreButtonTR) {
            @Override
            public void onClick() {
                // Show score screen
            }
        };
        _scoreButton.setScales(1, 1.1f);
        _scoreButton.setSoundFileName("Click.ogg");
        _mainMenuElementsLayer.attachChild(_scoreButton);
        registerTouchArea(_scoreButton);
    }

    private void reloadGame() {
        //TODO: Move _camera to init position
        //TODO: reinitialize statistics
        _score = 0;
        //TODO: reinitialize HUD
        _gameHud.setVisible(true);
        //TODO: Re initialize Santa

        _gameState = GameState.Started;

//        //TODO: remove
//        float dx = 150;
//        float dy = 200;
//        float initialX = _santa.getEntity().getX();
//        float initialY = _santa.getEntity().getY() - _santa.getEntity().getHeight() / 2;
//
//        StepBase leftStep = new WoodenStep(_physicsWorld,  this, initialX - dx, initialY + dy);
//        _dynamicGameElementsLayer.attachChild(leftStep.getEntity());
//        StepBase rightStep = new WoodenStep(_physicsWorld,  this, initialX+ dx, initialY + dy);
//        _dynamicGameElementsLayer.attachChild(rightStep.getEntity());
//
//        StepBase nextBaseStep = leftStep;
//        for (int i = 0; i < 100; i++) {
//            initialX = nextBaseStep.getEntity().getX();
//            initialY = nextBaseStep.getEntity().getY() + leftStep.getEntity().getHeight() / 2;
//            WoodenStep leftStep2 = new WoodenStep(_physicsWorld, this, initialX - dx, initialY + dy);
//            _dynamicGameElementsLayer.attachChild(leftStep2.getEntity());
//            WoodenStep rightStep2 = new WoodenStep(_physicsWorld, this, initialX + dx, initialY + dy);
//            _dynamicGameElementsLayer.attachChild(rightStep2.getEntity());
//            nextBaseStep = (int)(Math.random() * 10) > 5 ? leftStep2 : rightStep2;
//        }
    }

    private void addDebugModeText() {
        Context context = ResourceManager.Get().activity;
        int flags = 0;
        try {
            flags = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).flags;
        } catch (NameNotFoundException e) {
        }
        // Do not replace this with BuildProperties.DebugBuild. This is there to correctly identify debug build
        if ((flags &= ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            Text debugModeIndicatorText = new Text(0, 0, FontResources.Get().CondensedFont,
                    "Debug!!",
                    ResourceManager.Get().Vbo);
            MathUtil.SetScaledSize(debugModeIndicatorText, 40);
            debugModeIndicatorText.setPosition(_camera.getWidth() - debugModeIndicatorText.getWidth()
                            * debugModeIndicatorText.getScaleX() / 2,
                    _camera.getHeight() - debugModeIndicatorText.getHeightScaled());
            attachChild(debugModeIndicatorText);
            final float scale = debugModeIndicatorText.getScaleX();
            debugModeIndicatorText.registerEntityModifier(new LoopEntityModifier(
                    new SequenceEntityModifier(
                            new DelayModifier(3),
                            new ScaleModifier(0.5f, 0.001f, scale),
                            new ScaleModifier(0.5f, scale, 0.001f)
                    )));
        }
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown() && (_gameState == GameState.Started || _gameState == GameState.Running)) {
            if (_gameState == GameState.Started) {
                _gameState = GameState.Running;
            }
            if (ResourceManager.Get().camera.getCenterX() >= pSceneTouchEvent.getX()) {
                if (!reachedLeftEdge()) {
                    _santa.jumpLeft();
                }
            } else {
                if (!reachedRightEdge()) {
                    _santa.jumpRight();
                }
            }
            return true;
        }
        return false;
    }

    private boolean reachedLeftEdge() {
        return _santa.getEntity().getX() < _cameraWidth / 2 - 3 * SantaJumpConstants.HORIZONTAL_GAP_BETWEEN_STEPS / 2 + StepBase.STEP_BASE_WIDTH / 2;
    }

    private boolean reachedRightEdge() {
        return _santa.getEntity().getX() > _cameraWidth / 2 + 3 * SantaJumpConstants.HORIZONTAL_GAP_BETWEEN_STEPS / 2 - StepBase.STEP_BASE_WIDTH / 2;
    }
}
