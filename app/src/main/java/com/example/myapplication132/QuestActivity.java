package com.example.myapplication132;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class QuestActivity extends AppCompatActivity {

    Player player;
    private Button btnEvidence;
    private TextView questText;
    private LinearLayout actionButtonsLayout;
    private DatabaseReference mDatabase;
    private int currentStep = 1; // Начинаем с первого шага квеста
    private ArrayList<String> evidences; // Список улик для передачи в EvidenceActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        questText = findViewById(R.id.quest_text);
        actionButtonsLayout = findViewById(R.id.action_buttons_layout);
        btnEvidence = findViewById(R.id.btnEvidence);
        evidences = new ArrayList<>(); // Инициализируем список улик

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("player")) {
            player = (Player) intent.getSerializableExtra("player");  // Получаем переданный объект
        }

        Intent intent2 = new Intent(QuestActivity.this, InterrogationActivity.class);
        intent.putExtra("player", player); // Передаем объект player


        btnEvidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Передаем список улик в EvidenceActivity
                Intent intent = new Intent(QuestActivity.this, EvidenceActivity.class);
                intent.putStringArrayListExtra("evidences", evidences); // Передаем список улик
                startActivity(intent);
            }
        });

        // Инициализация Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Загружаем первый шаг
        loadQuestStep(currentStep);
    }

    // Загрузка шага квеста
    private void loadQuestStep(int step) {
        mDatabase.child("quest_steps").child(String.valueOf(step)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String text = dataSnapshot.child("text").getValue(String.class);
                    questText.setText(text);  // Обновление текста

                    // Загрузка улик для текущего шага
                    loadEvidence(dataSnapshot);

                    // Обновление кнопок действий
                    Iterable<DataSnapshot> actions = dataSnapshot.child("actions").getChildren();
                    updateActionButtons(actions);
                } else {
                    Toast.makeText(QuestActivity.this, "Конец квеста!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(QuestActivity.this, "Ошибка загрузки!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Загрузка улик для текущего шага
    private void loadEvidence(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> evidenceSnapshots = dataSnapshot.child("evidence").getChildren();
        for (DataSnapshot evidenceSnapshot : evidenceSnapshots) {
            String evidence = evidenceSnapshot.getValue(String.class);
            Log.d("QuestActivity1121", "Loaded evidence: " + evidence);
            if (evidence != null && !evidence.isEmpty()) {
                evidences.add(evidence); // Добавляем улику в список
            }
        }
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
            case "go_to_next_step":
                currentStep++;  // Переход к следующему шагу
                break;

            case "go_for_interrogation":
                Intent intent = new Intent(QuestActivity.this, InterrogationActivity.class);
                startActivity(intent);
                finish();

                break;

            case "back_to_lab":
                currentStep = 3;  // Переход к сцене осмотра устройства
                break;

            case "inspect_device":
                currentStep = 4;  // Переход к сцене осмотра устройства
                break;
            case "option1":
                currentStep = 5;  // Переход к сцене осмотра устройства
                break;
            case "option2":
                //Log.d("playerintel4", "Player intellect: " + player.getIntellect());
                if(player.checkIntelect()){
                    currentStep = 6;  // Переход к сцене осмотра устройства
                    break;}
                else {
                    currentStep = 7;
                    break;
                }
            case "back":
                currentStep = 4;  // Переход к сцене осмотра устройства
                break;

            case "check_floor":
                currentStep = 8;  // Переход к сцене осмотра устройства
                break;
            case "option3":
                currentStep = 9;  // Переход к сцене осмотра устройства
                break;
            case "option4":
                if(player.checkAttention()){
                    currentStep = 10;  // Переход к сцене осмотра устройства
                    break;}
                else {
                    currentStep = 11;
                    break;
                }
            case "back2":
                currentStep = 8;  // Переход к сцене осмотра устройства
                break;

            case "talk_to_guard":
                currentStep = 12;  // Переход к сцене осмотра устройства
                break;
            case "option5":
                currentStep = 13;  // Переход к сцене осмотра устройства
                break;
            case "option6":
                if(player.checkCharm()){
                    currentStep = 14;  // Переход к сцене осмотра устройства
                    break;}
                else {
                    currentStep = 15;
                    break;
                }
            case "back3":
                currentStep = 12;  // Переход к сцене осмотра устройства
                break;

            default:
                Toast.makeText(this, "Неизвестное действие!", Toast.LENGTH_SHORT).show();
                return;  // Прерывание выполнения, если действие неизвестно
        }

        // Загрузка нового шага
        loadQuestStep(currentStep);
    }
}
