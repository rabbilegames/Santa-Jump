package org.rabbilegames.CommonUi;

import org.andengine.entity.Entity;

/**
 * Created by asanka.samarawickram on 6/20/2016.
 */
public class BaseLayer extends Entity{

    @Override
    public void setAlpha(float pAlpha) {
        super.setAlpha(pAlpha);
        for (int childIndex = 0; childIndex < this.getChildCount(); childIndex++){
            this.getChildByIndex(childIndex).setAlpha(pAlpha);
        }
    }
}
