package com.example.myapplication.sort;

import com.example.myapplication.Memoir;

import java.util.Comparator;

public class sortRelease implements Comparator<Memoir> {
    @Override
    public int compare(Memoir o1, Memoir o2) {
        return o1.getRating().compareTo(o2.getRating());
    }
}
