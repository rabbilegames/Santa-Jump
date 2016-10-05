package com.rabbile.santajump.free;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.util.modifier.ease.EaseSineIn;
import org.rabbilegames.ResourceManager;

import org.rabbilegames.SplashSceneBase;
import org.rabbilegames.Util.MathUtil;

public class SplashScene extends SplashSceneBase {
    private Sprite _companyLogoSprite;

    @Override
    public void createScene() {
        ResourceManager.Get().loadSplashScreenResources();
        _companyLogoSprite = new Sprite(0, 0, ResourceManager.Get().CompanyLogo,
                ResourceManager.Get().Vbo) {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        };

        float middleX = ResourceManager.Get().camera.getWidth() / 2;
        float middleY = ResourceManager.Get().camera.getHeight() / 2;
        MathUtil.SetScaledSize(_companyLogoSprite, 300);
        _companyLogoSprite.setScale(3);
        _companyLogoSprite.setPosition(middleX, middleY);
        _companyLogoSprite.registerEntityModifier(new ScaleModifier(1.2f, 3f, 1, EaseSineIn.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                loadGameResources();
            }
        });
        attachChild(_companyLogoSprite);
    }

    private void loadGameResources() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ResourceManager.Get().LoadResources();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void OnUnloadScene() {
        super.OnUnloadScene();
        detachChildren();
    }
}
