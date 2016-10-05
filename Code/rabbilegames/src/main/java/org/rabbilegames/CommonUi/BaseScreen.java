package org.rabbilegames.CommonUi;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseBackInOut;
import org.andengine.util.modifier.ease.IEaseFunction;

import org.rabbilegames.ResourceManager;

public class BaseScreen extends Entity implements IEntityModifierListener
{
	final float RollingSpeedMagnitude = 2500f;
	public int modifiersRunning;

	public void RegisterSlideLeftModifiers(Entity entity, boolean roll, boolean immediate)
	{
		float rollingSpeed = RollingSpeedMagnitude;
		float rollingDistance = ResourceManager.Get().camera.getWidth();
		float radius = entity.getWidth() / 2;
		float angularVelocity = (float) (rollingSpeed / radius * 180 / Math.PI);
		float duration = rollingDistance / rollingSpeed;

		IEaseFunction easeFunction = EaseBackInOut.getInstance();
		if (immediate)
		{
			entity.setX(entity.getX() - rollingDistance);
			if (roll)
			{
				entity.setRotation(entity.getRotation() - duration * angularVelocity);
			}
		}
		else
		{
			if (modifiersRunning == 0)
			{
				onRollBegin();
			}
			modifiersRunning++;
			entity.registerEntityModifier(new MoveXModifier(duration, entity.getX(), entity.getX() - rollingDistance,
					this, easeFunction));

			if (roll)
			{
				entity.registerEntityModifier(
						new RotationModifier(duration, entity.getRotation(),
								entity.getRotation() - duration * angularVelocity,
								this, easeFunction));
				modifiersRunning++;
			}
		}
	}

	public void RegisterSlideRightModifiers(Entity entity, boolean roll, boolean immediate)
	{
		float rollingSpeed = -RollingSpeedMagnitude;
		float rollingDistance = -ResourceManager.Get().camera.getWidth();
		float radius = entity.getWidth() / 2;
		float angularVelocity = (float) (rollingSpeed / radius * 180 / Math.PI);
		float duration = rollingDistance / rollingSpeed;
		IEaseFunction easeFunction = EaseBackInOut.getInstance();
		if (immediate)
		{
			entity.setX(entity.getX() - rollingDistance);
			if (roll)
			{
				entity.setRotation(entity.getRotation() - duration * angularVelocity);
			}
		}
		else
		{
			if (modifiersRunning == 0)
			{
				onRollBegin();
			}
			modifiersRunning++;
			entity.registerEntityModifier(new MoveXModifier(duration, entity.getX(), entity.getX() - rollingDistance,
					this, easeFunction));

			if (roll)
			{
				entity.registerEntityModifier(
						new RotationModifier(duration, entity.getRotation(),
								entity.getRotation() - duration * angularVelocity,
								this, easeFunction));
				modifiersRunning++;
			}
		}
	}

	protected void onRollBegin()
	{
	}

	protected void onRollEnd()
	{
	}

	@Override
	public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem)
	{
	}

	@Override
	public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem)
	{
		modifiersRunning--;
		if (modifiersRunning == 0)
		{
			onRollEnd();
		}
	}
}
