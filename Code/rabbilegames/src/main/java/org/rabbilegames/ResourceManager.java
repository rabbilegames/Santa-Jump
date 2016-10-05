package org.rabbilegames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;


import com.badlogic.gdx.math.Vector2;

public class ResourceManager {
    public static final float DesignHeight = 1000;

    public boolean useHighQuality = true;

    public static boolean isUsingHighQualityGraphics() {
        return INSTANCE.useHighQuality;
    }
    public static final TextureOptions NormalTextureOption = TextureOptions.BILINEAR;
    public boolean AreResourceLoaded;
    // ==================== SPLASH RESOURCES ====================
    public ITextureRegion CompanyLogo;

    private static final ResourceManager INSTANCE = new ResourceManager();

    public Engine engine;
    public BaseGameActivity activity;
    public RabbileGameCamera camera;
    public VertexBufferObjectManager Vbo;
    List<ResourcesBase> _rabbileResourcesToLoad;

    public void setRabbileResources(List<ResourcesBase> rabbileResourcesToLoad){
        _rabbileResourcesToLoad = rabbileResourcesToLoad;
    }

    public void LoadResources() {
        for (ResourcesBase rabbileResource : _rabbileResourcesToLoad) {
            rabbileResource.Load();
        }
        AreResourceLoaded = true;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    public void loadSplashScreenResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.CompanyLogo = GetLimitableTR("CompanyLogo.png", NormalTextureOption);
    }

    public static void PrepareManager(Engine engine, RabbileGameCamera camera, VertexBufferObjectManager vbom) {
        Get().engine = engine;
        Get().camera = camera;
        Get().Vbo = vbom;
    }

    public static ResourceManager Get() {
        return INSTANCE;
    }

    public Vector2 GetCameraCenter() {
        return new Vector2(INSTANCE.camera.getWidth() / 2,
                INSTANCE.camera.getHeight() / 2);
    }

    public String GetTextFileContent(String path) {
        try {
            InputStream inputStream = activity.getAssets().open(path);
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream);
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            StringBuilder returnString = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                returnString.append(line);
                returnString.append("\r\n");
            }
            return returnString.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    // Gets the texture region and sets it to be double-sized if the quality of the graphics is set to Low
    public TextureRegion GetLimitableTR(String pTextureRegionPath, TextureOptions pTextureOptions) {
        final IBitmapTextureAtlasSource bitmapTextureAtlasSource = AssetBitmapTextureAtlasSource.create(activity
                        .getAssets(),
                BitmapTextureAtlasTextureRegionFactory.getAssetBasePath() + pTextureRegionPath);
        final BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),
                bitmapTextureAtlasSource.getTextureWidth(), bitmapTextureAtlasSource.getTextureHeight(),
                pTextureOptions);
        final TextureRegion textureRegion = new TextureRegion(bitmapTextureAtlas, 0, 0,
                bitmapTextureAtlasSource.getTextureWidth(), bitmapTextureAtlasSource.getTextureHeight(), false) {
            @Override
            public void updateUV() {
                if (!isUsingHighQualityGraphics()) {
                    this.mU = 0f;
                    this.mV = 0f;
                    this.mU2 = 1f;
                    this.mV2 = 1f;
                } else {
                    super.updateUV();
                }
            }
        };
        bitmapTextureAtlas.addTextureAtlasSource(bitmapTextureAtlasSource, 0, 0);
        bitmapTextureAtlas.load();
        if (!isUsingHighQualityGraphics())
            textureRegion.setTextureSize(textureRegion.getWidth() * 2f, textureRegion.getHeight() * 2f);
        return textureRegion;
    }

    @Deprecated
    public static String GetAppName() {
        return "RabbileGame";
    }
}
