package org.rabbilegames.CommonUi;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import org.rabbilegames.ResourceManager;

public abstract class HighlightButton extends TiledSprite
{
	public boolean mIsEnabled = true;
	private boolean mIsTouched = false;
	private boolean mTouchStartedOnThis = false;
	private boolean clicked = false;

	public int UnPressedTileIndex = 0;
	public int PressedTileIndex = 1;
	public int DisabledTileIndex = 2;

	public abstract void OnPressed();

	public HighlightButton(final float pX, final float pY, final float width, final float height, final TiledTextureRegion pTextureRegion)
	{
		super(pX, pY, width, height, pTextureRegion, ResourceManager.Get().Vbo);
		this.setCurrentTileIndex(UnPressedTileIndex);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed)
	{
		super.onManagedUpdate(pSecondsElapsed);
		if (mIsTouched)
		{
			this.setCurrentTileIndex(PressedTileIndex);
		}
		else if (!mIsTouched)
		{
			this.setCurrentTileIndex(UnPressedTileIndex);
		}

		if (clicked)
		{
			OnPressed();
			this.clicked = false;
		}
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
	{
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
		{
			if (pTouchAreaLocalX > this.getWidth()
					|| pTouchAreaLocalX < 0f
					|| pTouchAreaLocalY > this.getHeight()
					|| pTouchAreaLocalY < 0f)
			{
				mTouchStartedOnThis = false;
			}
			else
			{
				mTouchStartedOnThis = true;
				if (!this.clicked)
				{
					this.clicked = true;
				}
			}
			if (mIsEnabled)
				mIsTouched = true;
		}
		else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE)
		{
			if (pTouchAreaLocalX > this.getWidth()
					|| pTouchAreaLocalX < 0f
					|| pTouchAreaLocalY > this.getHeight()
					|| pTouchAreaLocalY < 0f)
			{
				if (mIsTouched)
				{
					mIsTouched = false;
				}
			}
			else
			{
				if (mTouchStartedOnThis && !mIsTouched)
					if (mIsEnabled)
						mIsTouched = true;
			}
		}
		else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP
				&& mIsTouched && mTouchStartedOnThis)
		{
			mIsTouched = false;
			mTouchStartedOnThis = false;
		}
		return true;
	}
}
