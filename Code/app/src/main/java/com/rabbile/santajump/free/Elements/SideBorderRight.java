package com.rabbile.santajump.free.Elements;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.rabbile.santajump.free.IGameSceneServices;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.Util.MathUtil;
import org.rabbilegames.framework.BaseGameElement;

/**
 * Created by asanka.samarawickram on 9/11/2016.
 */
public class SideBorderRight extends BaseGameElement<Sprite> {
    public static final String ID = "SideBorderRight";
    public static final float HEIGHT = 1000;

    private IGameSceneServices _gameSceneServices;

    public SideBorderRight(PhysicsWorld physicsWorld, IGameSceneServices gameSceneServices) {
        super(physicsWorld);
        _gameSceneServices = gameSceneServices;
        ITextureRegion textureRegion = GameResources.Get().SideBarLeftTR;
        float spriteWidth = MathUtil.GetScaledWidth(textureRegion, HEIGHT);
        Sprite sprite = new Sprite(0, 0, spriteWidth, HEIGHT, textureRegion, ResourceManager.Get().Vbo);
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
}
