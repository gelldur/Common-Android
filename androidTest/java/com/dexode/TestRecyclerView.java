package com.dexode;

import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.dexode.adapter.RecyclerAdapter;

import java.util.Random;

/**
 * Created by Dawid Drozd aka Gelldur on 15.04.16.
 */
public class TestRecyclerView extends ActivityInstrumentationTestCase2<TestActivity> {

	public TestRecyclerView() {
		super(TestActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		_activity = getActivity();
		SystemClock.sleep(1500);//Initial sleep
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SystemClock.sleep(4000);
	}

	public void testSimpleRemove() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				_activity.adapter.remove(1);
				_activity.adapter.commitChanges();
			}
		});
		checkAdapter();
	}

	public void testSimpleRemoveAdd() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				_activity.adapter.remove(1);
				_activity.adapter.remove(3);
				_activity.adapter.insertElement(3, new RecyclerAdapter.Element(0, 99999));
				_activity.adapter.commitChanges();
			}
		});
		checkAdapter();
	}

	public void testCase1() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				//We should see 1,4,5,7,8 //after removing
				//We should see 1,4,5,99998,99999,7,8 //after adding
				_activity.adapter.remove(1);
				_activity.adapter.remove(1);
				_activity.adapter.remove(3);
				_activity.adapter.insertElement(3, new RecyclerAdapter.Element(0, 99999));
				_activity.adapter.insertElement(3, new RecyclerAdapter.Element(0, 99998));
				_activity.adapter.commitChanges();
			}
		});
		checkAdapter();
	}

	public void testCaseClean() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				_activity.adapter.removeAllByType(0);//We have empty adapter
				_activity.adapter.commitChanges();
			}
		});
		checkAdapter();
	}

	public void testCase2() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				_activity.adapter.removeAllByType(0);//We have empty adapter
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 1));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 4));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 5));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 99999));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 99991));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 7));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 8));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 9));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 10));

				_activity.adapter.commitChanges();
			}
		});
		checkAdapter();
	}

	public void testCase3() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				_activity.adapter.removeAllByType(0);//We have empty adapter
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 1));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 2));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 3));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 99999));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 99991));

				_activity.adapter.commitChanges();
			}
		});
		checkAdapter();
	}

	public void testSwap() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				//Move test (swap 3 and 4)
				_activity.adapter.removeAllByType(0);//We have empty adapter
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 1));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 2));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 4));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 3));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 5));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 6));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 7));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 8));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 9));
				for (int i = 10; i < 100; ++i) {
					_activity.adapter.addElement(new RecyclerAdapter.Element(0, i));
				}

				_activity.adapter.commitChanges();
			}
		});
		checkAdapter();
	}

	public void testSwap2() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				//Move test (swap 3 and 4)
				_activity.adapter.removeAllByType(0);//We have empty adapter
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 2));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 1));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 4));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 3));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 5));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 6));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 7));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 8));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 9));
				for (int i = 10; i < 100; ++i) {
					_activity.adapter.addElement(new RecyclerAdapter.Element(0, i));
				}

				_activity.adapter.commitChanges();
			}
		});
		checkAdapter();
	}

	public void testCase4() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				//Move test (swap 3 and 4)
				_activity.adapter.removeAllByType(0);//We have empty adapter
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 2));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 1));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 3));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 10001));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 10002));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 10003));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 10004));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 6));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 7));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 8));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 9));
				for (int i = 10; i < 100; ++i) {
					_activity.adapter.addElement(new RecyclerAdapter.Element(0, i));
				}

				_activity.adapter.commitChanges();
			}
		});
		checkAdapter();
	}

	public void testFoundBug() throws Throwable {

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				_activity.adapter.removeAllByType(0);//We have empty adapter
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 209));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 208));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 207));

				_activity.adapter.commitChanges();
			}
		});

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				_activity.adapter.removeAllByType(0);//We have empty adapter

				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 214));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 209));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 213));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 215));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 212));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 208));
				_activity.adapter.addElement(new RecyclerAdapter.Element(0, 211));

				_activity.adapter.commitChanges();
			}
		});
		checkAdapter();
	}

	int idMax;

	//Random test for more checks :). Simply rename to enable
	public void atest1RandomFunnyTest_LoL() throws Throwable {
		idMax = _activity.adapter.getItemCount() + 100;
		long seed = 213128;
		final Random random = new Random(seed);

		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				_activity.adapter.removeAllByType(0);
				_activity.adapter.commitChanges();
			}
		});

		for (int i = 0; i < 1000; ++i) {
			getInstrumentation().runOnMainSync(new Runnable() {
				@Override
				public void run() {

					int operationsCount = random.nextInt(20);

					for (int j = 0; j < operationsCount; ++j) {
						int operation = random.nextInt(7);
						if (operation == 0) {
							_activity.adapter.addElement(new RecyclerAdapter.Element(0, ++idMax));
						} else if (operation == 1) {
							_activity.adapter.insertElement(inBoundsRandom(random),
															new RecyclerAdapter.Element(0, ++idMax));
						} else if (operation == 2 && _activity.adapter.getItemCount() != 0) {
							_activity.adapter.remove(inBoundsRandom(random));
						} else if (operation == 3 && _activity.adapter.getItemCount() != 0) {
							final int start = inBoundsRandom(random);
							int count = random.nextInt((int) ((_activity.adapter.getItemCount() - start) * 0.2) + 1);
							for (int z = 0; z < count; ++z) {
								_activity.adapter.remove(start);
							}
						} else if (operation == 4) {
							final int start = inBoundsRandom(random);
							int count = random.nextInt((int) ((_activity.adapter.getItemCount() - start) * 0.3) + 1);
							for (int z = 0; z < count; ++z) {
								_activity.adapter.insertElement(start, new RecyclerAdapter.Element(0, ++idMax));
							}
						}
					}

					if (_activity.adapter.getItemCount() > 100 && random.nextBoolean()) {
						_activity.adapter.removeAllByType(0);
					}
					_activity.adapter.commitChanges();
				}

			});

			checkAdapter();
		}
	}

	private int inBoundsRandom(final Random random) {
		if (_activity.adapter.getItemCount() == 0) {
			return 0;
		}
		return random.nextInt(_activity.adapter.getItemCount());
	}

	private void checkAdapter() throws Throwable {
		System.out.println("Checking");
		SystemClock.sleep(2000);
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {

				if (_activity.adapter.getItemCount() == 0) {
					assertEquals(_activity.listView.getChildCount(), 0);
				}

				for (int i = 0; i < _activity.listView.getChildCount(); ++i) {
					final TextView childAt = (TextView) _activity.listView.getChildAt(i);
					final int childAdapterPosition = _activity.listView.getChildAdapterPosition(childAt);

					int label = Integer.valueOf(childAt.getText().toString());
					int labelPosition = _activity.adapter.getPositionInElements(label);

					assertEquals(labelPosition, childAdapterPosition);
				}
			}
		});
	}

	private TestActivity _activity;
}
