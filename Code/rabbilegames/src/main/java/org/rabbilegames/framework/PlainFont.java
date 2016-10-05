package org.rabbilegames.framework;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;

public class PlainFont extends Font
{
	private final Paint fillPaint;

	public PlainFont(FontManager pFontManager, ITexture pTexture,
			Typeface typeface,
			float pSize)
	{
		super(pFontManager, pTexture, typeface, pSize, true, Color.BLACK);

		this.fillPaint = CreatePaint(typeface, pSize);

		LinearGradient linearGradient = new LinearGradient(0, 0, 0, pSize,
				Color.WHITE, Color.WHITE,
				TileMode.CLAMP);
		fillPaint.setShader(linearGradient);
	}

	Paint CreatePaint(Typeface typeface, float pSize)
	{
		Paint paint = new Paint();
		paint.setTypeface(typeface);
		paint.setTextSize(pSize);
		paint.setAntiAlias(true);
		return paint;
	}

	@Override
	protected void updateTextBounds(final String pCharacterAsString)
	{
		this.fillPaint.getTextBounds(pCharacterAsString, 0, 1, this.mTextBounds);
	}

	@Override
	protected void drawLetter(final String pCharacterAsString, final float pLeft, final float pTop)
	{
		this.mCanvas.drawText(pCharacterAsString, pLeft + Font.LETTER_TEXTURE_PADDING, pTop
				+ Font.LETTER_TEXTURE_PADDING, this.fillPaint);
	}
}
