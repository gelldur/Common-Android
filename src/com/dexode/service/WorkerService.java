package com.dexode.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dexode.util.log.Logger;

/**
 * Created by Dawid Drozd aka Gelldur on 1/13/16.
 */
public class WorkerService extends IntentService {

	public WorkerService() {
		super("BackgroundWorker");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.d("%s - onCreate", getClass().getSimpleName());
	}

	@Override
	public void onDestroy() {
		Logger.d("%s - onDestroy", getClass().getSimpleName());
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		if (intent == null) {
			return;
		}

		final String taskClassName = intent.getStringExtra(EXTRA_TASK);
		if (taskClassName == null) {
			Logger.e("Please set extra for %s!", getClass().getSimpleName());
			return;
		}

		try {
			final Class<Runnable> aClass = (Class<Runnable>) Class.forName(taskClassName);
			final Runnable runnable = aClass.newInstance();
			if (runnable instanceof RunnableWithParams) {
				RunnableWithParams withParams = (RunnableWithParams) runnable;
				withParams.setParams(intent.getBundleExtra(EXTRA_BUNDLE));
			}
			runnable.run();
		} catch (Exception e) {
			Logger.e(e);
		}
	}

	public static void executeAsync(@NonNull final Context context, final Class<?> clazz, @Nullable Bundle bundle) {
		Intent intentService = new Intent(context, WorkerService.class);

		intentService.putExtra(WorkerService.EXTRA_TASK, clazz.getName());
		intentService.putExtra(WorkerService.EXTRA_BUNDLE, bundle);

		context.startService(intentService);
	}

	public static final String EXTRA_TASK = "_task";
	public static final String EXTRA_BUNDLE = "_bundle";
}
