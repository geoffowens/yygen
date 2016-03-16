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
public class ThreeCorpus extends Object {
    
    public TreeMap<StrPair, ArrayList<String>> corpusMap;   
    public ArrayList<String> initials;
    public ArrayList<String> terminals;
    
    public ThreeCorpus(YakDB yDB) {
        
        corpusMap = new TreeMap<>();
        initials = new ArrayList<>();
        terminals = new ArrayList<>();
               
        for (String msg : yDB.messagesToMap) {
           String[] msgs = msg.toLowerCase().split(" ");
           initials.add(msgs[0]);
           terminals.add(msgs[msgs.length-1]);
           for (int j = 0; j < msgs.length-2; j++) {
               StrPair testKey = new StrPair(msgs[j], msgs[j+1]);
               if (corpusMap.containsKey(testKey)) {
                   ArrayList<String> curVals;
                   curVals = corpusMap.get(testKey);
                   curVals.add(msgs[j+2]);
               } else {
                   ArrayList<String> curVals = new ArrayList<>();
                   curVals.add(msgs[j+2]);
                   corpusMap.put(testKey, curVals);
               } //else
           }  //for j
       } //for i
    }
}
