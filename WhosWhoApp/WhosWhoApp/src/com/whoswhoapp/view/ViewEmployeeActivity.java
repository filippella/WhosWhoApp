package com.whoswhoapp.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.whoswhoapp.R;
import com.whoswhoapp.model.model.Employee;

/**
 * Created by Filippo-TheAppExpert on 1/13/2015.
 */
public class ViewEmployeeActivity extends Activity {

	private Employee employee = null;
	private TextView empName, empTitle, empBio, empPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_employee);

		employee = (Employee) getIntent().getExtras().getSerializable(
				"selectedEmployee");

		setUpViews();

		empName.setText(employee.getFullName());
		empTitle.setText(employee.getDepartment());
		empBio.setText(employee.getBiography());
		empPhoto.setText(employee.getPhotoURL());

	}

	private void setUpViews() {
		empName = (TextView) this.findViewById(R.id.nameTxt);
		empTitle = (TextView) this.findViewById(R.id.titleTxt);
		empBio = (TextView) this.findViewById(R.id.BioTxt);
		empPhoto = (TextView) this.findViewById(R.id.photoTxt);
	}

}
