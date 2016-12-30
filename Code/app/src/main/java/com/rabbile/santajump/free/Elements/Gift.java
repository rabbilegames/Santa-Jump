package com.rabbile.santajump.free.Elements;

import com.rabbile.santajump.free.IGameSceneServices;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.Util.MathUtil;


public class Gift extends SantaInteractor{
    public static final String ID = "Gift";
    public static final float HEIGHT = 100;
    private static final int ADDED_SCORE = 10;

    public Gift(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices) {
        super(physicsWorld, gameSceneServices);
        ITextureRegion textureRegion = GameResources.Get().GiftTR;
        float spriteWidth = MathUtil.GetScaledWidth(textureRegion, HEIGHT);
        Sprite sprite = new Sprite(0, 0, spriteWidth, HEIGHT, textureRegion, ResourceManager.Get().Vbo);
        createPhysics(physicsWorld, sprite);
    }

    @Override
    public String getBaseGameElementID() {
        return ID;
    }

    @Override
    public void onSantaHit() {
        if (!_operationFulfilled) {
            _gameSceneServices.addScore(ADDED_SCORE);
            disappearSantaInteractor(0, ADDED_SCORE);
            setPosition(0, 0);
            _operationFulfilled = true;
        }
    }
}
