package org.lastdesire.ldplugins.utils;

import java.util.HashMap;
import java.util.HashSet;

public class StringBigraph {
    private HashMap<String,HashSet<String>> leftMap,rightMap;

    public StringBigraph(){
        leftMap = new HashMap<String, HashSet<String>>();
        rightMap = new HashMap<String, HashSet<String>>();
    }

    public void addToMap(String lvar, String rvar){
        addToLeftMap(lvar, rvar);
        addToRightMap(rvar, lvar);
    }

    private void addToLeftMap(String key, String value){
        addToMap(leftMap,key,value);
    }

    private void addToRightMap(String key, String value){
        addToMap(rightMap,key,value);
    }

    private void addToMap(HashMap<String,HashSet<String>> map, String key, String value){
        if(map.containsKey(key)){
            map.get(key).add(value);
        }
        else {
            HashSet<String> set = new HashSet<String>();
            set.add(value);
            map.put(key,set);
        }
    }

    public boolean containsLeftKey(String key){
        return leftMap.containsKey(key);
    }

    public boolean containsRightKey(String key){
        return rightMap.containsKey(key);
    }

    public HashSet<String> getMappingToRight(String leftKey){
        return leftMap.get(leftKey);
    }

    public HashSet<String> getMappingToLeft(String rightKey){
        return rightMap.get(rightKey);
    }
}
