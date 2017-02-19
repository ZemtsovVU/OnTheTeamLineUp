package com.example.lineup;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LineUpWidget extends FrameLayout {
    private static final int ANIMATION_TIME_MILLIS = 200;
    private static final int FIRST_ANIMATION_WAVE_DELAY_MILLIS = 250;
    private static final int SECOND_ANIMATION_WAVE_DELAY_MILLIS = 500;

    private ViewGroup container;

    private OnClickListener onClickListener;

    private boolean withAnimation;
    private List<Integer> linesNeedCorrection = new ArrayList<>();
    private List<List<Integer>> lineUpList = new ArrayList<>();

    private int lineHeight;
    private int itemSize;

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

    public void setWithAnimation(boolean withAnimation) {
        this.withAnimation = withAnimation;
    }

    public void setLinesNeedCorrection(List<Integer> linesNeedCorrection) {
        this.linesNeedCorrection.clear();
        this.linesNeedCorrection.addAll(linesNeedCorrection);
    }

    public void setLineUpList(List<List<Integer>> lineUpList) {
        this.lineUpList.clear();
        this.lineUpList.addAll(lineUpList);
        showLineUp();
    }

    private void showLineUp() {
        container.removeAllViews();

        lineHeight = calculateLineHeight();
        itemSize = calculateItemSize();

        int lineCount = lineUpList.size();
        for (int i = 0; i < lineCount; i++) {
            showLine(i);
        }
    }

    private int calculateLineHeight() {
        int lineCount = lineUpList.size();
        return container.getHeight() / lineCount;
    }

    private int calculateItemSize() {
        int maxItems = lineUpList.size();
        for (List<Integer> list : lineUpList) {
            if (list.size() > maxItems) {
                maxItems = list.size();
            }
        }
        return container.getWidth() / maxItems;
    }

    private void showLine(int lineNumber) {
        List<Integer> lineList = lineUpList.get(lineNumber);

        int columnCount = lineList.size();
        int columnWidth = container.getWidth() / columnCount;

        int lineCenter = lineHeight / 2;
        int columnCenter = columnWidth / 2;

        for (int columnNumber = 0; columnNumber < columnCount; columnNumber++) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(itemSize, itemSize);

            int currentLineCenter = lineHeight * lineNumber + lineCenter;
            int currentColumnCenter = columnWidth * columnNumber + columnCenter;

            int itemY = currentLineCenter - layoutParams.height / 2;
            int itemX = currentColumnCenter - layoutParams.width / 2;

            PlayerWidget item = new PlayerWidget(getContext());
            item.setLayoutParams(layoutParams);
//            item.setPlayerIcon(); // TODO: 19.02.2017
//            item.setPlayerName(); // TODO: 19.02.2017
            item.setTag(String.format(Locale.getDefault(), "%d-%d", lineNumber, columnNumber));
            item.setOnClickListener(onClickListener);

            if (withAnimation) {
                showItemWithAnimation(item, itemY, itemX);
            } else {
                showItem(item, itemY, itemX);
            }
        }

        correctLineLayout(lineNumber);
    }

    private void showItem(PlayerWidget item, int y, int x) {
        item.setY(y);
        item.setX(x);

        container.addView(item);
    }

    private void showItemWithAnimation(final PlayerWidget item, final int y, final int x) {
        container.addView(item);

        container.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(item, "y", y),
                        ObjectAnimator.ofFloat(item, "x", x)
                );
                animatorSet.setDuration(ANIMATION_TIME_MILLIS);
                animatorSet.start();
            }
        }, FIRST_ANIMATION_WAVE_DELAY_MILLIS);
    }

    private void correctLineLayout(int lineNumber) {
        if (!needCorrection(lineNumber)) {
            return;
        }

        int itemRise = calculateItemRise();
        int indexShift = calculateIndexShift(lineNumber);
        int indexCenter = calculateIndexCenter(lineNumber);
        int evenNumberCorrection = calculateEvenNumberCorrection(lineNumber);

        for (int i = 0; i < indexCenter; i++) {
            int currentIndex = i + 1;

            int translationY = itemRise * currentIndex;

            int leftIndex = indexCenter - currentIndex - evenNumberCorrection;
            if (leftIndex >= 0) {
                if (withAnimation) {
                    correctItemPositionWithAnimation(leftIndex + indexShift, translationY);
                } else {
                    correctItemPosition(leftIndex + indexShift, translationY);
                }
            }

            int rightIndex = indexCenter + currentIndex;
            if (withAnimation) {
                correctItemPositionWithAnimation(rightIndex + indexShift, translationY);
            } else {
                correctItemPosition(rightIndex + indexShift, translationY);
            }
        }
    }

    private boolean needCorrection(int lineNumber) {
        if (linesNeedCorrection == null || linesNeedCorrection.isEmpty()) {
            return false;
        }

        for (Integer lineNeedCorrection : linesNeedCorrection) {
            if (lineNeedCorrection == lineNumber) {
                return true;
            }
        }

        return false;
    }

    private int calculateItemRise() {
        return container.getHeight() / 100 * 3; // 3% from container height
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

    private int calculateEvenNumberCorrection(int lineNumber) {
        return lineUpList.get(lineNumber).size() % 2 == 0 ? 1 : 0;
    }

    private void correctItemPosition(int position, int translationY) {
        View child = container.getChildAt(position);
        if (child != null) {
            child.setY(child.getY() - translationY);
        }
    }

    private void correctItemPositionWithAnimation(int position, final int translationY) {
        final View child = container.getChildAt(position);

        if (child == null) {
            return;
        }

        container.postDelayed(new Runnable() {
            @Override
            public void run() {
                int y = (int) child.getY() - translationY;
                ObjectAnimator.ofFloat(child, "y", y).setDuration(ANIMATION_TIME_MILLIS).start();
            }
        }, SECOND_ANIMATION_WAVE_DELAY_MILLIS);
    }
}
