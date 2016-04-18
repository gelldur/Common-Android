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
public class RecyclerAdapter extends RecyclerView.Adapter<BaseHolder> {

	public RecyclerAdapter(final Activity activity) {
		_layoutInflater = activity.getLayoutInflater();
		_commandManager = new RecyclerAdapterCommandManager(this);
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

	/**
	 * @param type
	 *
	 * @return position of type when found. Return -1 when not found
	 */
	public int firstIndexOfType(final int type) {
		for (int i = 0; i < _elements.size(); ++i) {
			if (_elements.get(i).layoutId == type) {
				return i;
			}
		}
		return -1;
	}

	public void addViewHolderCreator(int viewType, ViewHolderCreator creator) {
		_holderCreators.put(viewType, creator);
	}

	public void addElementAndHolderCreator(Element element, ViewHolderCreator creator) {
		addViewHolderCreator(element.layoutId, creator);
		addElement(element);
	}

	protected LayoutInflater getLayoutInflater() {
		return _layoutInflater;
	}

	public void insertElement(int position, Element element) {
		_elements.add(position, element);
		_commandManager.insert(position, getId(element));
	}

	public void addElement(Element element) {
		if (_elements.add(element)) {
			_commandManager.pushBackOne(getId(element));
		}
	}

	public void replaceElement(int position, Element element) {
		remove(position);
		insertElement(position, element);
	}

	public void update(int position, Object data) {
		_elements.get(position).data = data;
		_commandManager.update(position, getId(_elements.get(position)));
	}

	public void remove(int position) {
		final Element element = _elements.remove(position);
		_commandManager.remove(position);
	}

	public void addAllElements(final ArrayList<Element> elements) {
		_elements.addAll(elements);

		ArrayList<Integer> ids = new ArrayList<>(elements.size());
		for (Element element : elements) {
			ids.add(getId(element));
		}

		_commandManager.pushBackAll(ids);
	}

	public void commitChanges() {
		_commandManager.commit();
	}

	protected void setElements(ArrayList<Element> elements) {
		_elements = elements;
		_commandManager.reset();
		for (Element element : elements) {
			_commandManager.pushBackOne(getId(element));
		}
	}

	public int removeAllByType(int type) {
		ArrayList<Element> filteredElements = new ArrayList<>(_elements.size());
		int offset = 0;
		for (int i = 0; i < _elements.size(); ++i) {
			Element element = _elements.get(i);
			if (element.layoutId != type) {
				filteredElements.add(element);
			} else {
				_commandManager.remove(i - offset);
				++offset;
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

	private int getId(Element element) {
		if (element.data == null) {
			return 0;
		}
		return element.data.hashCode();
	}

	private ArrayList<Element> _elements = new ArrayList<>();
	private SparseArray<ViewHolderCreator> _holderCreators = new SparseArray<>(4);
	private LayoutInflater _layoutInflater;
	private RecyclerAdapterCommandManager _commandManager;

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
