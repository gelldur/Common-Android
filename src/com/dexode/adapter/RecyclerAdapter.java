package com.dexode.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexode.adapter.holder.BaseHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawid Drozd aka Gelldur on 16.02.16.
 */
//TODO need refactor
public class RecyclerAdapter extends RecyclerView.Adapter<BaseHolder> {

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

	public int getItemTypeCount(final int type) {
		int count = 0;
		for (Element element : _elements) {
			if (element.layoutId == type) {
				++count;
			}
		}
		return count;
	}

	public boolean isItemTypePresent(final int type) {
		for (Element element : _elements) {
			if (element.layoutId == type) {
				return true;
			}
		}
		return false;
	}

	public void addViewHolderCreator(int viewType, ViewHolderCreator creator) {
		_holderCreators.put(viewType, creator);
	}

	public void addElementAndHolderCreator(Element element, ViewHolderCreator creator) {
		addViewHolderCreator(element.layoutId, creator);
		addElement(element, true);
	}

	protected LayoutInflater getLayoutInflater() {
		return _layoutInflater;
	}

	public void insertElement(int position, Element element) {
		_elements.add(position, element);
		notifyItemInserted(position);
	}

	public void addElement(Element element, boolean notify) {
		if (_elements.add(element)) {
			if (notify) {
				notifyItemInserted(_elements.size() - 1);
			}
		}
	}

	public void addAllElements(final ArrayList<Element> elements, boolean notify) {
		int insertPosition = _elements.size();
		_elements.addAll(elements);
		if (notify) {
			notifyItemRangeInserted(insertPosition, elements.size());
		}
	}

	protected void setElements(ArrayList<Element> elements) {
		_elements = elements;
	}

	//TODO REFACTOR
	public int removeAllByType(int type, boolean notify) {
		ArrayList<Element> filteredElements = new ArrayList<>(_elements.size());
		for (int i = 0; i < _elements.size(); ++i) {
			Element element = _elements.get(i);
			if (element.layoutId != type) {
				filteredElements.add(element);
			} else if (notify) {
				notifyItemRemoved(i);
			}
		}
		_elements = filteredElements;
		return _elements.size();
	}

	public static ArrayList<Element> createArray(final int type, List<?> data) {
		ArrayList<Element> elements = new ArrayList<>(data.size());
		for (Object object : data) {
			elements.add(new Element(type, object));
		}
		return elements;
	}

	protected ArrayList<Element> getElements() {
		return _elements;
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
