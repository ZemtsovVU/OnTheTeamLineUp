package com.example.lineup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CheckBox animationCheckBox;
    private EditText correctLinesEditText;
    private Button goButton;
    private LineUpWidget lineUpWidget;

    private boolean firstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initListeners();
    }

    private void findViews() {
        animationCheckBox = (CheckBox) findViewById(R.id.animation_check_box);
        correctLinesEditText = (EditText) findViewById(R.id.correct_lines_edit_text);
        goButton = (Button) findViewById(R.id.go_button);
        lineUpWidget = (LineUpWidget) findViewById(R.id.line_up_widget);
    }

    private void initListeners() {
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineUpWidget.setWithAnimation(animationCheckBox.isChecked());
                lineUpWidget.setLinesNeedCorrection(getLinesNeedCorrection());

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

    private List<Integer> getLinesNeedCorrection() {
        String s = correctLinesEditText.getText().toString();
        if (TextUtils.isEmpty(s)) {
            return Collections.emptyList();
        }

        String[] lines = s.split(",");
        List<Integer> lineList = new ArrayList<>();
        for (String line : lines) {
            try {
                lineList.add(Integer.parseInt(line));
            } catch (NumberFormatException e) {
                // Do nothing
            }
        }
        return lineList;
    }
}
