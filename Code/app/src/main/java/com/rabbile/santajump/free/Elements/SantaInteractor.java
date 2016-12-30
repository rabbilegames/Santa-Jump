package com.rabbile.santajump.free.Elements;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.rabbile.santajump.free.IGameSceneServices;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.rabbilegames.framework.BaseGameElement;

/**
 * Created by asanka.samarawickram on 10/9/2016.
 */

public class SantaInteractor extends BaseGameElement<IShape> {
    protected IGameSceneServices _gameSceneServices;
    protected boolean _operationFulfilled = false;

    public SantaInteractor(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices) {
        super(physicsWorld);
        _gameSceneServices = gameSceneServices;
    }

    @Override
    public String getBaseGameElementID() {
        return getClass().getSimpleName();
    }

    protected void createPhysics(PhysicsWorld physicsWorld, IShape sprite)
    {
        Body body = PhysicsFactory.createBoxBody(physicsWorld, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(),
                BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(1, 0, 0, false, ElementCategory.BACKGROUND_OBJECT, ElementCategory.BACKGROUND_OBJECT, (short)0));
        registerPhysics(sprite, body);
    }

    public void startAnimation(){
        _entity.registerEntityModifier(new LoopEntityModifier(
                new SequenceEntityModifier(
                        new ScaleModifier(.5f, 1, .8f),
                        new ScaleModifier(.5f, .8f, 1)
                )
        ));
    }


    public void onSantaHit(){
    }

    protected void disappearSantaInteractor(int addedTime, int addedScore){
        _entity.registerEntityModifier(new ParallelEntityModifier(
                new MoveModifier(.8f, _entity.getX(), _entity.getY(), _entity.getX(), _entity.getY() + _entity.getHeight()),
                new AlphaModifier(.8f, 1, 0)
        ));
    }

    @Override
    public void showOnRecycle(float x, float y) {
        super.showOnRecycle(x, y);
        _entity.setAlpha(1);
        _operationFulfilled = false;
    }
}
