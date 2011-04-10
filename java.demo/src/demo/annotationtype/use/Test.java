package demo.annotationtype.use;
import demo.annotationtype.*;
public class Test {

	@RequestForEnhancement(id = 2868724, synopsis = "Enable time-travel", engineer = "Mr. Peabody", date = "4/1/3007")
	public static void test() {

	}

	@Preliminary
	public static void test2() {

	}
}

/**
 * It is permissible to omit the element name and equals sign (=) in a
 * single-element annotation whose element name is value, as shown below:
 **/

@Copyright("2002 Yoyodyne Propulsion Systems")
class OscillationOverthruster {
}