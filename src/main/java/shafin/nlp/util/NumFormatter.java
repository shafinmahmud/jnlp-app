package shafin.nlp.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumFormatter {

	public static String formatDecimal(Double value) {
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.CEILING);
		return df.format(value);
	}
}
