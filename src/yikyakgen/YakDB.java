/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yikyakgen;

import java.util.ArrayList;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author geoff
 */
public class YakDB extends Object implements java.io.Serializable {
    public ArrayList<String> messagesList;
    public ArrayList<Integer> msgIDList;
    public TreeMap<String,Integer> messageVotesMap;
    public String messagesToMap[];
    
    public YakDB(JSONArray ja){
        messageVotesMap = new TreeMap<>();
        messagesList = new ArrayList<>();
        for (int i = 0; i < ja.length(); i++) {
            JSONObject message = (JSONObject) ja.get(i);
            String text = (String) message.get("message");
            Integer votes = (Integer) (message.get("numberOfLikes"));
            messagesList.add(text);
            messageVotesMap.put(text, votes);
            messagesToMap = new String[messagesList.size()];
            messagesToMap = messagesList.toArray(messagesToMap);
        }
    }
    public YakDB(JSONArray ja, YakDB prev){
        messageVotesMap = prev.messageVotesMap;
        messagesList = prev.messagesList;
        messagesToMap = prev.messagesToMap;
        for (int i = 0; i < ja.length(); i++) {
            JSONObject message = (JSONObject) ja.get(i);
            String text = (String) message.get("message");
            Integer votes = (Integer) (message.get("numberOfLikes"));
            if (messagesList.contains(text)) {
                //System.out.println("match betwen existing and current data");
            } else {
                System.out.println("New Yak!");
                System.out.println(text);
                messagesList.add(text);
                messageVotesMap.put(text, votes);
                messagesToMap = new String[messagesList.size()];
                messagesToMap = messagesList.toArray(messagesToMap);
            }
        }
    }
    
    
}
