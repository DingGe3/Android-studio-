package com.example.myapplication;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;
import android.content.Intent;
import android.widget.Button;

public class BoardActivity extends AppCompatActivity {

    private final int ROWS = 10;
    private final int COLS = 10;
    private cell[][] board = new cell[ROWS][COLS];
    private GridLayout gridLayout;
    private ArrayList<String> contentList;

    private ArrayList<int[]> mainPathList;
    private ArrayList<int[]> branchPathList;

    private Player player;
    private int currentStep = -1; // 当前路径步数，-1表示尚未沿路径开始移动

    private Button rollDiceBtn;
    private TextView diceResult;
    private TextView playerView;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);

        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setRowCount(ROWS);
        gridLayout.setColumnCount(COLS);

        rollDiceBtn = findViewById(R.id.rollDiceBtn);
        diceResult = findViewById(R.id.diceResult);

        Button regenerateButton = findViewById(R.id.regenBtn);
        regenerateButton.setOnClickListener(v -> regenbtn());

        Intent intent = getIntent();
        contentList = intent.getStringArrayListExtra(MainActivity.EXTRA_INPUT_LIST);

        initcontentlist();
        initboard();
        generatepath();
        fillboardcontents();
        renderboard();

        rollDiceBtn.setOnClickListener(v -> {
            int dice = random.nextInt(3) + 1;
            diceResult.setText("骰子点数: " + dice);
            movePlayer(dice); // 自动处理路径选择或移动
        });
    }

    private void initcontentlist() {
        // 可填充contentList内容
    }

    private void regenbtn() {
        // 重置棋盘
        initboard();
        generatepath();
        fillboardcontents();
        renderboard();

        // 移除玩家
        if (playerView != null) {
            gridLayout.removeView(playerView);
            playerView = null;
        }

        // 重置玩家对象和路径步数
        player = null;
        currentStep = -1;

        // 重置骰子按钮和提示
        rollDiceBtn.setEnabled(true);
        diceResult.setText("骰子点数: ");
    }

    private void initboard() {
        for (int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                board[i][j] = new cell(i,j);
            }
        }
    }

    private void generatepath() {
        // 清空格子
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++){
                board[i][j].isPath = false;
                board[i][j].isStart = false;
                board[i][j].isEnd = false;
                board[i][j].isBranch = false;
            }
        }

        // 三套预设路径
        int[][] mainPath1 = {{0,1},{0,2},{0,3},{0,4},{1,4},{2,4},{3,4},{3,5},{3,6},{3,7},{3,8},{3,9},{4,9},{5,9},{6,9},{7,9},{8,9}};
        int[][] branchPath1 = {{1,0},{2,0},{3,0},{4,0},{4,1},{4,2},{4,3},{5,3},{6,3},{7,3},{8,3},{9,3},{9,4},{9,5},{9,6},{9,7},{9,8}};
        int[][] mainPath2 = {{0,1},{1,1},{1,2},{2,2},{2,3},{3,3},{3,4},{4,4},{4,5},{5,5},{5,6},{6,6},{6,7},{7,7},{7,8},{8,8},{8,9}};
        int[][] branchPath2 = {{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},{8,0},{9,0},{9,1},{9,2},{9,3},{9,4},{9,5},{9,6},{9,7},{9,8}};
        int[][] mainPath3 = {{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},{0,8},{0,9},{1,9},{2,9},{3,9},{4,9},{5,9},{6,9},{7,9},{8,9}};
        int[][] branchPath3 = {{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},{8,0},{9,0},{9,1},{9,2},{8,2},{7,2},{6,2},{5,2},{4,2},{3,2},{2,2},{2,3},{2,4},{3,4},{4,4},{5,4},{6,4},{7,4},{8,4},{9,4},{9,5},{9,6},{8,6},{7,6},{6,6},{5,6},{4,6},{3,6},{2,6},{2,7},{2,8}};

        int[][][] mainPaths = {mainPath1, mainPath2, mainPath3};
        int[][][] branchPaths = {branchPath1, branchPath2, branchPath3};

        Random rand = new Random();
        int scheme = rand.nextInt(mainPaths.length);
        int[][] selectedMain = mainPaths[scheme];
        int[][] selectedBranch = branchPaths[scheme];

        // 应用主路径
        for (int[] pos : selectedMain) {
            int r = pos[0], c = pos[1];
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS) board[r][c].isPath = true;
        }

        // 应用支路径
        for (int[] pos : selectedBranch) {
            int r = pos[0], c = pos[1];
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
                board[r][c].isPath = true;
                board[r][c].isBranch = true;
            }
        }

        // 起点与终点
        board[0][0].isStart = true;
        board[9][9].isEnd = true;

        // 转换为 ArrayList
        mainPathList = new ArrayList<>();
        branchPathList = new ArrayList<>();
        for(int[] pos : selectedMain) mainPathList.add(pos);
        for(int[] pos : selectedBranch) branchPathList.add(pos);
    }

    private void fillboardcontents(){
        Random rand = new Random();
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                if(board[i][j].isPath) board[i][j].content = contentList.get(rand.nextInt(contentList.size()));
                else board[i][j].content = "";
            }
        }
    }

    private void renderboard(){
        gridLayout.removeAllViews();
        for(int i = 0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                TextView tv = new TextView(this);
                cell cell = board[i][j];

                tv.setText(cell.content);
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams(new ViewGroup.LayoutParams(100,100));

                if (cell.isStart) tv.setBackgroundColor(0xff81c784);
                else if (cell.isEnd) tv.setBackgroundColor(0xffe57373);
                else if (cell.isBranch) tv.setBackgroundColor(0xff64b5f6);
                else if (cell.isPath) tv.setBackgroundColor(0xFFFFF176);
                else tv.setBackgroundColor(0xFFEEEEEE);

                gridLayout.addView(tv);
            }
        }
    }

    private void movePlayer(int steps) {
        // 玩家在起点，需要选择路径
        if(player == null || (player.getCurrentRow() == 0 && player.getCurrentCol() == 0 && currentStep == -1)) {
            askPlayerPath(steps);
        } else {
            moveAlongPath(steps);
        }
    }

    private void askPlayerPath(int steps) {
        String[] options = {"向右", "向下"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("请选择你的行进路线")
                .setItems(options, (dialog, which) -> {
                    ArrayList<int[]> selectedPath = (which == 0) ? mainPathList : branchPathList;

                    // 初始化玩家，棋子仍在起点
                    player = new Player(selectedPath);
                    currentStep = -1;

                    addPlayerView();

                    // 选择完路径后立即移动骰子步数
                    moveAlongPath(steps);
                })
                .setCancelable(false)
                .show();
    }

    private void moveAlongPath(int steps) {
        for(int i = 0; i < steps; i++){
            if(currentStep + 1 < player.getPath().size()){
                currentStep++;
                int[] pos = player.getPath().get(currentStep);
                player.setCurrentRow(pos[0]);
                player.setCurrentCol(pos[1]);
                updatePlayerView();
            } else {
                diceResult.setText("恭喜！到达终点！");
                rollDiceBtn.setEnabled(false);
                break;
            }
        }
    }

    private void addPlayerView(){
        playerView = new TextView(this);
        playerView.setText("●");
        playerView.setGravity(Gravity.CENTER);
        playerView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
        gridLayout.addView(playerView);
        updatePlayerView();
    }

    private void updatePlayerView(){
        int index = player.getCurrentRow() * COLS + player.getCurrentCol();
        gridLayout.removeView(playerView);
        gridLayout.addView(playerView, index);
    }
}
