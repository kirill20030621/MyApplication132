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

public class StageActivity2 extends AppCompatActivity {

    Player player;
    private Button btnEvidence;
    private TextView questText;
    private LinearLayout actionButtonsLayout;
    private DatabaseReference mDatabase;
    private int currentStep = 3; // начинаем с первого шага квеста
    private ArrayList<String> evidences; // список улик для передачи в EvidenceActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage2);

        questText = findViewById(R.id.quest_text);
        actionButtonsLayout = findViewById(R.id.action_buttons_layout);
        btnEvidence = findViewById(R.id.btnEvidence);

        evidences = getIntent().getStringArrayListExtra("evidences");
        if (evidences == null) {
            evidences = new ArrayList<>();
        }
        player = (Player) getIntent().getSerializableExtra("player");
        if (player == null) {
            player = new Player();  // eсли объект не был передан, создаем новый
        }

        btnEvidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // передаем список улик в EvidenceActivity
                Intent intent = new Intent(StageActivity2.this, EvidenceActivity.class);
                intent.putStringArrayListExtra("evidences", evidences); // Передаем список улик
                startActivity(intent);
            }
        });

        // инициализация Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // загружаем первый шаг
        loadQuestStep(currentStep);
    }

    // загрузка шага квеста
    private void loadQuestStep(int step) {
        mDatabase.child("quest_steps").child(String.valueOf(step)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String text = dataSnapshot.child("text").getValue(String.class);
                    questText.setText(text);  // обновление текста

                    // загрузка улик для текущего шага
                    loadEvidence(dataSnapshot);

                    // обновление кнопок действий
                    Iterable<DataSnapshot> actions = dataSnapshot.child("actions").getChildren();
                    updateActionButtons(actions);
                } else {
                    Toast.makeText(StageActivity2.this, "Конец квеста!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StageActivity2.this, "Ошибка загрузки!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // загрузка улик для текущего шага
    private void loadEvidence(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> evidenceSnapshots = dataSnapshot.child("evidence").getChildren();
        for (DataSnapshot evidenceSnapshot : evidenceSnapshots) {
            String evidence = evidenceSnapshot.getValue(String.class);
            Log.d("QuestActivity1121", "Loaded evidence: " + evidence);
            if (evidence != null && !evidence.isEmpty() && !evidences.contains(evidence)) {
                evidences.add(evidence); // добавляем улику в список, если ее там еще нет
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
        switch (actionId) {
            case "go_to_next_step":
                currentStep++;  // переход к следующему шагу
                break;

            case "go_for_interrogation":
                Intent intent = new Intent(StageActivity2.this, StageActivity3.class);
                intent.putExtra("player", player); // передаем объект player
                intent.putStringArrayListExtra("evidences", evidences);
                startActivity(intent);
                break;

            case "back_to_lab":
                currentStep = 3;
                break;
            case "inspect_device":
                currentStep = 4;
                break;
            case "option1":
                currentStep = 5;
                break;
            case "option2":
                //Log.d("playerintel4", "Player intellect: " + player.getIntellect());
                if(player.checkIntelect()){
                    currentStep = 6;
                    break;}
                else {
                    currentStep = 7;
                    break;
                }
            case "back":
                currentStep = 4;
                break;
            case "check_floor":
                currentStep = 8;
                break;
            case "option3":
                currentStep = 9;
                break;
            case "option4":
                if(player.checkAttention()){
                    currentStep = 10;
                    break;}
                else {
                    currentStep = 11;
                    break;
                }
            case "back2":
                currentStep = 8;
                break;
            case "talk_to_guard":
                currentStep = 12;
                break;
            case "option5":
                currentStep = 13;
                break;
            case "option6":
                if(player.checkCharm()){
                    currentStep = 14;
                    break;}
                else {
                    currentStep = 15;
                    break;
                }
            case "back3":
                currentStep = 12;
                break;
            default:
                Toast.makeText(this, "Неизвестное действие!", Toast.LENGTH_SHORT).show();
                return;  // прерывание выполнения, если действие неизвестно
        }

        // Загрузка нового шага
        loadQuestStep(currentStep);
    }
}
