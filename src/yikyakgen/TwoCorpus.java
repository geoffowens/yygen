/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yikyakgen;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author geoff
 */
public class TwoCorpus {
    
    public TreeMap<String, ArrayList<String>> corpusMap;
    
    public TwoCorpus(YakDB yDB) {
        //public static TreeMap<String, ArrayList<String>> createTwoMap(String[] messages) {        
        corpusMap = new TreeMap<>();
        for (String msg : yDB.messagesToMap) {
            String[] words = msg.toLowerCase().split(" ");
            for (int j = 0; j < words.length-2; j++) {
                String testKey = words[j];
                if (corpusMap.containsKey(testKey)) {
                    ArrayList<String> curVals;
                    curVals = corpusMap.get(testKey);
                    curVals.add(words[j+1]);
                    corpusMap.put(testKey, curVals);
                } else {
                    ArrayList<String> curVals = new ArrayList<>();
                    curVals.add(words[j+1]);
                    corpusMap.put(testKey, curVals);
                } //else
            }  //for j
        } //for i
    }
}
