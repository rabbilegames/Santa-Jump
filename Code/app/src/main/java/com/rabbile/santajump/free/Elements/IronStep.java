package com.rabbile.santajump.free.Elements;

import com.rabbile.santajump.free.IGameSceneServices;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.rabbilegames.ResourceManager;

public class IronStep extends StepBase {
    public static final String ID = "IronStep";

    public IronStep(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices) {
        super(physicsWorld, gameSceneServices);
        ITextureRegion textureRegion = GameResources.Get().IornStepTR;
        Sprite ironStep = new Sprite(0, 0, STEP_BASE_WIDTH, STEP_BASE_HEIGHT, textureRegion, ResourceManager.Get().Vbo);
        setStepSprite(ironStep);
        createPhysics();
    }

    @Override
    public String getBaseGameElementID() {
        return ID;
    }
}
