package com.rabbile.santajump.free;

import org.andengine.util.Constants;
import org.rabbilegames.RabbileGameCamera;

public class SantaJumpCamera extends RabbileGameCamera {
    float _minY;
    float _maxX;

    public SantaJumpCamera(float pX, float pY, float pWidth, float pHeight) {
        super(pX, pY, pWidth, pHeight);
        _minY = pHeight / 2;
    }

    @Override
    public void updateChaseEntity() {
        if (this._chaseEntity != null) {
            final float[] centerCoordinates = this._chaseEntity.getSceneCenterCoordinates();

            float minX = this.getWidth() / 2 - SantaJumpConstants.HORIZONTAL_GAP_BETWEEN_STEPS;
            float maxX = this.getWidth() / 2 + SantaJumpConstants.HORIZONTAL_GAP_BETWEEN_STEPS;

            float modifiedCenterX = centerCoordinates[Constants.VERTEX_INDEX_X];
            if (modifiedCenterX < minX){
                modifiedCenterX = minX;
            } else if (modifiedCenterX > maxX){
                modifiedCenterX = maxX;
            }
            float modifiedCenterY = centerCoordinates[Constants.VERTEX_INDEX_Y] + this.getHeight() / 5;
            modifiedCenterY = _minY > modifiedCenterY ? _minY : modifiedCenterY;
            this.setCenter(modifiedCenterX, modifiedCenterY);
        }
    }
}
