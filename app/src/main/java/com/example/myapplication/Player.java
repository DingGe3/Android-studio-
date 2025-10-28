package com.example.myapplication;

import java.util.ArrayList;

public class Player {
    // 当前棋子坐标
    private int currentRow;
    private int currentCol;

    // 当前棋子行走的路径
    private ArrayList<int[]> path;

    // 用来记录每个格子的content（暂时不使用）
    private ArrayList<String> contentRecord;

    // 构造函数
    public Player(ArrayList<int[]> path) {
        this.path = path;
        this.contentRecord = new ArrayList<>();
        // 默认从路径第一个格子开始
        if (path != null && !path.isEmpty()) {
            this.currentRow = path.get(0)[0];
            this.currentCol = path.get(0)[1];
        } else {
            this.currentRow = 0;
            this.currentCol = 0;
        }
    }

    // Getter & Setter
    public int getCurrentRow() {
        return currentRow;
    }

    public int getCurrentCol() {
        return currentCol;
    }

    public void setCurrentRow(int row) {
        this.currentRow = row;
    }

    public void setCurrentCol(int col) {
        this.currentCol = col;
    }

    public ArrayList<int[]> getPath() {
        return path;
    }

    public void setPath(ArrayList<int[]> path) {
        this.path = path;
    }

    public ArrayList<String> getContentRecord() {
        return contentRecord;
    }

    public void setContentRecord(ArrayList<String> contentRecord) {
        this.contentRecord = contentRecord;
    }

    // 移动棋子到下一个格子
    public void moveToStep(int step) {
        if (path != null && step >= 0 && step < path.size()) {
            this.currentRow = path.get(step)[0];
            this.currentCol = path.get(step)[1];
        }
    }
}
