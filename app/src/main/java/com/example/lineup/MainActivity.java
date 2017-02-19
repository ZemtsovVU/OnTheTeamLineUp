package com.example.lineup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button goButton;
    private LineUpWidget lineUpWidget;

    private boolean firstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        initListeners();
    }

    private void findViews() {
        goButton = (Button) findViewById(R.id.go_button);
        lineUpWidget = (LineUpWidget) findViewById(R.id.line_up_widget);
    }

    private void initViews() {
        lineUpWidget.setWithAnimation(true);
    }

    private void initListeners() {
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<List<Integer>> lineUpList;
                if (firstClick) {
                    lineUpList = LineUpGenerator.generateSimpleLineUp();
                    firstClick = false;
                } else {
                    lineUpList = LineUpGenerator.generateRandomLineUp();
                }
                lineUpWidget.setLineUpList(lineUpList);
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
}
