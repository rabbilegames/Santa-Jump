package org.rabbilegames.CommonUi;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.Scene;

import org.rabbilegames.ResourceManager;

public class Layer extends Scene
{
	Scene parentScene;

	public Layer(Scene parentScene)
	{
		super();
		this.parentScene = parentScene;

		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);

		this.setBackgroundEnabled(false);
	}

	public void Show()
	{
		this.setPosition(ResourceManager.Get().camera.getWidth() / 2,
				ResourceManager.Get().camera.getHeight() / 2);

		this.setScale(0.1f);
		this.parentScene.setChildScene(this, false, true, true);
		this.registerEntityModifier(new ScaleModifier(0.3f, 0.01f, 1));
	}

	public void Close()
	{
		Close(null);
	}

	public void Close(final Runnable runOnClose)
	{
		Layer.this.registerEntityModifier(new ScaleModifier(0.3f, 1, 0.01f)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				super.onModifierFinished(pItem);
				Layer.this.parentScene.clearChildScene();
				if (runOnClose != null)
				{
					runOnClose.run();
				}
			}
		});
	}

	public void OnBackKeyPressed()
	{
		Close();
	}
}
