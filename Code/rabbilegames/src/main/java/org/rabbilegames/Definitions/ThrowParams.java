package org.rabbilegames.Definitions;

public class ThrowParams
{
	private static ThrowParams instance;
	private int radius;
	public float X;
	public float Y;
	public float VelocityX;
	public float VelocityY;

	public int getRadius()
	{
		return radius;
	}

	public void setRadius(float pRadius)
	{
		radius = (int) (pRadius + 0.5f);
	}

	private ThrowParams()
	{
	}

	public static ThrowParams Instance()
	{
		if (instance == null)
		{
			instance = new ThrowParams();
		}
		return instance;
	}
}
