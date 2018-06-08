package com.heavenlyhell.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private static final String TAG = "NoteListActivity";
    public static final String NOTE_POSITION = "notePosition";
    private NoteRecyclerAdapter adapter;

//    private ListView lvNotes;
//    private ArrayAdapter<NoteInfo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
            }
        });

//        lvNotes = findViewById(R.id.lvNotes);
        setUpListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    private void setUpListView() {
        /*List<NoteInfo> notes = DataManager.getInstance().getNotes();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, notes);
        lvNotes.setAdapter(adapter);
        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentNote = new Intent(NoteListActivity.this, NoteActivity.class);
                NoteInfo noteInfo = (NoteInfo) lvNotes.getItemAtPosition(position);
                intentNote.putExtra(NOTE_POSITION, position);
                startActivity(intentNote);
            }
        });*/

        final RecyclerView rvNotes = findViewById(R.id.rvNotes);
        adapter = new NoteRecyclerAdapter(this,
                DataManager.getInstance().getNotes());
        rvNotes.setHasFixedSize(true);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setAdapter(adapter);
    }

}
