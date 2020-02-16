package com.mike_milk.bihu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.mike_milk.bihu.R;
import com.mike_milk.bihu.db.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<JSONObject>jsonObjectList;


    public CollectAdapter(Context context, User user, List<JSONObject>list){
        this.context=context;
        this.jsonObjectList=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private String Title;
        private String Avatar;
        private String AvatarName;
        private String Content;
        private String Picture;
        public ViewHolder(View view){
            super(view);
        }
        public void setData(Object o){
            TextView title=itemView.findViewById(R.id.collect_title);
            ImageView avatar=itemView.findViewById(R.id.collect_avatar);
            TextView avatarName=itemView.findViewById(R.id.collect_name);
            TextView content=itemView.findViewById(R.id.collect_content_tx);
            ImageView picture=itemView.findViewById(R.id.collect_content_im);
            JSONObject jsonObject=(JSONObject)o;
            try {
                Title=jsonObject.getString("title");
                Avatar=jsonObject.getString("authorAvatar");
                AvatarName=jsonObject.getString("authorName");
                Content=jsonObject.getString("content");
                Picture=jsonObject.getString("images");

                title.setText(Title);
                avatarName.setText(AvatarName);
                content.setText(Content);
                Picasso.with(itemView.getContext()).load(Avatar).into(avatar);
                Picasso.with(itemView.getContext()).load(Picture).into(picture);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (jsonObjectList!=null){
            return jsonObjectList.size();
    }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(jsonObjectList.get(position));
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.collect_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
}
