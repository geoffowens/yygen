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
public class TwoCorpus extends Object {
    
    public TreeMap<String, ArrayList<String>> corpusMap;
    public ArrayList<String> initials;
    public ArrayList<String> terminals;
    //we also want to store Initial symbols and Terminal symbols
    
    public TwoCorpus(YakDB yDB) {      
        corpusMap = new TreeMap<>();
        initials = new ArrayList<>();
        terminals = new ArrayList<>();
        for (String msg : yDB.messagesToMap) {
            String[] words = msg.toLowerCase().split(" ");
            initials.add(words[0]);
            terminals.add(words[words.length-1]);
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
