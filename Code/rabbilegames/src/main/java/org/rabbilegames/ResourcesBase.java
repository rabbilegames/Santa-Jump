package org.rabbilegames;

import org.andengine.opengl.texture.region.ITextureRegion;


public abstract class ResourcesBase
{
	boolean isLoaded;

	protected ITextureRegion[][] GetTiledRegions(String[][] tiledRegionIds, TextureResourcePack textureResourcePack)
	{
		ITextureRegion[][] tiledTextureRegions = new ITextureRegion[tiledRegionIds.length][];
		for (int row = 0; row < tiledRegionIds.length; row++)
		{
			tiledTextureRegions[row] = new ITextureRegion[tiledRegionIds[row].length];
			for (int col = 0; col < tiledRegionIds[row].length; col++)
			{
				tiledTextureRegions[row][col] = textureResourcePack.GetTextureRegion(tiledRegionIds[row][col]);
			}
		}
		return tiledTextureRegions;
	}

	protected String GetBaseImagesFolder()
	{
		int surfaceHeight = ResourceManager.Get().camera.getSurfaceHeight();
		if (surfaceHeight > 820)
		{
			return "Images/R1080/";
		}
		else if (surfaceHeight > 340)
		{
			return "Images/R480/";
		}
		else
		{
			return "Images/R320/";
		}
	}

	protected abstract void OnLoad();

	protected abstract void OnUnload();

	public final void Load()
	{
		if (!isLoaded)
		{
			OnLoad();
		}
	}

	public final void Unload()
	{
		if (isLoaded)
		{
			OnUnload();
		}
	}

}
