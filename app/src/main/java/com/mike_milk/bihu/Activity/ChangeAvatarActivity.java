package com.mike_milk.bihu.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.mike_milk.bihu.R;
import com.mike_milk.bihu.db.User;
import com.mike_milk.bihu.util.HttpConnectUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeAvatarActivity extends AppCompatActivity {
    private TextView ChangeAvatarBack;
    private TextView ChangeAvatarpic;
    private Button ChangeAvatarButton;
    private Intent intent;
    private String status;
    private String info;
    private User user;
    private Uri murl;
    private ImageView imageView;
    private String Qiniutoken;
    private String Url;
    private JSONObject mjsonObject;
    private String token;
    private String url;
    private String param;
    private String path;
    private  String site;
    private String name;
    private NavigationView navigationView;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changetouxiang);
        /*
        对控件的实例
         */
        ChangeAvatarBack=findViewById(R.id.change_touxiang_back);
        ChangeAvatarpic=findViewById(R.id.change_touxiang_xuanze);
        ChangeAvatarButton=findViewById(R.id.change_btn);
        imageView=findViewById(R.id.change_touxiang_im);
        circleImageView=findViewById(R.id.icon_image);
        LoginToken();
        setClick();
    }
    /*
    监听事件
     */
    public void setClick(){
        ChangeAvatarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ChangeAvatarpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        });

        /*
        开启一个线程
         */
        ChangeAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /*
                        得到七牛的token
                         */
                         getQiniuToken();
                         /*
                         七牛的储存
                          */
                         Qiniuchucun();
                    }
                }).start();
            }
        });

        /*circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getQiniuToken();
                        Qiniuchucun();
                    }
                }).start();
            }
        });*/
    }

    public void getQiniuToken(){
        /*
        GitHub上找到的获取七牛token的方法
         */
        Url="http://zzzia.net:8080/qiniu/";
        param="accessKey=pU2KcxHPPFM3VEtlvi7u5GoXM9nkCeWVaUzFY7c7"+
                "&secretKey=j-6q6Q9Si9gda41dnI5rbXox2SrKT6MwGQEGE24X"+"&bucket=zuoyebihu";
        String response=HttpConnectUtil.doSyncPost(Url,param);
        try {
            JSONObject object=new JSONObject(response);
            String info=object.getString("info");
            if ("success".equals(info)){
                String token=object.getString("token");
                if (token!=null){
                    Qiniutoken=token;
                }else {
                    Toast.makeText(ChangeAvatarActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Qiniuchucun(){
        /*
        对图片的储存
         */
        Configuration configuration= new Configuration.Builder()
                .chunkSize(512 * 1024)
                .putThreshhold(1024 * 1024)
                .connectTimeout(10)
                .useHttps(true)
                .responseTimeout(60)
                .build();
        UploadManager uploadManager=new UploadManager(configuration);
        uploadManager.put(path, null, Qiniutoken, new UpCompletionHandler() {
            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                if (responseInfo.isOK()){
                    try {
                         name=jsonObject.getString("key");
                        if (!"null".equals(name)){
                             site="http://q5ey1hmlp.bkt.clouddn.com/"+name;
                        }
                        param="token="+token+"&avatar="+site;
                        url="http://bihu.jay86.com/modifyAvatar.php";
                        HttpConnectUtil.doAsyncPost(url, param, new HttpConnectUtil.CallBack() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    mjsonObject=new JSONObject(response);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                try {
                                    if ("200".equals(mjsonObject.getString("status"))){
                                        Toast.makeText(ChangeAvatarActivity.this,"修改头像成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        },null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_CANCELED){
            Toast.makeText(ChangeAvatarActivity.this,"退出相册",Toast.LENGTH_SHORT).show();
            return;
        }
            try {
                murl=data.getData();
                imageView.setImageURI(murl);
                if (murl!=null){
                    path=getPath(murl);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

/*
服务器设置为private，错误 储存图片并返回图片的uri
 */
    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void LoginToken(){
        Intent intent=getIntent();
        user=intent.getParcelableExtra("user");
        token=user.getToken();
    }

}
