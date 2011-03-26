package com.google.code.t4eclipse.core.utility;

import java.util.HashMap;
import java.util.Map;

public class StringUtility {
	public static String processString(String old) {
		if (old == null)
			return old;
		else
			return old.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"",
					"\\\\\\\"").replaceAll("\\\'", "\\\\\\\'");


	}


	public void test(){
		Map utility=new HashMap();
		utility.put("Primaries","");
		utility.put("Level","");
		utility.put("Level Equation","1");
		utility.put("Level Equation Note","");
		utility.put("Level Spec/DPS","2");
		utility.put("Level Spec/DPS Note","");
		utility.put("Level Set","2");
		utility.put("Level Set Note","");
		utility.put("Timing","");
		utility.put("Timing Equation","1");
		utility.put("Timing Equation Note","");
		utility.put("Timing Spec/Wave","2");
		utility.put("Timing Spec/Wave Note","");
		utility.put("Timing Set","2");
		utility.put("Timing Set Note","");
		utility.put("Analog","");
		utility.put("Vector","\"spec_search\"");
		utility.put("Result","");
		utility.put("Pass/Fail","");
		utility.put("Exec Time","0.0");
		utility.put("Exec Num","0");
		utility.put("Testsuite Flags","");
		utility.put("Bypass","");
		utility.put("Set pass","");
		utility.put("Set fail","");
		utility.put("Hold","");
		utility.put("Hold on fail","");
		utility.put("Output on pass","");
		utility.put("Output on fail","");
		utility.put("Value on pass","");
		utility.put("Value on fail","");
		utility.put("Per pin on pass","");
		utility.put("Per pin on fail","");
		utility.put("WDB break point","");
		utility.put("Allow history","");
		utility.put("Force serial","");
		utility.put("Emulator disable","");
		utility.put("MX waves enable","");
		utility.put("Fail per label","");
		utility.put("FFC enable","");
		utility.put("FFC scan enable","");
		utility.put("Log first","");
		utility.put("FFV enable","");
		utility.put("FRG enable","");
		utility.put("H/L/I/U (multi exec)","");
		utility.put("Stop","");
		utility.put("HW DSP disable","");
		utility.put("Debug HW DSP","");
		utility.put("Exist HW DSP script","");
		utility.put("TM parameters","0");
		utility.put("Limit and Test No.","6");
		utility.put("DEFAULT FUNCTIONAL TEST","::");
		utility.put("Limit Name","DEFAULT FUNCTIONAL TEST");
		utility.put("Limit Value","");
		utility.put("Testnumber offset","");
		utility.put("Testnumber Increment","");
		utility.put("DEFAULT GENERAL TEST","::");
		utility.put("Limit Name","DEFAULT GENERAL TEST");
		utility.put("Limit Value","");
		utility.put("Testnumber offset","");
		utility.put("Testnumber Increment","");
		utility.put("DEFAULT DPS TEST","::");
		utility.put("Limit Name","DEFAULT DPS TEST");
		utility.put("Limit Value","");
		utility.put("Testnumber offset","");
		utility.put("Testnumber Increment","");
		utility.put("DEFAULT PMU TEST","::");
		utility.put("Limit Name","DEFAULT PMU TEST");
		utility.put("Limit Value","");
		utility.put("Testnumber offset","");
		utility.put("Testnumber Increment","");
		utility.put("DEFAULT SPEC SEARCH","::");
		utility.put("Limit Name","DEFAULT SPEC SEARCH");
		utility.put("Limit Value","");
		utility.put("Testnumber offset","");
		utility.put("Testnumber Increment","");
		utility.put("DEFAULT TIA TEST","::");
		utility.put("Limit Name","DEFAULT TIA TEST");
		utility.put("Limit Value","");
		utility.put("Testnumber offset","");
		utility.put("Testnumber Increment","");
	}
}
