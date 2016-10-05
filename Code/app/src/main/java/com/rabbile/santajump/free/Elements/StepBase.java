package com.rabbile.santajump.free.Elements;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.rabbile.santajump.free.IGameSceneServices;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.rabbilegames.framework.BaseGameElement;

public class StepBase extends BaseGameElement<Sprite> {
    public static final float STEP_BASE_HEIGHT = 25;
    public static final float STEP_BASE_WIDTH = 120;

    IGameSceneServices _gameSceneServices;

    public Sprite getStepSprite() {
        return _stepSprite;
    }

    public void setStepSprite(Sprite stepSprite) {
        _stepSprite = stepSprite;
    }

    Sprite _stepSprite;

    public StepBase(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices) {
        super(physicsWorld);
        _gameSceneServices = gameSceneServices;
    }

    protected void createPhysics()
    {
        Body body = PhysicsFactory.createBoxBody(_physicsWorld, _stepSprite.getX(), _stepSprite.getY(), _stepSprite.getWidth(), _stepSprite.getHeight(),
                BodyDef.BodyType.KinematicBody, PhysicsFactory.createFixtureDef(1, 0, 0, false, ElementCategory.GAME_OBJECT, ElementCategory.GAME_OBJECT, (short)0));
        registerPhysics(_stepSprite, body);
    }

    @Override
    public String getBaseGameElementID() {
        return getClass().getSimpleName();
    }
}
