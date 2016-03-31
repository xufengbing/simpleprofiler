**these two tool exist both on linux and window system**

**Getting Started with Java Management Extensions:**http://java.sun.com/developer/technicalArticles/J2SE/jmx.html

**Using Jconsole**http://download.oracle.com/javase/6/docs/technotes/guides/management/jconsole.html





**How To Solve "management agent is not enabled" Problem for JConsole**from: http://abtj.blogspot.com/2007/07/how-to-solve-management-agent-is-not.html

JConsole from JDK 6 supports dynamic attach capability, this means it is able to attach any running Java application which does not need to start with -Dcom.sun.management.jmxremote option. However, you may not able to attach a running Java process because of "The management agent is not enabled on this process" error. To solve this, just create a new temporary directory (e.g. C:\tmp) and points your TMP environment to there. (Control Panel -> System -> Advanced -> Environment Variables). Restart your Java application and now your JConsole should be able to attach it now. For more troubleshooting tip, you may read Daniel Fuchs Blogs and Sun Forum.