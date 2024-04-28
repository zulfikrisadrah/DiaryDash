package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        rv_notes = findViewById(R.id.rv_notes);
        rv_notes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DiaryAdapter diaryAdapter = new DiaryAdapter(this, DataSource.diaries);
        rv_notes.setAdapter(diaryAdapter);

        ImageView addNotes = findViewById(R.id.addNotes);
        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDiaryDialog();
            }
        });
    }

    private void showAddDiaryDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_add_diary);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText inputJudul = dialog.findViewById(R.id.inputJudul);
        EditText inputDiary = dialog.findViewById(R.id.inputDiary);
        Button submitBtn = dialog.findViewById(R.id.btn_submit);
        Button cancelBtn = dialog.findViewById(R.id.btn_cancel);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String judul = inputJudul.getText().toString().trim();
                String diary = inputDiary.getText().toString().trim();

                if (judul.isEmpty()) {
                    inputJudul.setError("Judul tidak boleh kosong");
                    return;
                } else if (judul.length() > 30) {
                    inputJudul.setError("Judul tidak boleh lebih dari 30 karakter");
                    return;
                }

                if (diary.isEmpty()) {
                    inputDiary.setError("Isi notes tidak boleh kosong");
                    return;
                }

                DataSource.diaries.add(0, new Diary(judul, diary, new Date()));
                rv_notes.getAdapter().notifyDataSetChanged();
                dialog.dismiss();

                Toast.makeText(MainActivity.this, "Diary Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
