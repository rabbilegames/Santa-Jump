package org.rabbilegames.Util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import org.rabbilegames.ResourceManager;

/**
 * Created by asanka.samarawickram on 7/3/2016.
 */
public class FrameworkHelper {
    public static boolean isDebugMode() {
        Context context = ResourceManager.Get().activity;
        int flags = 0;
        try {
            flags = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).flags;
        } catch (PackageManager.NameNotFoundException e) {
        }
        // Do not replace this with BuildProperties.DebugBuild. This is there to correctly identify debug build
        if ((flags &= ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            return true;
        } else {
            return false;
        }
    }
}
