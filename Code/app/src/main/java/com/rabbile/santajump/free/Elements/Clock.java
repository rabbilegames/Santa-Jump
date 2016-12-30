package com.rabbile.santajump.free.Elements;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.rabbile.santajump.free.IGameSceneServices;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.Util.MathUtil;
import org.rabbilegames.framework.BaseGameElement;


public class Clock extends SantaInteractor{
    public static final String ID = "Clock";
    public static final float HEIGHT = 120;
    public static final int ADDED_TIME = 5;

    public Clock(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices) {
        super(physicsWorld, gameSceneServices);
        ITextureRegion textureRegion = GameResources.Get().ClockTR;
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
            _gameSceneServices.addTime(ADDED_TIME);
            disappearSantaInteractor(ADDED_TIME, 0);
            setPosition(0, 0);
            _operationFulfilled = true;
        }
    }
}
