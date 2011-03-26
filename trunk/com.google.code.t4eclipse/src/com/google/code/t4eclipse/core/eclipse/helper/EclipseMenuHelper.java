/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/
package com.google.code.t4eclipse.core.eclipse.helper;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;



/**
 * This Class is used for click the eclipse main menu TODO: links to the HelperRoot class
 *
 * @author xufengbing
 *
 */
public class EclipseMenuHelper {



    public static EclipseMenuHelper getDefault() {
        return new EclipseMenuHelper();
    }




    /**
     * @param id
     *            menu id: "firstLevelName-secondLevelName-...-lastLevelName"
     */
    public void clickEclipseMenu(String id) {

        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

        CommandTarget ct = locateMenu(shell.getMenuBar(), id);
        if (ct != null) {
            Widget widget = ct.getWidget();
            ct.setFocus();
            Event e = new Event();
            e.type = SWT.Selection;
            e.widget = widget;
            e.display = widget.getDisplay();
           // processDisplayEvents(widget.getDisplay());
            widget.notifyListeners(e.type, e);
        }

    }

 



    public void clickMenu(Menu menu, String id) {

        CommandTarget ct = locateMenu(menu, id);
        if (ct != null) {
            Widget widget = ct.getWidget();
            ct.setFocus();
            Event e = new Event();
            e.type = SWT.Selection;
            e.widget = widget;
            e.display = menu.getDisplay();
            processDisplayEvents(menu.getDisplay());
            widget.notifyListeners(e.type, e);
        }

    }

    private CommandTarget locateMenu(Menu menu, String id) {
        MenuItem menuItem = locateMenuItem(menu, id);
        if (menuItem != null) {
            return new CommandTarget(menuItem, menu);
        }
        return null;

    }

    private MenuItem locateMenuItem(Menu menu, String ids) {
        // the eclipse menu don't need this step
        forceMenuOpen(menu);

        MenuItem[] items = menu.getItems();

        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];

            Menu submenu = item.getMenu();
            if (submenu != null) {
                /*
                 * Ali M.: We have to force the menu to open, since its items may not have
                 * already been created
                 */
                forceMenuOpen(submenu);
                MenuItem hit = locateMenuItem(submenu, ids);
                if (hit != null)
                    return hit;
            } else {
                if (foundItem(item, ids))
                    return item;
            }
        }
        return null;
    }

    private boolean foundItem(MenuItem item, String ids) {

        String itemString = getItemString(item);
        if (itemString != null && itemString.equals(ids))
            return true;
        return false;
    }

    private String getItemString(MenuItem item) {

        StringBuffer descriptiveText = new StringBuffer();
        findItemText(item, descriptiveText);

        /* Remove the ampersands */
        String itemText = descriptiveText.toString().replaceAll("\\&", "");
        return itemText;
    }

    /**
     * Walk through the item and return a descriptive text that corresponds to all item
     * selections leading to the item. (e.g. File-New-Project) works both for tree and
     * menu
     *
     * @param item
     *            The item
     * @param descriptiveText
     *            In the end, this buffer will contain a descriptive text of all items
     *            leading to the item that is passed in as argument
     */
    private void findItemText(Item item, StringBuffer descriptiveText) {
        if (item == null)
            return;

        String descriptiveTextStr = item.getText();
        if (descriptiveText.length() > 0)
            descriptiveTextStr += "-";

        descriptiveText.insert(0, descriptiveTextStr);

        Item parentItem = null;
        if (item instanceof MenuItem) {
            Menu menu = ((MenuItem) item).getParent();
            parentItem = (menu == null ? null : menu.getParentItem());
        } else if (item instanceof TreeItem) {
            parentItem = ((TreeItem) item).getParentItem();
        }

        if (parentItem != null)
            findItemText(parentItem, descriptiveText);
    }

    private void forceMenuOpen(Menu menu) {
        Event e = new Event();
        e.type = SWT.Show;
        e.widget = menu;
        menu.notifyListeners(e.type, e);
        processDisplayEvents(menu.getDisplay());
    }

    private void processDisplayEvents(Display display) {
        for (int i = 0;; i++) {
            if (!display.readAndDispatch())
                break;
        }
    }

}

class CommandTarget {
    private Widget widget;

    private Object context;

    public CommandTarget(Widget widget, Object context) {
        this.widget = widget;
        this.context = context;
    }

    public Widget getWidget() {
        return widget;
    }

    public Object getContext() {
        return context;
    }

    public void setFocus() {

        Display display = widget.getDisplay();
        if (widget instanceof Control) {
            Control c = (Control) widget;
            if (!c.equals(display.getFocusControl()))
                c.setFocus();
        }
    }
}