package com.zte.thanksbook.activities;

import com.zte.thanksbook.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NavigationFragment extends Fragment{
	private Activity myActivity;

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
               Bundle savedInstanceState) {
          View v = inflater.inflate(R.layout.navigation, container,false);
          
          this.myActivity = this.getActivity();
          
          Button b = (Button)v.findViewById(R.id.shadow_text_view);
          b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

		    	 FragmentTransaction ft = myActivity.getFragmentManager().beginTransaction();
		    	 
		    	 ft.remove(NavigationFragment.this).commit();
			}
		});
          
          return v;
     }
    
     
     public void closeMenu(View view)
     {
    	 Log.v(null, "111");
    	 
    	 FragmentTransaction ft = this.getActivity().getFragmentManager().beginTransaction();
    	 
    	 ft.remove(this).commit();
     }
}