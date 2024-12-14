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

public class StageActivity1 extends AppCompatActivity {

    Player player;

    private TextView questText;
    private LinearLayout actionButtonsLayout;
    private DatabaseReference mDatabase;
    private int currentStep = 1; // Начало шага


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage1);

        questText = findViewById(R.id.quest_text);
        actionButtonsLayout = findViewById(R.id.action_buttons_layout);

       // player = (Player) getIntent().getSerializableExtra("player");
        //if (player == null) {
        //    player = new Player();  // Если объект не был передан, создаем новый
        //}


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("player")) {
            player = (Player) intent.getSerializableExtra("player");  // Получаем переданный объект
        }

        //Intent intent2 = new Intent(StageActivity1.this, StageActivity2.class);
        //intent.putExtra("player", player); // Передаем объект player


        // инициализация Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // загружаем первый шаг
        loadQuestStep(currentStep);
    }

    // Загрузка шага квеста
    private void loadQuestStep(int step) {
        mDatabase.child("start").child(String.valueOf(step)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String text = dataSnapshot.child("text").getValue(String.class);
                    questText.setText(text);  // обновление текста



                    // обновление кнопок действий
                    Iterable<DataSnapshot> actions = dataSnapshot.child("actions").getChildren();
                    updateActionButtons(actions);
                } else {
                    Toast.makeText(StageActivity1.this, "Конец квеста!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StageActivity1.this, "Ошибка загрузки!", Toast.LENGTH_SHORT).show();
            }
        });
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
            case "go":
                currentStep++;  // переход к следующему шагу
                break;

            case "go_to_next_step":
                Intent intent = new Intent(StageActivity1.this, StageActivity2.class);
                intent.putExtra("player", player);
                startActivity(intent);
                finish();
                break;

            default:
                Toast.makeText(this, "Неизвестное действие!", Toast.LENGTH_SHORT).show();
                return;  // прерывание выполнения, если действие неизвестно
        }

        // Загрузка нового шага
        loadQuestStep(currentStep);
    }
}