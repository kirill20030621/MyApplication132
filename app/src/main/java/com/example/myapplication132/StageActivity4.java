package com.example.myapplication132;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StageActivity4 extends AppCompatActivity {
    private TextView questText;
    private LinearLayout actionButtonsLayout;

    private Button btnEvidence;
    private ArrayList<String> evidences;

    private DatabaseReference mDatabase;
    private int currentStep = 1; // начинаем с первого шага квеста

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage4);

        questText = findViewById(R.id.quest_text);
        actionButtonsLayout = findViewById(R.id.action_buttons_layout);
        btnEvidence = findViewById(R.id.btnEvidence);

        evidences = getIntent().getStringArrayListExtra("evidences");
        if (evidences == null) {
            evidences = new ArrayList<>();
        }


        btnEvidence.setOnClickListener(v -> {
            Intent intent = new Intent(StageActivity4.this, EvidenceActivity.class);
            intent.putStringArrayListExtra("evidences", evidences);
            startActivity(intent);
        });

        changeImage("stage4_1");


        // инициализация Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // загружаем первый шаг
        loadQuestStep(currentStep);
    }

    // загрузка шага квеста
    private void loadQuestStep(int step) {
        mDatabase.child("quest_steps3").child(String.valueOf(step)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String text = dataSnapshot.child("text").getValue(String.class);
                    questText.setText(text);  // обновление текста

                    // обновление кнопок действий
                    Iterable<DataSnapshot> actions = dataSnapshot.child("actions").getChildren();
                    updateActionButtons(actions);
                } else {
                    Toast.makeText(StageActivity4.this, "Конец квеста!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StageActivity4.this, "Ошибка загрузки!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEvidence(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> evidenceSnapshots = dataSnapshot.child("evidence").getChildren();
        for (DataSnapshot evidenceSnapshot : evidenceSnapshots) {
            String evidence = evidenceSnapshot.getValue(String.class);
            //Log.d("Final1121", "Loaded evidence: " + evidence);
            if (evidence != null && !evidence.isEmpty() && !evidences.contains(evidence)) {
                evidences.add(evidence);
            }
        }
    }

    // обновление кнопок действий
    private void updateActionButtons(Iterable<DataSnapshot> actions) {
        actionButtonsLayout.removeAllViews(); // очистка старых кнопок

        for (DataSnapshot actionSnapshot : actions) {
            String actionText = actionSnapshot.child("text").getValue(String.class);
            String actionId = actionSnapshot.child("action_id").getValue(String.class);

            Button actionButton = new Button(this);
            actionButton.setText(actionText);
            actionButton.setOnClickListener(v -> handleAction(actionId));

            actionButtonsLayout.addView(actionButton);  // добавление кнопки на экран
        }
    }

    // Обработка действий
    private void handleAction(String actionId) {
        ImageView imageView = findViewById(R.id.imageView);
        switch (actionId) {
            case "blame_doctor":
                btnEvidence.setVisibility(View.GONE);
                currentStep = 2;
                changeImage("stage4_1");
                break;
            case "blame_guard":
                btnEvidence.setVisibility(View.GONE);
                currentStep = 2;
                changeImage("stage4_1");
                break;
            case "blame_engineer":
                btnEvidence.setVisibility(View.GONE);
                currentStep = 2;
                changeImage("stage4_1");
                break;
            case "blame_assistant":
                btnEvidence.setVisibility(View.GONE);
                currentStep = 2;
                changeImage("stage4_1");
                break;
            case "blame_captain":
                btnEvidence.setVisibility(View.GONE);
                currentStep = 3;
                changeImage("stage4_1");
                break;
            case "option1":
                Intent intent = new Intent(StageActivity4.this, MainActivity.class);
                startActivity(intent);
                break;


            case "option2":
                Intent intent2 = new Intent(StageActivity4.this, MainActivity.class);
                startActivity(intent2);
                break;

            default:
                Toast.makeText(this, "Неизвестное действие!", Toast.LENGTH_SHORT).show();
                return;  // прерывание выполнения, если действие неизвестно
        }

        // загрузка нового шага
        loadQuestStep(currentStep);
    }

    private void changeImage(String imageName) {
        ImageView imageView = findViewById(R.id.imageView);  // Получаем ссылку на ImageView

        // Преобразуем имя изображения в ресурс из папки drawable
        int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (imageResId != 0) {
            imageView.setVisibility(View.VISIBLE);  // Показываем изображение
            imageView.setImageResource(imageResId);  // Загружаем изображение
        }
    }
}