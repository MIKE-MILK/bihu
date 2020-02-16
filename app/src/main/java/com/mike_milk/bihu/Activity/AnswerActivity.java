package com.mike_milk.bihu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mike_milk.bihu.Adapter.AnswerAdapter;
import com.mike_milk.bihu.Adapter.QuestionAdapter;
import com.mike_milk.bihu.R;
import com.mike_milk.bihu.util.HttpConnectUtil;
import com.mike_milk.bihu.util.SetImageViewUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnswerActivity extends AppCompatActivity {
    private Button Answerbtn;
    private EditText Answered;
    private String AnswerTitle;
    private String AnswerName;
    private String AnswerAvatar;
    private String AnswerTime;
    private String AnswerContent;
    private String AnswerImage;
    private String LoginToken;
    private String AnswerQid;
    private Intent mintent;
    private RecyclerView AnswerRecyclerView;
    private TextView AnswerContentTx;
    private ImageView AnswerContentIm;
    private TextView AnswerTimeTx;
    private ImageView AnswerAvatarim;
    private TextView AnswerNametx;
    private TextView AnswerTitletx;
    private TextView AnswerBack;
    private JSONArray jsonArray;
    private List<JSONObject>jsonObjectList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer);

        getData();
        initview();
        setData();
        setonClick();
    }

    private void setData(){
        AnswerContentTx.setText(AnswerContent);
        AnswerTimeTx.setText("发布于"+AnswerTime);
        AnswerNametx.setText(AnswerName);
        AnswerTitletx.setText(AnswerTitle);
        SetImageViewUtil.setImageToImageView(AnswerAvatarim,AnswerAvatar);
        SetImageViewUtil.setImageToImageView(AnswerContentIm,AnswerImage);
    }

    private void initview(){
        AnswerBack=findViewById(R.id.answer_back_tx);
        AnswerAvatarim=findViewById(R.id.iv_answer_avatar);
        Answerbtn=findViewById(R.id.btn_answer);
        AnswerContentTx=findViewById(R.id.tv_answer_content);
        AnswerContentIm=findViewById(R.id.iv_answer_image);
        AnswerTimeTx=findViewById(R.id.tv_answer_time);
        AnswerNametx=findViewById(R.id.tv_answer_name);
        AnswerTitletx=findViewById(R.id.tv_answer_title);
        Answered=findViewById(R.id.et_answer);
        AnswerRecyclerView=findViewById(R.id.answer_recyclerView);
    }


    public void setonClick(){
        AnswerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Answerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/answer.php", "qid=" + AnswerQid + "&content=" + Answered.getText().toString() + "&images=null&token=" + LoginToken, new HttpConnectUtil.CallBack() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object=new JSONObject(response);
                            if ("200".equals(object.getString("status"))){
                                Toast.makeText(AnswerActivity.this,"回答成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(AnswerActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
     private AnswerAdapter.OnItemClickListener Listener=new AnswerAdapter.OnItemClickListener() {
         @Override
         public void onItemClick(final View v, QuestionAdapter.ViewName viewName, final int position) {
             switch (v.getId()){
                 case R.id.discuss_exciting:
                     HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/getAnswerList.php", "page=null&count=100&qid=" + AnswerQid + "&token=" + LoginToken, new HttpConnectUtil.CallBack() {
                         @Override
                         public void onResponse(String response) {
                             try {
                                 JSONObject object=new JSONObject(response);
                                 if ("200".equals(object.getString("status"))){
                                     JSONObject data=object.getJSONObject("data");
                                     JSONArray jsonArray=data.getJSONArray("answers");
                                     jsonObjectList=new ArrayList<>();
                                     jsonObjectList.clear();
                                     for (int i=0;i<jsonArray.length();i++){
                                         jsonObjectList.add((JSONObject)jsonArray.get(i));
                                     }
                                     Collections.sort(jsonObjectList, new Comparator<JSONObject>() {
                                         @Override
                                         public int compare(JSONObject o1, JSONObject o2) {
                                             try {
                                                 return (Integer.parseInt(o1.getString("id")) - Integer.parseInt(o2.getString("id")));
                                             }catch (Exception e){
                                             return 0;
                                             }
                                         }
                                     });
                                 }else{
                                     Toast.makeText(AnswerActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();
                                 }
                             }catch (Exception e){
                                 e.printStackTrace();
                             }
                         }
                     });
                     try {
                         if ("false".equals(jsonObjectList.get(position).getString("is_exciting"))){
                             HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/exciting.php", "id" + jsonObjectList.get(position).getString("id") + "&type=2&token=" + LoginToken, new HttpConnectUtil.CallBack() {
                                 @Override
                                 public void onResponse(String response) {
                                     try {
                                         JSONObject jsonObject=new JSONObject(response);
                                         if ("200".equals(jsonObject.getString("status"))){
                                             Toast.makeText(AnswerActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
                                             View view=AnswerRecyclerView.getLayoutManager().findViewByPosition(position);
                                             ImageView imageView=view.findViewById(R.id.discuss_exciting);
                                             imageView.setImageResource(R.mipmap.zan2);
                                         }else {
                                             Toast.makeText(AnswerActivity.this,jsonObject.getString("info"),Toast.LENGTH_SHORT).show();
                                         }
                                     }catch (Exception e){
                                         e.printStackTrace();
                                     }
                                 }
                             });
                         }
                     }catch (Exception e){
                         e.printStackTrace();
                     }

                     try {
                         if ("true".equals(jsonObjectList.get(position).getString("is_exciting"))){
                             HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/cancelExciting.php", "id=" + jsonObjectList.get(position).getString("id") + "&type=2&token=" + LoginToken, new HttpConnectUtil.CallBack() {
                                 @Override
                                 public void onResponse(String response) {
                                     try {
                                         JSONObject jsonObject=new JSONObject(response);
                                         if ("200".equals(jsonObject.getString("status"))){
                                             Toast.makeText(AnswerActivity.this,"点赞取消",Toast.LENGTH_SHORT).show();
                                             View view=AnswerRecyclerView.getLayoutManager().findViewByPosition(position);
                                             ImageView imageView=view.findViewById(R.id.discuss_exciting);
                                             imageView.setImageResource(R.mipmap.dainzan);
                                         }else {
                                             Toast.makeText(AnswerActivity.this,jsonObject.getString("info"),Toast.LENGTH_SHORT).show();
                                         }
                                     }catch (Exception e){
                                         e.printStackTrace();
                                     }
                                 }
                             });
                         }

                     }catch (Exception e){
                         e.printStackTrace();
                     }
                     break;
                 case R.id.discuss_naive:
                     HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/getAnswerList.php", "page=null&count=100&qid=" + AnswerQid + "&token=" + LoginToken, new HttpConnectUtil.CallBack() {
                         @Override
                         public void onResponse(String response) {
                             try {
                                 JSONObject object=new JSONObject(response);
                                 if ("200".equals(object.getString("status"))){
                                     JSONObject data=object.getJSONObject("data");
                                     JSONArray jsonArray=data.getJSONArray("answers");
                                     jsonObjectList=new ArrayList<>();
                                     jsonObjectList.clear();
                                     for (int i=0;i<jsonArray.length();i++){
                                         jsonObjectList.add((JSONObject)jsonArray.get(i));
                                     }
                                     Collections.sort(jsonObjectList, new Comparator<JSONObject>() {
                                         @Override
                                         public int compare(JSONObject o1, JSONObject o2) {
                                             try {
                                                 return (Integer.parseInt(o1.getString("id")) - Integer.parseInt(o2.getString("id")));
                                             }catch (Exception e){
                                             return 0;
                                             }
                                         }
                                     });
                                 }else {
                                     Toast.makeText(AnswerActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();
                                 }
                             }catch (Exception e){
                                 e.printStackTrace();
                             }
                         }
                     });
                     try {
                         if ("false".equals(jsonObjectList.get(position).getString("is_naive"))){
                             HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/naive.php", "id=" + jsonObjectList.get(position).getString("id") + "&type=2&token=" + LoginToken, new HttpConnectUtil.CallBack() {
                                 @Override
                                 public void onResponse(String response) {
                                     try {
                                         JSONObject object=new JSONObject(response);
                                         if ("200".equals(object.getString("status"))){
                                             Toast.makeText(AnswerActivity.this,"踩成功",Toast.LENGTH_SHORT).show();
                                             View view=AnswerRecyclerView.getLayoutManager().findViewByPosition(position);
                                             ImageView imageView=view.findViewById(R.id.discuss_naive);
                                             imageView.setImageResource(R.mipmap.cai);
                                         }else {
                                             Toast.makeText(AnswerActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();
                                         }
                                     }catch (Exception e){
                                         e.printStackTrace();
                                     }
                                 }
                             });
                         }
                     }catch (Exception e){
                         e.printStackTrace();
                     }

                     try {
                      if ("true".equals(jsonObjectList.get(position).getString("is_naive"))){
                          HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/cancelNaive.php", "id=" + jsonObjectList.get(position).getString("id") + "&type=2&token=" + LoginToken, new HttpConnectUtil.CallBack() {
                              @Override
                              public void onResponse(String response) {
                                  try {
                                      JSONObject object=new JSONObject(response);
                                      if ("200".equals(object.getString("status"))){
                                          Toast.makeText(AnswerActivity.this,"取消踩成功",Toast.LENGTH_SHORT).show();
                                          View view=AnswerRecyclerView.getLayoutManager().findViewByPosition(position);
                                          ImageView imageView=view.findViewById(R.id.discuss_naive);
                                          imageView.setImageResource(R.mipmap.caicai);
                                      }else {
                                          Toast.makeText(AnswerActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();
                                      }
                                  }catch (Exception e){
                                      e.printStackTrace();
                                  }
                              }
                          });
                      }
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                     break;
                     default:
                         break;
             }
         }
     };

    private void getData(){
        mintent = getIntent();/*
        put,get不一致出错
        */
        AnswerTitle=mintent.getStringExtra("questionTitleKey");
        AnswerAvatar=mintent.getStringExtra("questionAvatarKey");
        AnswerImage=mintent.getStringExtra("questionImagesKey");
        AnswerTime=mintent.getStringExtra("questionTimeKey");
        AnswerName=mintent.getStringExtra("questionUsernameKey");
        AnswerContent=mintent.getStringExtra("questionContentKey");
        AnswerQid=mintent.getStringExtra("answerQidKey");
        LoginToken=mintent.getStringExtra("userLoginTokenKey");
        HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/getAnswerList.php", "page=null&count=20&qid=" + AnswerQid + "&token=" + LoginToken, new HttpConnectUtil.CallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONObject  data=object.getJSONObject("data");
                    jsonArray=data.getJSONArray("answers");
                    LinearLayoutManager  manager=new LinearLayoutManager(AnswerActivity.this);
                    AnswerRecyclerView.setLayoutManager(manager);
                    AnswerAdapter adapter=new AnswerAdapter(AnswerActivity.this,jsonArray);
                    AnswerRecyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(Listener);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
