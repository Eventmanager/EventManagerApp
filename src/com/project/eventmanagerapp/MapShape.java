package com.project.eventmanagerapp;

import android.graphics.Color;

public class MapShape {
	private int width;
	private float[][] points;
	private int color;
	private int stroke;
	
	public MapShape(int width, float[][] points, int color2, int stroke2) {
		this.width = width;
		this.points = points;
		this.color = color2;
		this.stroke = stroke2;
	}
	
	public float getWidth() {
		return (float)this.width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public float[][] getPoints() {
		return points;
	}
	public void setPoints(float[][] points) {
		this.points = points;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getStroke() {
		return stroke;
	}
	public void setStroke(int stroke) {
		this.stroke = stroke;
	}
	
	public String toString(){
		String returnString = "width: " + width;
		
		for(float[] f : points){
			returnString += " {" + f[0] + ", " + f[1] + "}";
		}
		returnString += " color: " + color;
		returnString += " stroke: " + stroke;
		
		return returnString;
	}
}
