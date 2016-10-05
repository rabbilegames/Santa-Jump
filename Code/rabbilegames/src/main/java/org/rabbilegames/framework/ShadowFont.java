package org.rabbilegames.framework;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;

public class ShadowFont extends Font
{
	private final Paint strokePaint;
	private final Paint gradientFillPaint;

	private final float strokeWidth;
	private final float shadowRadius;
	private final float shadowOffsetX;
	private final float shadowOffsetY;

	public ShadowFont(FontManager pFontManager, ITexture pTexture,
			Typeface typeface,
			float pSize)
	{
		super(pFontManager, pTexture, typeface, pSize, true, Color.BLACK);
		this.strokeWidth = pSize * 0.03f;
		this.shadowRadius = pSize * 0.06f;
		this.shadowOffsetX = pSize * 0.09f;
		this.shadowOffsetY = pSize * 0.07f;

		this.mPaint.setShadowLayer(shadowRadius, shadowOffsetX, shadowOffsetY,
				Color.argb(150, 0, 0, 0));

		this.strokePaint = CreatePaint(typeface, pSize);
		this.strokePaint.setStyle(Style.STROKE);
		this.strokePaint.setStrokeWidth(strokeWidth);
		this.strokePaint.setStrokeJoin(Join.ROUND);

		LinearGradient linearGradient = new LinearGradient(0, 0, 0, pSize,
				Color.rgb(220, 225, 228), Color.WHITE,
				TileMode.CLAMP);
		gradientFillPaint = CreatePaint(typeface, pSize);
		gradientFillPaint.setShader(linearGradient);
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
		this.strokePaint.getTextBounds(pCharacterAsString, 0, 1, this.mTextBounds);
		final int insetX = -(int) Math.ceil(this.strokeWidth * 0.5f);
		final int insetY = -(int) Math.ceil(this.strokeWidth * 0.5f);
		this.mTextBounds.inset(insetX, insetY);
		this.mTextBounds.right += (int) Math.ceil(shadowOffsetX + shadowRadius * 0.5f);
		this.mTextBounds.bottom += (int) Math.ceil(shadowOffsetY + shadowRadius * 0.5f);
	}

	@Override
	protected void drawLetter(final String pCharacterAsString, final float pLeft, final float pTop)
	{
		// Drawing shadow
		super.drawLetter(pCharacterAsString, pLeft, pTop);

		// Drawing gradient fill
		this.mCanvas.drawText(pCharacterAsString, pLeft + Font.LETTER_TEXTURE_PADDING, pTop
				+ Font.LETTER_TEXTURE_PADDING, this.gradientFillPaint);

		// Drawing double stroke
		this.strokePaint.setStrokeWidth(strokeWidth);
		this.strokePaint.setColor(Color.rgb(188, 190, 192));
		this.mCanvas.drawText(pCharacterAsString, pLeft + Font.LETTER_TEXTURE_PADDING, pTop
				+ Font.LETTER_TEXTURE_PADDING, this.strokePaint);
		this.strokePaint.setStrokeWidth(strokeWidth * 3 / 8);
		this.strokePaint.setColor(Color.rgb(65, 64, 66));
		this.mCanvas.drawText(pCharacterAsString, pLeft + Font.LETTER_TEXTURE_PADDING, pTop
				+ Font.LETTER_TEXTURE_PADDING, this.strokePaint);
	}
}
