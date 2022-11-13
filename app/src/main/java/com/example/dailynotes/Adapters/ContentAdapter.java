package com.example.dailynotes.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailynotes.Models.Page;
import com.example.dailynotes.R;
import com.example.dailynotes.Data.DailyNotesContracts;
import com.example.dailynotes.Data.DateExtraction;


public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.PageHolder> {
    private Cursor list;
    private Context context;
    private onPageClickListener listener;

    public interface onPageClickListener {
        void onPageClick(int position);

        boolean onLongPageClick(int position);
    }

    public ContentAdapter(Context context) {
        this.context = context;
        this.listener = (onPageClickListener) context;
    }

    @NonNull
    @Override
    public PageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View page = LayoutInflater.from(context).inflate(R.layout.page, parent, false);
        return new PageHolder(page, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PageHolder holder, int position) {
        Page tempPage = getPage(position);
        holder.bind(tempPage);

    }

    private Page getPage(int position) {
        list.moveToPosition(position);
        int titleColIndex = list.getColumnIndex(DailyNotesContracts.databaseEntry.TITLE_COL);
        int descpColIndex = list.getColumnIndex(DailyNotesContracts.databaseEntry.DESCRIPTION_COL);
        int idColIndex = list.getColumnIndex(DailyNotesContracts.databaseEntry.PAGE_ID);
        int dateColIndex = list.getColumnIndex(DailyNotesContracts.databaseEntry.DATE_COL);
        int colorIndex = list.getColumnIndex(DailyNotesContracts.databaseEntry.COLOR_INDEX);
        String title = list.getString(titleColIndex);
        String description = list.getString(descpColIndex);
        int id = list.getInt(idColIndex);
        long date = list.getLong(dateColIndex);
        String color=list.getString(colorIndex);
        return new Page(title, date, description, id, color);
    }

    @Override
    public int getItemCount() {
        if (null == list) return 0;
        return list.getCount();

    }

    public void swap(Cursor list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class PageHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        private TextView title, descp, date;
        private LinearLayout layout;
        private onPageClickListener listener;

        public PageHolder(@NonNull View itemView, onPageClickListener listener) {
            super(itemView);
            this.listener = listener;
            layout = itemView.findViewById(R.id.layout);
            title = layout.findViewById(R.id.page_title);
            descp = layout.findViewById(R.id.page_descp);
            date = layout.findViewById(R.id.page_date);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Page tempPage = getPage(getAdapterPosition());
            listener.onPageClick(tempPage.getId());
        }

        @Override
        public boolean onLongClick(View view) {
            return listener.onLongPageClick(getPage(getAdapterPosition()).getId());
        }


        public void bind(Page page) {
            String description = page.getDescription();
            description = description.replace("\n", "");
            long time = page.getDate();
            title.setText(page.getTitle());
            if (description.isEmpty()) descp.setVisibility(View.GONE);
            else {
                descp.setVisibility(View.VISIBLE);
                if (description.length() < 50)
                    descp.setText(description);
                else descp.setText(description.substring(0, 50) + "........");
            }
            date.setText(DateExtraction.generateDateString(time));
        }
    }
}