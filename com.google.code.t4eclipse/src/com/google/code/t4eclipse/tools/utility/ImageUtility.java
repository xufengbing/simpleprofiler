package com.google.code.t4eclipse.tools.utility;

import java.io.InputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
 

public class ImageUtility {

	/**
	 * Loads an image from the root of this package.
 	 * @param name The string name inclusive path to the image.
	 * @return returns the image or null.
	 */
	public static Image loadImageResource(String name) {
		try {
			Image ret = null;
			InputStream is =ImageUtility.class.getResourceAsStream(name);
			if (is != null) {
				ret = new Image(Display.getCurrent(), is);
				is.close();
			}
			return ret;
		} catch (Exception e1) {
			return null;
		}
	}
 

}
