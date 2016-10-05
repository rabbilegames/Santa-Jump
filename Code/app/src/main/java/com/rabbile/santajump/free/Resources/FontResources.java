package com.rabbile.santajump.free.Resources;

import android.graphics.Typeface;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;
import org.rabbilegames.ResourceManager;
import org.rabbilegames.ResourcesBase;

public class FontResources extends ResourcesBase {
    static FontResources instance;
    static String charactersInUse = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!$*:,.?";
    static String numbersInUse = "0123456789,. ";

    public Font CondensedFont;
    public Font MonospaceNumbersFont;
    public Font CourierNewRegularFont;

    public static FontResources Get() {
        if (instance == null) {
            instance = new FontResources();
        }
        return instance;
    }

    @Override
    protected void OnLoad() {
        CondensedFont = LoadFont("fonts/Condensed.ttf", charactersInUse.toCharArray());
        MonospaceNumbersFont = LoadFont("fonts/MonospaceNumbers.ttf", numbersInUse.toCharArray());
        CourierNewRegularFont = LoadFont("fonts/cour.ttf", charactersInUse.toCharArray());
    }

    @Override
    protected void OnUnload() {
        //Do something else useful
    }


    int GetSurfaceHeight() {
        return ResourceManager.Get().camera.getSurfaceHeight();
    }

    protected float GetFontHeight() {
        int surfaceHeight = GetSurfaceHeight();
        if (surfaceHeight > 820) {
            return 160;
        } else if (surfaceHeight > 340) {
            return 76;
        } else {
            return 50;
        }
    }

    protected int GetTextureWidth() {
        int surfaceHeight = GetSurfaceHeight();
        if (surfaceHeight > 820) {
            return 2048;
        } else if (surfaceHeight > 340) {
            return 1024;
        } else {
            return 512;
        }
    }

    protected int GetTextureHeight() {
        int surfaceHeight = GetSurfaceHeight();
        if (surfaceHeight > 820) {
            return 1024;
        } else if (surfaceHeight > 340) {
            return 512;
        } else {
            return 512;
        }
    }

    private Font LoadFont(String fontPath, char[] charactersToLoad) {
        BaseGameActivity activity = ResourceManager.Get().activity;
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(),
                GetTextureWidth(),
                GetTextureHeight(),
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), FontFactory.getAssetBasePath() + fontPath);
        Font font = new Font(activity.getFontManager(), mainFontTexture,
                typeface, GetFontHeight(), true, Color.WHITE);
        font.load();
        font.prepareLetters(charactersToLoad);
        return font;
    }
}
