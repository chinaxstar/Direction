package xstar.com.direction;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xstar.com.direction.databinding.ActivityMain2Binding;
import xstar.com.direction.opengl.MainRender;
import xstar.com.library.commons.recyclerutil.ObjAdapter;
import xstar.com.library.commons.recyclerutil.RvBuilder;
import xstar.com.library.commons.recyclerutil.SimpleAdapter;
import xstar.com.library.commons.recyclerutil.SimpleHolder;

public class MainActivity extends AppCompatActivity {

    ActivityMain2Binding main2Binding;
    MainRender mainRender=new MainRender();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(this, R.layout.activity_main2, null);
        main2Binding = ActivityMain2Binding.bind(rootView);
        setContentView(rootView);
        simpleAdapter.setLayout_id(android.R.layout.simple_list_item_1);
        List<String> list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.functions)));
        simpleAdapter.setItemIds(android.R.id.text1);
        simpleAdapter.setItemList(list);
        simpleAdapter.setOnItemClickListner(new ObjAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(View item, int position) {
                Class<? extends Activity> clazz = null;
                switch (position) {
                    case 0:
                        clazz = MainActivity.class;
                        break;
                    case 1:
                        clazz = GuideActivity.class;
                        break;
                    case 2:
                        clazz = BySunAndClockActivity.class;
                        break;
                }
                if (clazz != null) startActivity(new Intent(MainActivity.this, clazz));
            }
        });
        RvBuilder.Builder().setAdapter(simpleAdapter).build(main2Binding.functions);
        mainRender.setGlSurfaceView(main2Binding.glMain);
        main2Binding.glMain.setRenderer(mainRender);
    }

    private SimpleAdapter<String> simpleAdapter = new SimpleAdapter<String>() {
        @Override
        public void onBind(SimpleHolder holder, final int position, final String item) {
            holder.getTextView(android.R.id.text1).setText(item);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListner()!=null)getOnItemClickListner().onItemClick(v,position);
                }
            });
        }
    };

    public boolean isSupportEs2(){
        ActivityManager activityManager= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        return         activityManager.getDeviceConfigurationInfo().reqGlEsVersion>=0x2000;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (main2Binding.glMain!=null)main2Binding.glMain.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (main2Binding.glMain!=null)main2Binding.glMain.onResume();
    }
}
