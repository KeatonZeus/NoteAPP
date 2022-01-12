package com.example.leo.architectureexample;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.leo.architectureexample.Adapter.NoteAdapter;
import com.example.leo.architectureexample.Interface.ItemClickListener;
import com.example.leo.architectureexample.Model.Note;
import com.example.leo.architectureexample.ViewModel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;

    private NoteViewModel noteViewModel;

    private NoteAdapter noteAdapter;

    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        initFabButton();
        initSwipeListToDeleteItem();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_delet_all_note,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete_all:

                new XPopup.Builder(MainActivity.this).asConfirm("Delete All Notes", "Are you sure?",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                Log.d(TAG, "onConfirm: dialog delete all notes");
                                noteViewModel.deleteAllNotes();
                                //noteAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();

                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void initSwipeListToDeleteItem() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(MainActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(MainActivity.this, "on Swiped Note Deleted", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getBindingAdapterPosition();
                noteViewModel.delete(noteAdapter.getNotePosition(position));
                //noteAdapter.notifyDataSetChanged();
                //noteAdapter.notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(this,this,noteList); //實作介面所以中間那個用this
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "onItemClick: "+ position);
    }

    @Override
    public void onItemLongClick(int position) {
        Log.d(TAG, "onItemLongClick: "+ position);
    }
}