package org.rabbilegames;

import android.content.res.AssetManager;

import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TextureResourcePack
{
	ArrayList<TexturePackTextureRegionLibrary> texturePackTextureRegionLibraries;
	ArrayList<TexturePack> texturePacks;

	public TextureResourcePack(String texturePath)
	{
		AssetManager assetMgr = ResourceManager.Get().activity.getAssets();
		texturePackTextureRegionLibraries = new ArrayList<TexturePackTextureRegionLibrary>();
		texturePacks = new ArrayList<TexturePack>();

		try
		{
			String[] files = assetMgr.list(texturePath);

			for (String file : files)
			{
				if (".xml".compareTo(GetExtension(file)) == 0)
				{
					AddTexturePack(texturePath, file);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public ITextureRegion GetTextureRegion(String textureId)
	{
		for (TexturePackTextureRegionLibrary texturePackTextureRegionLibrary : texturePackTextureRegionLibraries)
		{
			if (texturePackTextureRegionLibrary.getSourceMapping().containsKey(textureId))
			{
				return texturePackTextureRegionLibrary.get(textureId);
			}
		}
		return null;
	}

	public ITiledTextureRegion GetTiledTextureRegion(String textureId, int pColumns, int pRows)
	{
		for (TexturePackTextureRegionLibrary texturePackTextureRegionLibrary : texturePackTextureRegionLibraries)
		{
			if (texturePackTextureRegionLibrary.getSourceMapping().containsKey(textureId))
			{
				return texturePackTextureRegionLibrary.get(textureId, pColumns, pRows);
			}
		}
		return null;
	}

	private void AddTexturePack(String texturePath, String file)
	{
		try
		{
			BaseGameActivity activity = ResourceManager.Get().activity;
			TexturePack texturePack = new TexturePackLoader(activity.getTextureManager(), texturePath + "/")
					.loadFromAsset(activity.getAssets(), file);
			texturePack.loadTexture();
			texturePacks.add(texturePack);
			texturePackTextureRegionLibraries.add(texturePack.getTexturePackTextureRegionLibrary());
		}
		catch (final TexturePackParseException e)
		{
			Debug.e(e);
		}
	}

	String GetExtension(String path)
	{
		String ext = "";
		int dotIndex = path.lastIndexOf('.');

		if (dotIndex > 0 && dotIndex < path.length() - 1)
		{
			ext = path.substring(dotIndex).toLowerCase();
		}
		return ext;
	}

	public static String PathCombine(String... paths)
	{
		File file = new File(paths[0]);

		for (int i = 1; i < paths.length; i++)
		{
			file = new File(file, paths[i]);
		}

		return file.getPath();
	}

	public void Unload()
	{
		for (TexturePack texturePack : texturePacks)
		{
			texturePack.unloadTexture();
		}
	}
}
