package com.example.myapplication132;

public class Player {
    private int intellect;  // Интеллект
    private int attention;  // Внимательность
    private int charm;      // Обаяние

    public Player() {
        // Пустой конструктор для Firebase
    }

    public Player(int intellect, int attention, int charm) {
        this.intellect = intellect;
        this.attention = attention;
        this.charm = charm;
    }

    public int getIntellect() {
        return intellect;
    }

    public void setIntellect(int intellect) {
        this.intellect = intellect;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getCharm() {
        return charm;
    }

    public void setCharm(int charm) {
        this.charm = charm;
    }

}
