package com.mike_milk.bihu.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mike_milk.bihu.R;
import com.mike_milk.bihu.util.SetImageViewUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private JSONArray jsonArray;
    private List<JSONObject>jsonObjectList;
    private OnItemClickListener onItemClickListener;
    public AnswerAdapter(Context context,JSONArray jsonArray){
        this.context=context;
        this.jsonArray=jsonArray;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ExcitingIm;
        private ImageView NaiveIm;
        private String avatar;
        private String authorName;
        private String Content;
        private String excitingStatus;
        private String naiveStatus;
        private String date;
        public ViewHolder(View view){
            super(view);
            ExcitingIm=view.findViewById(R.id.discuss_exciting);
            NaiveIm=view.findViewById(R.id.discuss_naive);

            itemView.setOnClickListener(AnswerAdapter.this);
            ExcitingIm.setOnClickListener(AnswerAdapter.this);
            NaiveIm.setOnClickListener(AnswerAdapter.this);
        }

        public void setData(Object o){
            ImageView avatarim=itemView.findViewById(R.id.discuss_avatar_im);
            TextView userName=itemView.findViewById(R.id.discuss_name_tx);
            TextView content=itemView.findViewById(R.id.discuss_neirong_tx);
            TextView time=itemView.findViewById(R.id.discuss_time_tx);
            ImageView exciting=itemView.findViewById(R.id.discuss_exciting);
            ImageView naive=itemView.findViewById(R.id.discuss_naive);
            JSONObject object=(JSONObject)o;
            try {
                avatar=object.getString("authorAvatar");
                authorName=object.getString("authorName");
                Content=object.getString("content");
                excitingStatus=object.getString("is_exciting");
                naiveStatus=object.getString("is_naive");
                date=object.getString("date");

                if ("true".equals(excitingStatus)){
                    exciting.setImageResource(R.mipmap.zan2);
                }else if ("false".equals(excitingStatus)){
                    exciting.setImageResource(R.mipmap.dainzan);
                }
                if ("true".equals(naiveStatus)){
                    naive.setImageResource(R.mipmap.caicai);
                }else if ("false".equals(naiveStatus)){
                    naive.setImageResource(R.mipmap.cai);
                }
                SetImageViewUtil.setImageToImageView(avatarim,avatar);
                userName.setText(authorName);
                content.setText(Content);
                time.setText("发布于"+date);
                Log.d("answer","获取的回答列表是");

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.answer_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
        holder.itemView.getTag(position);
        holder.NaiveIm.setTag(position);
        holder.ExcitingIm.setTag(position);
        jsonObjectList=new ArrayList<>();
        jsonObjectList.clear();
        for (int i=0;i<jsonArray.length();i++){
            jsonObjectList.add((JSONObject) jsonArray.get(i));
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
            holder.setData(jsonObjectList.get(position));
    }catch (Exception e){
        e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (jsonArray!=null){
            return jsonArray.length();
        }
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, QuestionAdapter.ViewName viewName, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (onItemClickListener != null) {
            switch (v.getId()) {
                case R.id.rv_question:
                    onItemClickListener.onItemClick(v, QuestionAdapter.ViewName.PRACTISE,position);
                default:
                    onItemClickListener.onItemClick(v, QuestionAdapter.ViewName.ITEM, position);
                    break;
            }
        }

    }
}
