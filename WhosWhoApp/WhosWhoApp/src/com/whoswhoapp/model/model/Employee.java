package com.whoswhoapp.model.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Filippo-TheAppExpert on 1/12/2015.
 */
public class Employee implements Serializable {

	private static final long serialVersionUID = 111696345129311948L;
	public byte[] imageByteArray;

	private String empID = "";
	private String fullName = "";
	private String department = "";
	private String biography = "";
	private String photoURL = "";
	private Bitmap photo = null;

	public String getEmpID() {
		return empID;
	}

	public void setEmpID(String empID) {
		this.empID = empID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {

		out.writeObject(empID);
		out.writeObject(fullName);
		out.writeObject(department);
		out.writeObject(biography);
		out.writeObject(photoURL);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
		byte bitmapBytes[] = byteStream.toByteArray();
		out.write(bitmapBytes, 0, bitmapBytes.length);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {

		empID = (String) in.readObject();
		fullName = (String) in.readObject();
		department = (String) in.readObject();
		biography = (String) in.readObject();
		photoURL = (String) in.readObject();

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int b;
		while ((b = in.read()) != -1)
			byteStream.write(b);
		byte bitmapBytes[] = byteStream.toByteArray();
		photo = BitmapFactory.decodeByteArray(bitmapBytes, 0,
				bitmapBytes.length);
	}

}
