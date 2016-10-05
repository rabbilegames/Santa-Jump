package org.rabbilegames.framework;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;

import org.rabbilegames.ResourceManager;

public class StitchedEntity extends Entity
{
	protected Sprite[][] sprites;
	protected float textureWidthTotal;
	protected float textureHeightTotal;

	public StitchedEntity(ITextureRegion[][] textureGrid)
	{
		sprites = new Sprite[textureGrid.length][];
		for (int row = 0; row < textureGrid.length; row++)
		{
			sprites[row] = new Sprite[textureGrid[row].length];
			for (int col = 0; col < textureGrid[row].length; col++)
			{
				sprites[row][col] = CreateSprite(textureGrid[row][col]);
				if (row == 0)
				{
					textureWidthTotal += textureGrid[row][col].getWidth();
				}
				if (col == 0)
				{
					textureHeightTotal += textureGrid[row][col].getHeight();
				}
				this.attachChild(sprites[row][col]);
			}
		}
		super.setWidth(textureWidthTotal);
		super.setHeight(textureHeightTotal);
		UpdateSprites();
	}

	protected Sprite CreateSprite(ITextureRegion textureRegion)
	{
		return new Sprite(0, 0, textureRegion,
				ResourceManager.Get().Vbo);
	}

	@Override
	public void setScale(float pScale)
	{
		super.setScale(pScale);
		UpdateSprites();
	}

	@Override
	public void setWidth(float pWidth)
	{
		super.setWidth(pWidth);
		UpdateSprites();
	}

	@Override
	public void setHeight(float pHeight)
	{
		super.setHeight(pHeight);
		UpdateSprites();
	}

	@Override
	public void setSize(float pWidth, float pHeight)
	{
		super.setSize(pWidth, pHeight);
		UpdateSprites();
	}

	private void UpdateSprites()
	{
		float y = 0;

		for (int row = sprites.length - 1; row >= 0; row--)
		{
			float x = 0;
			float rowHeight = 0;

			for (int col = 0; col < sprites[row].length; col++)
			{
				Sprite sprite = sprites[row][col];
				sprite.setSize(mWidth / textureWidthTotal * sprite.getTextureRegion().getWidth(),
						mHeight / textureHeightTotal * sprite.getTextureRegion().getHeight());
				sprite.setX(x + sprite.getWidth() / 2);
				sprite.setY(y + sprite.getHeight() / 2);
				sprite.setScale(mScaleX, mScaleY);
				x += sprite.getWidth();
				rowHeight = sprite.getHeight();
			}
			y += rowHeight;
		}
	}

	public void SetShaderProgram(ShaderProgram shaderProgram)
	{
		for (Sprite[] spriteRows : sprites)
		{
			for (Sprite sprite : spriteRows)
			{
				sprite.setShaderProgram(shaderProgram);
			}
		}
	}
}
