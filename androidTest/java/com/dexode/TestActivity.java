package com.dexode;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dexode.adapter.RecyclerAdapter;
import com.dexode.adapter.holder.BaseHolder;
import com.dexode.util.Utils;
import com.esportlivescore.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dawid Drozd aka Gelldur on 15.04.16.
 */
public class TestActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		setTheme(R.style.ESportLiveScore);
		super.onCreate(savedInstanceState);

		listView = new RecyclerView(this);
		listView.setItemAnimator(new DefaultItemAnimator());
		listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		setContentView(listView);

		adapter = new TestAdapterCommandDrived(this);

		listView.setAdapter(adapter);
		//		listView.postDelayed(new Runnable() {
		//			@Override
		//			public void run() {
		//
		//				adapter.items.remove(1);
		//				adapter.items.add(4, 20000);
		//				adapter.items.add(5, 30033);
		//
		//				adapter.notifyItemRemoved(1);
		//
		//				adapter.notifyItemChanged(4);
		//				adapter.notifyItemChanged(5);
		//			}
		//		}, 4000);
	}

	public TestAdapterCommandDrived adapter;
	public RecyclerView listView;

	public static class TestAdapterCommandDrived extends RecyclerAdapter {
		public TestAdapterCommandDrived(final Activity activity) {
			super(activity);
			for (int i = 1; i < 100; ++i) {
				addElement(new Element(0, i));
			}
			commitChanges();
		}

		public int getPositionInElements(int label) {
			final ArrayList<Element> elements = getElements();
			for (int i = 0; i < elements.size(); ++i) {
				if (elements.get(i).data.equals(label)) {
					return i;
				}
			}
			throw new RuntimeException("Label not found: " + label);
		}

		@Override
		public BaseHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

			final TextView textView = new TextView(parent.getContext());
			textView.setGravity(Gravity.CENTER_VERTICAL);
			textView.setPadding((int) Utils.convertDpToPixel(parent.getContext(), 5), 0, 0, 0);
			textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
																   (int) Utils.convertDpToPixel(parent.getContext(),
																								15)));

			return new BaseHolder(textView) {
				@Override
				public void setData(final Element element) {
					textView.setText(String.valueOf(element.data));
					textView.setTag(element.data);
					textView.setBackgroundColor(new Random((Integer) element.data).nextInt());
				}
			};
		}
	}

	public static class TestViewHolder extends RecyclerView.ViewHolder {

		public TestViewHolder(final View itemView) {
			super(itemView);
			_textView = (TextView) itemView;
			_textView.setClickable(false);
		}

		public void bind(final int position, final Integer integer) {
			_textView.setText(String.valueOf(integer));
			_textView.setBackgroundColor(new Random(integer).nextInt());
		}

		private TextView _textView;
	}

	public static class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {

		public TestAdapter() {
			for (int i = 1; i < 100; ++i) {
				items.add(i);
			}
		}

		@Override
		public TestViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

			TextView textView = new TextView(parent.getContext());
			textView.setGravity(Gravity.CENTER_VERTICAL);
			textView.setPadding(20, 0, 0, 0);
			textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
			return new TestViewHolder(textView);
		}

		@Override
		public void onBindViewHolder(final TestViewHolder holder, final int position) {
			holder.bind(position, items.get(position));
		}

		@Override
		public int getItemCount() {
			return items.size();
		}

		public ArrayList<Integer> items = new ArrayList<>(128);
	}
}
