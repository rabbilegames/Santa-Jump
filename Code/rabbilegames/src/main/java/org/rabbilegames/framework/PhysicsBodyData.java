package org.rabbilegames.framework;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.rabbilegames.ResourceManager;

import com.badlogic.gdx.math.Vector2;
import com.google.gson.stream.JsonReader;

public class PhysicsBodyData
{
	public ArrayList<RigidBody> RigidBodies;

	public void Load(String physicsDataFilePath)
	{
		String physicsBodyData = ResourceManager.Get().GetTextFileContent(physicsDataFilePath);
		StringReader stringReader = new StringReader(physicsBodyData);
		JsonReader reader = new JsonReader(stringReader);
		try
		{
			ReadObject(reader);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	void ReadObject(JsonReader reader) throws IOException
	{
		reader.beginObject();
		while (reader.hasNext())
		{
			String name = reader.nextName();
			if (name.equals("rigidBodies"))
			{
				ReadRigidBodies(reader);
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();
	}

	void ReadRigidBodies(JsonReader reader) throws IOException
	{
		RigidBodies = new ArrayList<RigidBody>();

		reader.beginArray();
		while (reader.hasNext())
		{
			RigidBodies.add(ReadRigidBody(reader));
		}
		reader.endArray();
	}

	private RigidBody ReadRigidBody(JsonReader reader) throws IOException
	{
		RigidBody rigidBody = new RigidBody();
		reader.beginObject();
		while (reader.hasNext())
		{
			String name = reader.nextName();
			if (name.equals("name"))
			{
				rigidBody.Name = reader.nextString();
			}
			else if (name.equals("imagePath"))
			{
				rigidBody.ImagePath = reader.nextString();
			}
			else if (name.equals("origin"))
			{
				rigidBody.Origin = ReadVector2(reader);
			}
			else if (name.equals("polygons"))
			{
				rigidBody.BodyPolygons = ReadBodyPolygons(reader);
			}
			else if (name.equals("shapes"))
			{
				rigidBody.ShapePolygons = ReadShapePolygons(reader);
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();
		return rigidBody;
	}

	private ArrayList<Polygon> ReadShapePolygons(JsonReader reader) throws IOException
	{
		ArrayList<Polygon> polygons = new ArrayList<Polygon>();
		reader.beginArray();
		while (reader.hasNext())
		{
			polygons.add(ReadShape(reader));
		}
		reader.endArray();

		return polygons;
	}

	private Polygon ReadShape(JsonReader reader) throws IOException
	{
		Polygon polygon = null;
		reader.beginObject();
		while (reader.hasNext())
		{
			String name = reader.nextName();
			if (name.equals("type"))
			{
				reader.nextString();
			}
			else if (name.equals("vertices"))
			{
				polygon = ReadPolygon(reader);
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();
		return polygon;
	}

	private ArrayList<Polygon> ReadBodyPolygons(JsonReader reader) throws IOException
	{
		ArrayList<Polygon> polygons = new ArrayList<Polygon>();

		reader.beginArray();
		while (reader.hasNext())
		{
			polygons.add(ReadPolygon(reader));
		}
		reader.endArray();
		return polygons;
	}

	private Polygon ReadPolygon(JsonReader reader) throws IOException
	{
		Polygon polygon = new Polygon();
		ArrayList<Vector2> points = new ArrayList<Vector2>();

		reader.beginArray();
		while (reader.hasNext())
		{
			points.add(ReadVector2(reader));
		}
		reader.endArray();
		polygon.Points = points;
		return polygon;
	}

	private Vector2 ReadVector2(JsonReader reader) throws IOException
	{
		Vector2 point = new Vector2();
		reader.beginObject();
		while (reader.hasNext())
		{
			String name = reader.nextName();
			if (name.equals("x"))
			{
				point.x = (float) reader.nextDouble();
			}
			else if (name.equals("y"))
			{
				point.y = (float) reader.nextDouble();
			}
		}
		reader.endObject();
		return point;
	}

	public ArrayList<Polygon> GetRigidBodies(String name)
	{
		for (RigidBody rigidBody : RigidBodies)
		{
			if (rigidBody.Name.equals(name))
			{
				return rigidBody.BodyPolygons;
			}
		}
		return null;
	}
}
