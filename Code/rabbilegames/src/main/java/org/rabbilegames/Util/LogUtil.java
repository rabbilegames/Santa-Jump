package org.rabbilegames.Util;

import org.rabbilegames.Definitions.BuildProperties;

import java.text.MessageFormat;


public class LogUtil
{
	public final static String AnalyticsHandlerTag = "AnalyticsHandler";
	public final static String AdHandlerTag = "AdHandler";
	public final static String ScoreloopTag = "Scoreloop";
	public final static String MiscTag = "Misc";

	public static void Log(String tag, String message, Object... args)
	{
		if (BuildProperties.DebugBuild)
		{
			String formattedMessage = MessageFormat.format(message, args);
			android.util.Log.v(tag, formattedMessage);
		}
	}

	public static void LogDebug(String tag, String message, Object... args)
	{
		if (BuildProperties.DebugBuild)
		{
			String formattedMessage = MessageFormat.format(message, args);
			android.util.Log.d(tag, formattedMessage);
		}
	}
}
