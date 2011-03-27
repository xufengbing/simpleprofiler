/**
 * 
 */
package org.eclipsercp.book.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class SampleDirectory {
	
	public static class ProjectImportDef {
		private Integer sampleNumber;
		private String projectName;
		
		public ProjectImportDef(Integer sampleNumber, String projectName) {
			this.sampleNumber = sampleNumber;
			this.projectName = projectName;
		}

		public String getProjectName() {
			return projectName;
		}

		public Integer getSampleNumber() {
			return sampleNumber;
		}
	}
	
	private File chapterDirectory;

	private Integer chapterNum;

	private List projects = new ArrayList();
	
	private List importDefs;

	SampleDirectory(File dir) {
		this.chapterDirectory = dir;
		extractChapterNum();
		extractDependencies();
	}

	private void extractDependencies() {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(java.io.File dir, String name) {
				return name.equals("imports.def");
			}
		};
		String[] candidates = chapterDirectory.list(filter);
		if(candidates.length == 1) {
			File importsFile = new File(chapterDirectory, candidates[0]);
			Properties props = null;
			if (importsFile.exists()) {
				try {
					props = new Properties();
					FileInputStream fis = new FileInputStream(importsFile);
					try {
						props.load(fis);
					} finally {
						fis.close();
					}
				} catch (IOException e) {
					props = null;
				}
				if(props != null) {
					importDefs = new ArrayList(props.size());
					for (Iterator it = props.keySet().iterator(); it.hasNext();) {
						String key = (String) it.next();
						try {
							Integer sampleNum = Integer.valueOf(key);
							String projectName = props.getProperty(key);
							importDefs.add(new ProjectImportDef(sampleNum, projectName));
						} catch(NumberFormatException e) {
							// skip
						}
					}
				}
			}
			
		}
	}

	private void extractChapterNum() {
		java.util.StringTokenizer tz = new java.util.StringTokenizer(chapterDirectory.getName());
		while(tz.hasMoreTokens()) {
			String elem = tz.nextToken();		
			try {
				chapterNum = Integer.valueOf(elem);
				return;
			} catch(NumberFormatException e) {
				chapterNum = new Integer(Integer.MAX_VALUE);
			}	
		}
	}

	void addProject(SampleProject project) {
		projects.add(project);
	}

	public Integer getChapterNum() {
		return chapterNum;
	}

	public File getChapterDirectory() {
		return chapterDirectory;
	}

	public List getProjects() {
		return projects;
	}
	
	public ProjectImportDef[] getImports() {
		if(importDefs == null)
			return new ProjectImportDef[0];
		return (ProjectImportDef[]) importDefs.toArray(new ProjectImportDef[importDefs.size()]);
	}
}