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

//    public boolean is_isAttached() {
//        return _isAttached;
//    }

//    public void AttchElement() {
//        this._isAttached = true;
//        this._entity.setVisible(true);
//        this._entity.setIgnoreUpdate(false);
//        this.gameSceneServices.AttachBaseGameElementEntity((IShape) this._entity);
//    }
//
//    public void DetachElement() {
//        if (this._isAttached) {
//            this._isAttached = false;
//            this._entity.setVisible(false);
//            this._entity.setIgnoreUpdate(true);
//            this._entity.detachSelf();
//        }
//    }

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
//        if (this.gameElementLevelData != null
//                && this.gameElementLevelData.getTranslationCommands().size() > 0) {
//            if (this.currentCommandIndex == 0
//                    && !this.commandInitialized) {
//                this.currentX = gameElementLevelData.getPositionX();
//                this.currentY = gameElementLevelData.getPositionY();
//            }
//
//            TranslationCommand currentCommand = this.gameElementLevelData.getTranslationCommands().get(this.currentCommandIndex);
//
//            if (commandFinished) {
//                this.currentCommandIndex = this.gameElementLevelData.getTranslationCommands().size() == this.currentCommandIndex + 1 ? 0 : this.currentCommandIndex + 1;
//                commandFinished = false;
//                destinationReached = false;
//                this.commandInitialized = false;
//            } else if (destinationReached) {
//                //wait until timeout complete
//                this.stoppedTime += pSecondsElapsed;
//                if (stoppedTime >= currentCommand.getInterval()) {
//                    this.stoppedTime = 0;
//                    commandFinished = true;
//                }
//            } else if (!this.commandInitialized) {
//                this.commandInitialized = true;
//                float length = currentCommand.getDistance();
//                float theta = MathUtils.degToRad(currentCommand.getAngle());
//                float velocity = currentCommand.getVelocity();
//                this.destinationX = (float) (this.currentX + length * Math.sin(theta));
//                this.destinationY = (float) (this.currentY + length * Math.cos(theta));
//                this.Vx = (float) (velocity * Math.sin(theta));
//                this.Vy = (float) (velocity * Math.cos(theta));
//
//                //apply velocity
//                this._body.setLinearVelocity(this.Vx, this.Vy);
//
//                this.currentX = this.destinationX;
//                this.currentY = this.destinationY;
//            } else {
//                //translate until destination reach
//                boolean reachedX = Vx == 0 ? true : (Vx > 0 ? destinationX <= this._entity.getX() : destinationX >= this._entity.getX());
//                boolean reachedY = Vy == 0 ? true : (Vy >= 0 ? destinationY <= this._entity.getY() : destinationY >= this._entity.getY());
//                this.destinationReached = reachedX && reachedY;
//                if (this.destinationReached) {
//                    this._body.setLinearVelocity(0, 0);
//                }
//            }
//        }
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
