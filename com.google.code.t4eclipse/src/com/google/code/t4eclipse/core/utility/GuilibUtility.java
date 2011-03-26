/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/

package com.google.code.t4eclipse.core.utility;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tracker;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;



/**
 * a utility class used for this framework<br>
 *
 * @author xufengbing
 *
 */
public class GuilibUtility {
    /**
     * TODO: use eclipse options to set such values.<br>
     * this var specify time delay on each actions increase this value for debug purpose
     * to show the UI operation
     */

    public static int TIME_INTERVAL = 0;

    /**
     * this var specify that the max time for a shell active on screen (the shell is not
     * the eclipse shell.<br>
     *
     * Default time is 2 minutes.
     */
    public static int TIME_OUT_FOR_SHELL = 2 * 60 * 1000;

    /**
     * TAG_ATTRIBUTE is the default attribute name used when set the widget attribute<br>*
     *
     * @see com.google.code.t4eclipse.core.finder.Finder#Finder()
     */
    public static final String DEFAULT_TAG_ATTRIBUTE = "93k_widget_tag";

    /**
     * this tag is used to tag a shell.
     */
    public static final String TIME_OUT_START_AT = "time_out_start_at";

    private static boolean isTimeOutDaemonThreadStarted = false;

    public static void runCheckTimeOutThread() {
        // only one instance of the checkblockshell thread need to be run during test
        if (!isTimeOutDaemonThreadStarted) {
            addTimeOutFilter();
            Thread thread = new Thread(new CheckBlockShell());
            thread.setDaemon(true);
            thread.start();
            isTimeOutDaemonThreadStarted = true;
        }
    }

    private static void addTimeOutFilter() {
        Display.getDefault().addFilter(SWT.Show, new Listener() {

            public void handleEvent(Event event) {

                Widget w = event.widget;
                if (w instanceof Shell) {
                    w
                            .setData(GuilibUtility.TIME_OUT_START_AT, System
                                    .currentTimeMillis());
                }

            }
        });
    }


    /**
     * this method is used to print style of a swt widget
     *
     * @param w
     * @return style string
     */
    public static String getStyle(Widget w) {
        int style = w.getStyle();
        String result = "";
        // -1 is the default one
        if (style == SWT.DEFAULT) {
            return "DEFAULT - bad!";
        }

        // << first, second right now 0. like 10 , 11, 110........
        if ((style & 1 << 1) != 0) {
            if (w instanceof CTabFolder || w instanceof StyledText || w instanceof List
                    || w instanceof Text || w instanceof Table || w instanceof Tree) {
                result += "MULTI | ";
            } else if (w instanceof Menu) {
                result += "BAR | ";
            } else if (w instanceof Label || w instanceof MenuItem
                    || w instanceof ToolItem) {
                result += "SEPARATOR | ";
            } else if (w instanceof Button) {
                result += "TOGGLE | ";
            } else if (w instanceof ProgressBar) {
                result += "INDETERMINATE | ";
            } else {
                result += "BAR or SEPARATOR or TOGGLE or MULTI or INDETERMINATE or DBCS | ";
            }
        }
        if ((style & 1 << 2) != 0) {
            if (w instanceof Menu || w instanceof ToolItem || w instanceof CoolItem
                    || w instanceof Combo) {
                result += "DROP_DOWN | ";
            } else if (w instanceof Button) {
                result += "ARROW | ";
            } else if (w instanceof CTabFolder || w instanceof StyledText
                    || w instanceof List || w instanceof Text || w instanceof Table
                    || w instanceof Tree) {
                result += "SINGLE | ";
            } else if (w instanceof Label || w instanceof Group) {
                result += "SHADOW_IN | ";
            } else if (w instanceof Decorations) {
                result += "TOOL | ";
            } else {
                result += "ALPHA or TOOL or SINGLE or ARROW or DROP_DOWN or SHADOW_IN | ";
            }
        }
        if ((style & 1 << 3) != 0) {
            if (w instanceof Menu) {
                result += "POP_UP | ";
            } else if (w instanceof Button || w instanceof MenuItem
                    || w instanceof ToolItem) {
                result += "PUSH | ";
            } else if (w instanceof Combo || w instanceof Text || w instanceof StyledText) {
                result += "READ_ONLY | ";
            } else if (w instanceof Label || w instanceof Group || w instanceof ToolBar) {
                result += "SHADOW_OUT | ";
            } else if (w instanceof Decorations) {
                result += "NO_TRIM | ";
            } else {
                result += "POP_UP or PUSH or READ_ONLY or SHADOW_OUT or NO_TRIM or NATIVE | ";
            }
        }
        if ((style & 1 << 4) != 0) {
            if (w instanceof Button || w instanceof MenuItem || w instanceof ToolItem) {
                result += "RADIO | ";
            } else if (w instanceof Group) {
                result += "SHADOW_ETCHED_IN | ";
            } else if (w instanceof Decorations || w instanceof Tracker) {
                result += "RESIZE | ";
            } else {
                result += "RESIZE or SHADOW_ETCHED_IN or RADIO or PHONETIC | ";
            }
        }
        if ((style & 1 << 5) != 0) {
            if (w instanceof Button || w instanceof MenuItem || w instanceof ToolItem
                    || w instanceof Table || w instanceof Tree) {
                result += "CHECK | ";
            } else if (w instanceof Label || w instanceof Group) {
                result += "SHADOW_NONE | ";
            } else if (w instanceof Decorations) {
                result += "TITLE | ";
                // } else if (w instanceof DateTime) {
            } else if (false) {

                result += "DATE | ";
            } else {
                result += "ROMAN or CHECK  or SHADOW_NONE or TITLE | ";
            }
        }
        if ((style & 1 << 6) != 0) {
            if (w instanceof MenuItem) {
                result += "CASCADE | ";
            } else if (w instanceof StyledText || w instanceof Label || w instanceof Text
                    || w instanceof ToolBar) {
                result += "WRAP | ";
            } else if (w instanceof Combo) {
                result += "SIMPLE | ";
            } else if (w instanceof Group) {
                result += "SHADOW_ETCHED_OUT | ";
            } else if (w instanceof Decorations || w instanceof CTabFolder
                    || w instanceof CTabItem) {
                result += "CLOSE | ";
            } else {
                result += "CLOSE or MENU or CASCADE or WRAP or SIMPLE or SHADOW_ETCHED_OUT | ";
            }
        }
        if ((style & 1 << 7) != 0) {
            if (w instanceof Decorations) {
                result += "MIN | ";
            } else if (w instanceof Button || w instanceof Tracker) {
                result += "UP | ";
            } else if (w instanceof CTabFolder) {
                result += "TOP | ";
                // } else if (w instanceof DateTime) {
            } else if (false) {

                result += "TIME | ";
            } else {
                result += "MIN or UP or TOP | ";
            }
        }
        if ((style & 1 << 8) != 0) {
            result += "HORIZONTAL | ";
        }
        if ((style & 1 << 9) != 0) {
            result += "VERTICAL | ";
        }
        if ((style & 1 << 10) != 0) {
            if (w instanceof Decorations) {
                result += "MAX | ";
            } else if (w instanceof Button || w instanceof Tracker) {
                result += "DOWN | ";
            } else if (w instanceof CTabFolder) {
                result += "BOTTOM | ";
                // } else if (w instanceof DateTime) {
            } else if (false) {
                result += "CALENDAR | ";
            } else {
                result += "MAX or DOWN or BOTTOM | ";
            }
        }
        if ((style & 1 << 11) != 0) {
            result += "BORDER | ";
        }
        if ((style & 1 << 12) != 0) {

            result += "CLIP_CHILDREN | ";

        }
        if ((style & 1 << 13) != 0) {
            result += "CLIP_SIBLINGS | ";
        }
        if ((style & 1 << 14) != 0) {
            result += "ON_TOP or LEAD or LEFT | ";
        }
        if ((style & 1 << 15) != 0) {
            if (w instanceof Shell) {
                result += "PRIMARY_MODAL | ";
            } else if (w instanceof Table || w instanceof Tree) {
                result += "HIDE_SELECTION | ";
            }
            // else if (w instanceof DateTime) {
            // result += "SHORT | ";
            // }

            else {
                result += "PRIMARY_MODAL or HIDE_SELECTION | ";
            }
        }
        if ((style & 1 << 16) != 0) {
            if (w instanceof StyledText || w instanceof Table || w instanceof Tree) {
                result += "FULL_SELECTION | ";
            } else if (w instanceof Shell) {
                result += "APPLICATION_MODAL | ";
            } else if (w instanceof ProgressBar) {
                result += "SMOOTH | ";
            }
            // else if (w instanceof DateTime) {
            // result += "MEDIUM | ";
            // }
            //
            else {
                result += "FULL_SELECTION or SMOOTH or APPLICATION_MODAL | ";
            }
        }
        if ((style & 1 << 17) != 0) {
            if (w instanceof Shell) {
                result += "SYSTEM_MODAL | ";
            } else if (w instanceof Button || w instanceof Label
                    || w instanceof TableColumn || w instanceof Tracker
                    || w instanceof ToolBar) {
                result += "TRAIL | ";
            } else {
                result += "SYSTEM_MODAL or TRAIL or RIGHT | ";
            }
        }
        if ((style & 1 << 18) != 0) {
            result += "NO_BACKGROUND | ";
        }
        if ((style & 1 << 19) != 0) {
            result += "NO_FOCUS | ";
        }
        if ((style & 1 << 20) != 0) {
            result += "NO_REDRAW_RESIZE | ";
        }
        if ((style & 1 << 21) != 0) {
            result += "NO_MERGE_PAINTS | ";
        }
        if ((style & 1 << 22) != 0) {
            if (w instanceof Text) {
                result += "PASSWORD | ";
            } else if (w instanceof Composite) {
                result += "NO_RADIO_GROUP | ";
            } else {
                result += "NO_RADIO_GROUP or PASSWORD | ";
            }
        }
        if ((style & 1 << 23) != 0) {
            result += "FLAT | ";
        }
        if ((style & 1 << 24) != 0) {
            if (w instanceof Button || w instanceof Label || w instanceof TableColumn) {
                result += "CENTER | ";
            } else {
                result += "EMBEDDED or CENTER | ";
            }
        }
        if ((style & 1 << 25) != 0) {
            result += "LEFT_TO_RIGHT | ";
        }
        if ((style & 1 << 26) != 0) {
            result += "RIGHT_TO_LEFT | ";
        }
        if ((style & 1 << 27) != 0) {
            result += "MIRRORED | ";
        }
        if ((style & 1 << 28) != 0) {
            // if (w instanceof DateTime) {
            if (false) {
                result += "LONG | ";
            } else {
                result += "VIRTUAL | ";
            }
        }
        if ((style & 1 << 29) != 0) {
            result += "DOUBLE_BUFFERED | ";
        }
        int lastOr = result.lastIndexOf("|");
        if (lastOr == result.length() - 2)
            result = result.substring(0, result.length() - 2);
        return result;
    }
 

}
