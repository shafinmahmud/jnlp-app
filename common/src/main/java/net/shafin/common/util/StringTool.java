package net.shafin.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class StringTool {

    /*
     * Some times UTF-8 encoded Text contains spaces that are not in general we
     * use. They are some special kind. Below is the list of all UNICODE space
     * charactes that we need to remove from the text for usual text processing.
     */
    private static final String REGEX_IN_GENERAL_PUNCTUATION = "\\p{InGeneral_Punctuation}";


    /*
     * In preprocessing we removes the UNICODE_SPACE_CHARACRTES from Text
     */
    public static String cleanPunctuation(String sb) {
        String temp = StringUtils.replacePattern(sb, REGEX_IN_GENERAL_PUNCTUATION, " ");
        temp = StringUtils.replacePattern(temp, "[\\*]", " ");
        return StringUtils.normalizeSpace(temp);
    }

    public static void main(String[] args) {
        String text = "সমাজ**অ**কল্যাণমন্ত্রীকে";
        System.out.println(text + " : " + cleanPunctuation(text));
    }

}
