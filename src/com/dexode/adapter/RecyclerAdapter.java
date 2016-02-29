package com.dexode.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexode.adapter.holder.BaseHolder;

import java.util.ArrayList;

/**
 * Created by Dawid Drozd aka Gelldur on 16.02.16.
 */
public abstract class RecyclerAdapter extends RecyclerView.Adapter<BaseHolder> {

	public RecyclerAdapter(final Activity activity) {
		_layoutInflater = activity.getLayoutInflater();
	}

	@Override
	public BaseHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

		final View view = _layoutInflater.inflate(viewType, parent, false);
		final ViewHolderCreator viewHolderCreator = _holderCreators.get(viewType);
		final BaseHolder viewHolder = viewHolderCreator.create(view);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final BaseHolder holder, final int position) {
		holder.setData(_elements.get(position));
	}

	@Override
	public int getItemViewType(final int position) {
		return _elements.get(position).layoutId;
	}

	@Override
	public int getItemCount() {
		return _elements.size();
	}

	public void addViewHolderCreator(int viewType, ViewHolderCreator creator) {
		_holderCreators.put(viewType, creator);
	}

	public void addElementAndHolderCreator(Element element, ViewHolderCreator creator) {
		_elements.add(element);
		addViewHolderCreator(element.layoutId, creator);
	}

	protected LayoutInflater getLayoutInflater() {
		return _layoutInflater;
	}

	public void addElement(Element element) {
		_elements.add(element);
	}

	private ArrayList<Element> _elements = new ArrayList<>();
	private SparseArray<ViewHolderCreator> _holderCreators = new SparseArray<>(4);
	private LayoutInflater _layoutInflater;

	public interface ViewHolderCreator {
		public BaseHolder create(final View itemView);
	}

	public static class Element {

		public Element(final int layoutId, final Object data) {
			this.layoutId = layoutId;
			this.data = data;
		}

		public int layoutId;
		public Object data;
	}
}
