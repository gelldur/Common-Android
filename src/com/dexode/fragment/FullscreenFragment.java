package com.dexode.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.WindowManager;

/**
 * @author Dawid Drozd
 *         You must manually call onKeyDown and onWindowFocusChanged.
 */
public class FullscreenFragment extends Fragment implements Runnable {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setFullscreen();
		if (Build.VERSION.SDK_INT > 10) {
			registerSystemUiVisibility();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (Build.VERSION.SDK_INT > 10) {
			unregisterSystemUiVisibility();
		}
		exitFullscreen(getActivity());
	}

	public static boolean isImmersiveAvailable() {
		return android.os.Build.VERSION.SDK_INT >= 19;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			_handler.removeCallbacks(this);
			_handler.postDelayed(this, 300);
		} else {
			_handler.removeCallbacks(this);
		}
	}

	public void onKeyDown(int keyCode) {
		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
			_handler.removeCallbacks(this);
			_handler.postDelayed(this, 500);
		}
	}

	@Override
	public void onStop() {
		_handler.removeCallbacks(this);
		super.onStop();
	}

	@Override
	public void run() {
		setFullscreen();
	}

	public void setFullscreen() {
		setFullscreen(getActivity());
	}

	public void setFullscreen(Activity activity) {
		if (Build.VERSION.SDK_INT > 10) {
			int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;

			if (isImmersiveAvailable()) {
				flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
						 View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			}

			activity.getWindow().getDecorView().setSystemUiVisibility(flags);
		} else {
			activity.getWindow()
					.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	public void exitFullscreen(Activity activity) {
		if (Build.VERSION.SDK_INT > 10) {
			activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		} else {
			activity.getWindow()
					.setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
							  WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
	}

	private Handler _handler = new Handler();

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void registerSystemUiVisibility() {
		final View decorView = getActivity().getWindow().getDecorView();
		decorView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {

			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
					setFullscreen();
				}
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void unregisterSystemUiVisibility() {
		final View decorView = getActivity().getWindow().getDecorView();
		decorView.setOnSystemUiVisibilityChangeListener(null);
	}
}
