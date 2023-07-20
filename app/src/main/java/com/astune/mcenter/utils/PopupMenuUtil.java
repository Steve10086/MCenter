package com.astune.mcenter.utils;

import android.view.View;
import androidx.appcompat.widget.PopupMenu;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PopupMenuUtil {
    /**
     * by inflection change the position of popup
     * @param menu popupMenu
     * @param x requested x coordinator
     * @param y requested y coordinator
     */
    public static void showMenuOnPosition(PopupMenu menu, int x, int y){
        try {
            //get MenuPopupHelper mPopup from menu
            Field mPopup = menu.getClass().getDeclaredField("mPopup");
            mPopup.setAccessible(true);
            Object o = mPopup.get(menu);
            //MenuPopupHelper -> show(int x, int y)
            Method show = o.getClass().getMethod("show", int.class, int.class);
            int[] position = new int[2];

            //get relative position of view in the screen
            Field mAnchor = menu.getClass().getDeclaredField("mAnchor");
            mAnchor.setAccessible(true);
            Object v = mAnchor.get(menu);
            Method getLocationInWindow = v.getClass().getMethod("getLocationInWindow", int[].class);
            Method getHeight = v.getClass().getMethod("getHeight");
            getLocationInWindow.invoke(v, position);

            //calc xOffset、yOffset
            int xOffset = (x - position[0]);
            int yOffset = (y - position[1] - (int)getHeight.invoke(v));
            show.invoke(o, xOffset, yOffset);
        } catch (NoSuchFieldException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            // prevent possible bug
            menu.show();
        }
    }
}
