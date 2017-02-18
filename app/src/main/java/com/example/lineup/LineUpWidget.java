package com.example.lineup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Locale;

public class LineUpWidget extends FrameLayout {
    private ViewGroup container;

    private OnClickListener onClickListener;

    public LineUpWidget(Context context) {
        super(context);
        init();
    }

    public LineUpWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineUpWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LineUpWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_line_up, this);
        container = (FrameLayout) view.findViewById(R.id.container_view_group);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setLineUpList(List<List<Integer>> lineUpList) {
        showLineUp(lineUpList);
    }

    private void showLineUp(List<List<Integer>> lineUpList) {
        int containerHeight = container.getHeight();
        int lineCount = lineUpList.size();

        float lineHeight = containerHeight / lineCount;

        for (int i = 0; i < lineCount; i++) {
            showLine(lineUpList.get(i), i, lineHeight);
        }
    }

    private void showLine(List<Integer> lineList, int lineNumber, float lineHeight) {
        int containerWidth = container.getWidth();
        int columnCount = lineList.size();

        float columnWidth = containerWidth / columnCount;

        float lineCenter = lineHeight / 2;
        float columnCenter = columnWidth / 2;

        for (int columnNumber = 0; columnNumber < columnCount; columnNumber++) {
            float y = lineHeight * lineNumber + lineCenter;
            float x = columnWidth * columnNumber + columnCenter;

            PlayerWidget playerWidget = new PlayerWidget(getContext());
            playerWidget.setPlayerNumber(lineList.get(columnNumber));

            playerWidget.setTag(String.format(Locale.getDefault(), "%d%d", lineNumber, columnNumber));
            playerWidget.setOnClickListener(onClickListener);

            float widgetY = y - playerWidget.getHeight() / 2;
            float widgetX = x - playerWidget.getWidth() / 2;

            playerWidget.setY(widgetY);
            playerWidget.setX(widgetX);

            container.addView(playerWidget);
        }
    }

    private void correctLine() {

    }
}
