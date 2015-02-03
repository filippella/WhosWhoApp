package com.whoswhoapp.model.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whoswhoapp.R;
import com.whoswhoapp.model.database.Database;
import com.whoswhoapp.model.model.Employee;
import com.whoswhoapp.model.utilities.Utils;

/**
 * Created by Filippo-TheAppExpert on 1/13/2015.
 */
public class EmployeeListAdapter extends ArrayAdapter<Employee> {

	private Context context = null;
	private List<Employee> employeeList = null;
	private Database db = null;

	private Map<String, SoftReference<Bitmap>> cache = null;

	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	private ExecutorService pool = null;

	public EmployeeListAdapter(Context context, List<Employee> employeeList) {

		super(context, R.layout.list_row, employeeList);

		this.context = context;
		this.employeeList = employeeList;
		this.db = new Database(context);

		this.db.dropTable();

		this.cache = new HashMap<String, SoftReference<Bitmap>>();

		this.pool = Executors.newFixedThreadPool(5);

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

		try {
			new ImageLoadTask(holder.photo).execute(currentEmployee);
		} catch (Exception e) {
		}

		holder.fullName.setText(currentEmployee.getFullName());
		holder.department.setText(currentEmployee.getDepartment());
		holder.description.setText(currentEmployee.getBiography());

		return convertView;
	}

	public static class ViewHolder {
		public ImageView photo;
		public TextView fullName, empNumber, department, description;

	}

	private class ImageLoadTask extends AsyncTask<Employee, Employee, Employee> {

		private Bitmap bitmap;

		private Employee employee = null;

		private ImageView bmImage = null;

		public ImageLoadTask(ImageView bmImage) {

			this.bmImage = bmImage;
		}

		public Bitmap getBitmapFromCache(Employee employee) {

			if (cache.containsKey(employee.getPhotoURL())) {
				return cache.get(employee.getPhotoURL()).get();
			}
			return null;
		}

		@Override
		protected Employee doInBackground(Employee... params) {

			employee = params[0];

			imageViews.put(bmImage, employee.getPhotoURL());

			bitmap = getBitmapFromCache(employee);

			if (bitmap != null) {

				employee.setPhoto(bitmap);

				publishProgress(employee);

			} else {

				pool.submit(new Runnable() {
					@Override
					public void run() {

						Bitmap bmp = downloadBitmap(employee.getPhotoURL(),
								150, 150);

						employee.setPhoto(bmp);

						publishProgress(employee);

					}
				});

			}

			return employee;
		}

		@Override
		protected void onProgressUpdate(Employee... values) {

			super.onProgressUpdate(values);

			String tag = imageViews.get(bmImage);

			if (tag != null && tag.equals(values[0].getPhotoURL())) {
				if (values[0].getPhoto() != null) {
					bmImage.setImageBitmap(Utils.getCircleBitmap(values[0]
							.getPhoto()));
				} else {
					bmImage.setImageDrawable(context.getResources()
							.getDrawable(R.drawable.ic_launcher));
					Log.d(null, "fail " + values[0].getPhotoURL());
				}
			}

		}

		private Bitmap downloadBitmap(String url, int width, int height) {

			try {

				Bitmap bitmap = BitmapFactory
						.decodeStream((InputStream) new URL(

						url).getContent());

				bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

				cache.put(url, new SoftReference<Bitmap>(bitmap));

				return bitmap;

			} catch (MalformedURLException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}
			return null;

		}

		@Override
		protected void onPostExecute(Employee result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				db.saveEmployee(result);
			} catch (Exception e) {
			}

		}

	}
}
