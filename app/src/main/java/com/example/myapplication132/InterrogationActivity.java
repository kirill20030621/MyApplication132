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

public class InterrogationActivity extends AppCompatActivity {

    Player player;
    private TextView questText;

    private Button btnEvidence;


    private LinearLayout actionButtonsLayout;
    private DatabaseReference mDatabase;

    private ArrayList<String> evidences;
    private int currentStep = 1; // Начинаем с первого шага квеста

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interrogation);

        player = (Player) getIntent().getSerializableExtra("player");
        if (player == null) {
            player = new Player();  // Если объект не был передан, создаем новый
        }

        questText = findViewById(R.id.quest_text);
        actionButtonsLayout = findViewById(R.id.action_buttons_layout);
        btnEvidence = findViewById(R.id.btnEvidence);
        evidences = new ArrayList<>(); //





        btnEvidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterrogationActivity.this, EvidenceActivity.class);
                intent.putStringArrayListExtra("evidences", evidences); // Передаем список улик
                startActivity(intent);
            }
        });



        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadQuestStep(currentStep);
    }

    private void loadQuestStep(int step) {
        mDatabase.child("quest_steps2").child(String.valueOf(step)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String text = dataSnapshot.child("text").getValue(String.class);
                    questText.setText(text);
                    // Загрузка улик для текущего шага
                    loadEvidence(dataSnapshot);

                    // Обновление кнопок действий
                    Iterable<DataSnapshot> actions = dataSnapshot.child("actions").getChildren();
                    updateActionButtons(dataSnapshot.child("actions").getChildren());
                } else {
                    Toast.makeText(InterrogationActivity.this, "Конец квеста!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(InterrogationActivity.this, "Ошибка загрузки!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEvidence(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> evidenceSnapshots = dataSnapshot.child("evidence").getChildren();
        for (DataSnapshot evidenceSnapshot : evidenceSnapshots) {
            String evidence = evidenceSnapshot.getValue(String.class);
            Log.d("InterActivity1121", "Loaded evidence: " + evidence);
            if (evidence != null && !evidence.isEmpty()) {
                evidences.add(evidence); // Добавляем улику в список
            }
        }
    }

    private void updateActionButtons(Iterable<DataSnapshot> actions) {
        actionButtonsLayout.removeAllViews();

        for (DataSnapshot actionSnapshot : actions) {
            String actionText = actionSnapshot.child("text").getValue(String.class);
            String actionId = actionSnapshot.child("action_id").getValue(String.class);

            Button actionButton = new Button(this);
            actionButton.setText(actionText);
            actionButton.setOnClickListener(v -> handleAction(actionId));

            actionButtonsLayout.addView(actionButton);
        }
    }
    // Обработка действий
    private void handleAction(String actionId) {
        switch (actionId) {
            case "back_to_suspects":
                currentStep = 1;  // Переход к следующему шагу
                break;

            case "back":
                currentStep = 2;  // Переход к следующему шагу
                break;
            case "back2":
                currentStep = 6;  // Переход к следующему шагу
                break;
            case "back3":
                currentStep = 10;  // Переход к следующему шагу
                break;
            case "back4":
                currentStep = 14;  // Переход к следующему шагу
                break;
            case "back5":
                currentStep = 18;  // Переход к следующему шагу
                break;

            case "option1":
                currentStep = 3;  // Переход к следующему шагу
                break;
            case "option2":
                Log.d("playerintel4", "Player intellect: " + player.getIntellect());
                if(player.checkIntelect()){
                    currentStep = 4;  // Переход к сцене осмотра устройства
                    break;}
                else {
                    currentStep = 5;
                    break;
                }
            case "option3":
                currentStep = 7;  // Переход к следующему шагу
                break;
            case "option4":
                if(player.checkAttention()){
                    currentStep = 8;  // Переход к сцене осмотра устройства
                    break;}
                else {
                    currentStep = 9;
                    break;
                }
            case "option5":
                currentStep = 11;  // Переход к следующему шагу
                break;
            case "option6":
                if(player.checkCharm()){
                    currentStep = 12;  // Переход к сцене осмотра устройства
                    break;}
                else {
                    currentStep = 13;
                    break;
                }
            case "option7":
                currentStep = 15;  // Переход к следующему шагу
                break;
            case "option8":
                if(player.checkIntelect()){
                    currentStep = 16;  // Переход к сцене осмотра устройства
                    break;}
                else {
                    currentStep = 17;
                    break;
                }
            case "option9":
                currentStep = 19;  // Переход к следующему шагу
                break;
            case "option10":
                if(player.checkCharm()){
                    currentStep = 20;  // Переход к сцене осмотра устройства
                    break;}
                else {
                    currentStep = 21;
                    break;
                }

            case "go_doctor":
                currentStep = 2;  // Переход к следующему шагу
                break;
            case "go_guard":
                currentStep = 6;  // Переход к следующему шагу
                break;
            case "go_engineer":
                currentStep = 10;  // Переход к следующему шагу
                break;
            case "go_assistant":
                currentStep = 14;  // Переход к следующему шагу
                break;
            case "go_captain":
                currentStep = 18;  // Переход к следующему шагу
                break;

            case "report_culprit":
                Intent intent = new Intent(InterrogationActivity.this, FinalActivity.class);
                startActivity(intent);
                finish();
                break;


            case "go_back":
                Intent intent2 = new Intent(InterrogationActivity.this, QuestActivity.class);
                startActivity(intent2);
                finish();

                break;




            default:
                Toast.makeText(this, "Неизвестное действие!", Toast.LENGTH_SHORT).show();
                return;  // Прерывание выполнения, если действие неизвестно
        }

        // Загрузка нового шага
        loadQuestStep(currentStep);
    }

}
