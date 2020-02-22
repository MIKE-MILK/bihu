package com.mike_milk.bihu.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mike_milk.bihu.R;
import com.mike_milk.bihu.db.Qestion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
参考了一篇CSDN的博客
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>implements View.OnClickListener {
    private List<Qestion.Data.Qestions>qestionsList;
    private Context context;
    private JSONArray mjsonarray;
    private String Date;
    private OnItemClickListener onItemClickListener;
    private String images;
    private String ExcitingStatus;
    private String NaiveStatus;
    private String FavoriteStatus;
    private List list;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.qestion,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private String AuthorName;
        private String Avatar;
        private String Content;
        private String Title;
        private final ImageView Answerim;
        private final ImageView Excitingim;
        private final ImageView Naiveim;
        private final ImageView Favoriteim;

        public ViewHolder( View view){
            super(view);
            Answerim= itemView.findViewById(R.id.question_answer_pinglun);
            Excitingim= itemView.findViewById(R.id.question_answer_zan);
            Naiveim= itemView.findViewById(R.id.question_answer_cai);
            Favoriteim= itemView.findViewById(R.id.question_answer_favorite);

            view.setOnClickListener(QuestionAdapter.this);
            Answerim.setOnClickListener(QuestionAdapter.this);
            Excitingim.setOnClickListener(QuestionAdapter.this);
            Naiveim.setOnClickListener(QuestionAdapter.this);
            Favoriteim.setOnClickListener(QuestionAdapter.this);
        }

        public void setData(Object o) {
            ImageView avatarim=itemView.findViewById(R.id.qestion_name_im);
            TextView authortx=itemView.findViewById(R.id.qestion_name_tx);
            TextView contenttx=itemView.findViewById(R.id.qestion_neirong_tx);
            TextView title=itemView.findViewById(R.id.qestion_biaoti_tx);
            TextView timetx=itemView.findViewById(R.id.qestion_time_tx);
            ImageView imgeim=itemView.findViewById(R.id.qestion_content_im);
            ImageView excitingim=itemView.findViewById(R.id.question_answer_zan);
            ImageView nativeim=itemView.findViewById(R.id.question_answer_cai);
            ImageView favoriteim=itemView.findViewById(R.id.question_answer_favorite);
            JSONObject object=(JSONObject)o;
            try {
                AuthorName=object.getString("authorName");
                Avatar=object.getString("authorAvatar");
                Content=object.getString("content");
                Title=object.getString("title");
                Date=object.getString("date");
                images=object.getString("images");
                ExcitingStatus=object.getString("is_exciting");
                NaiveStatus=object.getString("is_naive");
                FavoriteStatus=object.getString("is_favorite");
                authortx.setText(AuthorName);
                contenttx.setText(Content);
                timetx.setText(Date+"时发布");
                title.setText(Title);
                Picasso.with(itemView.getContext()).load(Avatar).into(avatarim);
                Picasso.with(itemView.getContext()).load(images).into(imgeim);
                Log.d("adpter","这里执行了");
                if ("null".equals(images)){
                    imgeim.setVisibility(View.GONE);
                }
                if ("true".equals(ExcitingStatus)){
                    excitingim.setImageResource(R.mipmap.zan2);
                }else if ("false".equals(ExcitingStatus)){
                    excitingim.setImageResource(R.mipmap.zanzanzan);
                }
                if ("true".equals(NaiveStatus)){
                    nativeim.setImageResource(R.mipmap.cc);
                }else if ("false".equals(NaiveStatus)){
                    nativeim.setImageResource(R.mipmap.ccc);
                }
                if ("true".equals(FavoriteStatus)){
                    favoriteim.setImageResource(R.mipmap.love);
                }else if ("false".equals(FavoriteStatus)){
                    favoriteim.setImageResource(R.mipmap.xihuan);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public QuestionAdapter(Context context,JSONArray data){
        this.mjsonarray=data;
        Log.d("questionAdapter","data是"+mjsonarray);
        this.context=context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Favoriteim.setTag(position);
        holder.Naiveim.setTag(position);
        holder.Excitingim.setTag(position);
        holder.Answerim.setTag(position);
        holder.itemView.getTag(position);
        list=new ArrayList();
        list.clear();
        try {
            for (int i=mjsonarray.length();i>0;i--){
                for (int j=mjsonarray.length();j>0;j--){
                    if (Integer.parseInt(mjsonarray.getJSONObject(j - 1).getString("id")) == i){
                        list.add(mjsonarray.getJSONObject(j-1));
                    }
                    }
                }/*
                出错
                */
            if(list.size()!=0){
            holder.setData(list.get(position));
            }else {
                return;
            }
            }catch (Exception e){
            e.printStackTrace();
        }
        }

    @Override
    public int getItemCount() {
        if (mjsonarray!=null){
            return mjsonarray.length();
        }
        return 0;
    }

    public interface OnItemClickListener{
        void onClick(View view,ViewName viewName,int position);
    }

    public enum ViewName {
        ITEM,
        PRACTISE
    }



    @Override
    public void onClick(View v) {
       int position = (int) v.getTag();
        if (onItemClickListener != null) {
            switch (v.getId()) {
                case R.id.rv_question:
                    onItemClickListener.onClick(v,ViewName.PRACTISE,position);
                    break;
                default:
                    onItemClickListener.onClick(v, ViewName.ITEM, position);
                    break;
            }
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}



