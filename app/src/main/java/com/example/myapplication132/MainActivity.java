package com.example.myapplication132;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private EditText editIntellect, editAttention, editCharm;
    private Button btnSave;

    private FirebaseDatabase database;
    private DatabaseReference playerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация Firebase
        database = FirebaseDatabase.getInstance();
        playerRef = database.getReference("player"); // Ссылка на игрока в базе

        // Поиск элементов интерфейса
        editIntellect = findViewById(R.id.editIntellect);
        editAttention = findViewById(R.id.editAttention);
        editCharm = findViewById(R.id.editCharm);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> savePlayerAttributes());
    }

    private void savePlayerAttributes() {
        // Считывание введенных данных
        String intellectStr = editIntellect.getText().toString();
        String attentionStr = editAttention.getText().toString();
        String charmStr = editCharm.getText().toString();

        if (TextUtils.isEmpty(intellectStr) || TextUtils.isEmpty(attentionStr) || TextUtils.isEmpty(charmStr)) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        int intellect = Integer.parseInt(intellectStr);
        int attention = Integer.parseInt(attentionStr);
        int charm = Integer.parseInt(charmStr);

        // Проверка на сумму не более 10
        if (intellect + attention + charm > 10) {
            Toast.makeText(this, "Сумма характеристик не должна превышать 10", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создание объекта игрока
        Player player = new Player(intellect, attention, charm);

        // Сохранение данных в Firebase
        playerRef.setValue(player)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Характеристики сохранены", Toast.LENGTH_SHORT).show();

                    // Передаем данные о игроке в QuestActivity
                    Intent intent = new Intent(MainActivity.this, QuestActivity.class);
                    intent.putExtra("player", player);  // Передаем объект игрока
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Ошибка сохранения данных", Toast.LENGTH_SHORT).show());
    }
}