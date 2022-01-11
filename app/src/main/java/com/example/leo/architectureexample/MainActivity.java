package com.example.leo.architectureexample;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.leo.architectureexample.Adapter.NoteAdapter;
import com.example.leo.architectureexample.Model.Note;
import com.example.leo.architectureexample.ViewModel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final int ADD_NOTE_REQUEST = 1;
    private NoteViewModel noteViewModel;

    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        initFabButton();

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Log.d(TAG, "onChanged: called");

                noteAdapter.setNotes(notes);
                noteAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initFabButton() {
        FloatingActionButton fab_add_note = findViewById(R.id.fab_add_note);

        //registerForActivityResult 取代 startActivityForResult
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
                            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
                            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY,1);

                            Note note = new Note(title,description,priority);
                            noteViewModel.insert(note);

                            Toast.makeText(MainActivity.this, "Note Add Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        fab_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Intent you want to start
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
    }
}