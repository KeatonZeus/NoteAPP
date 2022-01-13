package com.example.leo.architectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID =  "com.example.leo.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.leo.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.leo.architectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.leo.architectureexample.EXTRA_PRIORITY";

    private EditText edt_title,edt_description;
    private NumberPicker numberPicker_priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initView();

        //action bar 左邊
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        getSupportActionBar().setHomeActionContentDescription("CLOSE");

        //資料有ID ? 做Edit : 做Add
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            edt_title.setText(intent.getStringExtra(EXTRA_TITLE));
            edt_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPicker_priority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }else {
            setTitle("Add Note");
        }
    }

    private void initView() {
        edt_title = findViewById(R.id.edt_title);
        edt_description = findViewById(R.id.edt_description);
        numberPicker_priority = findViewById(R.id.numberPicker_priority);

        numberPicker_priority.setMaxValue(5);
        numberPicker_priority.setMinValue(1);
    }

    //get note from edt and numPicker, using startActivityForResult
    private void saveNote(){
        String title = edt_title.getText().toString();
        String description = edt_description.getText().toString();
        int priority = numberPicker_priority.getValue();

        //trim(), 刪除字的前後空白, 有不合格式的就showToast
        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please inert a Title or Description", Toast.LENGTH_SHORT).show();
            return; //跳過不合格的繼續下面程式碼, 類似if else寫法
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        //取得id 丟回MainActivity
        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if (id != -1){
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_save_note,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}