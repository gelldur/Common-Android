package com.dexode.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dexode.adapter.RecyclerAdapter;

/**
 * Created by Dawid Drozd aka Gelldur on 22.02.16.
 */
public abstract class BaseHolder extends RecyclerView.ViewHolder {
	public BaseHolder(final View itemView) {
		super(itemView);
	}


	public abstract void setData(final RecyclerAdapter.Element element);
}
