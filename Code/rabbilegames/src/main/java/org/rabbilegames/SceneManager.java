package org.rabbilegames;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

public class SceneManager {
    SplashSceneBase _spalshScene = null;
    BaseScene mTransitionScene = new BaseScene() {
        @Override
        public SceneType getSceneType() {
            return SceneType.SCENE_TRANSITION;
        }
    };

    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    private static final SceneManager INSTANCE = new SceneManager();

    private BaseScene currentScene;

    private Engine engine = ResourceManager.Get().engine;

    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void setSceneDirect(final BaseScene scene) {
        engine.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                currentScene = scene;
                engine.setScene(scene);
            }
        });
    }

    public void setScene(final BaseScene inScene, boolean isInSceneTop) {
        setSceneDirect(mTransitionScene);
        inScene.OnLoadSceneResources();
        inScene.OnLoadScene();
        final BaseScene outScene = currentScene;
        outScene.detachSelf();
        if (isInSceneTop) {
            mTransitionScene.attachChild(outScene);
            mTransitionScene.attachChild(inScene);
        } else {
            mTransitionScene.attachChild(inScene);
            mTransitionScene.attachChild(outScene);
        }

        inScene.beginTransitionIn();
        currentScene.beginTransitionOut();

        engine.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if (!(inScene.isSceneInTransition() || outScene.isSceneInTransition())) {
                    mTransitionScene.detachChildren();
                    currentScene = inScene;
                    inScene.onTransitionFinished();
                    engine.setScene(inScene);
                    engine.unregisterUpdateHandler(this);
                    engine.runOnUpdateThread(new Runnable() {
                        @Override
                        public void run() {
                            outScene.OnUnloadScene();
                            outScene.disposeScene();
                        }
                    });
                }
            }

            @Override
            public void reset() {
            }
        });
    }

    public void prepare(SplashSceneBase splashSceneBase) {
        _spalshScene = splashSceneBase;
    }

    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static SceneManager get() {
        return INSTANCE;
    }

    public BaseScene getCurrentScene() {
        return currentScene;
    }

    public void CreateAndLoadSplashScene(OnCreateSceneCallback createSceneCallback) {
        setSceneDirect(_spalshScene);
        createSceneCallback.onCreateSceneFinished(_spalshScene);
    }
}
