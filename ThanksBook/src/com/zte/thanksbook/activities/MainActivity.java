package com.zte.thanksbook.activities;

import com.zte.thanksbook.R;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    
     private ImageButton slipButton;
     private ImageButton indexButton;
     private ImageView img;
     
     WindowManager manager;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getActionBar().hide();
        
        manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE); 
        
        slipButton = (ImageButton)this.findViewById(R.id.main_slip);
        indexButton = (ImageButton)this.findViewById(R.id.main_app);
        //点击首页图标事件
        slipButton.setOnClickListener(new OnClickListener(){
               @Override
               public void onClick(View arg0) {
            	   /*WindowManager.LayoutParams lp = new WindowManager.LayoutParams(  
            			   LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT,  
            			   WindowManager.LayoutParams.TYPE_APPLICATION,  
            			   WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  
            			   | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  
            			   PixelFormat.TRANSLUCENT);  
            			     
            			   lp.gravity = Gravity.BOTTOM;// 可以自定义显示的位置  
            			   lp.y = 10;   
            			   View mNightView = new TextView(MainActivity.this);  
            			   mNightView.setBackgroundColor(0x80000000);  
            			   manager.addView(mNightView, lp);*/
            	   
                    img = (ImageView)findViewById(R.id.ima);
                    NavigationFragment navigation = new NavigationFragment();
                    FragmentManager fragManager = getFragmentManager();
                    FragmentTransaction tran = fragManager.beginTransaction();
                    //tran.add(navigation, null);
                    tran.replace(R.id.content, navigation);
                    tran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    //img.setBackgroundColor(Color.parseColor("#86222222"));
                    tran.addToBackStack(null);
                    tran.commit();
               }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
   
}
