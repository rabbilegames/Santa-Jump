package org.rabbilegames.Definitions;

public class PhysicsProperties
{
	public static PhysicsProperties instance;

	public float Density;
	public float Friction;
	public float Elasticity;
	public float LinearDamping;
	public float AngularDamping;

	private PhysicsProperties()
	{
	}

	public static PhysicsProperties Instance()
	{
		if (instance == null)
		{
			instance = new PhysicsProperties();
		}
		return instance;
	}
}
