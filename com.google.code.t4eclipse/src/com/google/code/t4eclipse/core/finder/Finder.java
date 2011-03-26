/*******************************************************************************
 * Copyright (c) 2006 Verigy. All rights reserved.
 *
 * Contributors:
 *     Verigy - initial API and implementation
 *******************************************************************************/
package com.google.code.t4eclipse.core.finder;

import java.util.ArrayList;
import java.util.Stack;


import org.eclipse.swt.widgets.Widget;

import com.google.code.t4eclipse.core.utility.GuilibUtility;


/**
 *
 * This class is used to find a widget in View/Editor/or under a Composite<br >
 *
 * @author xufengbing
 *
 */
public abstract class Finder {

    Stack<Widget> s;

    /**
     * @return default FinderImp
     */
    public static synchronized Finder getDefault() {
        return new FinderImp();
    }

    public Widget findOneByCondition(Widget w, IConditionFind fc) {
        // check the params
        if (w == null || fc == null)
            return null;

        s.push(w);
        while (!s.isEmpty()) {
            Widget tmpWidget = s.pop();
            if (tmpWidget != null) {
                if (fc.check(tmpWidget)) {
                    return tmpWidget;
                }
                addWidget(tmpWidget);
            }
        }
        return null;
    }

    public Widget[] findAllByCondition(Widget w, IConditionFind fc) {
        // check the params
        if (w == null || fc == null)
            return null;

        ArrayList<Widget> ws = new ArrayList<Widget>();

        s.push(w);
        while (!s.isEmpty()) {
            Widget tmpWidget = s.pop();
            if (tmpWidget != null) {
                if (fc.check(tmpWidget)) {
                    ws.add(tmpWidget);
                }
                addWidget(tmpWidget);
            }
        }
        return ws.toArray(new Widget[0]);

    }

    public Widget findIndexByCondition(Widget w, IConditionFind fc, int index) {
        // check the params
        int count = 0;
        if (w == null || fc == null)
            return null;

        s.push(w);
        while (!s.isEmpty()) {
            Widget tmpWidget = s.pop();
            if (tmpWidget != null) {
                if (fc.check(tmpWidget)) {
                    count++;
                    if (count == index) {
                        return tmpWidget;
                    }
                }
                addWidget(tmpWidget);
            }
        }
        return null;
    }

    public Widget findOneByClass(Widget w, Class cas) {
        // check the params
        if (w == null || cas == null)
            return null;

        return findOneByCondition(w, new ConditionClassFind(cas));
    }

    public Widget[] findAllByClass(Widget w, Class cas) {
        // check the params
        if (w == null || cas == null)
            return null;

        return findAllByCondition(w, new ConditionClassFind(cas));
    }

    public Widget findIndexByClass(Widget w, Class cas, int index) {
        // check the params
        if (w == null || cas == null)
            return null;

        return findIndexByCondition(w, new ConditionClassFind(cas), index);
    }

 
 
   
    protected abstract void addWidget(Widget w);

}
