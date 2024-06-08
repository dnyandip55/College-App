package com.example.collegeapp.ebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeapp.R;

import java.util.List;

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.EbookViewHolder> {
    private final Context context;
    private final List<EbookData> list;

    public EbookAdapter(Context context, List<EbookData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.ebook_item_layout,parent,false);

        return new EbookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EbookViewHolder holder, int position) {
        EbookData ebook = list.get(position);
        Log.d("EbookAdapter", "Name: " + ebook.getName());

        holder.ebookName.setText(ebook.getName());
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context,pdfViewerActivity.class);
            intent.putExtra("pdfUrl",ebook.getPdfUrl());
            context.startActivity(intent);
        });

        holder.ebookDownload.setOnClickListener(view -> {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(ebook.getPdfUrl()));
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class EbookViewHolder extends RecyclerView.ViewHolder {
        private final TextView ebookName;
        private final ImageView ebookDownload;

        public EbookViewHolder(@NonNull View itemView) {
            super(itemView);

            ebookName=itemView.findViewById(R.id.ebookName);
            ebookDownload=itemView.findViewById(R.id.ebookDownload);


        }
    }
}
