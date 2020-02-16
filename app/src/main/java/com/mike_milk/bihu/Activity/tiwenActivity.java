package com.mike_milk.bihu.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mike_milk.bihu.R;
import com.mike_milk.bihu.db.User;
import com.mike_milk.bihu.util.HttpConnectUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

public class tiwenActivity extends AppCompatActivity {

    private TextView tianjia;
    private TextView back;
    private EditText title;
    private EditText content;
    private ImageView pic;
    private Button button;
    private User user;
    private Intent intent;
    private String tiwenUrl;
    private String Titletx;
    private String Contenttx;
    private String name;
    private Uri uri;
    private String Token;
    private String path;
    private String giturl;
    private String param;
    private String LoginToken;
    private String data;
    private JSONObject mjsonObject;
    private String mImgUrl = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiwen);
        tianjia=findViewById(R.id.tv_tiwen_add_pictures);
        back=findViewById(R.id.tv_tiwen_back);
        title=findViewById(R.id.tiwen_title_ed);
        content=findViewById(R.id.tiwen_content_ed);
        pic=findViewById(R.id.tiwen_im);
        button=findViewById(R.id.tiwen_btn);

        setClick();
    }
    private void setClick(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (path!=null){
                    final Configuration configuration = new Configuration.Builder()
                            .chunkSize(512 * 1024)
                            .putThreshhold(1024 * 1024)
                            .connectTimeout(10)
                            .useHttps(true)
                            .responseTimeout(60)
                            .build();
                    giturl="http://zzzia.net:8080/qiniu/";
                    param="accessKey=pU2KcxHPPFM3VEtlvi7u5GoXM9nkCeWVaUzFY7c7"+
                            "&secretKey=j-6q6Q9Si9gda41dnI5rbXox2SrKT6MwGQEGE24X"+"&bucket=zuoyebihu";
                    HttpConnectUtil.doAsyncPost(giturl, param, new HttpConnectUtil.CallBack() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                 JSONObject object=new JSONObject(response);
                                 String info=object.getString("info");
                                if ("success".equals(info)){
                                    String token=object.getString("token");
                                    if (token!=null){
                                        Token=token;
                                    UploadManager manager=new UploadManager(configuration);
                                    manager.put(path, null, Token, new UpCompletionHandler() {
                                        @Override
                                        public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                                            if (responseInfo.isOK()){
                                                Toast.makeText(tiwenActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                            try {
                                                name=jsonObject.getString("key");
                                                mImgUrl="http://q5ey1hmlp.bkt.clouddn.com/"+name;
                                                question();
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            }
                                        }
                                    },null);}
                                }else {
                                    Toast.makeText(tiwenActivity.this,"网路错误",Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    question();
                }
            }
        });
        tianjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_CANCELED){
            Toast.makeText(tiwenActivity.this,"退出相册",Toast.LENGTH_SHORT).show();
        return;
        }
            try {
                uri=data.getData();
                pic.setImageURI(uri);
                if (uri!=null){
                    path=getPath(uri);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void toMainActivity(){
        finish();
    }

    private void question(){
        Titletx=title.getText().toString();
        Contenttx=content.getText().toString();
        Intent intent=getIntent();
        User user=intent.getParcelableExtra("user");
        LoginToken=user.getToken();
        tiwenUrl="http://bihu.jay86.com/question.php";
        data="title="+Titletx+"&content="+Contenttx+"&images="+mImgUrl+"&token="+LoginToken;
        HttpConnectUtil.doAsyncPost(tiwenUrl, data, new HttpConnectUtil.CallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    mjsonObject=new JSONObject(response);

                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    if ("200".equals(mjsonObject.getString("status"))){
                        Toast.makeText(tiwenActivity.this,"提问成功",Toast.LENGTH_SHORT).show();
                        toMainActivity();
                    }else {
                        Toast.makeText(tiwenActivity.this,mjsonObject.getString("info"),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }
}
