package org.eclipsercp.book.tools.actions;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipsercp.book.tools.SampleDirectory;
import org.eclipsercp.book.tools.compare.ResourceToFileCompareInput;

public class CompareSamplesOperation {
	
	public void run(SampleDirectory firstElement) {
		CompareConfiguration cc = new CompareConfiguration();
		ResourceToFileCompareInput input = new ResourceToFileCompareInput(
				new Integer(1), firstElement.getChapterNum(), cc);
		input.setSelection(ResourcesPlugin.getWorkspace().getRoot(),
				firstElement.getChapterDirectory());
		// TODO would be nice to have a progress dialog show-up but the compare
		// editor uses the IProgressService which doesn't work with an open
		// dialog.
		CompareUI.openCompareEditor(input);
	}
}
