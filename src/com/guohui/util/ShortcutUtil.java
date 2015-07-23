package com.guohui.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

public class ShortcutUtil {
	public static void createShortCut(Activity paramActivity, int paramInt1,
			int paramInt2) {
		Intent localIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		localIntent.putExtra("duplicate", false);
		localIntent.putExtra("android.intent.extra.shortcut.NAME", paramActivity.getString(paramInt2));
		localIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE",
				Intent.ShortcutIconResource.fromContext(paramActivity.getApplicationContext(), paramInt1));
		localIntent.putExtra("android.intent.extra.shortcut.INTENT",
				new Intent(paramActivity.getApplicationContext(), paramActivity.getClass()));
		paramActivity.sendBroadcast(localIntent);
	}

	public static void deleteShortCut(Activity paramActivity, int paramInt1) {
		Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, paramActivity.getString(paramInt1));
		shortcut.putExtra( Intent.EXTRA_SHORTCUT_INTENT,
				new Intent(paramActivity.getApplicationContext(), paramActivity
						.getClass()));
		paramActivity.sendBroadcast(shortcut);
	}

	public static boolean hasShortCut(Activity paramActivity, int paramInt1) {
		boolean isInstallShortcut = false;
		Uri CONTENT_URI;
		ContentResolver cr = paramActivity.getContentResolver();
		if (Build.VERSION.SDK_INT > 6) {
			CONTENT_URI = Uri .parse("content://com.android.launcher2.settings/favorites?notify=true");
		} else {
			CONTENT_URI = Uri .parse("content://com.android.launcher.settings/favorites?notify=true");
		}
		Cursor c = cr.query(CONTENT_URI,
				new String[] { "title", "iconResource" }, "title=?",
				new String[] { paramActivity.getString(paramInt1).trim() },
				null);
		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
		}
		return isInstallShortcut;
	}
}