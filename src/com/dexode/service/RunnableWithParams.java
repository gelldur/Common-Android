package com.dexode.service;

import android.os.Bundle;

/**
 * Created by Dawid Drozd aka Gelldur on 1/13/16.
 */
public interface RunnableWithParams extends Runnable {
	void setParams(final Bundle bundle);
}
