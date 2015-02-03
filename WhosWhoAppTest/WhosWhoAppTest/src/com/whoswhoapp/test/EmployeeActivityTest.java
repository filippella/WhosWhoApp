package com.whoswhoapp.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.whoswhoapp.R;
import com.whoswhoapp.model.model.Employee;
import com.whoswhoapp.model.utilities.Utils;
import com.whoswhoapp.view.MainActivity;

public class EmployeeActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;

	private ListView mListView;

	private Button reload = null;
	private Employee employee = null;

	private CountDownLatch signal = null;

	public EmployeeActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mActivity = getActivity();
		signal = new CountDownLatch(1);

		mListView = (ListView) mActivity.findViewById(R.id.listView);
		reload = (Button) mActivity.findViewById(R.id.reloadBtn);

	}

	@SmallTest
	public void testNetworkAvailability() throws Exception {

		boolean expected = true;

		boolean actual = Utils.isNetworkAvailable(mActivity);

		assertEquals(expected, actual);

	}

	@LargeTest
	public void testEmployeeInfo() throws Throwable {

		if (Utils.isNetworkAvailable(mActivity)) {

			signal.await(5, TimeUnit.SECONDS);

			String expected = "Daniel Joseph";

			employee = (Employee) mListView.getAdapter().getItem(0);

			String actual = employee.getFullName();

			assertEquals(expected, actual);

			int expectedVal = 47;

			int actualVal = mListView.getAdapter().getCount();

			assertEquals(expectedVal, actualVal);
		}

	}

}
