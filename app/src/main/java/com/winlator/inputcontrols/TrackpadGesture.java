package com.winlator.inputcontrols;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tap, two-finger tap, and press-and-hold decisions for an on-screen trackpad.
 * Cursor movement stays with the caller. A finger that travels past the tap
 * slop before the hold delay only moves the cursor.
 */
final class TrackpadGesture {
    enum Action {
        PRESS_LEFT,
        RELEASE_LEFT,
        CLICK_LEFT,
        PRESS_RIGHT,
        RELEASE_RIGHT,
        CLICK_RIGHT
    }

    static final long HOLD_MILLISECONDS = 200L;
    static final float MAX_TAP_TRAVEL = 10f;
    static final long CLICK_RELEASE_DELAY_MS = 30L;

    private boolean active;
    private boolean twoFinger;
    private boolean moved;
    private boolean held;
    private boolean resolved;
    private boolean heldRight;
    private int fingers;
    private float travel;
    private long downTime;
    private boolean secondFingerAttached;

    void primaryDown(long now) {
        active = true;
        twoFinger = false;
        moved = false;
        held = false;
        resolved = false;
        heldRight = false;
        fingers = 1;
        travel = 0f;
        downTime = now;
        secondFingerAttached = false;
    }

    List<Action> secondFingerDown(long now) {
        secondFingerAttached = false;
        if (!active || resolved || twoFinger) return Collections.emptyList();
        secondFingerAttached = true;
        twoFinger = true;
        fingers = 2;
        if (!(held && !heldRight)) return Collections.emptyList();

        held = false;
        if (moved || travel >= MAX_TAP_TRAVEL || now - downTime < HOLD_MILLISECONDS) {
            return Collections.singletonList(Action.RELEASE_LEFT);
        }
        held = true;
        heldRight = true;
        return actions(Action.RELEASE_LEFT, Action.PRESS_RIGHT);
    }

    List<Action> moveTo(float travelFromStart, long now) {
        if (!active || resolved) return Collections.emptyList();
        if (travelFromStart > travel) travel = travelFromStart;
        if (held) return Collections.emptyList();
        if (travel >= MAX_TAP_TRAVEL) {
            moved = true;
            return Collections.emptyList();
        }
        return holdTimeout(now);
    }

    List<Action> holdTimeout(long now) {
        if (!active || resolved || held || moved) return Collections.emptyList();
        if (travel >= MAX_TAP_TRAVEL) {
            moved = true;
            return Collections.emptyList();
        }
        if (now - downTime < HOLD_MILLISECONDS) return Collections.emptyList();
        held = true;
        heldRight = twoFinger;
        return Collections.singletonList(heldRight ? Action.PRESS_RIGHT : Action.PRESS_LEFT);
    }

    List<Action> pointerUp() {
        if (fingers <= 0) return Collections.emptyList();
        fingers--;
        List<Action> actions = resolved ? Collections.emptyList() : finish();
        if (fingers == 0) reset();
        return actions;
    }

    List<Action> cancel() {
        if (!active) return Collections.emptyList();
        List<Action> actions = Collections.emptyList();
        if (held) actions = Collections.singletonList(heldRight ? Action.RELEASE_RIGHT : Action.RELEASE_LEFT);
        reset();
        return actions;
    }

    boolean hasContact() {
        return active && fingers > 0;
    }

    boolean attachedSecondFinger() {
        return secondFingerAttached;
    }

    private static List<Action> actions(Action first, Action second) {
        ArrayList<Action> actions = new ArrayList<>(2);
        actions.add(first);
        actions.add(second);
        return actions;
    }

    private List<Action> finish() {
        resolved = true;
        if (held) {
            boolean right = heldRight;
            held = false;
            return Collections.singletonList(right ? Action.RELEASE_RIGHT : Action.RELEASE_LEFT);
        }
        if (moved || travel >= MAX_TAP_TRAVEL) return Collections.emptyList();
        return Collections.singletonList(twoFinger ? Action.CLICK_RIGHT : Action.CLICK_LEFT);
    }

    private void reset() {
        active = false;
        twoFinger = false;
        moved = false;
        held = false;
        resolved = false;
        heldRight = false;
        fingers = 0;
        travel = 0f;
        downTime = 0L;
        secondFingerAttached = false;
    }
}
