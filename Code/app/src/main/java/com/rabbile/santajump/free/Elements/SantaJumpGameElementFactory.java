package com.rabbile.santajump.free.Elements;

import com.rabbile.santajump.free.IGameSceneServices;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.rabbilegames.framework.BaseGameElement;
import org.rabbilegames.framework.BaseGameElementFactory;


public class SantaJumpGameElementFactory extends BaseGameElementFactory {

    private IGameSceneServices _gameSceneServices;
    private PhysicsWorld _physicsWorld;

    public SantaJumpGameElementFactory(IGameSceneServices gameSceneServices, PhysicsWorld physicsWorld){

        _gameSceneServices = gameSceneServices;
        _physicsWorld = physicsWorld;
    }

    @Override
    public BaseGameElement create(String baseGameElementID) {
        switch(baseGameElementID){
            case WoodenStep.ID:
                return new WoodenStep(_physicsWorld, _gameSceneServices, 0, 0);
            case BrokenStep.ID:
                return new BrokenStep(_physicsWorld, _gameSceneServices);
            case Cloud.ID:
                return new Cloud(_physicsWorld, _gameSceneServices);
            case SideBorderLeft.ID:
                return new SideBorderLeft(_physicsWorld, _gameSceneServices);
            case SideBorderRight.ID:
                return new SideBorderRight(_physicsWorld, _gameSceneServices);
        }
        return null;
    }
}
