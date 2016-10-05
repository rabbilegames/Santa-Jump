package org.rabbilegames.CommonUi;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;

import org.rabbilegames.ResourceManager;

public class GameStatusScreen extends Scene
{
	public GameStatusScreen()
	{
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);

		this.setBackgroundEnabled(false);

		final Rectangle fadableBGRect = new Rectangle(0f, 0f,
				ResourceManager.Get().camera.getWidth(),
				ResourceManager.Get().camera.getHeight(),
				ResourceManager.Get().Vbo);
		fadableBGRect.setColor(0f, 0f, 0f, 0.8f);
		fadableBGRect.setZIndex(-1000);
		this.attachChild(fadableBGRect);
	}

	public void OnBackKeyPressed()
	{
	}
}
