package org.rabbilegames.Definitions;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.adt.color.Color;

public class WorldPhysicsDefinition
{
	public static final float SideScrollVelocityPixels = 500;
	public static final float SantaJumpHeightPixels = 250;
	public static final float SantaJumpDistancePixels = 450;
	public static float SantaRunLeftMargin = 200;
	public static float PlatformHeight = 200;
	public static Color LightBlue = new Color(0f / 255f, 168f / 255f, 222f / 255f);
	public static Color DarkerAsh = new Color(15f / 255f, 19f / 255f, 16f / 255f);
	public static Color DarkAsh = new Color(30f / 255f, 38f / 255f, 32f / 255f);
	public static Color Maroon = new Color(239f / 255, 65f / 255, 54f / 255);

	private static float WorldGravity; // This gets recalculated in UpdateVariables()
	private static float SantaJumpVerticalVelocity; // This gets recalculated in UpdateVariables()
	private static float SideScrollVelocityReal; // This gets recalculated in UpdateVariables()

	static boolean variablesCalculated;

	//TODO: Constants
	public static final int SantaDesignHeight = 250;
	public static final int GrinchDesignHeight = 250;
	public static final int SantaStartX = 0;
	public static final int SantaStartY = 250;
	public static final int GrinchStartX = 1000;
	public static final int GrinchStartY = 250;
	public static final int BombDesignHeight = 50;

	static void UpdateVariables()
	{
		if (!variablesCalculated)
		{
			SideScrollVelocityReal = SideScrollVelocityPixels / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

			float jdReal = SantaJumpDistancePixels / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
			float jHReal = SantaJumpHeightPixels / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
			float a = jdReal * jdReal / (16 * jHReal * SideScrollVelocityReal * SideScrollVelocityReal);
			float b = -jdReal / SideScrollVelocityReal / 2;

			// If it needs to check the validity of parameters following can be enabled
			//	float c = jHReal;			
			//	float det = b * b - 4 * a * c;
			//	if (det < 0)
			//	{
			//		throw new InvalidParameterException();
			//	}
			//	SantaJumpVerticalVelocity = (-b + (float) Math.sqrt(det)) / 2 / a;
			//  float u2 = (-b - (float) Math.sqrt(det)) / 2 / a;

			SantaJumpVerticalVelocity = (-b) / 2 / a;
			WorldGravity = -SantaJumpVerticalVelocity * SantaJumpVerticalVelocity / 2 / jHReal;

			variablesCalculated = true;
		}
	}

	public static float GetSideScrollVelocityReal()
	{
		UpdateVariables();
		return SideScrollVelocityReal;
	}

	public static float GetGravity()
	{
		UpdateVariables();
		return WorldGravity;
	}

	public static float GetJumpVerticalVelocity()
	{
		UpdateVariables();
		return SantaJumpVerticalVelocity;
	}
}
