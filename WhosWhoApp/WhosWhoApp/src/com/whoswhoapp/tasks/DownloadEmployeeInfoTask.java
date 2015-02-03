package com.whoswhoapp.tasks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.whoswhoapp.model.interfaces.EmployeeInfoListener;
import com.whoswhoapp.model.model.Employee;
import com.whoswhoapp.model.utilities.Utils;

public class DownloadEmployeeInfoTask extends
		AsyncTask<String, Employee, String> {

	private Context context = null;
	private Employee newEmployee = null;
	private ProgressDialog mProgressDialog = null;
	private EmployeeInfoListener employeeInfoListener = null;
	private int checkForPublish = 0;

	public DownloadEmployeeInfoTask(Context context,
			EmployeeInfoListener employeeInfoListener) {
		this.context = context;
		this.employeeInfoListener = employeeInfoListener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		mProgressDialog = new ProgressDialog(this.context);
		mProgressDialog.setTitle("Please wait...");
		mProgressDialog.setMessage("Downloading Team-Info");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.show();
	}

	@Override
	protected String doInBackground(String... urls) {

		String page = "";
		try {

			page = Utils.getPage(urls[0]);

			Document document = Jsoup.parse(page);
			Elements users = document.select("#users .wrapper .row");

			int count = 0;
			int tempCount = 1;

			for (Element element : users) {

				Elements singleRow = element.getAllElements();

				for (Element single : singleRow) {

					Elements singleUser = single.getAllElements();

					for (Element user : singleUser) {

						Tag tag = single.tag();

						if (tag.getName().equalsIgnoreCase("img")) {
							String imgURL = user.attr("src");
							newEmployee.setPhotoURL(imgURL);
						} else if (tag.getName().equalsIgnoreCase("h3")) {
							String fullName = user.text();
							newEmployee.setFullName(fullName);
						} else if (tag.getName().equalsIgnoreCase("p")) {

							if (single.hasClass("user-description")) {
								String description = user.text();
								newEmployee.setBiography(description);
								count++;
							} else {
								String department = user.text();
								newEmployee.setDepartment(department);
							}
						}
					}

					if (count != tempCount) {
						tempCount = count;
						if (checkForPublish > 0) {
							publishProgress(newEmployee);
						}
						newEmployee = new Employee();
						checkForPublish++;
					}
				}

			}

		} catch (Exception e) {
		}
		return page;
	}

	@Override
	protected void onProgressUpdate(Employee... values) {
		super.onProgressUpdate(values);
		this.employeeInfoListener.showEmployee(values[0]);
	}

	@Override
	protected void onPostExecute(String result) {

		mProgressDialog.dismiss();
	}
}
