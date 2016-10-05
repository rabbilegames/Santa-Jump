package org.rabbilegames.CommonUi;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import android.opengl.GLES20;

import org.rabbilegames.ResourceManager;
import org.rabbilegames.SFXManager;
import org.rabbilegames.Util.MathUtil;

/**
 * The GrowButton class simply shows an image that grows to a specific scale
 * while the player is touching it and returns to its original scale when the
 * touch is lifted or lost.
 * <p/>
 * ** @author Brian Broyles - IFL Game Studio
 **/
public abstract class GrowButton extends Entity {

    // ====================================================
    // CONSTANTS
    // ====================================================
    private static final float mGROW_DURATION_SECONDS = 0.05f;
    private static final float mNORMAL_SCALE_DEFAULT = 1f;
    private static final float mGROWN_SCALE_DEFAULT = 1.2f;
    private static final float mENABLED_ALPHA = 1f;
    private static final float mDISABLED_ALPHA = 0.5f;

    // ====================================================
    // VARIABLES
    // ====================================================
    boolean isClickDisabled;
    public boolean mIsEnabled = true;
    private float mNormalScale = mNORMAL_SCALE_DEFAULT;
    private float mGrownScale = mGROWN_SCALE_DEFAULT;
    private boolean mIsTouched = false;
    private boolean mIsLarge = false;
    private boolean mIsClicked = false;
    private boolean mIsClickedRegistered = false;
    private boolean mTouchStartedOnThis = false;
    TiledSprite tiledSprite;
    Sprite sprite;
    String _soundFileName;

    // ====================================================
    // ABSTRACT METHOD
    // ====================================================
    public abstract void onClick();

    // ====================================================
    // CONSTRUCTOR
    // ====================================================

    public GrowButton(final float pX, final float pY, float width, float height) {
        super(pX, pY, width, height);
    }

    public GrowButton(final float pX, final float pY, float width, float height,
                      final ITextureRegion pTextureRegion) {
        super(pX, pY, width, height);
        sprite = new Sprite(width / 2, height / 2, width, height, pTextureRegion,
                ResourceManager.Get().Vbo);

        this.attachChild(sprite);
    }

    public GrowButton(final float pX, final float pY,
                      final ITiledTextureRegion tiledTextureRegion) {
        super(pX, pY, tiledTextureRegion.getWidth(), tiledTextureRegion.getHeight());
        tiledSprite = new TiledSprite(tiledTextureRegion.getWidth() / 2, tiledTextureRegion.getHeight() / 2,
                tiledTextureRegion,
                ResourceManager.Get().Vbo);

        this.attachChild(tiledSprite);
    }

    public GrowButton(final float pX, final float pY, float width, float height,
                      final ITiledTextureRegion tiledTextureRegion) {
        super(pX, pY, width, height);
        tiledSprite = new TiledSprite(width / 2, height / 2, width, height, tiledTextureRegion,
                ResourceManager.Get().Vbo);

        this.attachChild(tiledSprite);
    }

    public GrowButton(final float pX, final float pY,
                      final ITextureRegion pTextureRegion) {
        this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion);
    }

    // ====================================================
    // METHODS
    // ====================================================

    public void SetButtonHeight(float height) {
        float width = height;
        if (sprite != null) {
            width = MathUtil.GetScaledWidth(sprite.getTextureRegion(), height);
            sprite.setWidth(width);
            sprite.setHeight(height);
            sprite.setX(width / 2);
            sprite.setY(height / 2);
        } else if (tiledSprite != null) {
            width = MathUtil.GetScaledWidth(tiledSprite.getTextureRegion(), height);
            tiledSprite.setWidth(width);
            tiledSprite.setHeight(height);
            tiledSprite.setX(width / 2);
            tiledSprite.setY(height / 2);
        }
        this.setWidth(width);
        this.setHeight(height);
    }

    public void setScales(final float pNormalScale, final float pGrownScale) {
        mNormalScale = pNormalScale;
        mGrownScale = pGrownScale;
        this.setScale(pNormalScale);
    }

    public void setSoundFileName(String soundFileName) {
        _soundFileName = soundFileName;
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if (!mIsLarge && mIsTouched && !mIsClickedRegistered) {
            mIsClickedRegistered = true;
            this.registerEntityModifier(new ScaleModifier(mGROW_DURATION_SECONDS, mNormalScale, mGrownScale) {
                @Override
                protected void onModifierFinished(final IEntity pItem) {
                    super.onModifierFinished(pItem);
                    mIsLarge = true;
                }
            });
        } else if (mIsLarge && !mIsTouched) {
            this.registerEntityModifier(new ScaleModifier(mGROW_DURATION_SECONDS, mGrownScale, mNormalScale) {
                @Override
                protected void onModifierFinished(final IEntity pItem) {
                    super.onModifierFinished(pItem);
                    mIsLarge = false;
                    if (mIsClicked) {
                        if (!isClickDisabled) {
                            isClickDisabled = true;
                            if (_soundFileName != null && !_soundFileName.isEmpty()) {
                                SFXManager.Get().Play(_soundFileName, 1);
                            }
                            onClick();
                            registerEntityModifier(new DelayModifier(0.5f) {
                                protected void onModifierFinished(IEntity pItem) {
                                    isClickDisabled = false;
                                }

                                ;
                            });
                        }
                        mIsClicked = false;
                        mIsClickedRegistered = false;
                    }
                }
            });
            mIsLarge = false;
        }
//        if (mIsEnabled) {
//            if (this.getAlpha() != mENABLED_ALPHA)
//                this.setAlpha(mENABLED_ALPHA);
//        } else {
//            if (this.getAlpha() != mDISABLED_ALPHA)
//                this.setAlpha(mDISABLED_ALPHA);
//        }
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (mIsClicked) {
            return true;
        }
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
            if (pTouchAreaLocalX > this.getWidth() || pTouchAreaLocalX < 0f
                    || pTouchAreaLocalY > this.getHeight()
                    || pTouchAreaLocalY < 0f) {
                mTouchStartedOnThis = false;
            } else {
                mTouchStartedOnThis = true;
            }
            if (mIsEnabled)
                mIsTouched = true;
        } else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
            if (pTouchAreaLocalX > this.getWidth() || pTouchAreaLocalX < 0f
                    || pTouchAreaLocalY > this.getHeight()
                    || pTouchAreaLocalY < 0f) {
                if (mIsTouched) {
                    mIsTouched = false;
                }
            } else {
                if (mTouchStartedOnThis && !mIsTouched) {
                    if (mIsEnabled) {
                        mIsTouched = true;
                    }
                }
            }
        } else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
            if (mIsTouched && mTouchStartedOnThis) {
                mIsTouched = false;
                mIsClicked = true;
                registerEntityModifier(new DelayModifier(0.5f) {
                    @Override
                    protected void onModifierFinished(IEntity pItem) {
                        super.onModifierFinished(pItem);
                        mIsClicked = false;
                    }
                });
                mTouchStartedOnThis = false;
            }
        }
        return true;
    }

    public void SetTileIndex(int index) {
        if (tiledSprite != null) {
            tiledSprite.setCurrentTileIndex(index);
        }
    }

    public void EnableInvertMode() {
        sprite.setBlendFunction(GLES20.GL_ONE_MINUS_DST_COLOR, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void setIsClickDisabled(boolean value) {
        isClickDisabled = value;
    }

    public void setFlippedHorizontal(boolean pFlippedHorizontal) {
        if (sprite != null) {
            sprite.setFlippedHorizontal(pFlippedHorizontal);
        }
    }

    public void SetTextureRegion(ITextureRegion textureRegion) {
        detachChild(sprite);
        Sprite spriteNew = new Sprite(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(),
                textureRegion,
                ResourceManager.Get().Vbo);
        spriteNew.setScale(sprite.getScaleX(), sprite.getScaleY());
        spriteNew.setFlipped(sprite.isFlippedHorizontal(), sprite.isFlippedVertical());
        spriteNew.setRotation(sprite.getRotation());
        attachChild(spriteNew);
    }

    @Override
    public void setAlpha(float pAlpha) {
        super.setAlpha(pAlpha);
        for (int childIndex = 0; childIndex < this.getChildCount(); childIndex++){
            this.getChildByIndex(childIndex).setAlpha(pAlpha);
        }
    }
}
