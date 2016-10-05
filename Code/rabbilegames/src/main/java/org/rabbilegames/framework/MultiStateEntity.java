package org.rabbilegames.framework;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;

public class MultiStateEntity extends Entity
{
	Entity[] stateEntities;

	public MultiStateEntity(int stateCount)
	{
		stateEntities = new Entity[stateCount];
		for (int stateIndex = 0; stateIndex < stateEntities.length; stateIndex++)
		{
			stateEntities[stateIndex] = new Entity();
			attachChild(stateEntities[stateIndex]);
		}
	}

	public void AttachToState(IEntity entity, int state)
	{
		stateEntities[state].attachChild(entity);
	}

	public void SetState(int state)
	{
		for (int stateIndex = 0; stateIndex < stateEntities.length; stateIndex++)
		{
			if (stateIndex == state)
			{
				stateEntities[stateIndex].setVisible(true);
			}
			else
			{
				stateEntities[stateIndex].setVisible(false);
			}
		}
	}
}
