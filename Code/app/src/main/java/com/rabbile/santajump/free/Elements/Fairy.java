package com.rabbile.santajump.free.Elements;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.rabbile.santajump.free.IGameSceneServices;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.Util.MathUtil;
import org.rabbilegames.framework.BaseGameElement;


public class Fairy extends SantaInteractor {
    public static final String ID = "Fairy";
    public static final float HEIGHT = 120;
    private static final int ADDED_TIME = 5;
    private static final int ADDED_SCORE = 10;


    public Fairy(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices) {
        super(physicsWorld, gameSceneServices);
        ITextureRegion textureRegion = GameResources.Get().FairyTTR.getTextureRegion(0);
        float spriteWidth = MathUtil.GetScaledWidth(textureRegion, HEIGHT);
        AnimatedSprite sprite = new AnimatedSprite(0, 0, spriteWidth, HEIGHT, GameResources.Get().FairyTTR, ResourceManager.Get().Vbo);
        createPhysics(physicsWorld, sprite);
    }

    @Override
    public String getBaseGameElementID() {
        return ID;
    }

    @Override
    public void onSantaHit() {
        if (!_operationFulfilled) {
            _gameSceneServices.addTime(ADDED_TIME);
            _gameSceneServices.addScore(ADDED_SCORE);
            disappearSantaInteractor(ADDED_TIME, ADDED_SCORE);
            setPosition(0, 0);
            _operationFulfilled = true;
        }
    }
}
