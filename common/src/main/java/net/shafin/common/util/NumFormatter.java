package net.shafin.common.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class NumFormatter {

    public static String formatDecimal(Double value) {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(value);
    }
}
