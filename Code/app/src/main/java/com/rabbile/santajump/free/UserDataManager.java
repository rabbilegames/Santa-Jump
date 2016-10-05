package com.rabbile.santajump.free;

import org.rabbilegames.ResourceManager;

public class UserDataManager
{
	public static final String SHARED_PREFS_MAIN = "ChrismasSagaSettings";
	public static final String BEST_TIME_KEY = SHARED_PREFS_MAIN + ".BestTime";
	public static final String SCORELOOP_TERMS_ACCEPTED_KEY = SHARED_PREFS_MAIN + ".ScoreloopTermsAccepted";
	public static final String APPICATION_STARTED_COUNT = SHARED_PREFS_MAIN + ".AppStartedTimes";
	public static final String IS_RATED = SHARED_PREFS_MAIN + ".Rated";
	public static final String IS_RATE_REQUESTED = SHARED_PREFS_MAIN + ".RateRequested";
	public static final String IS_RATE_REQUESTED_PLAY_COUNT = SHARED_PREFS_MAIN + ".RateRequestedPlayCount";

	public static String GetWorldLevelPlayCountKey(int worldIndex, int levelIndex)
	{
		return String.format("%s.World%d.Level%d.PlayCount", SHARED_PREFS_MAIN, worldIndex, levelIndex);
	}

	public static int GetWorldLevelPlayCount(int worldIndex, int levelIndex)
	{
		return ReadIntFromSharedPreferences(GetWorldLevelPlayCountKey(worldIndex, levelIndex));
	}

	public static void IncrementWorldLevelPlayCount(int worldIndex, int levelIndex)
	{
		int newCount = ReadIntFromSharedPreferences(GetWorldLevelPlayCountKey(worldIndex, levelIndex)) + 1;
		WriteIntToSharedPreferences(GetWorldLevelPlayCountKey(worldIndex, levelIndex), newCount);
	}

	public static int GetTotalPlayCount()
	{
		int totalPlayCount = 0;
		for (int worldIndex = 0; worldIndex < 2; worldIndex++)
		{
			for (int levelIndex = 0; levelIndex < 30; levelIndex++)
			{
				totalPlayCount += ReadIntFromSharedPreferences(GetWorldLevelPlayCountKey(worldIndex, levelIndex));
			}
		}
		return totalPlayCount;
	}

	public static boolean GetIsRated()
	{
		return ReadBooleanFromSharedPreferences(IS_RATED, false);
	}

	public static void SetIsRated()
	{
		WriteBoolToSharedPreferences(IS_RATED, true);
	}

	public static void SetIsRateRequested(boolean value)
	{
		WriteBoolToSharedPreferences(IS_RATE_REQUESTED, value);
	}

	public static boolean GetIsRateRequested()
	{
		return ReadBooleanFromSharedPreferences(IS_RATE_REQUESTED, false);
	}

	public static int GetRateRequestedPlayCount()
	{
		return ReadIntFromSharedPreferences(IS_RATE_REQUESTED_PLAY_COUNT, 0);
	}

	public static void SetRateRequestedPlayCount(int count)
	{
		WriteIntToSharedPreferences(IS_RATE_REQUESTED_PLAY_COUNT, count);
	}

	public static int GetAppStartedTimes()
	{
		return ReadIntFromSharedPreferences(APPICATION_STARTED_COUNT, 0);
	}

	public static void IncrementAppStartedCount()
	{
		int count = GetAppStartedTimes() + 1;
		WriteIntToSharedPreferences(APPICATION_STARTED_COUNT, count);
	}

	static String GetWorldHighestUnlockedLevelKey(int worldIndex)
	{
		return String.format("%s.World%d.HighestUnlockedLevel", SHARED_PREFS_MAIN, worldIndex);
	}

	public static String GetLevelStarsKey(int worldIndex, final int levelIndex)
	{
		return String.format("%s.World%d.Level%d.Stars", SHARED_PREFS_MAIN, worldIndex, levelIndex);
	}

	public static int GetHighestUnlockedLevel(int worldIndex)
	{
		return ReadIntFromSharedPreferences(GetWorldHighestUnlockedLevelKey(worldIndex), 0);
	}

	public static void SetHighestUnlockedLevel(int worldIndex, int levelIndex)
	{
		if (GetHighestUnlockedLevel(worldIndex) < levelIndex)
		{
			WriteIntToSharedPreferences(GetWorldHighestUnlockedLevelKey(worldIndex), levelIndex);
		}
	}

	public static int ReadIntFromSharedPreferences(final String pStr)
	{
		return ReadIntFromSharedPreferences(pStr, 0);
	}

	private static int ReadIntFromSharedPreferences(final String pStr, int defaultValue)
	{
		return ResourceManager.Get().activity.getSharedPreferences(SHARED_PREFS_MAIN, 0)
				.getInt(pStr, defaultValue);
	}

	private static int WriteIntToSharedPreferences(final String pStr, final int pValue)
	{
		// The apply() method requires API level 9 in the manifest.
		ResourceManager.Get().activity.getSharedPreferences(SHARED_PREFS_MAIN, 0).edit().putInt(pStr, pValue)
				.commit();
		return pValue;
	}

	private static boolean ReadBooleanFromSharedPreferences(String pStr, boolean defaultValue)
	{
		return ResourceManager.Get().activity.getSharedPreferences(SHARED_PREFS_MAIN, 0)
				.getBoolean(pStr, defaultValue);
	}

	private static boolean WriteBoolToSharedPreferences(final String pStr, final boolean pValue)
	{
		// The apply() method requires API level 9 in the manifest.
		ResourceManager.Get().activity.getSharedPreferences(SHARED_PREFS_MAIN, 0).edit()
				.putBoolean(pStr, pValue).commit();
		return pValue;
	}

	public static float GetHighScore()
	{
		return GetFloat(BEST_TIME_KEY, 0);
	}

	public static boolean TryToSetBestTime(float timeInSeconds)
	{
		if (GetHighScore() < timeInSeconds)
		{
			SetFloat(BEST_TIME_KEY, timeInSeconds);
			return true;
		}
		return false;
	}

	private static void SetFloat(String key, float val)
	{
		ResourceManager.Get().activity.getSharedPreferences(SHARED_PREFS_MAIN, 0).edit()
				.putFloat(key, val).commit();
	}

	private static float GetFloat(String key, float defaultVal)
	{
		return ResourceManager.Get().activity.getSharedPreferences(SHARED_PREFS_MAIN, 0)
				.getFloat(key, defaultVal);
	}

	public static void SetIsScoreloopTermsAccepted(Boolean scoreloopAccepted)
	{
		WriteBoolToSharedPreferences(SCORELOOP_TERMS_ACCEPTED_KEY, scoreloopAccepted);
	}

	public static boolean GetIsScoreloopTermsAccepted()
	{
		return ReadBooleanFromSharedPreferences(SCORELOOP_TERMS_ACCEPTED_KEY, false);
	}

	public static int GetWorldScore(int worldIndex)
	{
		//		int score = 0;
		//		for (int levelIndex = 0; levelIndex < 30; levelIndex++)
		//		{
		//			score += GetLevelHighScore(worldIndex, levelIndex);
		//		}
		//		return score;
		return 0;
	}
}
