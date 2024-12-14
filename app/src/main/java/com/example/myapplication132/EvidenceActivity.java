package com.example.myapplication132;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EvidenceActivity extends AppCompatActivity {
    private LinearLayout evidenceList;
    private Button btnBackToQuest;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidence);

        evidenceList = findViewById(R.id.evidence_list);
        btnBackToQuest = findViewById(R.id.btn_back_to_quest);

        // инициализация Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("evidences");

        loadEvidences();

        // кнопка возвращения
        btnBackToQuest.setOnClickListener(v -> finish());
    }

    private void loadEvidences() {
        // получаем список улик из Intent
        ArrayList<String> evidences = getIntent().getStringArrayListExtra("evidences");

        if (evidences != null && !evidences.isEmpty()) {
            // добавляем все улики в список
            for (String evidence : evidences) {
                addEvidenceToView(evidence);
            }
        } else {
            showEmptyMessage();
        }
    }

    private void addEvidenceToView(String evidence) {
        TextView evidenceText = new TextView(this);
        evidenceText.setText("• " + evidence);
        evidenceText.setTextSize(18);
        evidenceText.setPadding(0, 8, 0, 8);
        evidenceList.addView(evidenceText);
    }

    private void showEmptyMessage() {
        TextView emptyText = new TextView(this);
        emptyText.setText("Улик пока нет.");
        emptyText.setTextSize(18);
        evidenceList.addView(emptyText);
    }
}
