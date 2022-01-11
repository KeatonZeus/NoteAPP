package com.example.leo.architectureexample.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leo.architectureexample.Model.Note;
import com.example.leo.architectureexample.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>{

    private List<Note> noteList = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_note,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_title.setText(noteList.get(position).getTitle());
        holder.txt_description.setText(noteList.get(position).getDescription());
        holder.txt_priority.setText(String.valueOf(noteList.get(position).getPriority()));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    //
    public void setNotes(List<Note> noteList){
        this.noteList = noteList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title;
        private TextView txt_description;
        private TextView txt_priority;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.txt_title);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_priority = itemView.findViewById(R.id.txt_priority);
        }
    }
}
