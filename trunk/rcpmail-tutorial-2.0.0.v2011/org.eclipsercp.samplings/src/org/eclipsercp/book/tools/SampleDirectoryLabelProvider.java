/**
 * 
 */
package org.eclipsercp.book.tools;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class SampleDirectoryLabelProvider extends LabelProvider implements IFontProvider {
	private Image image;
	private final ISelectionProvider selectionProvider;

	public SampleDirectoryLabelProvider(ISelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
	}
	
	public String getText(Object element) {
		SampleDirectory chapterRecord = (SampleDirectory) element;
		return (chapterRecord).getChapterDirectory().getName();
	}
	
	public Image getImage(Object element) {
		if(image == null) {
			image = AbstractUIPlugin.imageDescriptorFromPlugin(IConstants.PLUGIN_ID, "icons/bkmrk_nav.gif").createImage(true);
		}
		return image;
	}
	
	public Font getFont(Object element) {
		SampleDirectory chapterRecord = (SampleDirectory) element;
		if(SamplesModel.getImportedSamplesId().equals(chapterRecord.getChapterNum())) {
			return JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT); 
		}
		return null;
	}
}