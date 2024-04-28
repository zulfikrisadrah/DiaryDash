package com.example.myapplication;

import java.util.ArrayList;
import java.util.Date;

public class DataSource {
    public static ArrayList<Diary> diaries = generateDummyChats();
    private static ArrayList<Diary> generateDummyChats(){
        ArrayList<Diary> diaries = new ArrayList<>();
        diaries.add(new Diary("Pemrogrman Mobile", "Tugas membuat aplikasi", new Date()));
        return diaries;
    }
}
