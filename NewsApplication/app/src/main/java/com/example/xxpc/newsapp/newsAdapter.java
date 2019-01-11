package com.example.xxpc.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.holder> {
    String url;
    Context context;
    ArrayList<newsModel> newsModelArrayList;


    public newsAdapter(Context context, ArrayList<newsModel> newsModelArrayList) {
        this.context = context;
        this.newsModelArrayList = newsModelArrayList;
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_of_news, parent, false);
        return new holder(v);
    }



    @Override
    public void onBindViewHolder(final holder holder, int position) {

        newsModel model = newsModelArrayList.get(position);
        holder.textViewType.setText(model.getTypenews());
        holder.textViewNameSec.setText(model.getSectionName());
        holder.textViewTime.setText(model.getWebPublicationDate());
        holder.textViewTitle.setText(model.getWebTitle());
        holder.textViewArth.setText(model.getNameArth());
        url = model.getWebUrl();
    }
    @Override
    public int getItemCount() {
        return newsModelArrayList.size();
    }
    public void setNews(ArrayList<newsModel> data) {
        Log.e("_______",data.get(0).sectionName+"!!!!");
        newsModelArrayList=new ArrayList<>();
        newsModelArrayList.addAll(data);
        Log.e("____1___",newsModelArrayList.get(0).sectionName+"!!!!");
        notifyDataSetChanged();
    }

    class holder extends RecyclerView.ViewHolder {
        TextView textViewType;
        TextView textViewTime;
        TextView textViewTitle;
        TextView textViewLink;
        TextView textViewNameSec;
        TextView textViewArth;
        public holder(final View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.timeid);
            textViewType = itemView.findViewById(R.id.type);
            textViewNameSec = itemView.findViewById(R.id.nameSec);
            textViewTitle = itemView.findViewById(R.id.titleNews);
            textViewLink = itemView.findViewById(R.id.linkid);
            textViewArth = itemView.findViewById(R.id.nameArth);

            textViewLink.setMovementMethod(LinkMovementMethod.getInstance());
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(url));
                    itemView.getRootView().getContext().startActivity(browserIntent);
                }
            });
        }
    }
}
