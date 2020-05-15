package com.example.demo.Sampling;

import com.example.demo.ServiceInterface.SimilarityAlgorithm;


import java.util.*;
import java.util.regex.Pattern;

public class JaccardSimilarity implements SimilarityAlgorithm {


    /**
     * to match variable
     */
//    private static final Pattern FEATURE_PATTERN = Pattern.compile("[a-zA-Z0-9$_]+");
    private static final Pattern SPACE_REG = Pattern.compile("\\s+");


    private static final int k = 3;

    public double get(final String a, final String b){
        if(a == null){ throw new NullPointerException("a must not be null"); }
        if(b == null){ throw new NullPointerException("b must not be null"); }
        if(a == b){ return 1; }
        return getJaccard(a, b);
    }



    public final Map<String, Integer> getProfile(final String string) {
        HashMap<String, Integer> shingles = new HashMap<String, Integer>();

        String string_no_space = SPACE_REG.matcher(string).replaceAll(" ");
        for (int i = 0; i < (string_no_space.length() - k + 1); i++) {
            String shingle = string_no_space.substring(i, i + k);
            Integer old = shingles.get(shingle);
            if (old != null) {
                shingles.put(shingle, old + 1);
            } else {
                shingles.put(shingle, 1);
            }
        }

        return Collections.unmodifiableMap(shingles);
    }


    private double getJaccard(final String a, final String b){
        Map<String, Integer> profile1 = getProfile(a);
        Map<String, Integer> profile2 = getProfile(b);

        Set<String> union = new HashSet<String>();
        union.addAll(profile1.keySet());
        union.addAll(profile2.keySet());

        int inter = profile1.keySet().size() + profile2.keySet().size() - union.size();


        return 1.0 * inter / union.size();
    }

}
