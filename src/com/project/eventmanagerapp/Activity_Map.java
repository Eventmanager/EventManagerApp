package com.project.eventmanagerapp;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

public class Activity_Map extends FragmentActivity {

	private GoogleMap eventMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (eventMap == null) {
            // Try to obtain the map from the SupportMapFragment.
        	eventMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (eventMap != null) {
                setUpMap();
            }
        }
    }
    
    private void setUpMap() {
    	eventMap.addMarker(new MarkerOptions().position(new LatLng(51.9167, 4.5000)).title("Rotterdam"));
    	eventMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.9167, 4.5000), 12.0f));
    	
    	//Example code for shapes. In this case a triangle near the marker.
    	/*Polygon poly1 = new ShapeBuilder().points(new float[][]{{51.9167f,4.5000f},{51.9177f,4.5006f},{51.9157f,4.5090f}}).build();
    	Polygon poly2 = new ShapeBuilder().points(new float[][]{{51.9187f,4.5000f},{51.9197f,4.5006f},{51.9177f,4.5090f}})
    			.width(5f).fill(Color.RED).stroke(Color.BLUE).build();*/
    	
    	//Example code for an image. In this case near the marker.
    	/*GroundOverlay image1 = new ImageBuilder().position(new LatLng(51.9167, 4.4980)).width(2000f).image("house").build();*/
    	
    	//Example code for drawing a semi-transparent circle.
    	Circle circle1 = new CircleBuilder().position(new LatLng(51.9167, 4.4980)).radius(3000).stroke(Color.GREEN).fill(Color.argb(50,200,0,0)).strokeWidth(3f).build();
    }
    
    
    /**
     * Creates an image on the map, requires a position (latlng object), width and the image as a string. Width in meters.
     * You can also supply a rotation, a float value describing the amount of degrees, clockwise.
     */
    private class ImageBuilder{
    	
    	LatLng position;
    	Float width = null;
    	Float rotation = null;
    	String image = null;
    	
    	public ImageBuilder(){}
    	
    	public ImageBuilder position(LatLng pos){
    		this.position = pos;
    		return this;
    	}
    	
    	public ImageBuilder width(Float w){
    		this.width = w;
    		return this;
    	}
    	
    	public ImageBuilder rotation(Float rotation){
    		this.rotation = rotation;
    		return this;
    	}
    	
    	public ImageBuilder image(String image){
    		this.image= image;
    		return this;
    	}
    	
    	public GroundOverlay build(){
    		GroundOverlayOptions im = new GroundOverlayOptions();
    		
    		if(this.image != null)
    			im.image(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(this.image, "drawable", getPackageName())));
            
    		if(this.position != null && this.width != null)
            	im.position(this.position, this.width);
        	
    		if(rotation != null)
        		im.bearing(this.rotation);
        	
        	return eventMap.addGroundOverlay(im);
    	}
    }
    
    /**
     * Class to create a polygon shape with a set colour as fill and/or stroke and a set of points as floats.
     */
    private class ShapeBuilder{
    	

    	Float width = null;
    	Integer fill = null;
    	Integer stroke = null;
    	float[][] points;
    	
    	public ShapeBuilder(){}
    	
    	public ShapeBuilder stroke(Integer stroke){
    		this.stroke = stroke;
    		return this;
    	}
    	
    	public ShapeBuilder fill(Integer fill){
    		this.fill = fill;
    		return this;
    	}
    	public ShapeBuilder width(Float size){
        	this.width = size;
        	return this;
    	}
    	public ShapeBuilder points(float[][] points){
    		this.points = points;
    		return this;
    	}
    	public Polygon build(){
    		PolygonOptions poly = new PolygonOptions();
    		for(float[] point : this.points)
        		poly.add(new LatLng(point[0],point[1]));
    		
    		if(this.fill != null)
        		poly.fillColor(this.fill);
    		if(this.width != null)
        		poly.strokeWidth(this.width);
    		if(this.stroke != null)
        		poly.strokeColor(this.stroke);
    		
    		return eventMap.addPolygon(poly);
    	}
    	
    }
    
    private class CircleBuilder{
    	

    	Float strokeWidth = null;
    	Integer fill = null;
    	Integer stroke = null;
    	int radius = 0;
    	LatLng position;
    	
    	public CircleBuilder(){}
    	
    	public CircleBuilder stroke(Integer stroke){ // Colour of the stroke around the circle.
    		this.stroke = stroke;
    		return this;
    	}
    	
    	public CircleBuilder fill(Integer fill){ // Fill colour of the circle.
    		this.fill = fill;
    		return this;
    	}
    	public CircleBuilder strokeWidth(Float size){ // Width of the stroke around the circle.
        	this.strokeWidth = size;
        	return this;
    	}
    	public CircleBuilder position(LatLng pos){ // Center of the circle.
    		this.position = pos;
    		return this;
    	}
    	public CircleBuilder radius(int m){ // Radius of the circle.
    		this.radius = m;
    		return this;
    	}
    	public Circle build(){ // Create the circle, draw it on the map and return it as a Circle.
    		CircleOptions circle = new CircleOptions();
    		circle.center(position);
    		circle.radius(radius);
    		
    		if(this.fill != null)
    			circle.fillColor(this.fill);
    		if(this.strokeWidth != null)
    			circle.strokeWidth(this.strokeWidth);
    		if(this.stroke != null)
    			circle.strokeColor(this.stroke);
    		
    		return eventMap.addCircle(circle);
    	}
    	
    }
     
}
