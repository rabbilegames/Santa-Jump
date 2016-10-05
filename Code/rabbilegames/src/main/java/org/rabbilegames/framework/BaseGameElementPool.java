package org.rabbilegames.framework;

import android.util.Log;

import org.rabbilegames.Util.FrameworkHelper;
import org.rabbilegames.framework.BaseGameElement;
import org.rabbilegames.framework.BaseGameElementFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Pool to contain base game elements
// pass the BaseGameElementFactory upon creation, which will create base game elements based on ID
// obtainPoolItem and handleRecycleItem methods are used to get and put to the pool
public class BaseGameElementPool{
    private Map<String, List<BaseGameElement>> _objectPool;
    private BaseGameElementFactory _baseGameElementFactory;
    private boolean _loggingEnabled = false;

    private int _newObjectCreationCount = 0;
    private int _recycleCount = 0;
    private int _objectRequestCount = 0;

    public BaseGameElementPool(BaseGameElementFactory baseGameElementFactory){
        _objectPool = new HashMap<>();
        _baseGameElementFactory = baseGameElementFactory;
        if (FrameworkHelper.isDebugMode()){
            _loggingEnabled = true;
        }
    }

    public synchronized BaseGameElement obtainPoolItem(String baseGameElementID, final float pX, final float pY) {
        _objectRequestCount++;
        BaseGameElement gameElement = getItemFromPool(baseGameElementID);
        gameElement.setPosition(pX, pY);
        gameElement.getEntity().setVisible(true);
        gameElement.getEntity().setIgnoreUpdate(false);
        logStatus();
        return gameElement;
    }

    public void handleRecycleItem(BaseGameElement pItem) {
        _recycleCount++;
        putItemToPool(pItem);
        pItem.getEntity().setVisible(false);
        pItem.getEntity().setIgnoreUpdate(true);
        pItem.getEntity().clearEntityModifiers();
        pItem.getEntity().clearUpdateHandlers();
        logStatus();
    }

    private void logStatus() {
        if (_loggingEnabled){
            Log.d("BaseGameElementPool", String.format("Object requests: %d, Object creation: %d, Object recycled: %d", _objectRequestCount, _newObjectCreationCount, _recycleCount));
        }
    }

    public boolean isLoggingEnabled() {
        return _loggingEnabled;
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        _loggingEnabled = loggingEnabled;
    }

    private void putItemToPool(BaseGameElement pItem) {
        if (!_objectPool.containsKey(pItem.getBaseGameElementID())){
            List<BaseGameElement> itemList = new ArrayList<>();
            _objectPool.put(pItem.getBaseGameElementID(), itemList);
        }
        _objectPool.get(pItem.getBaseGameElementID()).add(pItem);
    }

    private BaseGameElement getItemFromPool(String baseGameElementID) {
        if (_objectPool.containsKey(baseGameElementID)) {
            List<BaseGameElement> itemList = _objectPool.get(baseGameElementID);
            if (itemList.size() > 0) {
                BaseGameElement item = itemList.get(0);
                itemList.remove(0);
                return item;
            }
        }
        BaseGameElement item = _baseGameElementFactory.create(baseGameElementID);
        _newObjectCreationCount++;
        return item;
    }
}
