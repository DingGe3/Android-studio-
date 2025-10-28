package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_INPUT_LIST = "input_list";

    private EditText inputText;
    private Button addButton;
    private TextView resultText;
    private Button startbutton;
    private ArrayList<String> inputList = new ArrayList<>();
    public ArrayList<String> getInputList(){
        return new ArrayList<>(inputList);
    }
    public void jumpto(View view){
        Intent intent = new Intent(MainActivity.this, BoardActivity.class);
        intent.putStringArrayListExtra(EXTRA_INPUT_LIST,getInputList());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView marqueeText = findViewById(R.id.marquee);
        marqueeText.setSelected(true);
        inputText = findViewById(R.id.input_text);
        addButton = findViewById(R.id.addbutton);
        resultText = findViewById(R.id.resulttext);
        startbutton = findViewById(R.id.start);

        startbutton.setOnClickListener(v -> jumpto(v));

        addButton.setOnClickListener(v -> {
            String input = inputText.getText().toString().trim();
            if(!input.isEmpty()){
                inputList.add(input);
                updateresultText();
                inputText.setText("");
            }
        });
        Button resetbutton = findViewById(R.id.reset);
        resetbutton.setOnClickListener(v -> {
            inputList.clear();
            updateresultText();
        });
    }
    private void updateresultText() {
        StringBuilder builder = new StringBuilder();
        builder.append("当前输入内容：\n");
        for (int i = 0; i < inputList.size(); i++) {
            builder.append(i + 1).append(". ").append(inputList.get(i)).append("\n");
        }
        resultText.setText(builder.toString());
    }



}
