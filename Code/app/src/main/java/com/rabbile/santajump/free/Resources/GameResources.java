package com.rabbile.santajump.free.Resources;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.rabbilegames.ResourcesBase;
import org.rabbilegames.TextureResourcePack;


public class GameResources extends ResourcesBase {
    static GameResources _instance;
    TextureResourcePack _textureResourcePack;


    public ITextureRegion MainMenuFloorTR;
    public ITextureRegion MainMenuMountainTR;
    public ITextureRegion MainMenuScoreButtonTR;
    public ITextureRegion MainMenuStartButtonTR;
    public ITextureRegion MainMenuTipsLeftTR;
    public ITextureRegion MainMenuTipsRightTR;
    public ITextureRegion MainMenuTreeTR;
    public ITiledTextureRegion SantaTTR;
    public ITextureRegion MainMenuTitleTR;
    public ITextureRegion SideBarLeftTR;
    public ITextureRegion SideBarRightTR;
    public ITextureRegion CloudTR;
    public ITextureRegion WoodenStepTR;
    public ITextureRegion BrokenStepTR;
    public ITextureRegion IornStepTR;
    public ITextureRegion FairyTTR;
    public ITextureRegion ClockTR;
    public ITextureRegion GiftTR;
    public ITextureRegion MainMenuRateButtonTR;

    @Override
    protected void OnLoad() {
        _textureResourcePack = new TextureResourcePack(GetBaseImagesFolder() + "Game");

        MainMenuFloorTR = _textureResourcePack.GetTextureRegion(GameIds.Game_MainMenuFloor);
        MainMenuMountainTR = _textureResourcePack.GetTextureRegion(GameIds.Game_MainMenuMountain);
        MainMenuScoreButtonTR = _textureResourcePack.GetTextureRegion(GameIds.Game_MainMenuScoreButton);
        MainMenuStartButtonTR = _textureResourcePack.GetTextureRegion(GameIds.Game_MainMenuStartButton);
        MainMenuTipsLeftTR = _textureResourcePack.GetTextureRegion(GameIds.Game_MainMenuTipsLeft);
        MainMenuTipsRightTR = _textureResourcePack.GetTextureRegion(GameIds.Game_MainMenuTipsRight);
        MainMenuTitleTR = _textureResourcePack.GetTextureRegion(GameIds.Game_MainMenuTitle);
        MainMenuTreeTR = _textureResourcePack.GetTextureRegion(GameIds.Game_MainMenuTree);
        MainMenuRateButtonTR = _textureResourcePack.GetTextureRegion(GameIds.Game_MainMenuRateButton);

        SantaTTR = _textureResourcePack.GetTiledTextureRegion(GameIds.Game_SantaSpriteSheet, 2, 2);
        SideBarLeftTR = _textureResourcePack.GetTextureRegion(GameIds.Game_SideBorderLeft);
        SideBarRightTR = _textureResourcePack.GetTextureRegion(GameIds.Game_SideBorderRight);
        CloudTR = _textureResourcePack.GetTextureRegion(GameIds.Game_Cloud);
        WoodenStepTR = _textureResourcePack.GetTextureRegion(GameIds.Game_WoodenStep);
        BrokenStepTR= _textureResourcePack.GetTextureRegion(GameIds.Game_BrokenStep);
        IornStepTR= _textureResourcePack.GetTextureRegion(GameIds.Game_IornStep);
        FairyTTR= _textureResourcePack.GetTextureRegion(GameIds.Game_FairySpriteSheet);
        ClockTR= _textureResourcePack.GetTextureRegion(GameIds.Game_Clock);
        GiftTR= _textureResourcePack.GetTextureRegion(GameIds.Game_Gift);
    }

    @Override
    protected void OnUnload() {
        if (_textureResourcePack != null) {
            _textureResourcePack.Unload();
            _textureResourcePack = null;
        }
    }

    public static GameResources Get() {
        if (_instance == null){
            _instance = new GameResources();
        }
        return _instance;
    }
}
