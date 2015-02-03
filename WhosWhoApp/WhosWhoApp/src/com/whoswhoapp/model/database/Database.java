package com.whoswhoapp.model.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.whoswhoapp.model.model.Employee;
import com.whoswhoapp.model.utilities.Utils;

public class Database extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "team.db";
	private static final String TABLE_NAME = "employee";
	private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase database = null;
	private static Employee employee = null;
	private static ArrayList<Employee> empList = new ArrayList<Employee>();

	private static final String CREATE_EMPLOYEE_TABLE = "CREATE TABLE IF NOT EXISTS `"
			+ TABLE_NAME
			+ "` (`photo` mediumblob,"
			+ "`photoURL` varchar(500) DEFAULT NULL,"
			+ "`fullName` varchar(500) DEFAULT NULL,"
			+ "`department` varchar(500) DEFAULT NULL,"
			+ "`biography` TEXT DEFAULT NULL,"
			+ "`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
			+ "PRIMARY KEY (photoURL, fullName,department,biography));";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.database = this.getWritableDatabase();
	}

	public void onCreate(SQLiteDatabase db) {

		try {
			db.execSQL(CREATE_EMPLOYEE_TABLE);
		} catch (SQLException msg) {
			Log.d("SQL ERROR", msg.getMessage());
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	public void saveEmployee(Employee employee) {

		ContentValues cv = new ContentValues();

		cv.put("photo", Utils.getBytes(employee.getPhoto()));

		cv.put("photoURL", employee.getPhotoURL());
		cv.put("fullName", employee.getFullName());
		cv.put("department", employee.getDepartment());
		cv.put("biography", employee.getBiography());

		try {
			database.insert(TABLE_NAME, null, cv);
		} catch (SQLException e) {
		}

	}

	public ArrayList<Employee> getDetails() throws Exception {

		Cursor cur = database.query(true, "employee", new String[] { "photo",
				"photoURL", "fullName", "department", "biography" }, null,
				null, null, null, null, null);

		byte[] blob = null;
		String fullName, photoURL, department, description;

		if (!empList.isEmpty()) {
			empList.clear();
		}

		if (cur.getCount() > 0) {
			cur.moveToPosition(-1);
			while (cur.moveToNext()) {

				blob = cur.getBlob(cur.getColumnIndex("photo"));

				photoURL = cur.getString(cur.getColumnIndex("photoURL"));
				fullName = cur.getString(cur.getColumnIndex("fullName"));

				department = cur.getString(cur.getColumnIndex("department"));

				description = cur.getString(cur.getColumnIndex("biography"));

				employee = new Employee();

				employee.setFullName(fullName);
				employee.setPhotoURL(photoURL);
				employee.setDepartment(department);
				employee.setBiography(description);
				employee.setPhoto(Utils.getPhoto(blob));

				empList.add(employee);
			}
			cur.close();
			return empList;
		}
		cur.close();

		Log.d("DB Status", "Here 2");

		return null;
	}

	public void dropTable() {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}

}