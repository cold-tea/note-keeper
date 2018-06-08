package com.heavenlyhell.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<NoteInfo> notes;

    public NoteRecyclerAdapter(Context context, List<NoteInfo> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_note_list,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNoteTitle.setText(notes.get(position).getCourse().getTitle());
        holder.tvCourseTitle.setText(notes.get(position).getTitle());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView tvNoteTitle;
        public final TextView tvCourseTitle;
        public int position;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tvCourseTitle);
            tvCourseTitle = itemView.findViewById(R.id.tvNoteTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentNote = new Intent(context, NoteActivity.class);
                    intentNote.putExtra(NoteListActivity.NOTE_POSITION, position);
                    context.startActivity(intentNote);
                }
            });
        }
    }
}
