package org.rabbilegames.framework;

import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.util.adt.pool.GenericPool;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class ReusablePhysicsConnector extends PhysicsConnector
{
	IEntity entity;
	Body body;
	static GenericPool<ReusablePhysicsConnector> reusablePhysicsConnectors = new GenericPool<ReusablePhysicsConnector>()
	{
		@Override
		protected ReusablePhysicsConnector onAllocatePoolItem()
		{
			return new ReusablePhysicsConnector();
		}
	};

	public static ReusablePhysicsConnector Get(IEntity pEntity, Body pBody)
	{
		ReusablePhysicsConnector reusablePhysicsConnector = reusablePhysicsConnectors.obtainPoolItem();
		reusablePhysicsConnector.Initialize(pEntity, pBody);
		return reusablePhysicsConnector;
	}

	public static void Recycle(ReusablePhysicsConnector reusablePhysicsConnector)
	{
		reusablePhysicsConnectors.recyclePoolItem(reusablePhysicsConnector);
	}

	public ReusablePhysicsConnector()
	{
		super(null, null);
	}

	void Initialize(IEntity pEntity, Body pBody)
	{
		this.entity = pEntity;
		this.body = pBody;
		mUpdatePosition = true;
		mUpdateRotation = true;
	}

	@Override
	public IEntity getEntity()
	{
		return this.entity;
	}

	@Override
	public Body getBody()
	{
		return this.body;
	}

	public void UpdateToBody()
	{
		body.setTransform(entity.getX() / mPixelToMeterRatio, entity.getY() / mPixelToMeterRatio,
				-MathUtils.degToRad(entity.getRotation()));
	}

	@Override
	public void onUpdate(final float pSecondsElapsed)
	{
		if (this.mUpdatePosition)
		{
			final Vector2 position = body.getPosition();
			entity.setPosition(position.x * mPixelToMeterRatio, position.y * mPixelToMeterRatio);
		}

		if (this.mUpdateRotation)
		{
			final float angle = body.getAngle();
			entity.setRotation(-MathUtils.radToDeg(angle));
		}
	}
}
