package demo.enums;

import java.util.EnumSet;

public class EnumSetAndMapDemo {
	  enum Day { SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY }

	  public static void main(String[] args) {
		    for (Day d : EnumSet.range(Day.MONDAY, Day.FRIDAY))
		        System.out.println(d);
//		    EnumSet.of(Style.BOLD, Style.ITALIC)
		    //TODO: more on this topic
	}
}
