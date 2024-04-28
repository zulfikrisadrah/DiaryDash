package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {

    private ArrayList<Diary> diaries;
    private Context context;

    public DiaryAdapter(Context context, ArrayList<Diary> diaries) {
        this.context = context;
        this.diaries = diaries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryAdapter.ViewHolder holder, int position) {
        Diary diary = diaries.get(position);

        holder.title.setText(diary.getTitle());
        holder.notes.setText(diary.getNotes());
        holder.timestamp.setText(formatDate(diary.getTimestamp()));

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    showDeleteConfirmationDialog(adapterPosition);
                }
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    showReadDialog(adapterPosition);
                }
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    showEditDialog(adapterPosition);
                }
            }
        });
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    @Override
    public int getItemCount() {
        return diaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, notes, timestamp;
        private ImageView deleteBtn, editBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.judul);
            notes = itemView.findViewById(R.id.notes);
            timestamp = itemView.findViewById(R.id.timestamp);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
        }
    }

    private void showDeleteConfirmationDialog(int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_dialog_bg));

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnDelete = dialog.findViewById(R.id.btn_delete);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaries.remove(position);
                notifyItemRemoved(position);
                dialog.dismiss();
                Toast.makeText(context, "Diary Berhasil Dihapus", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showReadDialog(int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_read_diary);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView inputJudul = dialog.findViewById(R.id.inputJudul);
        TextView inputDiary = dialog.findViewById(R.id.inputDiary);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        Diary selectedDiary = diaries.get(position);
        inputJudul.setText(selectedDiary.getTitle());
        inputDiary.setText(selectedDiary.getNotes());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showEditDialog(int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_edit_diary);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText inputJudul = dialog.findViewById(R.id.inputJudul);
        EditText inputDiary = dialog.findViewById(R.id.inputDiary);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnSave = dialog.findViewById(R.id.btn_submit);

        Diary selectedDiary = diaries.get(position);
        inputJudul.setText(selectedDiary.getTitle());
        inputDiary.setText(selectedDiary.getNotes());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newJudul = inputJudul.getText().toString().trim();
                String newNotes = inputDiary.getText().toString().trim();

                if (newJudul.isEmpty()) {
                    inputJudul.setError("Judul tidak boleh kosong");
                    return;
                }  else if (newJudul.length() > 30) {
                    inputJudul.setError("Judul tidak boleh lebih dari 30 karakter");
                    return;

                } else if (newNotes.isEmpty()) {
                    inputDiary.setError("Isi notes tidak boleh kosong");
                    return;
                }

                selectedDiary.setTitle(newJudul);
                selectedDiary.setNotes(newNotes);

                notifyItemChanged(position);

                dialog.dismiss();

                Toast.makeText(context, "Diary Berhasil Diperbarui", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

}
