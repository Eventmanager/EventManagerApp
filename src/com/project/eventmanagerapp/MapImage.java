package com.project.eventmanagerapp;

import com.google.android.gms.maps.model.LatLng;

public class MapImage {
	private int width;
	private float rotation;
	private String imageName;
	private float latitude;
	private float longitude;
	
	public MapImage(int width, float rotation, String imageName, float latitude, float longitude) {
		this.width = width;
		this.rotation = rotation;
		this.imageName = imageName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public float getWidth() {
		return (float)width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	public LatLng getCoords(){
		return new LatLng(this.latitude,this.longitude);
	}
}
