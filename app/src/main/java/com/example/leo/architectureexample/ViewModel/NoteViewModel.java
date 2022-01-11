package com.example.leo.architectureexample.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.leo.architectureexample.Model.Note;
import com.example.leo.architectureexample.Repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    // Extending AndroidViewModel but not ViewModel because we want Application and its context
    // We should not pass context of activity as view model has to outlive the activity (doing this can cause memory leaks)

    private NoteRepository noteRepo;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);

        noteRepo = new NoteRepository(application);
        allNotes = noteRepo.getAllNotes();
    }

    public void insert(Note note){
        noteRepo.insert(note);
    }

    public void update(Note note){
        noteRepo.update(note);
    }

    public void delete(Note note){
        noteRepo.delete(note);
    }

    public void deleteAllNotes(){
        noteRepo.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
}
