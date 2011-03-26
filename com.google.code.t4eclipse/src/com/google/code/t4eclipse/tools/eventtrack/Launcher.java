package com.google.code.t4eclipse.tools.eventtrack;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Launcher {
    private Shell shell;

    private Map<Integer, String> eventMap = new Hashtable<Integer, String>();
    {
        eventMap.put(SWT.Paint, "Paint");
        eventMap.put(SWT.Selection, "Selection");
        eventMap.put(SWT.DefaultSelection, "DefaultSelection");
        eventMap.put(SWT.Dispose, "Dispose");
        eventMap.put(SWT.FocusIn, "FocusIn");
        eventMap.put(SWT.FocusOut, "FocusOut");
        eventMap.put(SWT.Hide, "Hide");
        eventMap.put(SWT.Show, "Show");
        eventMap.put(SWT.KeyDown, "KeyDown");
        eventMap.put(SWT.KeyUp, "KeyUp");
        eventMap.put(SWT.MouseDown, "MouseDown");
        eventMap.put(SWT.MouseUp, "MouseUp");
        eventMap.put(SWT.MouseDoubleClick, "MouseDoubleClick");
        eventMap.put(SWT.MouseMove, "MouseMove");
        eventMap.put(SWT.Resize, "Resize");
        eventMap.put(SWT.Move, "Move");
        eventMap.put(SWT.Close, "Close");
        eventMap.put(SWT.Activate, "Activate");
        eventMap.put(SWT.Deactivate, "Deactivate");
        eventMap.put(SWT.Iconify, "Iconify");
        eventMap.put(SWT.Deiconify, "Deiconify");
        eventMap.put(SWT.Expand, "Expand");
        eventMap.put(SWT.Collapse, "Collapse");
        eventMap.put(SWT.Modify, "Modify");
        eventMap.put(SWT.Verify, "Verify");
        eventMap.put(SWT.Help, "Help");
        eventMap.put(SWT.Arm, "Arm");
        eventMap.put(SWT.MouseExit, "MouseExit");
        eventMap.put(SWT.MouseEnter, "MouseEnter");
        eventMap.put(SWT.MouseHover, "MouseHover");
        eventMap.put(SWT.Traverse, "Traverse");
    }

    private Listener uniListener = new Listener() {
        public void handleEvent(Event event) {
            String eventName = eventMap.get(event.type);
          
        }
    };

    private void addHooks(Display display) {
        Set<Integer> eventTypes = eventMap.keySet();
        for (Integer eventType : eventTypes)
            display.addFilter(eventType, uniListener);
    }

    private void removeHooks(Display display) {
        Set<Integer> eventTypes = eventMap.keySet();
        for (Integer eventType : eventTypes)
            display.removeFilter(eventType, uniListener);
    }

    public Launcher() {
        super();
        Display display = new Display();
        shell = new Shell(display);
        shell.setText("SWTAppNQ3");
        shell.setSize(320, 240);
        Button button = new Button(shell, SWT.NONE);
        button.setText("Cool Button");
        button
                .addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
                    public void widgetSelected(
                            org.eclipse.swt.events.SelectionEvent e) {
                        MessageBox messageBox = new MessageBox(shell);
                        messageBox.setMessage("Pressed");
                        messageBox.open();
                    }
                });

        button.pack();
        addHooks(display);

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        removeHooks(display);
        display.dispose();
    }

    public static void main(String[] args) {
        new Launcher();
    }
}


