package com.project.eventmanagerapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Planning extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planning);
		
	    LinearLayout mainlinear= (LinearLayout) findViewById(R.id.planninglayout);
	    
	    String[][] planninginfo = new String[][]{{"Begin van dit stuk","Nog wat van dit","En tot slot dit en dan eindigt het ongeveer"},{"Hier begint deze","Gaat hier weer verder","En uiteindelijk hier"},
	    										{"Deze gaat hier van start", "Heeft wat meer dan de rest", "Kort", "Eindigt hier"}};
	    
	    
		LinearLayout [] linear = new LinearLayout[planninginfo.length];

		for(int i=0;i<linear.length;i++)
		{
			linear[i] = new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mainlinear.addView(linear[i], params);
			linear[i].setOrientation(LinearLayout.HORIZONTAL);
		
		
			TextView [] txt =new TextView[planninginfo[i].length];
			
	
			for(int j=0;j<txt.length;j++)
			{
				TextView divider = new TextView(Activity_Planning.this);
				
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(pxtodp(2), LayoutParams.MATCH_PARENT);
			    llp.setMargins(pxtodp(5), 0, pxtodp(5), 0); // llp.setMargins(left, top, right, bottom);
			    divider.setLayoutParams(llp);
			    
				divider.setBackgroundColor(Color.GRAY);
				
				txt[j]=new TextView(Activity_Planning.this);
				txt[j].setText(planninginfo[i][j]);
				txt[j].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				
				linear[i].addView(txt[j]);
				if(j != txt.length-1)
					linear[i].addView(divider);
			}
		}
	}
	
	private int pxtodp(int px){
		return (int)(getResources().getDisplayMetrics().density*px);
	}
}