package com.example.myapplication;
import android.widget.ImageView;

public class cell {
    public int row, col;
    public boolean isPath = false;
    public boolean isBranch = false;
    public boolean isStart = false;
    public boolean isEnd = false;
    public String content = "";
    public cell(int r, int c) {
        row = r;
        col = c;
    }
}
