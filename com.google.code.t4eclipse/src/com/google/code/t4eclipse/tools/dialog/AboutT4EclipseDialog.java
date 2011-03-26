/*
 * Created on Dec 24, 2005
 *
 */
package com.google.code.t4eclipse.tools.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.about.AboutItem;
//import org.eclipse.ui.internal.about.AboutTextManager;

import com.google.code.t4eclipse.tools.utility.ImageUtility;

public class AboutT4EclipseDialog extends Dialog {
//	private AboutItem item;

	public AboutT4EclipseDialog(Shell parentShell) {
		super(parentShell);
//		item = AboutTextManager.scan(this.getT4EclipseDescriptionText());
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("About T4Eclipse");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite root = new Composite(parent, SWT.None);
		Color background = JFaceColors.getBannerBackground(parent.getDisplay());
		Color foreground = JFaceColors.getBannerForeground(parent.getDisplay());
		root.setBackground(background);
		root.setForeground(foreground);

		final int minWidth = 500;
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.minimumWidth = minWidth;
		root.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;

		root.setLayout(layout);
		Label imageLabel = new Label(root, SWT.NONE);

		final Image image = ImageUtility
				.loadImageResource("/icons/bigT4Eclipse2.GIF");
		imageLabel.setImage(image);
		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_CENTER;

		imageLabel.setLayoutData(data);

		StyledText text = new StyledText(root, SWT.MULTI | SWT.WRAP
				| SWT.READ_ONLY);

		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.minimumWidth = minWidth;
		text.setLayoutData(data);

		text.setCaret(null);
		text.setFont(parent.getFont());
		text.setText(getT4EclipseDescriptionText());
		text.setCursor(null);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));

//		AboutTextManager aboutTextManager = new AboutTextManager(text);
//		aboutTextManager.setItem(item);

		applyDialogFont(parent);
		return parent;
	}

	static void addImage(Image image, int offset, StyledText styledText) {
		StyleRange style = new StyleRange();
		style.start = offset;
		style.length = 1;
//		style.data = image;
		Rectangle rect = image.getBounds();
		style.metrics = new GlyphMetrics(rect.height, 0, rect.width);
		styledText.setStyleRange(style);
	}

	private String getT4EclipseDescriptionText() {
		String info = "T4Eclipse \n";
		info += "Version:1.2.1\n";
		info += "Licensed under the Eclipse Public License 1.0.\n";
		info += "Copyright Ben Xu. 2008-2010\n\n";
		info += "T4Eclipse project home:\n";
		info += "http://code.google.com/p/t4eclipse/\n\n";
		info += "T4Eclipse @ Eclipse MarketPlace:\n";
		info += "http://marketplace.eclipse.org/content/tool-eclipse-t4eclipse\n";

		return info;
	}

}
