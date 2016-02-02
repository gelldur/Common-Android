package com.dexode.util;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Dawid Drozd aka Gelldur on 02.02.16.
 * Modeled on https://gist.github.com/mipreamble/b6d4b3d65b0b4775a22e
 */
public class RecyclerViewHelper {

	public static boolean isScrolledToBottom(final RecyclerView recyclerView) {
		final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		final View child = findOneVisibleChild(recyclerView, layoutManager.getChildCount() - 1, -1, false, true);
		if (child == null) {
			return false;
		}

		if (recyclerView.getChildAdapterPosition(child) + 1 < recyclerView.getChildCount()) {
			return false;
		}

		OrientationHelper helper;
		if (layoutManager.canScrollVertically()) {
			helper = OrientationHelper.createVerticalHelper(layoutManager);
		} else {
			helper = OrientationHelper.createHorizontalHelper(layoutManager);
		}

		final int childEnd = helper.getDecoratedEnd(child);
		return childEnd <= helper.getEndAfterPadding();
	}

	/**
	 * Returns the adapter position of the first visible view. This position does not include
	 * adapter changes that were dispatched after the last layout pass.
	 *
	 * @return The adapter position of the first visible item or {@link RecyclerView#NO_POSITION} if
	 * there aren't any visible items.
	 */
	public static int findFirstVisibleItemPosition(final RecyclerView recyclerView) {
		final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		final View child = findOneVisibleChild(recyclerView, 0, layoutManager.getChildCount(), false, true);
		return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
	}

	/**
	 * Returns the adapter position of the first fully visible view. This position does not include
	 * adapter changes that were dispatched after the last layout pass.
	 *
	 * @return The adapter position of the first fully visible item or
	 * {@link RecyclerView#NO_POSITION} if there aren't any visible items.
	 */
	public static int findFirstCompletelyVisibleItemPosition(final RecyclerView recyclerView) {
		final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		final View child = findOneVisibleChild(recyclerView, 0, layoutManager.getChildCount(), true, false);
		return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
	}

	/**
	 * Returns the adapter position of the last visible view. This position does not include
	 * adapter changes that were dispatched after the last layout pass.
	 *
	 * @return The adapter position of the last visible view or {@link RecyclerView#NO_POSITION} if
	 * there aren't any visible items
	 */
	public static int findLastVisibleItemPosition(final RecyclerView recyclerView) {
		final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		final View child = findOneVisibleChild(recyclerView, layoutManager.getChildCount() - 1, -1, false, true);
		return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
	}

	/**
	 * Returns the adapter position of the last fully visible view. This position does not include
	 * adapter changes that were dispatched after the last layout pass.
	 *
	 * @return The adapter position of the last fully visible view or
	 * {@link RecyclerView#NO_POSITION} if there aren't any visible items.
	 */
	public static int findLastCompletelyVisibleItemPosition(final RecyclerView recyclerView) {
		final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		final View child = findOneVisibleChild(recyclerView, layoutManager.getChildCount() - 1, -1, true, false);
		return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
	}

	private static View findOneVisibleChild(final RecyclerView recyclerView, int fromIndex, int toIndex,
											boolean completelyVisible, boolean acceptPartiallyVisible) {
		final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		OrientationHelper helper;
		if (layoutManager.canScrollVertically()) {
			helper = OrientationHelper.createVerticalHelper(layoutManager);
		} else {
			helper = OrientationHelper.createHorizontalHelper(layoutManager);
		}

		final int start = helper.getStartAfterPadding();
		final int end = helper.getEndAfterPadding();
		final int next = toIndex > fromIndex ? 1 : -1;
		View partiallyVisible = null;
		for (int i = fromIndex; i != toIndex; i += next) {
			final View child = layoutManager.getChildAt(i);
			final int childStart = helper.getDecoratedStart(child);
			final int childEnd = helper.getDecoratedEnd(child);
			if (childStart < end && childEnd > start) {
				if (completelyVisible) {
					if (childStart >= start && childEnd <= end) {
						return child;
					} else if (acceptPartiallyVisible && partiallyVisible == null) {
						partiallyVisible = child;
					}
				} else {
					return child;
				}
			}
		}
		return partiallyVisible;
	}
}

