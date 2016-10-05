package com.rabbile.santajump.free.Elements;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rabbile.santajump.free.IGameSceneServices;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;
import org.rabbilegames.ResourceManager;

public class BrokenStep extends StepBase{
    private enum State{
        Solid,
        Falling,
    }
    public static final String ID = "BrokenStep";
    private State _state;
    public BrokenStep(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices) {
        super(physicsWorld, gameSceneServices);
        _state = State.Solid;
        ITextureRegion textureRegion = GameResources.Get().BrokenStepTR;
        Sprite fallingSprite = new Sprite(0, 0, STEP_BASE_WIDTH, STEP_BASE_HEIGHT, textureRegion, ResourceManager.Get().Vbo);
        fallingSprite.setColor(Color.RED);
        setStepSprite(fallingSprite);
        createPhysics();
    }

    @Override
    public String getBaseGameElementID() {
        return ID;
    }

    public void startFalling() {
        _body.setType(BodyDef.BodyType.DynamicBody);
        _state = State.Falling;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if (_state == State.Falling){
            contact.setEnabled(false);
        }
    }
}
