package com.example.lineup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button goButton;
    private LineUpWidget lineUpWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineUpWidget.setLineUpList(getLineUpList());
            }
        });

        lineUpWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playerPosition = (String) view.getTag();
                Log.d(TAG, "onClick: playerPosition = " + playerPosition);
                Log.d(TAG, "onClick: view.getHeight() = " + view.getHeight());
                Log.d(TAG, "onClick: view.getWidth() = " + view.getWidth());
                Toast.makeText(MainActivity.this, playerPosition, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findViews() {
        goButton = (Button) findViewById(R.id.go_button);
        lineUpWidget = (LineUpWidget) findViewById(R.id.line_up_widget);
    }

    private List<List<Integer>> getLineUpList() {
        List<Integer> firstLine = getLineList(2);
        List<Integer> secondLine = getLineList(5); // TODO: 18.02.2017
        List<Integer> thirdLine = getLineList(3);
        List<Integer> fourthLine = getLineList(1);
        return Arrays.asList(firstLine, secondLine, thirdLine, fourthLine);
    }

    private List<Integer> getLineList(int columnCount) {
        List<Integer> lineList = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            lineList.add(i);
        }
        return lineList;
    }
}
