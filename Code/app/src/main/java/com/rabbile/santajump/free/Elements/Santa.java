package com.rabbile.santajump.free.Elements;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rabbile.santajump.free.IGameSceneServices;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.Util.MathUtil;
import org.rabbilegames.framework.BaseGameElement;


public class Santa extends BaseGameElement<AnimatedSprite> {
    public static final String ID = "SANTA";
    private enum SantaState {
        Idle,
        Jumping,
        Falling,
        Dead,
    }
    public static final float SANTA_HEIGHT = 155;
    public final float impulseX = 365;
    public final float impulseY = 1500;
    IGameSceneServices _gameSceneServices;
    SantaState _state;
    StepBase _lastStandingStepBase = null;


    public Santa(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices, float initialX, float initialY) {
        super(physicsWorld);
        _gameSceneServices = gameSceneServices;
        ITextureRegion textureRegion = GameResources.Get().SantaTTR.getTextureRegion(0);
        float santaHeight = MathUtil.GetScaledWidth(textureRegion, SANTA_HEIGHT);
        AnimatedSprite santaSprite= new AnimatedSprite(initialX, initialY, santaHeight, SANTA_HEIGHT, GameResources.Get().SantaTTR,
                ResourceManager.Get().Vbo);
        createPhysics(physicsWorld, santaSprite);
        _state = SantaState.Idle;
    }

    private void createPhysics(PhysicsWorld physicsWorld, AnimatedSprite sprite)
    {
        Body body = PhysicsFactory.createBoxBody(physicsWorld, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(),
                BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1, 0, 0, false, ElementCategory.GAME_OBJECT, ElementCategory.GAME_OBJECT, (short)0));
        registerPhysics(sprite, body);
    }

    public void jumpLeft(){
        if (_state == SantaState.Idle) {
            if (_lastStandingStepBase != null) {
                setPosition(_lastStandingStepBase.getEntity().getX(), getEntity().getY());
            }
            _body.applyLinearImpulse(-impulseX, impulseY, _body.getWorldCenter().x, _body.getWorldCenter().y);
            _entity.stopAnimation(2);
            _state = SantaState.Jumping;
        }
    }

    public void jumpRight(){
        if (_state == SantaState.Idle)  {
            if (_lastStandingStepBase != null) {
                setPosition(_lastStandingStepBase.getEntity().getX(), getEntity().getY());
            }
            _body.applyLinearImpulse(impulseX, impulseY, _body.getWorldCenter().x, _body.getWorldCenter().y);
            _entity.stopAnimation(3);
            _state = SantaState.Jumping;
        }
    }

    @Override
    public String getBaseGameElementID() {
        return ID;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if (_state == SantaState.Falling) {
            contact.setEnabled(false);
        } else if (_state == SantaState.Jumping){
            Object userDataA = contact.getFixtureA().getBody().getUserData();
            Object userDataB = contact.getFixtureB().getBody().getUserData();
            Object collidedObjectUserData = userDataA instanceof Santa ? userDataB : userDataA;
            if (collidedObjectUserData instanceof StepBase) {
                StepBase collidedStep = (StepBase) collidedObjectUserData;
                if (collidedStep.getEntity().getY() + collidedStep.getEntity().getHeight() / 2 > _entity.getY() - _entity.getHeight() / 2) {
                    //Log.d("ABCD",  String.format("%f > %f. vy = %f", collidedStep.getEntity().getY() + collidedStep.getEntity().getHeight(), _entity.getY() - _entity.getHeight(), _body.getLinearVelocity().y ));
                    contact.setEnabled(false);
                } else {
                    if (collidedObjectUserData instanceof WoodenStep) {
                        if (_body.getLinearVelocity().y <= 0) {
                            _body.setLinearVelocity(0, 0);
                            _entity.stopAnimation(0);
                            _lastStandingStepBase = collidedStep;
                            _gameSceneServices.onSantaJumpOnWoodenStep();
                            _state = SantaState.Idle;

                        }
                    } else if (collidedObjectUserData instanceof BrokenStep) {
                        if (_body.getLinearVelocity().y <= 0) {
                            _body.setLinearVelocity(0, 0);
                            _entity.stopAnimation(1);
                            _lastStandingStepBase = collidedStep;
                            BrokenStep fallingStep = (BrokenStep) collidedObjectUserData;
                            fallingStep.startFalling();
                            _gameSceneServices.onSantaJumpOnFallingStep();
                            _state = SantaState.Falling;
                        }
                    }
                }
            }
        }
    }
}
