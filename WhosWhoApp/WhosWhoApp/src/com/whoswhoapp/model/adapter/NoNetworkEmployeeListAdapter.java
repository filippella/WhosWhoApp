package com.whoswhoapp.model.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whoswhoapp.R;
import com.whoswhoapp.model.model.Employee;
import com.whoswhoapp.model.utilities.Utils;

/**
 * Created by Filippo-TheAppExpert on 1/13/2015.
 */
public class NoNetworkEmployeeListAdapter extends ArrayAdapter<Employee> {

	private Context context = null;
	private List<Employee> employeeList = null;

	public NoNetworkEmployeeListAdapter(Context context,
			List<Employee> employeeList) {

		super(context, R.layout.list_row, employeeList);

		this.context = context;
		this.employeeList = employeeList;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(this.context).inflate(
					R.layout.list_row, parent, false);

			holder = new ViewHolder();

			holder.photo = (ImageView) convertView.findViewById(R.id.photo);
			holder.fullName = (TextView) convertView
					.findViewById(R.id.fullName);
			holder.department = (TextView) convertView
					.findViewById(R.id.department);
			holder.description = (TextView) convertView
					.findViewById(R.id.empDescription);
			holder.empNumber = (TextView) convertView
					.findViewById(R.id.empNumber);

			convertView.setTag(holder);
			convertView.setTag(R.id.photo, holder.photo);
			convertView.setTag(R.id.fullName, holder.fullName);
			convertView.setTag(R.id.department, holder.department);
			convertView.setTag(R.id.department, holder.description);
			convertView.setTag(R.id.empNumber, holder.empNumber);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Employee currentEmployee = employeeList.get(position);

		holder.empNumber.setText(Integer.toString(position + 1) + ". ");

		holder.photo.setImageBitmap(Utils.getCircleBitmap(currentEmployee
				.getPhoto()));

		holder.fullName.setText(currentEmployee.getFullName());
		holder.department.setText(currentEmployee.getDepartment());
		holder.description.setText(currentEmployee.getBiography());

		return convertView;
	}

	public static class ViewHolder {
		public ImageView photo;
		public TextView fullName, empNumber, department, description;

	}
}
