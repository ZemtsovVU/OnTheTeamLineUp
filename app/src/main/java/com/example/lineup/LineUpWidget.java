package com.example.lineup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Locale;

public class LineUpWidget extends FrameLayout {
    private static final int[] LINES_NEED_CORRECTION = {1};

    private ViewGroup container;

    private OnClickListener onClickListener;

    private List<List<Integer>> lineUpList;

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
        this.lineUpList = lineUpList;

        container.removeAllViews();
        // calculate size
        showLineUp();
    }

    private void showLineUp() {
        int containerHeight = container.getHeight();
        int lineCount = lineUpList.size();

        float lineHeight = containerHeight / lineCount;

        float playerSize = calculatePlayerSize();

        for (int i = 0; i < lineCount; i++) {
            showLine(i, lineHeight, playerSize);
        }
    }

    private float calculatePlayerSize() {
        int maxColumn = 0;
        for (List<Integer> list : lineUpList) {
            if (list.size() > maxColumn) {
                maxColumn = list.size();
            }
        }
        return container.getWidth() / maxColumn;
    }

    private void showLine(int lineNumber, float lineHeight, float playerSize) {
        int containerWidth = container.getWidth();
        int columnCount = lineUpList.get(lineNumber).size();

        float columnWidth = containerWidth / columnCount;

        float lineCenter = lineHeight / 2;
        float columnCenter = columnWidth / 2;

        for (int columnNumber = 0; columnNumber < columnCount; columnNumber++) {
            float y = lineHeight * lineNumber + lineCenter;
            float x = columnWidth * columnNumber + columnCenter;

            PlayerWidget playerWidget = new PlayerWidget(getContext());
            playerWidget.setPlayerNumber(lineUpList.get(lineNumber).get(columnNumber));

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

        correctLineLayout(lineNumber);
    }

    private void correctLineLayout(int lineNumber) {
        if (!needCorrection(lineNumber)) {
            return;
        }

        int itemRise = calculateItemRise();
        int indexShift = calculateIndexShift(lineNumber);
        int indexCenter = calculateIndexCenter(lineNumber);

        for (int i = 0; i < indexCenter; i++) {
            int currentIndex = i + 1;

            int translationY = itemRise * currentIndex;

            int leftIndex = indexCenter - currentIndex + indexShift;
            correctItemPosition(leftIndex, translationY);

            int rightIndex = indexCenter + currentIndex + indexShift;
            correctItemPosition(rightIndex, translationY);
        }
    }

    private boolean needCorrection(int lineNumber) {
        for (Integer lineNeedCorrection : LINES_NEED_CORRECTION) {
            if (lineNeedCorrection == lineNumber) {
                return true;
            }
        }
        return false;
    }

    private int calculateItemRise() {
        return container.getHeight() / 50; // 2% from container height
    }

    private int calculateIndexShift(int lineNumber) {
        int indexShift = 0;
        for (int i = 0; i < lineNumber; i++) {
            indexShift += lineUpList.get(i).size();
        }
        return indexShift;
    }

    private int calculateIndexCenter(int lineNumber) {
        return (int) Math.floor(lineUpList.get(lineNumber).size() / 2);
    }

    private void correctItemPosition(int position, int translationY) {
        View child = container.getChildAt(position);
        if (child != null) {
            child.setY(child.getY() - translationY);
        }
    }
}
