package org.rabbilegames.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.math.Vector2;

public class PointUtil
{
	public static float FindMinY(ArrayList<Vector2> points)
	{
		float minY = Float.MAX_VALUE;
		for (Vector2 vert : points)
		{
			if (minY > vert.y)
			{
				minY = vert.y;
			}
		}
		return minY;
	}

	public static float FindMaxY(ArrayList<Vector2> points)
	{
		float maxY = Float.MIN_VALUE;
		for (Vector2 vert : points)
		{
			if (maxY < vert.y)
			{
				maxY = vert.y;
			}
		}
		return maxY;
	}

	public static void SortByX(ArrayList<Vector2> points)
	{
		Collections.sort(points, new Comparator<Vector2>()
		{
			@Override
			public int compare(Vector2 arg0, Vector2 arg1)
			{
				return (int) (arg0.x - arg1.x);
			}
		});
	}

	public static boolean AreEqual(Vector2 vert1, Vector2 vert2)
	{
		return vert1.x == vert2.x && vert1.y == vert2.y;
	}

	public static float dst2(Vector2 vert1, float px, float py)
	{
		return (float) Math.sqrt((vert1.x - px) * (vert1.x - px) + (vert1.y - py) * (vert1.y - py));
	}

	public static float dst2(float x0, float y0, float x1, float y1)
	{
		return (float) Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
	}

	public static int ClosestPointIndex(ArrayList<Vector2> points, float px, float py)
	{
		int closestPointIndex = 0;
		float dist2ToClosestPoint = dst2(points.get(0), px, py);

		for (int ptIndex = 1; ptIndex < points.size(); ptIndex++)
		{
			float dist2 = dst2(points.get(ptIndex), px, py);
			if (dist2 < dist2ToClosestPoint)
			{
				dist2ToClosestPoint = dist2;
				closestPointIndex = ptIndex;
			}
		}
		return closestPointIndex;
	}

	public static double AngleTo(Vector2 reference, Vector2 endPt)
	{
		return Math.atan2(endPt.y - reference.y, endPt.x - reference.x);
	}

	public static void Serialize(Vector2 pt, DataOutputStream dataOutputStream) throws IOException
	{
		dataOutputStream.writeFloat(pt.x);
		dataOutputStream.writeFloat(pt.y);
	}

	public static void SerializePointList(ArrayList<Vector2> points, DataOutputStream dataOutputStream)
			throws IOException
	{
		dataOutputStream.writeInt(points.size());
		for (Vector2 pt : points)
		{
			PointUtil.Serialize(pt, dataOutputStream);
		}
	}

	public static void Deserialize(Vector2 vector2, DataInputStream dataInputStream) throws IOException
	{
		vector2.x = dataInputStream.readFloat();
		vector2.y = dataInputStream.readFloat();
	}

	public static void DeserializePointList(ArrayList<Vector2> points, DataInputStream dataInputStream)
			throws IOException
	{
		int pointCount = dataInputStream.readInt();
		for (int pointIndex = 0; pointIndex < pointCount; pointIndex++)
		{
			Vector2 vector2 = new Vector2();
			Deserialize(vector2, dataInputStream);
			points.add(vector2);
		}
	}

	public static boolean AreSame(Vector2 vector1, Vector2 vector2)
	{
		return vector1.x == vector2.x && vector1.y == vector2.y;
	}

	public static float PerpendicularDistance(Vector2 point1, Vector2 point2, Vector2 point)
	{
		float area = (float) Math.abs(0.5 * (point1.x * point2.y + point2.x * point.y +
				point.x * point1.y - point2.x * point1.y -
				point.x * point2.y - point1.x * point.y));
		float bottom = (float) Math.sqrt((point1.x - point2.x) * (point1.x - point2.x)
				+ (point1.y - point2.y) * (point1.y - point2.y));
		float height = area / bottom * 2;

		return height;
	}

	public static float PerpendicularDistance(float point1X, float point1Y, float point2X, float point2Y,
			float pointX, float pointY)
	{
		float area = (float) Math.abs(0.5 * (point1X * point2Y + point2X * pointY +
				pointX * point1Y - point2X * point1Y -
				pointX * point2Y - point1X * pointY));
		float bottom = (float) Math.sqrt((point1X - point2X) * (point1X - point2X)
				+ (point1Y - point2Y) * (point1Y - point2Y));
		float height = area / bottom * 2;

		return height;
	}

	public static void Rotate(Vector2 vector, float angle)
	{
		double oldX = vector.x;
		double oldY = vector.y;
		vector.x = (float) (oldX * Math.cos(angle) - oldY * Math.sin(angle));
		vector.y = (float) (oldY * Math.cos(angle) + oldX * Math.sin(angle));
	}

	public static void Rotate(Vector2 vector, float angle, float refX, float refY)
	{
		float cosValue = (float) Math.cos(angle);
		float sinValue = (float) Math.sin(angle);
		float oldX = vector.x;
		float oldY = vector.y;
		vector.x = oldX * cosValue - oldY * sinValue + refX * (1 - cosValue) + refY * sinValue;
		vector.y = oldY * cosValue + oldX * sinValue + refY * (1 - cosValue) - refX * sinValue;
	}
}
