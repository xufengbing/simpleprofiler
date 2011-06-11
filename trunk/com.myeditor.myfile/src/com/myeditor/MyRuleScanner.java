package com.myeditor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class MyRuleScanner extends RuleBasedScanner {
	private static Color TAG_COLOR =
		new Color(Display.getCurrent(), new RGB(200, 0, 0));
	private static Color COMMENT_COLOR =
		new Color(Display.getCurrent(), new RGB(0, 200, 0));

	public MyRuleScanner() {
		IToken tagToken = new Token(new TextAttribute(TAG_COLOR));
		IToken commentToken = new Token(new TextAttribute(COMMENT_COLOR));

		IRule[] rules = new IRule[2];
		rules[0] = new SingleLineRule("<myTag", "myTag>", tagToken);
		rules[1] = (new EndOfLineRule("//", commentToken));
		setRules(rules);
	}
}
