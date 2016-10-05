package com.rabbile.santajump.free.Elements;

import com.rabbile.santajump.free.IGameSceneServices;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.Util.MathUtil;

public class WoodenStep extends StepBase{
    public static final String ID = "WoodenStep";
    public WoodenStep(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices, float initialX, float initialY) {
        super(physicsWorld, gameSceneServices);
        ITextureRegion textureRegion = GameResources.Get().WoodenStepTR;
        Sprite woodenStep = new Sprite(initialX, initialY, STEP_BASE_WIDTH, STEP_BASE_HEIGHT, textureRegion, ResourceManager.Get().Vbo);
        setStepSprite(woodenStep);
        createPhysics();
    }

    @Override
    public String getBaseGameElementID() {
        return ID;
    }
}