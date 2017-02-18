package com.example.lineup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class PlayerWidget extends FrameLayout {
//    private TextView playerNumberTextView;

    public PlayerWidget(Context context) {
        super(context);
        init();
    }

    public PlayerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PlayerWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_player, this);
//        playerNumberTextView = (TextView) view.findViewById(R.id.player_number_text_view);
    }

    public void setPlayerNumber(int number) {
//        playerNumberTextView.setText(String.valueOf(number));
    }
}
