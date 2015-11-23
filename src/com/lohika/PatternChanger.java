package com.lohika;

import java.util.Map;

/**
 * @author Pavel on 11/23/15.
 */
public class PatternChanger {
    public static String changePattern(String pattern, Map<String, String> changes) {
        // usage example
        // mp.put("[Drish]", "Pish");
        // String res = changePattern("pish pish [drish]", mp);
        String result = pattern;
        for (Map.Entry<String, String> change : changes.entrySet())
            result = result.replaceAll("(?i)\\Q" + change.getKey()+"\\E", change.getValue());
        return result;
    }
}
