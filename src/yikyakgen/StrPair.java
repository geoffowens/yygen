/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yikyakgen;

/**
 *
 * @author geoff
 */
public class StrPair extends Object implements Comparable {
    public String x;
    public String y;
    
    public StrPair(String xa, String ya){
        this.x = xa;
        this.y = ya;
    }

    @Override
    public int compareTo(Object o) {
        StrPair obj = (StrPair) o;
        if (this.x.equals(obj.x)){
            if (this.x.equals(obj.y)) {
                return 0;
            }
        }
        return -1;
    }
    
}
