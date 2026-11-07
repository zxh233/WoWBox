package com.winlator.inputcontrols;

import android.content.Context;

import androidx.annotation.NonNull;

import com.winlator.xserver.Pointer;
import com.winlator.xserver.XKeycode;

import java.util.ArrayList;

public enum Binding {
    NONE, MOUSE_LEFT_BUTTON, MOUSE_MIDDLE_BUTTON, MOUSE_RIGHT_BUTTON, MOUSE_MOVE_LEFT, MOUSE_MOVE_RIGHT, MOUSE_MOVE_UP, MOUSE_MOVE_DOWN, MOUSE_SCROLL_UP, MOUSE_SCROLL_DOWN, KEY_UP, KEY_RIGHT, KEY_DOWN, KEY_LEFT, KEY_ENTER, KEY_ESC, KEY_BKSP, KEY_DEL, KEY_TAB, KEY_SPACE, KEY_CTRL_L, KEY_CTRL_R, KEY_SHIFT_L, KEY_SHIFT_R, KEY_ALT_L, KEY_ALT_R, KEY_HOME, KEY_PRTSCN, KEY_PG_UP, KEY_PG_DOWN, KEY_CAPS_LOCK, KEY_NUM_LOCK, KEY_0, KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9, KEY_A, KEY_B, KEY_C, KEY_D, KEY_E, KEY_F, KEY_G, KEY_H, KEY_I, KEY_J, KEY_K, KEY_L, KEY_M, KEY_N, KEY_O, KEY_P, KEY_Q, KEY_R, KEY_S, KEY_T, KEY_U, KEY_V, KEY_W, KEY_X, KEY_Y, KEY_Z, KEY_BRACKET_LEFT, KEY_BRACKET_RIGHT, KEY_BACKSLASH, KEY_SLASH, KEY_SEMICOLON, KEY_COMMA, KEY_PERIOD, KEY_APOSTROPHE, KEY_KP_ADD, KEY_MINUS, KEY_F1, KEY_F2, KEY_F3, KEY_F4, KEY_F5, KEY_F6, KEY_F7, KEY_F8, KEY_F9, KEY_F10, KEY_F11, KEY_F12, KEY_KP_0, KEY_KP_1, KEY_KP_2, KEY_KP_3, KEY_KP_4, KEY_KP_5, KEY_KP_6, KEY_KP_7, KEY_KP_8, KEY_KP_9, GAMEPAD_BUTTON_A, GAMEPAD_BUTTON_B, GAMEPAD_BUTTON_X, GAMEPAD_BUTTON_Y, GAMEPAD_BUTTON_L1, GAMEPAD_BUTTON_R1, GAMEPAD_BUTTON_SELECT, GAMEPAD_BUTTON_START, GAMEPAD_BUTTON_L3, GAMEPAD_BUTTON_R3, GAMEPAD_BUTTON_L2, GAMEPAD_BUTTON_R2, GAMEPAD_BUTTON_C, GAMEPAD_BUTTON_Z, GAMEPAD_LEFT_THUMB_UP, GAMEPAD_LEFT_THUMB_RIGHT, GAMEPAD_LEFT_THUMB_DOWN, GAMEPAD_LEFT_THUMB_LEFT, GAMEPAD_RIGHT_THUMB_UP, GAMEPAD_RIGHT_THUMB_RIGHT, GAMEPAD_RIGHT_THUMB_DOWN, GAMEPAD_RIGHT_THUMB_LEFT, GAMEPAD_DPAD_UP, GAMEPAD_DPAD_RIGHT, GAMEPAD_DPAD_DOWN, GAMEPAD_DPAD_LEFT;
    public final XKeycode keycode;

    Binding() {
        XKeycode keycode;
        try {
            keycode = XKeycode.valueOf(name());
        }
        catch (IllegalArgumentException e) {
            keycode = XKeycode.KEY_NONE;
            String name = name();
            if (name.equals("KEY_PG_UP")) {
                keycode = XKeycode.KEY_PRIOR;
            }
            else if (name.equals("KEY_PG_DOWN")) {
                keycode = XKeycode.KEY_NEXT;
            }
        }
        this.keycode = keycode;
    }

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case KEY_SHIFT_L:
                return "L SHIFT";
            case KEY_SHIFT_R:
                return "R SHIFT";
            case KEY_CTRL_L:
                return "L CTRL";
            case KEY_CTRL_R:
                return "R CTRL";
            case KEY_ALT_L:
                return "L ALT";
            case KEY_ALT_R:
                return "R ALT";
            case KEY_BRACKET_LEFT:
                return "[";
            case KEY_BRACKET_RIGHT:
                return "]";
            case KEY_BACKSLASH:
                return "\\";
            case KEY_SLASH:
                return "/";
            case KEY_SEMICOLON:
                return ";";
            case KEY_COMMA:
                return ",";
            case KEY_PERIOD:
                return ".";
            case KEY_APOSTROPHE:
                return "'";
            case KEY_MINUS:
                return "-";
            case KEY_KP_ADD:
                return "+";
            default:
                return super.toString().replaceAll("^(MOUSE_)|(KEY_)|(GAMEPAD_)", "").replace("KP_", "NUMPAD_").replace("_", " ");
        }
    }

    public static boolean isChinese(Context context) {
        if (context == null) return false;
        return "zh".equals(context.getResources().getConfiguration().getLocales().get(0).getLanguage());
    }

    public String listLabel(Context context) {
        if (!isChinese(context)) return toString();
        switch (this) {
            case NONE: return "无";
            case MOUSE_LEFT_BUTTON: return "鼠标左键";
            case MOUSE_MIDDLE_BUTTON: return "鼠标中键";
            case MOUSE_RIGHT_BUTTON: return "鼠标右键";
            case MOUSE_MOVE_LEFT: return "鼠标左移";
            case MOUSE_MOVE_RIGHT: return "鼠标右移";
            case MOUSE_MOVE_UP: return "鼠标上移";
            case MOUSE_MOVE_DOWN: return "鼠标下移";
            case MOUSE_SCROLL_UP: return "滚轮向上";
            case MOUSE_SCROLL_DOWN: return "滚轮向下";
            case KEY_UP: return "上";
            case KEY_RIGHT: return "右";
            case KEY_DOWN: return "下";
            case KEY_LEFT: return "左";
            case KEY_ENTER: return "回车";
            case KEY_ESC: return "Esc";
            case KEY_BKSP: return "退格";
            case KEY_DEL: return "删除";
            case KEY_TAB: return "Tab";
            case KEY_SPACE: return "空格";
            case KEY_CTRL_L: return "左 Ctrl";
            case KEY_CTRL_R: return "右 Ctrl";
            case KEY_SHIFT_L: return "左 Shift";
            case KEY_SHIFT_R: return "右 Shift";
            case KEY_ALT_L: return "左 Alt";
            case KEY_ALT_R: return "右 Alt";
            case KEY_HOME: return "Home";
            case KEY_PRTSCN: return "打印屏幕";
            case KEY_PG_UP: return "上页";
            case KEY_PG_DOWN: return "下页";
            case KEY_CAPS_LOCK: return "大写锁定";
            case KEY_NUM_LOCK: return "数字锁定";
            case KEY_KP_0: return "小键盘 0";
            case KEY_KP_1: return "小键盘 1";
            case KEY_KP_2: return "小键盘 2";
            case KEY_KP_3: return "小键盘 3";
            case KEY_KP_4: return "小键盘 4";
            case KEY_KP_5: return "小键盘 5";
            case KEY_KP_6: return "小键盘 6";
            case KEY_KP_7: return "小键盘 7";
            case KEY_KP_8: return "小键盘 8";
            case KEY_KP_9: return "小键盘 9";
            case GAMEPAD_BUTTON_A: return "手柄 A";
            case GAMEPAD_BUTTON_B: return "手柄 B";
            case GAMEPAD_BUTTON_X: return "手柄 X";
            case GAMEPAD_BUTTON_Y: return "手柄 Y";
            case GAMEPAD_BUTTON_L1: return "手柄 L1";
            case GAMEPAD_BUTTON_R1: return "手柄 R1";
            case GAMEPAD_BUTTON_SELECT: return "手柄选择";
            case GAMEPAD_BUTTON_START: return "手柄开始";
            case GAMEPAD_BUTTON_L3: return "手柄 L3";
            case GAMEPAD_BUTTON_R3: return "手柄 R3";
            case GAMEPAD_BUTTON_L2: return "手柄 L2";
            case GAMEPAD_BUTTON_R2: return "手柄 R2";
            case GAMEPAD_BUTTON_C: return "手柄 C";
            case GAMEPAD_BUTTON_Z: return "手柄 Z";
            case GAMEPAD_LEFT_THUMB_UP: return "左摇杆上";
            case GAMEPAD_LEFT_THUMB_RIGHT: return "左摇杆右";
            case GAMEPAD_LEFT_THUMB_DOWN: return "左摇杆下";
            case GAMEPAD_LEFT_THUMB_LEFT: return "左摇杆左";
            case GAMEPAD_RIGHT_THUMB_UP: return "右摇杆上";
            case GAMEPAD_RIGHT_THUMB_RIGHT: return "右摇杆右";
            case GAMEPAD_RIGHT_THUMB_DOWN: return "右摇杆下";
            case GAMEPAD_RIGHT_THUMB_LEFT: return "右摇杆左";
            case GAMEPAD_DPAD_UP: return "十字键上";
            case GAMEPAD_DPAD_RIGHT: return "十字键右";
            case GAMEPAD_DPAD_DOWN: return "十字键下";
            case GAMEPAD_DPAD_LEFT: return "十字键左";
            default: return toString();
        }
    }

    public String buttonLabel(Context context) {
        if (!isChinese(context)) return null;
        switch (this) {
            case NONE: return "无";
            case MOUSE_LEFT_BUTTON: return "左键";
            case MOUSE_MIDDLE_BUTTON: return "中键";
            case MOUSE_RIGHT_BUTTON: return "右键";
            case MOUSE_MOVE_LEFT: return "左移";
            case MOUSE_MOVE_RIGHT: return "右移";
            case MOUSE_MOVE_UP: return "上移";
            case MOUSE_MOVE_DOWN: return "下移";
            case MOUSE_SCROLL_UP: return "滚上";
            case MOUSE_SCROLL_DOWN: return "滚下";
            case KEY_UP: return "上";
            case KEY_RIGHT: return "右";
            case KEY_DOWN: return "下";
            case KEY_LEFT: return "左";
            case KEY_ENTER: return "回车";
            case KEY_BKSP: return "退格";
            case KEY_DEL: return "删除";
            case KEY_SPACE: return "空格";
            case KEY_CTRL_L: return "左Ctrl";
            case KEY_CTRL_R: return "右Ctrl";
            case KEY_SHIFT_L: return "左Shift";
            case KEY_SHIFT_R: return "右Shift";
            case KEY_ALT_L: return "左Alt";
            case KEY_ALT_R: return "右Alt";
            case KEY_PRTSCN: return "截屏";
            case KEY_PG_UP: return "上页";
            case KEY_PG_DOWN: return "下页";
            case KEY_CAPS_LOCK: return "大写";
            case KEY_NUM_LOCK: return "数锁";
            case KEY_KP_0: return "小0";
            case KEY_KP_1: return "小1";
            case KEY_KP_2: return "小2";
            case KEY_KP_3: return "小3";
            case KEY_KP_4: return "小4";
            case KEY_KP_5: return "小5";
            case KEY_KP_6: return "小6";
            case KEY_KP_7: return "小7";
            case KEY_KP_8: return "小8";
            case KEY_KP_9: return "小9";
            case GAMEPAD_BUTTON_SELECT: return "选择";
            case GAMEPAD_BUTTON_START: return "开始";
            case GAMEPAD_LEFT_THUMB_UP: return "L上";
            case GAMEPAD_LEFT_THUMB_RIGHT: return "L右";
            case GAMEPAD_LEFT_THUMB_DOWN: return "L下";
            case GAMEPAD_LEFT_THUMB_LEFT: return "L左";
            case GAMEPAD_RIGHT_THUMB_UP: return "R上";
            case GAMEPAD_RIGHT_THUMB_RIGHT: return "R右";
            case GAMEPAD_RIGHT_THUMB_DOWN: return "R下";
            case GAMEPAD_RIGHT_THUMB_LEFT: return "R左";
            case GAMEPAD_DPAD_UP: return "上";
            case GAMEPAD_DPAD_RIGHT: return "右";
            case GAMEPAD_DPAD_DOWN: return "下";
            case GAMEPAD_DPAD_LEFT: return "左";
            default: return null;
        }
    }

    public static Binding fromString(String name) {
        switch (name) {
            case "KEY_CTRL":
                return Binding.KEY_CTRL_L;
            case "KEY_SHIFT":
                return Binding.KEY_SHIFT_L;
            case "KEY_ALT":
                return Binding.KEY_ALT_L;
            default:
                return valueOf(name);
        }
    }

    public Pointer.Button getPointerButton() {
        switch (this) {
            case MOUSE_LEFT_BUTTON:
                return Pointer.Button.BUTTON_LEFT;
            case MOUSE_MIDDLE_BUTTON:
                return Pointer.Button.BUTTON_MIDDLE;
            case MOUSE_RIGHT_BUTTON:
                return Pointer.Button.BUTTON_RIGHT;
            case MOUSE_SCROLL_UP:
                return Pointer.Button.BUTTON_SCROLL_UP;
            case MOUSE_SCROLL_DOWN:
                return Pointer.Button.BUTTON_SCROLL_DOWN;
            default:
                return null;
        }
    }

    public boolean isMouse() {
        return name().startsWith("MOUSE_");
    }

    public boolean isKeyboard() {
        return name().startsWith("KEY_") || this == NONE;
    }

    public boolean isGamepad() {
        return name().startsWith("GAMEPAD_");
    }

    public boolean isMouseMove() {
        return this == MOUSE_MOVE_UP || this == MOUSE_MOVE_RIGHT || this == MOUSE_MOVE_DOWN || this == MOUSE_MOVE_LEFT;
    }

    public static String[] mouseBindingLabels(Context context) {
        ArrayList<String> names = new ArrayList<>();
        for (Binding binding : values()) if (binding.isMouse()) names.add(binding.listLabel(context));
        return names.toArray(new String[0]);
    }

    public static String[] keyboardBindingLabels(Context context) {
        ArrayList<String> labels = new ArrayList<>();
        for (Binding binding : values()) if (binding.isKeyboard()) labels.add(binding.listLabel(context));
        return labels.toArray(new String[0]);
    }

    public static String[] gamepadBindingLabels(Context context) {
        ArrayList<String> names = new ArrayList<>();
        for (Binding binding : values()) if (binding.isGamepad()) names.add(binding.listLabel(context));
        return names.toArray(new String[0]);
    }

    public static Binding[] mouseBindingValues() {
        ArrayList<Binding> labels = new ArrayList<>();
        for (Binding binding : values()) if (binding.isMouse()) labels.add(binding);
        return labels.toArray(new Binding[0]);
    }

    public static Binding[] keyboardBindingValues() {
        ArrayList<Binding> values = new ArrayList<>();
        for (Binding binding : values()) if (binding.isKeyboard()) values.add(binding);
        return values.toArray(new Binding[0]);
    }

    public static Binding[] gamepadBindingValues() {
        ArrayList<Binding> labels = new ArrayList<>();
        for (Binding binding : values()) if (binding.isGamepad()) labels.add(binding);
        return labels.toArray(new Binding[0]);
    }
}
