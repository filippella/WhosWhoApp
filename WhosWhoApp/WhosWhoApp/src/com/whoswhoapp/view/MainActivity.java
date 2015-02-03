package com.whoswhoapp.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.whoswhoapp.R;
import com.whoswhoapp.model.adapter.EmployeeListAdapter;
import com.whoswhoapp.model.adapter.NoNetworkEmployeeListAdapter;
import com.whoswhoapp.model.database.Database;
import com.whoswhoapp.model.interfaces.EmployeeInfoListener;
import com.whoswhoapp.model.model.Employee;
import com.whoswhoapp.model.utilities.Utils;
import com.whoswhoapp.tasks.DownloadEmployeeInfoTask;

/**
 * Created by Filippo-TheAppExpert on 1/13/2015.
 */
public class MainActivity extends Activity implements EmployeeInfoListener {

	private List<Employee> employeeList = new ArrayList<Employee>();
	private ListView employeeListView = null;
	private ArrayAdapter<Employee> adapter = null;
	private Button reload = null;
	private Database db = null;
	private DownloadEmployeeInfoTask downloadEmployeeInfoTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_list);

		init();

		start();
	}

	private void init() {

		this.db = new Database(MainActivity.this);

		employeeListView = (ListView) this.findViewById(R.id.listView);
		reload = (Button) this.findViewById(R.id.reloadBtn);

		employeeListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						try {
							Employee selectedEmployee = (Employee) employeeListView
									.getAdapter().getItem(position);

							Intent clickedEmployee = new Intent(
									MainActivity.this,
									ViewEmployeeActivity.class);
							clickedEmployee.putExtra("selectedEmployee",
									selectedEmployee);
							startActivity(clickedEmployee);
						} catch (Exception e) {
							Toast.makeText(
									getApplicationContext(),
									"Cannot Display Activity! "
											+ e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}

					}
				});

		reload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				start();
			}
		});
	}

	private void start() {

		if (Utils.isNetworkAvailable(MainActivity.this)) {

			clearProcess();

			adapter = new EmployeeListAdapter(MainActivity.this,
					employeeList);

			employeeListView.setAdapter(adapter);

			downloadEmployeeInfoTask = new DownloadEmployeeInfoTask(
					MainActivity.this, this);
			downloadEmployeeInfoTask.execute(Utils.BASE_URL);
		} else {
			Toast.makeText(getApplicationContext(),
					"There is no Network available!", Toast.LENGTH_SHORT)
					.show();
			Toast.makeText(getApplicationContext(),
					"Fetching Data from Database!", Toast.LENGTH_SHORT).show();

			clearProcess();

			try {
				employeeList = db.getDetails();

				adapter = new NoNetworkEmployeeListAdapter(
						MainActivity.this, employeeList);
				employeeListView.setAdapter(adapter);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void clearProcess() {

		if (!employeeList.isEmpty()) {
			employeeList.clear();
			adapter = null;
		}
	}

	@Override
	public void showEmployee(Employee employee) {

		employeeList.add(employee);
		adapter.notifyDataSetChanged();

	}

}
