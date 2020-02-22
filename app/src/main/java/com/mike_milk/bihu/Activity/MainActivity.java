package com.mike_milk.bihu.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.material.navigation.NavigationView;
import com.mike_milk.bihu.Adapter.QuestionAdapter;
import com.mike_milk.bihu.R;
import com.mike_milk.bihu.db.User;
import com.mike_milk.bihu.util.HttpConnectUtil;
import com.mike_milk.bihu.util.PermisionUtils;
import com.mike_milk.bihu.util.SetImageViewUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private EditText accountText;
    private EditText passwordText;
    private Intent intent;
    private MenuItem changepassword;
    private MenuItem tiwen;
    private MenuItem changeavatar;
    private MenuItem collect;
    private MenuItem tuichu;
    private MenuItem aboutus;
    private User user;
    private RecyclerView questionRecyclerView;
    private String param;
    private String getQuestionlist;
    private String intentAvatar;
    private JSONArray jsonArray;
    private String intentTitle;
    private String intentTiwen;
    private String intentuserName;
    private String intentContent;
    private String intentpic;
    private String intentTime;
    private String intentqid;
    private String Url;
    private CircleImageView avatar;
    private List<JSONObject>excitinglist;
    private List<JSONObject>naivelist;
    private List<JSONObject>collectlist;
    private List<JSONObject>questionlist;
    private LinearLayoutManager linearLayoutManager;
    private Bitmap bitmap;
    private SwipeRefreshLayout swipeRefreshLayout;
    private QuestionAdapter adapter;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getuser();
        view();
        initListen();
        getquestionlist();
        touxiang();
        //检测读写权限
        PermisionUtils.verifyStoragePermissions(this);

    }


    private void getquestionlist(){
        Url=" http://bihu.jay86.com/getQuestionList.php";
        HttpConnectUtil.doAsyncPost(Url,  "page=null&count=500&token=" + user.getToken(), new HttpConnectUtil.CallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    Log.d("Main","问题列表是"+response);
                    if ("200".equals(object.getString("status"))){
                        JSONObject object1=object.getJSONObject("data");
                        jsonArray=object1.getJSONArray("questions");
                        questionlist=new ArrayList<>();
                        questionlist.clear();
                        for (int i=jsonArray.length();i>0;i--){
                            for (int j=jsonArray.length();j>0;j--){
                                if (Integer.parseInt(jsonArray.getJSONObject(j-1).getString("id"))==i){
                                    questionlist.add(jsonArray.getJSONObject(j-1));
                                }
                            }
                        }
                        datarecyclerview();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
    }
        });
    }

    private void datarecyclerview(){
        linearLayoutManager=new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        questionRecyclerView.setLayoutManager(linearLayoutManager);
        adapter=new QuestionAdapter(MainActivity.this,jsonArray);
        adapter.setOnItemClickListener(onItemClickListener);
        questionRecyclerView.setAdapter(adapter);
    }

    private void touxiang(){
        if (user.getAvatar()!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        bitmap= SetImageViewUtil.getBitmap(user.getAvatar());
                        if (bitmap!=null){
                            avatar.post(new Runnable() {
                                @Override
                                public void run() {
                                    avatar.setImageBitmap(bitmap);
                                }
                            });
                        }else {
                            Toast.makeText(MainActivity.this,"获取头像失败",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void view(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        tuichu=findViewById(R.id.esc);
        aboutus=findViewById(R.id.nav_www);
        changepassword = navView.getMenu().findItem(R.id.changepassword);
        changeavatar = navView.getMenu().findItem(R.id.touxiang);
        collect = navView.getMenu().findItem(R.id.nav_like);
        tiwen = navView.getMenu().findItem(R.id.nav_settings);
        questionRecyclerView=findViewById(R.id.rv_question);
        swipeRefreshLayout=findViewById(R.id.gengxin_qestion);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.bluebackgroung);
            navView.setCheckedItem(R.id.nav_like);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getquestionlist();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }


    private QuestionAdapter.OnItemClickListener onItemClickListener=new QuestionAdapter.OnItemClickListener() {
        @Override
        public void onClick( View v, QuestionAdapter.ViewName viewName, final int position) {
            switch(v.getId()){
                case R.id.question_answer_zan:
                    HttpConnectUtil.doAsyncPost(Url, param, new HttpConnectUtil.CallBack() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                Log.d("dainzan","问题列表为"+response);
                                if ("200".equals(object.getString("status"))){
                                    JSONObject object1=object.getJSONObject("data");
                                    jsonArray=object1.getJSONArray("questions");
                                    excitinglist=new ArrayList();
                                    excitinglist.clear();
                                    for (int i=jsonArray.length();i>0;i--){
                                        for (int j=jsonArray.length();j>0;j--){
                                            if (Integer.parseInt(jsonArray.getJSONObject(j-1).getString("id"))==i){
                                                excitinglist.add(jsonArray.getJSONObject(j-1));
                                            }
                                        }
                                }

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });

                    try {
                        if ("false".equals(excitinglist.get(position).getString("is_exciting"))){
                            HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/exciting.php", "id=" + excitinglist.get(position).getString("id")
                                    + "&type=1&token=" + user.getToken(), new HttpConnectUtil.CallBack() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject object=new JSONObject(response);
                                        if("200".equals(object.getString("status"))){
                                            Toast.makeText(MainActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
                                            View view=questionRecyclerView.getLayoutManager().findViewByPosition(position);
                                            ImageView imageView=view.findViewById(R.id.question_answer_zan);
                                            imageView.setImageResource(R.mipmap.zan2);
                                        }else {
                                            Toast.makeText(MainActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else if ("true".equals(excitinglist.get(position).getString("is_exciting"))){
                            HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/cancelExciting.php", "id=" + excitinglist.get(position).getString("id")
                                    + "&type=1&token=" + user.getToken(), new HttpConnectUtil.CallBack() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject object=new JSONObject(response);
                                        if ("200".equals(object.getString("status"))){
                                            Toast.makeText(MainActivity.this,"取消赞成功",Toast.LENGTH_SHORT).show();
                                            View view=questionRecyclerView.getLayoutManager().findViewByPosition(position);
                                            ImageView imageView=view.findViewById(R.id.question_answer_zan);
                                            imageView.setImageResource(R.mipmap.zanzanzan);
                                        }else {
                                            Toast.makeText(MainActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();
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

                case R.id.question_answer_cai:
                    HttpConnectUtil.doAsyncPost(Url, param, new HttpConnectUtil.CallBack() {
                        @Override
                        public void onResponse(String response) {
                     try {
                         JSONObject object=new JSONObject(response);
                         if ("200".equals(object.getString("status"))){
                             JSONObject object1=object.getJSONObject("data");
                             jsonArray=object1.getJSONArray("questions");
                             naivelist=new ArrayList();
                             naivelist.clear();
                             for (int i=jsonArray.length();i>0;i--){
                                 for (int j=jsonArray.length();j>0;j--){
                                     if (Integer.parseInt(jsonArray.getJSONObject(j-1).getString("id"))==i){
                                         naivelist.add(jsonArray.getJSONObject(j-1));
                                     }
                                 }
                             }
                         }
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                        }
                    });
                    try {
                        System.out.println("naivelist = " + naivelist);
                        if ("false".equals(naivelist.get(position).getString("is_naive"))){
                            HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/naive.php","id="+naivelist.get(position).getString("id")
                                    + "&type=1&token="+user.getToken(), new HttpConnectUtil.CallBack() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject object=new JSONObject(response);
                                        if ("200".equals(object.getString("status"))){
                                            Toast.makeText(MainActivity.this,"踩成功",Toast.LENGTH_SHORT).show();
                                            View view=questionRecyclerView.getLayoutManager().findViewByPosition(position);
                                            ImageView imageView=view.findViewById(R.id.question_answer_cai);
                                            imageView.setImageResource(R.mipmap.cc);
                                        }else {
                                            Toast.makeText(MainActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else if ("true".equals(naivelist.get(position).getString("is_naive"))){
                            HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/cancelNaive.php", "id=" + naivelist.get(position).getString("id")
                                    + "&type=1&token=" + user.getToken(), new HttpConnectUtil.CallBack() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject object=new JSONObject(response);
                                        if ("200".equals(object.getString("status"))){
                                            Toast.makeText(MainActivity.this,"取消踩成功",Toast.LENGTH_SHORT).show();
                                            View view=questionRecyclerView.getLayoutManager().findViewByPosition(position);
                                            ImageView imageView=view.findViewById(R.id.question_answer_cai);
                                            imageView.setImageResource(R.mipmap.ccc);
                                        }else {
                                            Toast.makeText(MainActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();
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
                case R.id.question_answer_favorite:
                    HttpConnectUtil.doAsyncPost(Url, param, new HttpConnectUtil.CallBack() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object=new JSONObject(response);
                                if ("200".equals(object.getString("status"))){
                                    JSONObject object1=object.getJSONObject("data");
                                    jsonArray=object1.getJSONArray("questions");
                                    collectlist=new ArrayList();
                                    collectlist.clear();
                                    for (int i=jsonArray.length();i>0;i--){
                                        for (int j=jsonArray.length();j>0;j--){
                                            if (Integer.parseInt(jsonArray.getJSONObject(j-1).getString("id"))==i){
                                                collectlist.add(jsonArray.getJSONObject(j-1));
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    try {
                        if ("false".equals(collectlist.get(position).getString("is_favorite"))){
                            HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/favorite.php", "qid=" + collectlist.get(position).getString("id")
                                    + "&token=" + user.getToken(), new HttpConnectUtil.CallBack() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject object=new JSONObject(response);
                                        if ("200".equals(object.getString("status"))
                                        ){
                                            Toast.makeText(MainActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                                            View view1=questionRecyclerView.getLayoutManager().findViewByPosition(position);
                                            ImageView imageView=view1.findViewById(R.id.question_answer_favorite);
                                            imageView.setImageResource(R.mipmap.love);
                                        }else {
                                            Toast.makeText(MainActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else if ("true".equals(collectlist.get(position).getString("is_favorite"))){
                            HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/cancelFavorite.php", "qid=" + collectlist.get(position).getString("id")
                                    + "&token=" + user.getToken(), new HttpConnectUtil.CallBack() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject object=new JSONObject(response);
                                        if ("200".equals(object.getString("status"))){
                                            Toast.makeText(MainActivity.this,"取消收藏成功",Toast.LENGTH_SHORT).show();
                                            View view=questionRecyclerView.getLayoutManager().findViewByPosition(position);
                                            ImageView imageView=view.findViewById(R.id.question_answer_favorite);
                                            imageView.setImageResource(R.mipmap.xihuan);
                                        }else {
                                            Toast.makeText(MainActivity.this,object.getString("info"),Toast.LENGTH_SHORT).show();
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
                case R.id.question_answer_pinglun:

                            try {
                                JSONObject object=questionlist.get(position);
                                intentAvatar=object.getString("authorAvatar");
                                intentContent=object.getString("content");
                                intentpic=object.getString("images");
                                intentTime=object.getString("date");
                                intentTitle=object.getString("title");
                                intentuserName=object.getString("authorName");
                                intentqid=object.getString("id");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Intent intent=new Intent(MainActivity.this,AnswerActivity.class);
                            intent.putExtra("questionTitleKey",intentTitle);
                            intent.putExtra("questionAvatarKey",intentAvatar);
                            intent.putExtra("questionTimeKey",intentTime);
                            intent.putExtra("questionContentKey",intentContent);
                            intent.putExtra("questionImagesKey",intentpic);
                            intent.putExtra("answerQidKey",intentqid);
                            intent.putExtra("questionUsernameKey",intentuserName);
                            intent.putExtra("userLoginTokenKey",user.getToken());
                            startActivity(intent);
                            break;
                            default:
                                try {
                                    JSONObject questionData=questionlist.get(position);
                                    intentTitle=questionData.getString("title");
                                    intentAvatar=questionData.getString("authorAvatar");
                                    intentContent=questionData.getString("content");
                                    intentTime=questionData.getString("date");
                                    intentpic=questionData.getString("images");
                                    intentqid=questionData.getString("id");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                 Intent intent1=new Intent(MainActivity.this,AnswerActivity.class);
                                intent1.putExtra("questionTitleKey",intentTitle);
                                intent1.putExtra("questionAvatarKey",intentAvatar);
                                intent1.putExtra("questionTimeKey",intentTime);
                                intent1.putExtra("questionContentKey",intentContent);
                                intent1.putExtra("questionImagesKey",intentpic);
                                intent1.putExtra("answerQidKey",intentqid);
                                intent1.putExtra("userLoginTokenKey",user.getToken());
                                startActivity(intent1);
                                break;
            }
        }
        };
    private void getuser(){
        Intent intent=getIntent();
        user=intent.getParcelableExtra("user");
    }
    /*
    未传入数据出错
     */
   private void initListen(){
       changeavatar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               Intent intent = new Intent(MainActivity.this, ChangeAvatarActivity.class);
               intent.putExtra("user",user);
               startActivity(intent);
               return true;
           }
       });
       changepassword.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
               intent.putExtra("user",user);
               startActivity(intent);
               return true;
           }
       });
       collect.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               Intent intent = new Intent(MainActivity.this, CollectActivity.class);
               intent.putExtra("user",user);
               startActivity(intent);
               return true;
           }
       });
       tiwen.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               Intent intent = new Intent(MainActivity.this, tiwenActivity.class);
               intent.putExtra("user",user);
               startActivity(intent);
               return true;
           }
       });

   }
}

