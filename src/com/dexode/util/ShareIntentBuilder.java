package com.dexode.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.dexode.util.log.Logger;
import com.google.android.gms.plus.PlusShare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawid Drozd aka Gelldur on 5/27/15.
 */
public class ShareIntentBuilder {

	public static ShareIntentBuilder create() {
		return new ShareIntentBuilder();
	}

	public ShareIntentBuilder shareByEmail(String subject, String text) {
		return shareByEmail(subject, text, null);
	}

	public ShareIntentBuilder shareByEmail(String subject, String text, @Nullable String sendToEMail) {

		Intent emailIntent;
		if (sendToEMail != null) {
			emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", sendToEMail, null));
		} else {
			emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
		}

		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, text);

		_intents.add(emailIntent);
		return this;
	}

	public ShareIntentBuilder shareBySms(String textBody) {
		return shareBySms(textBody, null);
	}

	public ShareIntentBuilder shareBySms(String textBody, @Nullable String sendToNumber) {
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		String uri = "sms:";
		if (sendToNumber != null) {
			uri += sendToNumber;
		}

		smsIntent.setData(Uri.parse(uri));
		smsIntent.putExtra("sms_body", textBody);

		_intents.add(smsIntent);

		return this;
	}

	public ShareIntentBuilder shareByGooglePlus(Context context, String text, String contentUrl) {
		Intent googlePlusIntent = new PlusShare.Builder(context).setType("text/plain")
																.setText(text)
																.setContentUrl(Uri.parse(contentUrl))
																.getIntent();

		_intents.add(googlePlusIntent);

		return this;
	}

	public ShareIntentBuilder shareByMessanger(String text) {
		Intent messangerIntent = new Intent(Intent.ACTION_SEND);
		messangerIntent.putExtra(Intent.EXTRA_TEXT, text);
		messangerIntent.setType("text/plain");
		messangerIntent.setPackage("com.facebook.orca");

		_intents.add(messangerIntent);

		return this;
	}

	public Intent build(String chooserTitle, final Context context) throws NoShareIntentsException {
		if (_intents.isEmpty()) {
			throw new IllegalStateException("Please add shares!");
		}

		final PackageManager packageManager = context.getPackageManager();

		ArrayList<LabeledIntent> extraIntents = new ArrayList<>(_intents.size() * 2);
		Intent mainIntent = null;

		for (int i = 0; i < _intents.size(); ++i) {
			Intent intent = _intents.get(i);

			List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
			if (resolveInfoList == null) {//Some devices return null...
				continue;
			}
			if (mainIntent == null && resolveInfoList.isEmpty() == false) {
				mainIntent = intent;
				//This will be main chooser so we don't want duplicates
				continue;
			}
			for (ResolveInfo info : resolveInfoList) {
				// Extract the label, append it, and repackage it in a LabeledIntent
				String packageName = info.activityInfo.packageName;

				intent.setComponent(new ComponentName(packageName, info.activityInfo.name));
				final LabeledIntent labeledIntent =
						new LabeledIntent(intent, packageName, info.loadLabel(packageManager), info.icon);
				extraIntents.add(labeledIntent);
			}
		}

		if (mainIntent == null) {
			Logger.e("No app can't handle such share request");
			throw new NoShareIntentsException("No app can't handle such share request");
		}

		final Intent chooser = Intent.createChooser(mainIntent, chooserTitle);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents.toArray(new LabeledIntent[extraIntents.size()]));
		return chooser;
	}

	/**
	 * A lot of application will report here. Apps like AndroidBeam or Google Keep
	 *
	 * @param text
	 *
	 * @return builder
	 */
	public ShareIntentBuilder shareByActionSend(final String text) {
		Intent sendToIntent = new Intent(Intent.ACTION_SEND, null);
		sendToIntent.setType("text/plain");
		sendToIntent.putExtra(Intent.EXTRA_TEXT, text);

		_intents.add(sendToIntent);
		return this;
	}

	private ArrayList<Intent> _intents = new ArrayList<>(8);

	public static class NoShareIntentsException extends Exception {

		public NoShareIntentsException(String message) {
			super(message);
		}
	}
}
