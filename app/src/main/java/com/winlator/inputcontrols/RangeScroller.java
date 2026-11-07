package com.winlator.inputcontrols;

import android.graphics.Rect;

import com.winlator.widget.InputControlsView;
import com.winlator.widget.TouchpadView;

import java.util.Timer;
import java.util.TimerTask;

public class RangeScroller {
    private final InputControlsView inputControlsView;
    private final ControlElement element;
    private float scrollOffset;
    private float currentOffset;
    private float lastPosition;
    private long touchTime;
    private Binding binding = Binding.NONE;
    private boolean isActionDown = false;
    private boolean scrolling = false;
    private boolean bindingPressed = false;
    private long generation;
    private Timer timer;

    public RangeScroller(InputControlsView inputControlsView, ControlElement element) {
        this.inputControlsView = inputControlsView;
        this.element = element;
    }

    public float getElementSize() {
        Rect boundingBox = element.getBoundingBox();
        return (float)Math.max(boundingBox.width(), boundingBox.height()) / getSlotCount();
    }

    private int getSlotCount() {
        int count = element.getBindingCount();
        if (!element.isScrollable()) count = Math.min(count, element.getRange().max & 0xff);
        return Math.max(count, 1);
    }

    public void resetScroll() {
        scrollOffset = 0;
        currentOffset = 0;
    }

    public float getScrollSize() {
        return getElementSize() * element.getRange().max;
    }

    public float getScrollOffset() {
        return element.isScrollable() ? scrollOffset : 0;
    }

    public byte[] getRangeIndex() {
        if (!element.isScrollable()) return new byte[]{0, (byte)getSlotCount()};

        ControlElement.Range range = element.getRange();
        byte from = (byte)Math.floor((scrollOffset / getElementSize()) % range.max);
        if (from < 0) from = (byte)(range.max + from);
        byte to = (byte)(from + element.getBindingCount() + 1);
        return new byte[]{from, to};
    }

    private Binding getBindingByPosition(float x, float y) {
        Rect boundingBox = element.getBoundingBox();
        ControlElement.Range range = element.getRange();
        float offset = element.getOrientation() == 0 ? x - boundingBox.left : y - boundingBox.top;
        if (element.isScrollable()) offset -= currentOffset;
        int index = (int)Math.floor(offset / getElementSize());
        if (!element.isScrollable()) {
            if (index < 0 || index >= getSlotCount()) return Binding.NONE;
        }
        else {
            index %= range.max;
            if (index < 0) index += range.max;
        }

        switch (range) {
            case FROM_A_TO_Z:
                return Binding.valueOf("KEY_"+((char)(65 + index)));
            case FROM_0_TO_9:
                return Binding.valueOf("KEY_"+((index + 1) % 10));
            case FROM_F1_TO_F12:
                return Binding.valueOf("KEY_F"+(index + 1));
            case FROM_NP0_TO_NP9:
                return Binding.valueOf("KEY_KP_"+((index + 1) % 10));
            default:
                return Binding.NONE;
        }
    }

    private boolean isTap() {
        return (System.currentTimeMillis() - touchTime) < TouchpadView.MAX_TAP_MILLISECONDS;
    }

    private void destroyTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void handleTouchDown(float x, float y) {
        cancelTouch();

        scrolling = false;
        isActionDown = true;
        binding = getBindingByPosition(x, y);
        touchTime = System.currentTimeMillis();
        lastPosition = element.getOrientation() == 0 ? x : y;
        element.setBinding(Binding.NONE);
        if (!element.isScrollable()) {
            setBindingPressed(true);
            return;
        }

        final long touchGeneration = generation;

        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                inputControlsView.post(() -> {
                    if (generation == touchGeneration && isActionDown && !scrolling) setBindingPressed(true);
                });
            }
        }, TouchpadView.MAX_TAP_MILLISECONDS);
    }

    public void handleTouchMove(float x, float y) {
        if (!element.isScrollable()) return;
        if (isActionDown) {
            float position = element.getOrientation() == 0 ? x : y;
            float deltaPosition = position - lastPosition;

            if (Math.abs(deltaPosition) >= TouchpadView.MAX_TAP_TRAVEL_DISTANCE) {
                scrolling = true;
                destroyTimer();
            }

            if (scrolling) {
                currentOffset += deltaPosition;

                float scrollSize = getScrollSize();
                scrollOffset = -currentOffset % scrollSize;
                if (scrollOffset < 0) scrollOffset = scrollSize + scrollOffset;

                lastPosition = position;
                inputControlsView.invalidate();
            }
        }
    }

    public void handleTouchUp() {
        if (isActionDown) {
            destroyTimer();
            if (!element.isScrollable()) {
                setBindingPressed(false);
                isActionDown = false;
                return;
            }
            final long releaseGeneration = ++generation;
            if (isTap() && !scrolling) {
                setBindingPressed(true);
                inputControlsView.postDelayed(() -> {
                    if (generation == releaseGeneration) setBindingPressed(false);
                }, 30);
            }
            else setBindingPressed(false);
        }
        isActionDown = false;
    }

    private void setBindingPressed(boolean pressed) {
        if (bindingPressed == pressed) return;
        inputControlsView.handleInputEvent(binding, pressed);
        bindingPressed = pressed;
    }

    public void cancelTouch() {
        ++generation;
        destroyTimer();
        setBindingPressed(false);
        isActionDown = false;
        scrolling = false;
        binding = Binding.NONE;
        touchTime = 0;
        lastPosition = 0;
    }
}
