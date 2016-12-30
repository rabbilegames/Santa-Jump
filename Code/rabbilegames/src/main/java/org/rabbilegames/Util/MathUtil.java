package org.rabbilegames.Util;

import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;

import org.rabbilegames.CommonUi.GrowButton;

import com.badlogic.gdx.math.Vector2;

public class MathUtil
{
	static Random random = new Random();

	public static int NextRandInt(int max)
	{
		return random.nextInt(max);
	}

	public static int NextRandInt(int min, int max)
	{
		return min + random.nextInt(max - min);
	}

	public static boolean NextRandBool()
	{
		return random.nextFloat() < 0.5f;
	}

	public static float NextRand()
	{
		return random.nextFloat();
	}

	public static float NextRand(float max)
	{
		return max * random.nextFloat();
	}

	public static float NextRand(float min, float max)
	{
		return min + (max - min) * random.nextFloat();
	}

	public static float ToRadian(float angleInDegrees)
	{
		return (float) (angleInDegrees * Math.PI / 180);
	}

	public static float cosDeg(float angle)
	{
		return (float) Math.cos(ToRadian(angle));
	}

	public static float sinDeg(float angle)
	{
		return (float) Math.sin(ToRadian(angle));
	}

	public static float DirectionAngleTo(float referencePointX, float referencePointY, Vector2 directionPoint)
	{
		return (float) Math.atan2(directionPoint.y - referencePointY, directionPoint.x - referencePointX);
	}

	public static float ThreePointAngle(Vector2 referencePoint, Vector2 directionPoint, Vector2 endPoint)
	{
		return ThreePointAngle(referencePoint, directionPoint, endPoint.x, endPoint.y);
	}

	public static float ThreePointAngle(Vector2 referencePoint, Vector2 directionPoint, float endPointX, float endPointY)
	{
		double angle1 = Math.atan2(endPointY - referencePoint.y, endPointX - referencePoint.x);
		double angle2 = Math.atan2(directionPoint.y - referencePoint.y, directionPoint.x - referencePoint.x);
		while (angle1 < 0)
		{
			angle1 += Math.PI * 2.0;
		}
		while (angle2 < angle1)
		{
			angle2 += Math.PI * 2.0;
		}
		return (float) (angle1 - angle2);
	}

	public static float ThreePointAngle(float referencePointX, float referencePointY, Vector2 directionPoint,
			float endPointX, float endPointY)
	{
		double angle1 = Math.atan2(endPointY - referencePointY, endPointX - referencePointX);
		double angle2 = Math.atan2(directionPoint.y - referencePointY, directionPoint.x - referencePointX);
		while (angle1 < 0)
		{
			angle1 += Math.PI * 2.0;
		}
		while (angle2 < angle1)
		{
			angle2 += Math.PI * 2.0;
		}
		return (float) (angle1 - angle2);
	}

	public static float DistanceTo(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	public static boolean Between(float value, float bound1, float bound2)
	{
		return (value - bound1) * (value - bound2) < 0;
	}

	public static boolean OnOrBetween(float value, float bound1, float bound2)
	{
		return (value - bound1) * (value - bound2) <= 0;
	}

	public static float GetScaledWidth(final float realWidth, final float realHeight, final float designHeight)
	{
		return (realWidth * designHeight / realHeight);
	}

	public static float GetScaledWidth(ITextureRegion texture, final float designHeight)
	{
		return (texture.getWidth() * designHeight / texture.getHeight());
	}

	public static void SetScaledSize(Entity entity, ITextureRegion texture, final float designHeight)
	{
		entity.setHeight(designHeight);
		entity.setWidth(GetScaledWidth(texture, designHeight));
	}

	public static void SetScaledSizeFromWidth(Sprite sprite, final float designWidth)
	{
		SetScaledSizeFromWidth(sprite, sprite.getTextureRegion(), designWidth);
	}

	public static void SetScaledSizeFromWidth(Entity entity, ITextureRegion texture, final float designWidth)
	{
		entity.setWidth(designWidth);
		entity.setHeight(designWidth / GetScaledWidth(texture, 1));
	}

	public static void SetScaledSize(Sprite sprite, final float designHeight)
	{
		SetScaledSize(sprite, sprite.getTextureRegion(), designHeight);
	}

	public static void SetScaledSize(IEntity entity, ITextureRegion[][] textureGrid, final float designHeight)
	{
		float textureWidthTotal = 0;
		float textureHeightTotal = 0;
		for (int row = 0; row < textureGrid.length; row++)
		{
			for (int col = 0; col < textureGrid[row].length; col++)
			{
				if (row == 0)
				{
					textureWidthTotal += textureGrid[row][col].getWidth();
				}
				if (col == 0)
				{
					textureHeightTotal += textureGrid[row][col].getHeight();
				}
			}
		}
		float width = GetScaledWidth(textureWidthTotal, textureHeightTotal, designHeight);
		entity.setSize(width, designHeight);
	}

	public static void SetScaledSize(Text text, float designHeight)
	{
		text.setScale(designHeight / text.getHeight());
	}

	public static void FitToBox(Text text, float width, float height)
	{
		if (text.getHeight() / height > text.getWidth() / width)
		{
			text.setScale(height / text.getHeight());
		}
		else
		{
			text.setScale(width / text.getWidth());
		}
	}

	public static void ArrangeHorizontally(float startX, float endX, GrowButton... buttons)
	{
		float buttonWidthTotal = 0;
		for (GrowButton button : buttons)
		{
			buttonWidthTotal += button.getWidth();
		}
		float itemSpacing = (endX - startX - buttonWidthTotal) / buttons.length;

		float pos = startX + itemSpacing / 2;
		for (GrowButton button : buttons)
		{
			button.setX(pos + button.getWidth() / 2);
			pos += button.getWidth() + itemSpacing;
		}
	}

	public static void ArrangeHorizontallyCenter(float centerX, float centerY, float itemSpacing, IEntity... entities)
	{
		float entityWidthTotal = 0;
		int entityCount = 0;
		for (IEntity entity : entities)
		{
			entityWidthTotal += entity.getWidth();
			entityCount++;
		}

		float pos = centerX - (entityWidthTotal + itemSpacing * (entityCount - 1)) / 2;
		for (IEntity entity : entities)
		{
			entity.setX(pos + entity.getWidth() / 2);
			entity.setY(centerY);
			pos += entity.getWidth() + itemSpacing;
		}
	}

	static float[] degree2EquationSolution = new float[2];

	public static float[] SolveDeg2Equation(float a, float b, float c)
	{
		float deno = b * b - 4 * a * c;
		if (deno < 0)
		{
			return null;
		}
		else
		{
			float denoSqrt = (float) Math.sqrt(deno);
			degree2EquationSolution[0] = (-b + denoSqrt) / (2 * a);
			degree2EquationSolution[1] = (-b - denoSqrt) / (2 * a);
			return degree2EquationSolution;
		}
	}

	public static float Interpolate(float x1, float x2, float y1, float y2, float x)
	{
		return y1 + (y2 - y1) / (x2 - x1) * (x - x1);
	}

	public static float InterpolateAndLimit(float x1, float x2, float y1, float y2, float x)
	{
		float y = Interpolate(x1, x2, y1, y2, x);
		if (y1 < y2)
		{
			return Math.min(Math.max(y1, y), y2);
		}
		else
		{
			return Math.min(Math.max(y2, y), y1);
		}
	}

	public static boolean nextBoolean(double probability) {
		return NextRand(0, 1) > (1 - probability);
	}
}
