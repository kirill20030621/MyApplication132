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

public class StageActivity3 extends AppCompatActivity {

    Player player;
    private TextView questText;
    private Button btnEvidence;
    private LinearLayout actionButtonsLayout;
    private DatabaseReference mDatabase;
    private ArrayList<String> evidences;
    private int currentStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage3);

        evidences = getIntent().getStringArrayListExtra("evidences");
        if (evidences == null) {
            evidences = new ArrayList<>();
        }
        player = (Player) getIntent().getSerializableExtra("player");
        if (player == null) {
            player = new Player();
        }

        questText = findViewById(R.id.quest_text);
        actionButtonsLayout = findViewById(R.id.action_buttons_layout);
        btnEvidence = findViewById(R.id.btnEvidence);

        btnEvidence.setOnClickListener(v -> {
            Intent intent = new Intent(StageActivity3.this, EvidenceActivity.class);
            intent.putStringArrayListExtra("evidences", evidences);
            startActivity(intent);
        });

        changeImage("stage2_3");

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
                    loadEvidence(dataSnapshot);
                    updateActionButtons(dataSnapshot.child("actions").getChildren());
                } else {
                    Toast.makeText(StageActivity3.this, "Конец квеста!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StageActivity3.this, "Ошибка загрузки!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEvidence(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> evidenceSnapshots = dataSnapshot.child("evidence").getChildren();
        for (DataSnapshot evidenceSnapshot : evidenceSnapshots) {
            String evidence = evidenceSnapshot.getValue(String.class);
            //Log.d("InterActivity1121", "Loaded evidence: " + evidence);
            if (evidence != null && !evidence.isEmpty() && !evidences.contains(evidence)) {
                evidences.add(evidence);
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

    // обработка действий
    private void handleAction(String actionId) {
        ImageView imageView = findViewById(R.id.imageView);
        switch (actionId) {
            case "back_to_suspects":
                currentStep = 1;
                changeImage("stage2_3");
                break;

            case "back":
                currentStep = 2;
                changeImage("stage3_2");
                break;
            case "back2":
                currentStep = 6;
                break;
            case "back3":
                currentStep = 10;
                changeImage("stage3_10");
                break;
            case "back4":
                currentStep = 14;
                changeImage("stage3_14");
                break;
            case "back5":
                currentStep = 18;
                break;

            case "option1":
                currentStep = 3;
                changeImage("stage3_2");
                break;
            case "option2":

                if(player.checkIntelect()){
                    currentStep = 4;
                    changeImage("stage3_2");
                    break;}
                else {
                    currentStep = 5;
                    changeImage("stage3_2");
                    break;
                }
            case "option3":
                currentStep = 7;
                break;
            case "option4":

                if(player.checkAttention()){
                    currentStep = 8;
                    break;}
                else {
                    currentStep = 9;
                    break;
                }
            case "option5":
                currentStep = 11;
                changeImage("stage3_10");
                break;
            case "option6":
                if(player.checkCharm()){
                    currentStep = 12;
                    changeImage("stage3_10");
                    break;}
                else {
                    currentStep = 13;
                    changeImage("stage3_10");
                    break;
                }
            case "option7":
                currentStep = 15;
                changeImage("stage3_14");
                break;
            case "option8":
                if(player.checkIntelect()){
                    currentStep = 16;
                    changeImage("stage3_14");
                    break;}
                else {
                    currentStep = 17;
                    changeImage("stage3_14");
                    break;
                }
            case "option9":
                currentStep = 19;
                break;
            case "option10":
                if(player.checkCharm()){
                    currentStep = 20;
                    break;}
                else {
                    currentStep = 21;
                    break;
                }

            case "go_doctor":
                currentStep = 2;
                changeImage("stage3_2");

                break;
            case "go_guard":
                currentStep = 6;
                break;
            case "go_engineer":
                currentStep = 10;
                changeImage("stage3_10");
                break;
            case "go_assistant":
                currentStep = 14;
                changeImage("stage3_14");
                break;
            case "go_captain":
                currentStep = 18;
                break;

            case "report_culprit":
                Intent intent = new Intent(StageActivity3.this, StageActivity4.class);
                intent.putStringArrayListExtra("evidences", evidences);
                startActivity(intent);

                break;


            case "go_back":
                Intent intent2 = new Intent(StageActivity3.this, StageActivity2.class);
                intent2.putExtra("player", player); // передаем объект player
                intent2.putStringArrayListExtra("evidences", evidences);
                startActivity(intent2);
                break;

            default:
                Toast.makeText(this, "Неизвестное действие!", Toast.LENGTH_SHORT).show();
                return;  // Прерывание выполнения, если действие неизвестно
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
