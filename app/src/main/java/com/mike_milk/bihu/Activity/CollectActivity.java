package com.mike_milk.bihu.Activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mike_milk.bihu.Adapter.CollectAdapter;
import com.mike_milk.bihu.R;
import com.mike_milk.bihu.db.User;
import com.mike_milk.bihu.util.HttpConnectUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectActivity extends AppCompatActivity {
    private TextView CollectBack;
    private RecyclerView CollectRecyclerView;
    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect);
        initview();
        iniintent();
        setClick();
        list();
    }
    public void setClick(){
        CollectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void initview(){
        CollectBack=findViewById(R.id.collect_back);
        CollectRecyclerView=findViewById(R.id.collect_recyclerview);
    }

    private void iniintent(){
        Intent intent=getIntent();
        user=intent.getParcelableExtra("user");
    }

    private void list(){
        HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/getFavoriteList.php","page=null&count=100&token="+user.getToken(), new HttpConnectUtil.CallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    if ("200".equals(object.getString("status"))){
                        JSONObject data=object.getJSONObject("data");
                        JSONArray jsonArray=data.getJSONArray("questions");
                        List<JSONObject>jsonObjectList=new ArrayList<>();
                        jsonObjectList.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            jsonObjectList.add((JSONObject)jsonArray.get(i));
                        }
                        Collections.sort(jsonObjectList, new Comparator<JSONObject>() {
                            @Override
                            public int compare(JSONObject o1, JSONObject o2) {
                                try {
                                    return (Integer.parseInt(o1.getString("id")) - Integer.parseInt(o2.getString("id")));
                                } catch (Exception e) {
                                    return 0;
                                }
                            }
                        });
                        LinearLayoutManager manager=new LinearLayoutManager(CollectActivity.this);
                        CollectRecyclerView.setLayoutManager(manager);
                        CollectAdapter adapter=new CollectAdapter(CollectActivity.this,user,jsonObjectList);
                        CollectRecyclerView.setAdapter(adapter);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
