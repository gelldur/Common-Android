package com.dexode.adapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Dawid Drozd aka Gelldur on 15.04.16.
 */
@RunWith(MockitoJUnitRunner.class)
public class RecyclerAdapterCommandManagerTest {

	@Mock
	RecyclerAdapterCommandManager.AdapterWrapper _adapterWrapper;

	@Test
	public void simpleTest() {

		RecyclerAdapterCommandManager manager = new RecyclerAdapterCommandManager(_adapterWrapper);
		manager.onAttachedToRecyclerView();
		manager.pushBackOne(1);
		manager.pushBackOne(2);
		manager.pushBackOne(3);
		manager.pushBackOne(4);
		manager.pushBackOne(5);

		manager.commit();

		verify(_adapterWrapper, atLeastOnce()).notifyDataSetChanged();
	}

	@Test
	public void checkTransaction() {
		RecyclerAdapterCommandManager manager = new RecyclerAdapterCommandManager(_adapterWrapper);
		manager.onAttachedToRecyclerView();
		manager.pushBackOne(1);
		manager.pushBackOne(2);
		manager.pushBackOne(3);
		manager.pushBackOne(4);
		manager.pushBackOne(5);

		manager.commit();

		verify(_adapterWrapper, atLeastOnce()).notifyDataSetChanged();

		manager.remove(0);
		manager.insert(0, 1);

		verify(_adapterWrapper, times(2)).notifyDataSetChanged();
		verify(_adapterWrapper, never()).notifyItemMoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeInserted(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeRemoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemInserted(anyInt());
		verify(_adapterWrapper, never()).notifyItemRemoved(anyInt());
		verify(_adapterWrapper, never()).notifyItemChanged(anyInt());

		manager.remove(1);
		manager.insert(1, 2);

		verify(_adapterWrapper, times(2)).notifyDataSetChanged();
		verify(_adapterWrapper, never()).notifyItemMoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeInserted(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeRemoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemInserted(anyInt());
		verify(_adapterWrapper, never()).notifyItemRemoved(anyInt());
		verify(_adapterWrapper, never()).notifyItemChanged(anyInt());

		manager.remove(2);
		manager.insert(2, 3);

		verify(_adapterWrapper, times(2)).notifyDataSetChanged();
		verify(_adapterWrapper, never()).notifyItemMoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeInserted(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeRemoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemInserted(anyInt());
		verify(_adapterWrapper, never()).notifyItemRemoved(anyInt());
		verify(_adapterWrapper, never()).notifyItemChanged(anyInt());

		manager.remove(3);
		manager.insert(3, 4);

		verify(_adapterWrapper, times(2)).notifyDataSetChanged();
		verify(_adapterWrapper, never()).notifyItemMoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeInserted(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeRemoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemInserted(anyInt());
		verify(_adapterWrapper, never()).notifyItemRemoved(anyInt());
		verify(_adapterWrapper, never()).notifyItemChanged(anyInt());

		manager.remove(4);
		manager.insert(4, 5);

		verify(_adapterWrapper, times(2)).notifyDataSetChanged();
		verify(_adapterWrapper, never()).notifyItemMoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeInserted(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeRemoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemInserted(anyInt());
		verify(_adapterWrapper, never()).notifyItemRemoved(anyInt());
		verify(_adapterWrapper, never()).notifyItemChanged(anyInt());
	}

	@Test
	public void checkClean() {
		RecyclerAdapterCommandManager manager = new RecyclerAdapterCommandManager(_adapterWrapper);
		manager.onAttachedToRecyclerView();
		manager.pushBackOne(1);
		manager.pushBackOne(2);
		manager.pushBackOne(3);
		manager.pushBackOne(4);
		manager.pushBackOne(5);

		manager.commit();

		manager.reset();

		manager.pushBackOne(1);
		manager.pushBackOne(2);
		manager.pushBackOne(3);
		manager.pushBackOne(4);
		manager.pushBackOne(5);

		manager.commit();

		verify(_adapterWrapper, times(2)).notifyDataSetChanged();
		verify(_adapterWrapper, never()).notifyItemMoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeInserted(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeRemoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemInserted(anyInt());
		verify(_adapterWrapper, never()).notifyItemRemoved(anyInt());
		verify(_adapterWrapper, never()).notifyItemChanged(anyInt());
	}

	@Test
	public void checkChanges() {
		RecyclerAdapterCommandManager manager = new RecyclerAdapterCommandManager(_adapterWrapper);
		manager.onAttachedToRecyclerView();
		manager.pushBackOne(1);
		manager.pushBackOne(2);
		manager.pushBackOne(3);
		manager.pushBackOne(4);
		manager.pushBackOne(5);

		manager.commit();

		manager.reset();
		manager.pushBackOne(1);
		manager.pushBackOne(3);
		manager.pushBackOne(5);

		manager.commit();

		verify(_adapterWrapper, times(2)).notifyDataSetChanged();
		verify(_adapterWrapper, never()).notifyItemMoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeInserted(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeRemoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemInserted(anyInt());
		verify(_adapterWrapper, never()).notifyItemChanged(anyInt());
		verify(_adapterWrapper, times(1)).notifyItemRemoved(1);
		verify(_adapterWrapper, times(1)).notifyItemRemoved(2);
	}

	@Test
	public void checkChangesAddRemove() {
		RecyclerAdapterCommandManager manager = new RecyclerAdapterCommandManager(_adapterWrapper);
		manager.onAttachedToRecyclerView();
		manager.pushBackOne(1);
		manager.pushBackOne(2);
		manager.pushBackOne(3);
		manager.pushBackOne(4);
		manager.pushBackOne(5);

		manager.commit();

		manager.reset();
		manager.pushBackOne(1);
		manager.pushBackOne(3);
		manager.pushBackOne(6);
		manager.pushBackOne(5);

		manager.commit();

		verify(_adapterWrapper, times(2)).notifyDataSetChanged();
		verify(_adapterWrapper, never()).notifyItemMoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeInserted(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeRemoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemChanged(anyInt());
		verify(_adapterWrapper, times(1)).notifyItemRemoved(1);
		verify(_adapterWrapper, times(1)).notifyItemRemoved(2);
		verify(_adapterWrapper, times(1)).notifyItemInserted(2);
	}

	@Test
	public void checkChangesMove() {
		RecyclerAdapterCommandManager manager = new RecyclerAdapterCommandManager(_adapterWrapper);
		manager.onAttachedToRecyclerView();
		manager.pushBackOne(1);
		manager.pushBackOne(2);
		manager.pushBackOne(3);
		manager.pushBackOne(4);
		manager.pushBackOne(5);

		manager.commit();

		manager.reset();
		manager.pushBackOne(5);
		manager.pushBackOne(4);
		manager.pushBackOne(3);
		manager.pushBackOne(2);
		manager.pushBackOne(1);

		manager.commit();

		verify(_adapterWrapper, times(2)).notifyDataSetChanged();
		verify(_adapterWrapper, times(2)).notifyItemMoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeInserted(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemRangeRemoved(anyInt(), anyInt());
		verify(_adapterWrapper, never()).notifyItemInserted(anyInt());
		verify(_adapterWrapper, never()).notifyItemRemoved(anyInt());
		verify(_adapterWrapper, never()).notifyItemRemoved(anyInt());
		verify(_adapterWrapper, never()).notifyItemChanged(anyInt());
	}
}
