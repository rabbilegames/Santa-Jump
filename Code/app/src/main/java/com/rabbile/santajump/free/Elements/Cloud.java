package com.rabbile.santajump.free.Elements;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.rabbile.santajump.free.IGameSceneServices;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.Util.MathUtil;
import org.rabbilegames.framework.BaseGameElement;

public class Cloud extends BaseGameElement<Sprite> {
    public static final String ID = "Cloud";
    public static final float HEIGHT = 155;

    private IGameSceneServices _gameSceneServices;

    public Cloud(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices) {
        super(physicsWorld);
        _gameSceneServices = gameSceneServices;
        ITextureRegion textureRegion = GameResources.Get().CloudTR;
        float spriteWidth = MathUtil.GetScaledWidth(textureRegion, HEIGHT);
        Sprite sprite = new Sprite(0, 0, spriteWidth, HEIGHT, textureRegion, ResourceManager.Get().Vbo);
        sprite.setAlpha(.75f);
        createPhysics(physicsWorld, sprite);
    }

    @Override
    public String getBaseGameElementID() {
        return ID;
    }

    private void createPhysics(PhysicsWorld physicsWorld, Sprite sprite)
    {
        Body body = PhysicsFactory.createBoxBody(physicsWorld, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(),
                BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(1, 0, 0, false, ElementCategory.BACKGROUND_OBJECT, ElementCategory.BACKGROUND_OBJECT, (short)0));
        registerPhysics(sprite, body);
    }

    public void transformRandom() {
        float scaleFactor = MathUtil.NextRand(.5f, 1.5f);
        _entity.setScaleX(MathUtil.NextRandBool() ? scaleFactor : -scaleFactor);
        _entity.setScaleY(Math.abs(scaleFactor));
    }
}
