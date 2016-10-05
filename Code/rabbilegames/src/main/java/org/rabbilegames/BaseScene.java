package org.rabbilegames;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

public abstract class BaseScene extends Scene
{
	protected Engine _engine;
	protected Activity _activity;
	protected ResourceManager _resourcesManager;
	protected VertexBufferObjectManager _vbom;
	protected RabbileGameCamera _camera;

	private boolean _isSceneInTransition;

	//---------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------

	public BaseScene()
	{
		_resourcesManager = ResourceManager.Get();
		_engine = _resourcesManager.engine;
		_activity = _resourcesManager.activity;
		_vbom = _resourcesManager.Vbo;
		_camera = _resourcesManager.camera;
		createScene();
	}

	//---------------------------------------------
	// ABSTRACTION
	//---------------------------------------------

	public void createScene()
	{
	}

	public void onBackKeyPressed()
	{
	}

	public abstract SceneType getSceneType();

	public void disposeScene()
	{
	}

	public void OnLoadScene()
	{
	}

	public void OnUnloadScene()
	{
	}

	public void OnLoadSceneResources()
	{
	}

	public void OnShowScene()
	{
	}

	public boolean isSceneInTransition()
	{
		return _isSceneInTransition;
	}

	public void setIsSceneInTransition(boolean value)
	{
		_isSceneInTransition = value;
	}

	public void beginTransitionIn()
	{
	}

	public void beginTransitionOut()
	{
	}

	public void onTransitionFinished()
	{
	}
}
