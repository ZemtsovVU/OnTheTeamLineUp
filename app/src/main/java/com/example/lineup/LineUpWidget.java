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
        container.removeAllViews();
        // calculate size
        showLineUp(lineUpList);
    }

    private void showLineUp(List<List<Integer>> lineUpList) {
        int containerHeight = container.getHeight();
        int lineCount = lineUpList.size();

        float lineHeight = containerHeight / lineCount;

        float playerSize = calculatePlayerSize(lineUpList);

        for (int i = 0; i < lineCount; i++) {
            showLine(lineUpList.get(i), i, lineHeight, playerSize);
        }
    }

    private float calculatePlayerSize(List<List<Integer>> lineUpList) {
        int maxColumn = 0;
        for (List<Integer> list : lineUpList) {
            if (list.size() > maxColumn) {
                maxColumn = list.size();
            }
        }
        return container.getWidth() / maxColumn;
    }

    private void showLine(List<Integer> lineList, int lineNumber, float lineHeight, float playerSize) {
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

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams((int) playerSize, (int) playerSize);
            playerWidget.setLayoutParams(layoutParams);

            playerWidget.setTag(String.format(Locale.getDefault(), "%d%d", lineNumber, columnNumber));
            playerWidget.setOnClickListener(onClickListener);

            float widgetY = y - playerSize / 2;
            float widgetX = x - playerSize / 2;

            playerWidget.setY(widgetY);
            playerWidget.setX(widgetX);

            container.addView(playerWidget);
        }

        correctLine(lineList, lineNumber);
    }

    private void correctLine(List<Integer> lineList, int lineNumber) {
        if (lineNumber != 1) {
            return;
        }

        int centerIndex = (int) Math.floor(lineList.size() / 2);

        for (int i = 1; i < centerIndex + 1; i++) {
            float y = 30 * i;

            int leftIndex = centerIndex - i;
            View leftView = container.getChildAt(leftIndex + 2);
            if (leftView != null) {
                String tag = (String) leftView.getTag();
//                leftView.setTranslationY(-y);
                leftView.setY(leftView.getY() - y);
            }

            int rightIndex = centerIndex + i;
            View rightView = container.getChildAt(rightIndex + 2);
            if (rightView != null) {
                String tag = (String) rightView.getTag();
//                rightView.setTranslationY(-y);
                rightView.setY(rightView.getY() - y);
            }
        }
    }
}
