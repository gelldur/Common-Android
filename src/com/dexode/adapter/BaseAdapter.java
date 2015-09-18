package com.dexode.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Simple base Adapter that lets you use Holder pattern
 *
 * @author Dawid Drozd
 */
public abstract class BaseAdapter extends android.widget.BaseAdapter implements ViewHolderAdapterHelper.Adapter {

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Probably you should override this and use it to retrieve inflated views
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return _helper.getView(position, convertView, parent);
	}

	private ViewHolderAdapterHelper _helper = new ViewHolderAdapterHelper(this);
}
