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
    	createShape(new float[][]{{51.9167f,4.5000f},{51.9177f,4.5006f},{51.9157f,4.5090f}});
    	createShape(new float[][]{{51.9177f,4.5000f},{51.9187f,4.5006f},{51.9167f,4.5090f}},Color.BLUE,Color.RED,5.0f);
    	
    	//Example code for an image. In this case near the marker.
    	createImage(new LatLng(51.9167, 4.4980),2000f,"house",0f);
    }
    
    /**
     * Creates a polygon shape with a set colour as fill and/or stroke and a set of points as floats.
     * If you don't supply a fill and stroke, it will default to black for stroke and transparent for fill.
     */
    private Polygon createShape(float[][] points){
    	return createShape(points,null,null,null);
    }
    private Polygon createShape(float[][] points, Integer stroke, Integer fill,Float size) {
    	
    	PolygonOptions poly = new PolygonOptions();
    	if(stroke != null)
    		poly.strokeColor(stroke);
    	if(fill != null)
    		poly.fillColor(fill);
    	if(size != null)
    		poly.strokeWidth(size);
    	
    	for(float[] point : points)
    		poly.add(new LatLng(point[0],point[1]));
    	
    	return eventMap.addPolygon(poly);
    }
    
    /**
     * Creates an image on the map, requires a position (latlng object), width and the image as a string. Width and height in meters.
     * You can also supply a rotation, a float value describing the amount of degrees, clockwise.
     */
    private GroundOverlay createImage(LatLng pos,float width,String im,float rotation){
    	
    	GroundOverlayOptions image = new GroundOverlayOptions()
        .image(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(im, "drawable", getPackageName())))
        .position(pos, width);
    	if(rotation != 0f)
        image.bearing(rotation);
    	
    	return eventMap.addGroundOverlay(image);
    	
    }
     
}