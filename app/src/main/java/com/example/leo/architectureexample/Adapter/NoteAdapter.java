package com.example.leo.architectureexample.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leo.architectureexample.Interface.ItemClickListener;
import com.example.leo.architectureexample.Model.Note;
import com.example.leo.architectureexample.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.MyViewHolder> {

    private static final String TAG = "NoteAdapter";

    private Context context;
    private ItemClickListener itemClickListener;

    public NoteAdapter(Context context, ItemClickListener itemClickListener) {
        super(NoteAdapter.DIFF_CALLBACK);
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Note>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Note oldNote, @NonNull Note newNote) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldNote.getId() == newNote.getId();
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Note oldNote, @NonNull Note newNote) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldNote.getTitle().equals(newNote.getTitle()) &&
                            oldNote.getDescription().equals(newNote.getDescription()) &&
                            oldNote.getPriority() == newNote.getPriority();
                }
            };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_note,parent,false);
        return new MyViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_title.setText(getItem(position).getTitle());
        holder.txt_description.setText(getItem(position).getDescription());
        holder.txt_priority.setText(String.valueOf(getItem(position).getPriority()));
    }

    //get the position in list
    public Note getNotePosition(int position){
        return getItem(position);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title;
        private TextView txt_description;
        private TextView txt_priority;

        public MyViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.txt_title);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_priority = itemView.findViewById(R.id.txt_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener != null){
                        int position = getBindingAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            itemClickListener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(itemClickListener != null){
                        int position = getBindingAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            itemClickListener.onItemLongClick(position);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
