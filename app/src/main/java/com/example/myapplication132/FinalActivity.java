package com.example.myapplication132;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FinalActivity extends AppCompatActivity {
    private TextView questText;
    private LinearLayout actionButtonsLayout;
    private DatabaseReference mDatabase;
    private int currentStep = 1; // Начинаем с первого шага квеста

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        questText = findViewById(R.id.quest_text);
        actionButtonsLayout = findViewById(R.id.action_buttons_layout);

        // Инициализация Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Загружаем первый шаг
        loadQuestStep(currentStep);
    }

    // Загрузка шага квеста
    private void loadQuestStep(int step) {
        mDatabase.child("quest_steps3").child(String.valueOf(step)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String text = dataSnapshot.child("text").getValue(String.class);
                    questText.setText(text);  // Обновление текста

                    // Обновление кнопок действий
                    Iterable<DataSnapshot> actions = dataSnapshot.child("actions").getChildren();
                    updateActionButtons(actions);
                } else {
                    Toast.makeText(FinalActivity.this, "Конец квеста!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FinalActivity.this, "Ошибка загрузки!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Обновление кнопок действий
    private void updateActionButtons(Iterable<DataSnapshot> actions) {
        actionButtonsLayout.removeAllViews(); // Очистка старых кнопок

        for (DataSnapshot actionSnapshot : actions) {
            String actionText = actionSnapshot.child("text").getValue(String.class);
            String actionId = actionSnapshot.child("action_id").getValue(String.class);

            Button actionButton = new Button(this);
            actionButton.setText(actionText);
            actionButton.setOnClickListener(v -> handleAction(actionId));

            actionButtonsLayout.addView(actionButton);  // Добавление кнопки на экран
        }
    }

    // Обработка действий
    private void handleAction(String actionId) {
        switch (actionId) {
            case "blame_doctor":
                currentStep = 2;  // Переход к следующему шагу
                break;
            case "blame_guard":
                currentStep = 2;  // Переход к следующему шагу
                break;
            case "blame_engineer":
                currentStep = 2;  // Переход к следующему шагу
                break;
            case "blame_assistant":
                currentStep = 2;  // Переход к следующему шагу
                break;
            case "blame_captain":
                currentStep = 3;  // Переход к следующему шагу
                break;
            case "option1":
                Intent intent = new Intent(FinalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case "option2":
                currentStep = 12;  // Переход к следующему шагу
                break;






            default:
                Toast.makeText(this, "Неизвестное действие!", Toast.LENGTH_SHORT).show();
                return;  // Прерывание выполнения, если действие неизвестно
        }

        // Загрузка нового шага
        loadQuestStep(currentStep);
    }
}