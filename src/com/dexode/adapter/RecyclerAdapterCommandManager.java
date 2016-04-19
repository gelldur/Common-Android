package com.dexode.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dawid Drozd aka Gelldur on 14.04.16.
 */
public class RecyclerAdapterCommandManager {

	public RecyclerAdapterCommandManager(final AdapterWrapper adapterWrapper) {
		_adapter = adapterWrapper;
	}

	public RecyclerAdapterCommandManager(final RecyclerView.Adapter adapter) {
		_adapter = new AdapterWrapper() {
			@Override
			public void notifyItemInserted(final int position) {
				adapter.notifyItemInserted(position);
			}

			@Override
			public void notifyItemChanged(final int position) {
				adapter.notifyItemChanged(position);
			}

			@Override
			public void notifyItemRangeInserted(final int position, final int count) {
				adapter.notifyItemRangeInserted(position, count);
			}

			@Override
			public void notifyItemRemoved(final int position) {
				adapter.notifyItemRemoved(position);
			}

			@Override
			public void notifyItemRangeRemoved(final int position, final int count) {
				adapter.notifyItemRangeRemoved(position, count);
			}

			@Override
			public void notifyDataSetChanged() {
				adapter.notifyDataSetChanged();
			}

			@Override
			public void notifyItemMoved(final int fromPosition, final int toPosition) {
				adapter.notifyItemMoved(fromPosition, toPosition);
			}
		};
	}

	public void pushBackOne(final int id) {
		checkStartChanges();
		_changes.add(id);
	}

	public void pushBackAll(final ArrayList<Integer> ids) {
		checkStartChanges();
		_changes.addAll(ids);
	}

	public void insert(int position, final int id) {
		checkStartChanges();
		_changes.add(position, id);
	}

	public void remove(int position) {
		checkStartChanges();
		_changes.remove(position);
	}

	public void reset() {
		checkStartChanges();
		_changes.clear();
		_updates.clear();
	}

	public void update(final int position, final int id) {
		_updates.add(id);
	}

	public void commit() {
		commit(false);
	}

	public void commit(boolean skipProcessing) {
		if (_isReady == false) {
			return;
		}

		skipProcessing = _stable.isEmpty() || skipProcessing;
		if (skipProcessing) {
			_stable = _changes;
			_changes = null;
			_updates.clear();
			_adapter.notifyDataSetChanged();
			return;
		}

		if (_changes != null) {
			process();
			_stable = _changes;
		}

		_changes = null;
		for (Integer updateId : _updates) {
			final int position = _stable.indexOf(updateId);
			if (position > 0) {
				_adapter.notifyItemChanged(position);
			}
		}
		_updates.clear();
	}

	private void process() {
		ArrayList<Range> inserted = new ArrayList<>(32);
		ArrayList<Range> removed = new ArrayList<>(32);
		ArrayList<Integer> processed = new ArrayList<>(_stable);

		int offset = 0;
		//Search for removed
		for (int i = 0; i < _stable.size(); ++i) {
			if (_changes.contains(_stable.get(i))) {
				continue;
			}

			processed.remove(i - offset);
			appendRange(removed, i, -offset);
			++offset;
		}

		//Search for added
		for (int i = 0; i < _changes.size(); ++i) {
			if (_stable.contains(_changes.get(i)) == false) {
				processed.add(i, _changes.get(i));
				appendRange(inserted, i, 0);
			}
		}

		for (Range range : removed) {
			if (range.start == range.end) {
				_adapter.notifyItemRemoved(range.start);
			} else {
				_adapter.notifyItemRangeRemoved(range.start, range.end - range.start + 1);
			}
		}

		for (Range range : inserted) {
			if (range.start == range.end) {
				_adapter.notifyItemInserted(range.start);
			} else {
				_adapter.notifyItemRangeInserted(range.start, range.end - range.start + 1);
			}
		}

		//Search for moved
		for (int i = 0; i < _changes.size(); ++i) {
			for (int j = i + 1; j < processed.size(); ++j) {
				if (processed.get(j) == _changes.get(i)) {
					_adapter.notifyItemMoved(j, i);
					break;
				}
			}
		}
	}

	private void checkStartChanges() {
		if (_changes == null) {
			_changes = new ArrayList<>(_stable);
		}
	}

	private void appendRange(ArrayList<Range> rangeArray, int position, final int offset) {
		if (rangeArray.isEmpty()) {
			rangeArray.add(new Range(position + offset, position + offset));
		} else {
			final Range range = rangeArray.get(rangeArray.size() - 1);
			if (range.end + 1 == position) {
				range.end = position;
			} else {
				rangeArray.add(new Range(position + offset, position + offset));
			}
		}
	}

	public void onAttachedToRecyclerView() {
		_isReady = true;
		commit();
	}

	private boolean _isReady = false;
	private ArrayList<Integer> _changes = new ArrayList<>(32);
	private Set<Integer> _updates = new HashSet<>(32);
	private ArrayList<Integer> _stable = new ArrayList<>(32);

	private AdapterWrapper _adapter;

	private static class Range {

		public Range(final int start, final int end) {
			this.start = start;
			this.end = end;
		}

		public int start;
		public int end;
	}

	public interface AdapterWrapper {

		void notifyItemInserted(final int position);

		void notifyItemChanged(final int position);

		void notifyItemRangeInserted(final int position, final int count);

		void notifyItemRemoved(final int position);

		void notifyItemRangeRemoved(final int position, final int count);

		void notifyDataSetChanged();

		void notifyItemMoved(int fromPosition, int toPosition);
	}
}
