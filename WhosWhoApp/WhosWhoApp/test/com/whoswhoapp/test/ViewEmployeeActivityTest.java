package com.whoswhoapp.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;

import com.whoswhoapp.R;
import com.whoswhoapp.view.MainActivity;

@RunWith(RobolectricTestRunner.class)
public class ViewEmployeeActivityTest {

	Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();

	@Test
	public void test() throws Exception {

		
		
		
		String hello = new MainActivity().getResources().getString(R.string.viewTeam);
		
		String expected = "Hello";

		String actual = "Hello";

		assertThat(hello, equalTo("Click Here to view the Team"));

	}
}