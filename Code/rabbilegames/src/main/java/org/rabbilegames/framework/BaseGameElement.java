package org.rabbilegames.framework;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.shape.IShape;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public abstract class BaseGameElement<T extends IShape> implements IUpdateHandler, ContactListener {
    protected PhysicsWorld _physicsWorld;
    protected T _entity;
    protected Body _body;
    protected PhysicsConnector _connector;

    public abstract String getBaseGameElementID();

    public BaseGameElement(PhysicsWorld physicsWorld) {
        _physicsWorld = physicsWorld;
    }

    public T getEntity() {
        return _entity;
    }

    public Body getBody() {
        return _body;
    }

    public void registerPhysics (T entity, Body body) {
        _entity = entity;
        _body = body;
        _connector = new PhysicsConnector(entity, body);
        _physicsWorld.registerPhysicsConnector(_connector);
        _body.setUserData(this);
        _entity.registerUpdateHandler(this);
    }

    public void Distroy() {
        this._entity.setVisible(false);
        this._entity.detachSelf();
    }

    public void setPosition(float x, float y) {
        _body.setTransform(x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
    }


    @Override
    public void onUpdate(float pSecondsElapsed) {
    }

    public void hideOnRecycle(float x, float y) {
        setPosition(x, y);
        _entity.setVisible(false);
        _entity.setIgnoreUpdate(true);
        _entity.clearEntityModifiers();
        _entity.clearUpdateHandlers();
    }

    public void showOnRecycle(float x, float y){
        setPosition(x, y);
        _entity.setVisible(true);
        _entity.setIgnoreUpdate(false);
    }

    @Override
    public void reset() {

    }

    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public static ContactListener BaseGameElementContactListener = new ContactListener() {
        @Override
        public void beginContact(Contact contact) {
            BaseGameElement physicsObjectA = (BaseGameElement) contact.getFixtureA().getBody().getUserData();
            BaseGameElement physicsObjectB = (BaseGameElement) contact.getFixtureB().getBody().getUserData();

            if (physicsObjectA != null) {
                physicsObjectA.beginContact(contact);
            }
            if (physicsObjectB != null) {
                physicsObjectB.beginContact(contact);
            }
        }

        @Override
        public void endContact(Contact contact) {
            BaseGameElement physicsObjectA = (BaseGameElement) contact.getFixtureA().getBody().getUserData();
            BaseGameElement physicsObjectB = (BaseGameElement) contact.getFixtureB().getBody().getUserData();

            if (physicsObjectA != null) {
                physicsObjectA.endContact(contact);
            }
            if (physicsObjectB != null) {
                physicsObjectB.endContact(contact);
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            BaseGameElement physicsObjectA = (BaseGameElement) contact.getFixtureA().getBody().getUserData();
            BaseGameElement physicsObjectB = (BaseGameElement) contact.getFixtureB().getBody().getUserData();

            if (physicsObjectA != null) {
                physicsObjectA.preSolve(contact, oldManifold);
            }
            if (physicsObjectB != null) {
                physicsObjectB.preSolve(contact, oldManifold);
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            BaseGameElement physicsObjectA = (BaseGameElement) contact.getFixtureA().getBody().getUserData();
            BaseGameElement physicsObjectB = (BaseGameElement) contact.getFixtureB().getBody().getUserData();

            if (physicsObjectA != null) {
                physicsObjectA.postSolve(contact, impulse);
            }
            if (physicsObjectB != null) {
                physicsObjectB.postSolve(contact, impulse);
            }
        }
    };
}
