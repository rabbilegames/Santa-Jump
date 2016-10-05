package org.rabbilegames.Util;

import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import org.rabbilegames.framework.Polygon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class PhysicsUtil
{
	public static Body createRightEdgeChamferedBody(final PhysicsWorld pPhysicsWorld, final IEntity entity,
			final BodyType pBodyType, final FixtureDef pFixtureDef)
	{
		/* Remember that the vertices are relative to the center-coordinates of the Shape. */
		final float halfWidth = entity.getWidthScaled() * 0.5f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		final float halfHeight = entity.getHeightScaled() * 0.5f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

		final float top = halfHeight;
		final float bottom = -halfHeight;

		final float left = -halfWidth;
		final float right = halfWidth;

		final Vector2[] vertices = {
				new Vector2(left, bottom),
				new Vector2(right - 5f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, bottom),
				new Vector2(right, bottom + 5f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),
				new Vector2(right, top),
				new Vector2(left, top)
		};

		return PhysicsFactory.createPolygonBody(pPhysicsWorld, entity, vertices, pBodyType, pFixtureDef);
	}

	public static ArrayList<Body> CreateBodies(Entity entity, PhysicsWorld physicsWorld,
			FixtureDef fixtureDef, BodyType bodyType, ArrayList<Polygon> polygons)
	{
		ArrayList<Body> bodies = new ArrayList<Body>();
		final float halfWidth = entity.getWidthScaled() * 0.5f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		final float halfHeight = entity.getHeightScaled() * 0.5f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

		float maxLength = Math.max(entity.getWidthScaled(), entity.getHeightScaled());
		for (Polygon polygon : polygons)
		{
			int ptCount = polygon.Points.size();
			Vector2[] vertices = new Vector2[ptCount];
			for (int ptIndex = 0; ptIndex < ptCount; ptIndex++)
			{
				Vector2 point = Vector2Pool.obtain(polygon.Points.get(ptIndex));
				point.set(point.x * maxLength / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - halfWidth,
						point.y * maxLength / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - halfHeight);
				vertices[ptIndex] = point;
			}

			//Body body1 = createRightEdgeChamferedBody(physicsWorld, entity, bodyType, fixtureDef);
			Body body = PhysicsFactory.createPolygonBody(physicsWorld, entity, vertices, bodyType, fixtureDef);
			bodies.add(body);
			for (int ptIndex = 0; ptIndex < ptCount; ptIndex++)
			{
				Vector2Pool.recycle(vertices[ptIndex]);
			}
		}
		return bodies;
	}
}
