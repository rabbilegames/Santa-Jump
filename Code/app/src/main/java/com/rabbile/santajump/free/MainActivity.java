package com.rabbile.santajump.free;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.rabbile.santajump.free.Resources.FontResources;
import com.rabbile.santajump.free.Resources.GameResources;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.rabbilegames.RabbileGameCamera;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.ResourcesBase;
import org.rabbilegames.SFXManager;
import org.rabbilegames.SceneManager;
import org.rabbilegames.SwitchableFixedStepEngine;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;



public class MainActivity extends BaseGameActivity {
    public enum DeviceResolution {
        Hdpi,
        Mdpi,
        Ldpi,
    }

    public static int DeviceResolutionWidth = 800;
    public static int DeviceResolutionHeight = 480;

    float _cameraWidth;
    float _cameraHeight;
    SantaJumpCamera _camera;

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        return new SwitchableFixedStepEngine(pEngineOptions, 100, false);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DeviceResolutionHeight = metrics.heightPixels;
        DeviceResolutionWidth = metrics.widthPixels;

        _cameraWidth = DeviceResolutionWidth * ResourceManager.DesignHeight / DeviceResolutionHeight;
        _cameraHeight = ResourceManager.DesignHeight;

        //Make the _camera to be of design height device width
        _camera = new SantaJumpCamera(0, 0, _cameraWidth, _cameraHeight);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
                new RatioResolutionPolicy(_cameraWidth, _cameraHeight), _camera);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);

        // Default is 5, better to reduce this
        engineOptions.getAudioOptions().getSoundOptions().setMaxSimultaneousStreams(5);

        engineOptions.getRenderOptions().setDithering(true);
        //engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback oncreateResourcesCallBack) throws IOException {
        ResourceManager.PrepareManager(mEngine, _camera, this.mEngine.getVertexBufferObjectManager());
        SceneManager.get().prepare(new SplashScene());
        List<ResourcesBase> gameResources = Arrays.asList(FontResources.Get(), GameResources.Get());
        ResourceManager.Get().setRabbileResources(gameResources);
        SFXManager.Get().Load("audio/", "Click.ogg");
        oncreateResourcesCallBack.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback createSceneCallback) throws IOException {
        UserDataManager.IncrementAppStartedCount();
        SceneManager.get().CreateAndLoadSplashScene(createSceneCallback);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback onPopulateSceneCallback) throws IOException {
        this.mEngine.registerUpdateHandler(new IUpdateHandler()
        {
            float elapsedTime;

            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                elapsedTime += pSecondsElapsed;
                if (ResourceManager.Get().AreResourceLoaded && elapsedTime > 3.5f)
                {
                    GameScene mainMenuScene = new GameScene(true);
                    SceneManager.get().setScene(mainMenuScene, true);
                    mEngine.unregisterUpdateHandler(this);
                }
            }

            @Override
            public void reset()
            {
            }
        });
        onPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (SceneManager.get().getCurrentScene() != null)
            {
                SceneManager.get().getCurrentScene().onBackKeyPressed();
            }
        }
        return false;
    }

    public static DeviceResolution GetDeviceResolution()
    {
        if (DeviceResolutionHeight >= 1080)
        {
            return DeviceResolution.Hdpi;
        }
        else if (DeviceResolutionHeight >= 480)
        {
            return DeviceResolution.Mdpi;
        }
        else
        {
            return DeviceResolution.Ldpi;
        }
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ResourceManager.Get().activity = this;
        super.onCreate(pSavedInstanceState);

        View decorView = getWindow().getDecorView();

        int sdkInt = android.os.Build.VERSION.SDK_INT;
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        @SuppressWarnings("deprecation")
        int uiOptions = View.STATUS_BAR_HIDDEN;
        if (sdkInt >= 14)
        {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;// hide nav bar
        }

        //TODO: Setup remineder service
        //TODO: Setup analytics
        //TODO: Setup ads
        //TODO: Setup webconfig manager
    }
}
