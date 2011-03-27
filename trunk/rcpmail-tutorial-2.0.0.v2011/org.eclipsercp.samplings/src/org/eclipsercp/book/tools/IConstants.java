package org.eclipsercp.book.tools;

import org.eclipse.core.runtime.QualifiedName;

public interface IConstants {

	/*
	 * Plug-in ids for both the tools plug-in and the plug-in that contains the
	 * sample code.
	 */
	public static final String PLUGIN_ID = "org.eclipsercp.samplings";

	/*
	 * Preference node name for the path to the samples directory
	 */
	public static final String PATH_PREF = "path";

	public static final String PROMPT_OVERWRITE_PREF = "dont_ask_again_to_overwrite";

	/*
	 * Persistant property used to cache the chapter number currently loaded in
	 * the workspace. The property is stored on each imported project.
	 */
	public QualifiedName CHAP_NUM_KEY = new QualifiedName(
			"org.eclipsercp.book.tools", "chapnum");

	/*
	 * Persistent property used to remember which projects in the workspace
	 * where created by the samples manager. This allows the tool to clean-up
	 * only the projects in created.
	 */
	public QualifiedName SAMPLES_KEY = new QualifiedName(
			"org.eclipsercp.book.tools", "sample-project");
}
