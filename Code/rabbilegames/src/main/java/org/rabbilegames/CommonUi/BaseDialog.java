package org.rabbilegames.CommonUi;


import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.rabbilegames.ResourceManager;


public class BaseDialog extends Scene
{
    public BaseDialog()
    {
        this.setTouchAreaBindingOnActionDownEnabled(true);
        this.setTouchAreaBindingOnActionMoveEnabled(true);

        this.setBackgroundEnabled(false);

        final Rectangle fadableBGRect = new Rectangle(0f, 0f, ResourceManager.Get().camera.getWidth(),
                ResourceManager.Get().camera.getHeight(), ResourceManager.Get().Vbo);
        fadableBGRect.setColor(0f, 0f, 0f, 0.8f);
        this.attachChild(fadableBGRect);
    }
}
