package com.squareup.picasso;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Dawid Drozd aka Gelldur on 9/9/15.
 * You need picasso lib to use this :)
 * compile 'com.squareup.picasso:picasso:+'
 */
public class CacheHelper {
	public static void clearCache(Picasso picasso) {
		picasso.cache.clear();
	}

	@Nullable
	public static String findCached(final Context context, final String key) {
		File cacheFolder = new File(context.getCacheDir(), "picasso-cache");
		if (!cacheFolder.exists()) {
			return null;
		}

		final com.squareup.okhttp.Cache cache = new com.squareup.okhttp.Cache(cacheFolder, Integer.MAX_VALUE);
		try {
			final Iterator<String> urls = cache.urls();
			while (urls.hasNext()) {
				final String url = urls.next();
				if (url.contains(key)) {
					return url;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}
}
