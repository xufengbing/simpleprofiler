Current status: initial technical investigation is done(using javasist to instrument eclipse java/plug-in project and collect data during run/debug)
I am quite busy currently and looking for anyone who are interested in this project.

The project aim to provides a simplest performance tuning tool for eclipse environment, with almost no performance lost for host java/eclipse program, fully controllable and  integrated to eclipse development environment seamlessly. (just as eclemma as a perfect eclipse coverage tool)

Existing java profiling tool:

TPTP: http://www.eclipse.org/tptp/

Yourkit: http://www.yourkit.com/    (commercial and worth the money)

MAT: http://www.eclipse.org/mat/  (quite good for memory)

CodePro profile http://www.eclipse.org/proposals/tools.rat/   (will become eclipse project soon)

JVisualVM http://visualvm.java.net/index.html  (quite good in JDK)

Jconsole and JvisualVM
after java1.6 when using hotspot vm, jconsole and jvisualvm can directly(no need to configure jmx:)

#Tryed on windows, some bug found that blocked using in windows, fixing solution is here:
http://abtj.blogspot.com/2007/07/how-to-solve-management-agent-is-not.html







