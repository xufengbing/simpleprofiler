/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/
package com.google.code.t4eclipse.core.utility;


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import com.google.code.t4eclipse.core.swt.helper.HelperRoot;
import com.google.code.t4eclipse.core.swt.helper.Displays.BooleanResult;


/**
 *
 * for all applications ,only one such thread exists actively
 *
 * @author xufengbing
 *
 */
class CheckBlockShell extends HelperRoot implements Runnable {

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (true) {
            sleepPrim(10 * 1000);
            boolean isTimeOut = syncExec(new BooleanResult() {

                public boolean result() {
                    Shell s = Display.getDefault().getActiveShell();
                    if (s != null) {
                        Object data = s.getData(GuilibUtility.TIME_OUT_START_AT);
                        if (data != null) {
                            long timeStart = (Long) data;
                            long used = System.currentTimeMillis() - timeStart;
                            if (used > GuilibUtility.TIME_OUT_FOR_SHELL) {
                                s.dispose();
                                return true;
                            }

                        }
                    }
                    return false;
                }
            });
            if (isTimeOut) {

            }
        }
    }

    private void sleepPrim(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException exception) {
            // Empty block intended.
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.google.code.t4eclipse.core.swt.helper.HelperRoot#checkInstance(org.eclipse.swt.widgets.Widget)
     */
    protected void checkInstance(Widget widget) {
        // TODO Auto-generated method stub

    }
}
