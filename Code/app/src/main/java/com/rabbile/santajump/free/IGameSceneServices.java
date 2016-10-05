package com.rabbile.santajump.free;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;


public interface IGameSceneServices {
    Engine GetEngine();

    void RegisterTouchArea(Entity entity);

    void hideChildScene();

    void onSantaJumpOnFallingStep();

    void onSantaJumpOnWoodenStep();

    float getCameraWidth();

    float getCameraHeight();

    HUD GetHUD();
}
