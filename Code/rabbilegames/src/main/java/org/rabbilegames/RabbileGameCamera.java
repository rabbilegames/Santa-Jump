package org.rabbilegames;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.IEntity;
import org.andengine.util.Constants;

/**
 * Created by asanka.samarawickram on 5/23/2016.
 */
public class RabbileGameCamera extends BoundCamera {
    boolean _isEntityChaseEnabed = false;
    protected IEntity _chaseEntity;

    public RabbileGameCamera(float pX, float pY, float pWidth, float pHeight)
    {
        super(pX, pY, pWidth, pHeight);
        setBoundsEnabled(false);
    }

    public void set_chaseEntity(IEntity pChaseEntity)
    {
        _chaseEntity = pChaseEntity;
        if (pChaseEntity == null)
        {
            _isEntityChaseEnabed = false;
        }
        else
        {
            _isEntityChaseEnabed = true;
        }
        super.set_chaseEntity(pChaseEntity);
    }

    public void SetZoomFactor(float value)
    {
        //this.setZoomFactor(value);
    }
}
