package com.winlator.inputcontrols;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TrackpadGestureTest {
    @Test
    public void singleFingerTapClicksLeft() {
        TrackpadGesture gesture = new TrackpadGesture();
        gesture.primaryDown(0L);

        List<TrackpadGesture.Action> actions = gesture.pointerUp();

        assertEquals(Arrays.asList(TrackpadGesture.Action.CLICK_LEFT), actions);
        assertFalse(gesture.hasContact());
    }

    @Test
    public void holdWithoutReleasePressesLeftUntilLift() {
        TrackpadGesture gesture = new TrackpadGesture();
        gesture.primaryDown(0L);

        assertEquals(Arrays.asList(TrackpadGesture.Action.PRESS_LEFT), gesture.holdTimeout(TrackpadGesture.HOLD_MILLISECONDS));
        assertEquals(
                Arrays.asList(TrackpadGesture.Action.RELEASE_LEFT),
                gesture.pointerUp()
        );
    }

    @Test
    public void movementPastSlopDoesNotPressOrClick() {
        TrackpadGesture gesture = new TrackpadGesture();
        gesture.primaryDown(0L);

        assertTrue(gesture.moveTo(TrackpadGesture.MAX_TAP_TRAVEL, 40L).isEmpty());
        assertTrue(gesture.holdTimeout(TrackpadGesture.HOLD_MILLISECONDS).isEmpty());
        assertTrue(gesture.pointerUp().isEmpty());
    }

    @Test
    public void holdThenMoveKeepsLeftPressedUntilLift() {
        TrackpadGesture gesture = new TrackpadGesture();
        gesture.primaryDown(0L);
        gesture.holdTimeout(TrackpadGesture.HOLD_MILLISECONDS);

        assertTrue(gesture.moveTo(40f, 240L).isEmpty());
        assertEquals(Arrays.asList(TrackpadGesture.Action.RELEASE_LEFT), gesture.pointerUp());
    }

    @Test
    public void twoFingerTapClicksRightOnce() {
        TrackpadGesture gesture = new TrackpadGesture();
        gesture.primaryDown(0L);
        assertTrue(gesture.secondFingerDown(20L).isEmpty());

        assertEquals(Arrays.asList(TrackpadGesture.Action.CLICK_RIGHT), gesture.pointerUp());
        assertTrue(gesture.hasContact());
        assertTrue(gesture.pointerUp().isEmpty());
        assertFalse(gesture.hasContact());
    }

    @Test
    public void twoFingerHoldPressesRight() {
        TrackpadGesture gesture = new TrackpadGesture();
        gesture.primaryDown(0L);
        gesture.secondFingerDown(30L);

        assertEquals(Arrays.asList(TrackpadGesture.Action.PRESS_RIGHT), gesture.holdTimeout(TrackpadGesture.HOLD_MILLISECONDS));
        assertEquals(Arrays.asList(TrackpadGesture.Action.RELEASE_RIGHT), gesture.pointerUp());
        assertTrue(gesture.pointerUp().isEmpty());
    }

    @Test
    public void secondFingerAfterLeftHoldSwitchesToRight() {
        TrackpadGesture gesture = new TrackpadGesture();
        gesture.primaryDown(0L);
        gesture.holdTimeout(TrackpadGesture.HOLD_MILLISECONDS);

        assertEquals(
                Arrays.asList(TrackpadGesture.Action.RELEASE_LEFT, TrackpadGesture.Action.PRESS_RIGHT),
                gesture.secondFingerDown(TrackpadGesture.HOLD_MILLISECONDS + 20L)
        );
        assertEquals(
                Arrays.asList(TrackpadGesture.Action.RELEASE_RIGHT),
                gesture.pointerUp()
        );
    }

    @Test
    public void fingerAddedAfterGestureResolvesDoesNotDropTheRemainingContact() {
        TrackpadGesture gesture = new TrackpadGesture();
        gesture.primaryDown(0L);
        gesture.secondFingerDown(20L);
        gesture.pointerUp();

        assertTrue(gesture.hasContact());
        assertTrue(gesture.secondFingerDown(100L).isEmpty());
        assertFalse(gesture.attachedSecondFinger());
        assertTrue(gesture.hasContact());
        assertTrue(gesture.pointerUp().isEmpty());
        assertFalse(gesture.hasContact());
    }

    @Test
    public void cancelWhileHeldReleasesWithoutClick() {
        TrackpadGesture gesture = new TrackpadGesture();
        gesture.primaryDown(0L);
        gesture.holdTimeout(TrackpadGesture.HOLD_MILLISECONDS);

        assertEquals(Arrays.asList(TrackpadGesture.Action.RELEASE_LEFT), gesture.cancel());
        assertTrue(gesture.pointerUp().isEmpty());
    }
}
