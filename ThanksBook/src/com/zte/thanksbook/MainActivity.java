package com.zte.thanksbook;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {
    
     private ImageButton slipButton;
     private ImageButton indexButton;
     private ImageView img;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getActionBar().hide();
        slipButton = (ImageButton)this.findViewById(R.id.main_slip);
        indexButton = (ImageButton)this.findViewById(R.id.main_app);
        //点击首页图标事件
        slipButton.setOnClickListener(new OnClickListener(){
               @Override
               public void onClick(View arg0) {
                    img = (ImageView)findViewById(R.id.ima);
                    NavigationFragment navigation = new NavigationFragment();
                    FragmentManager fragManager = getFragmentManager();
                    FragmentTransaction tran = fragManager.beginTransaction();
                    //tran.add(navigation, null);
                    tran.replace(R.id.content, navigation);
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
