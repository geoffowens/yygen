/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yikyakgen;

import Yak_Hax.Yak_Hax_Mimerme.Exceptions.RequestException;
import Yak_Hax.Yak_Hax_Mimerme.Exceptions.SleepyServerException;
import Yak_Hax.Yak_Hax_Mimerme.YikYakAPI;
import Yak_Hax.Yak_Hax_Mimerme.YikYakProfile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author geoff
 */
public class YikYakGen {

    public File inFile;
    public static YakDB yDB;
    public static TwoCorpus corpus;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        YikYakProfile.USER_ID = "9353E632AC4DE6F88CBB1E74D83180E1";
        YikYakProfile.TOKEN = "79177FDC5F29169E63AFFC6384FE6DA3";
        //UDID = 71C0E6EA65A2BA47084CEB931A604702
        YikYakProfile.USER_AGENT = "Dalvik/1.6.0 (Linux; U; Android 4.4.4; Nexus 5 Build/9785,) 2.9.2";
        YikYakProfile.ALTITUDE = "0";
        YikYakProfile.LAT = "41.789146";
        YikYakProfile.LONG = "-87.601066";
        YikYakProfile.ACCURACY = "1";
        YikYakProfile.BASECAMP = "0";

        //Build request map for request
        TreeMap<String, String> requestMap = new TreeMap<>();
        requestMap.put("userID", YikYakProfile.USER_ID);
        requestMap.put("lat", YikYakProfile.LAT);
        requestMap.put("long", YikYakProfile.LONG);
        requestMap.put("userLat", YikYakProfile.LAT);
        requestMap.put("userLong", YikYakProfile.LONG);
        requestMap.put("accuracy", YikYakProfile.ACCURACY);
        requestMap.put("bc", YikYakProfile.BASECAMP);

        //Login to yikyak api
        YikYakAPI.login(YikYakProfile.USER_ID, YikYakProfile.TOKEN, YikYakProfile.USER_AGENT);

        //Create list of yaks using YakDB object
        String result;
        result = attemptQuery(requestMap);
        result = result.substring(8, result.length() - 8);
        JSONObject resultObject = new JSONObject(result.trim());
        JSONArray messagesArray = (JSONArray) resultObject.get("messages");
        try {
            FileInputStream fileIn = new FileInputStream("./yakdb.ser");
            ObjectInputStream inStream = new ObjectInputStream(fileIn);
            YakDB oldDB;
            oldDB = (YakDB) inStream.readObject();
            inStream.close();
            fileIn.close();
      
            yDB = new YakDB(messagesArray, oldDB);
            
        } catch (IOException i) {
            i.printStackTrace();
            yDB = new YakDB(messagesArray);
        } catch (ClassNotFoundException ex) {
            yDB = new YakDB(messagesArray);
            Logger.getLogger(YikYakGen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        corpus = new TwoCorpus(yDB);

        try {
            FileOutputStream fileOut = new FileOutputStream("./yakdb.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(yDB);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in ./yakdb.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            ArrayList<String> wordsList = wordsWithTerminals(corpus);
            String[] wordsArray = new String[wordsList.size()];
            wordsArray = wordsList.toArray(wordsArray);
            String out = "";
            for (String word : wordsArray) {
                out = out + " " + word;
            }
            System.out.println(out);
        }
    }

    public static String attemptQuery(TreeMap<String, String> rm) {
        YikYakAPI.login(YikYakProfile.USER_ID, YikYakProfile.TOKEN, YikYakProfile.USER_AGENT);
        try {
            return (YikYakAPI.getYaks(rm)).toString();
        } catch (IOException ex) {
            Logger.getLogger(YikYakGen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SleepyServerException ex) {
            Logger.getLogger(YikYakGen.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex1) {
                Logger.getLogger(YikYakGen.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return attemptQuery(rm);
        } catch (RequestException ex) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex1) {
                Logger.getLogger(YikYakGen.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return attemptQuery(rm);
        }
        return "";
    }

    public static ArrayList<String> pickTenWords(TreeMap<String, ArrayList<String>> map) {
        Random random = new Random();
        ArrayList<String> keys = new ArrayList<>(map.keySet());
        ArrayList<String> ret = new ArrayList<>();
        ret.add(keys.get(random.nextInt(keys.size())));
        for (int i = 0; i < 20; i++) {
            ArrayList<String> potentials = map.get(ret.get(ret.size() - 1));
            if (potentials == null) {
                return ret;
            }
            //otherwise: select a random element of potentials and add it to ret
            ret.add(potentials.get(random.nextInt(potentials.size())));
        }
        return ret;
    }
    
    public static ArrayList<String> wordsWithTerminals(TwoCorpus corpus) {
        Random random = new Random();
        ArrayList<String> keys = new ArrayList<>(corpus.corpusMap.keySet());
        ArrayList<String> ret = new ArrayList<>();
        ret.add(corpus.initials.get(random.nextInt(corpus.initials.size())));
        for (int i = 0; i < 20; i++) {
            ArrayList<String> potentials = corpus.corpusMap.get(ret.get(ret.size() - 1));
            if (potentials == null) {
                return ret;
            }
            String word = potentials.get(random.nextInt(potentials.size()));
            ret.add(word);
            if (corpus.terminals.contains(word)) {
                return ret;
            }
        }
        return ret;
    }
}
