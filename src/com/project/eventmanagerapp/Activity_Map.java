package com.project.eventmanagerapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import android.os.AsyncTask;
import android.os.Bundle;

public class Activity_Map extends FragmentActivity {

	private GoogleMap eventMap;
	ArrayList<MapImage> mapImages = new ArrayList<MapImage>();
	ArrayList<MapShape> mapShapes = new ArrayList<MapShape>();
	final Context context = this;
	ArrayList<Polygon> shapeList = new ArrayList<Polygon>();
	ArrayList<GroundOverlay> imageList = new ArrayList<GroundOverlay>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ArrayList<ArrayList> temp = MapManager.getInstance(context).getMapItems();
        mapImages = temp.get(0);
        mapShapes = temp.get(1);
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
    	eventMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.967978, 4.515135), 15f));
   
    	for(MapShape s: mapShapes)
    	{
    		shapeList.add(new ShapeBuilder().points(s.getPoints()).width(s.getWidth()).fill(s.getColor()).stroke(s.getStroke()).build());
    	}
    	
    	for(MapImage i: mapImages)
    	{
    		new DrawImageTask().execute(i);
    	}
    	
    }
    
    
    /**
     * Creates an image on the map, requires a position (latlng object), width and the image as a string. Width in meters.
     * You can also supply a rotation, a float value describing the amount of degrees, clockwise.
     */
    private class ImageBuilder{
    	
    	LatLng position;
    	Float width = null;
    	Float rotation = null;
    	Bitmap image = null;
    	
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
    	
    	public ImageBuilder image(Bitmap image){
			this.image= image;
    		return this;
    	}
    	
    	public GroundOverlay build(){
    		GroundOverlayOptions im = new GroundOverlayOptions();
    		
    		if(this.image != null)
    		{
    			//im.image(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(this.image, "drawable", getPackageName())));

				im.image(BitmapDescriptorFactory.fromBitmap(image));
				
    			
    		}
            
    		if(this.position != null && this.width != null)
            	im.position(this.position, this.width);
        	
    		if(rotation != null)
        		im.bearing(this.rotation);
    		
    		im.zIndex(5f);
        	
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
    		
    		poly.zIndex(3f);
    		
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
    
    class DrawImageTask extends AsyncTask<MapImage, int[], MapImage> {

    	Bitmap imagery;
        @Override
        protected MapImage doInBackground(MapImage... params) {
          Bitmap bitmap = null;

          try {
			bitmap = BitmapFactory.decodeStream(new URL(params[0].getImageName()).openConnection().getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          this.imagery = bitmap;
          return params[0];
        }

        @Override
        protected void onPostExecute(MapImage result) {
          super.onPostExecute(result);
          //return bitmapResult;
          imageList.add(new ImageBuilder().position(result.getCoords()).width(result.getWidth()).image(imagery).rotation(result.getRotation()).build());
        }
    }
     
}
