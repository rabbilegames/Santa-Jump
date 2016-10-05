package org.rabbilegames.framework;

public class FloatModifier
{
	float currentValue;
	float changePerSecond;
	float totalSecondsElapsed;
	float duration;

	public FloatModifier(float duration, float start, float end)
	{
		this.duration = duration;
		changePerSecond = (end - start) / duration;
		currentValue = start;
	}

	protected void OnValueUpdate(float value)
	{

	}

	public void OnUpdate(float secondsElapsed)
	{
		if (duration > totalSecondsElapsed)
		{
			totalSecondsElapsed += secondsElapsed;
			if (totalSecondsElapsed > duration)
			{
				secondsElapsed = duration - totalSecondsElapsed + secondsElapsed;
			}
			currentValue += changePerSecond * secondsElapsed;
			OnValueUpdate(currentValue);
		}
	}

	public float GetCurrentValue()
	{
		return currentValue;
	}
}
