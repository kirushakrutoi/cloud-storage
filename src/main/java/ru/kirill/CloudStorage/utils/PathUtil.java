package ru.kirill.CloudStorage.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PathUtil {
    public static Map<String, String> createMap(String path){
        String[] folders = path.split("/");
        Map<String, String> map = new LinkedHashMap<>();
        StringBuilder newPath = new StringBuilder();

        for(String folder : folders){
            newPath.append(folder).append("/");
            map.put(newPath.toString(), folder);
        }

        return map;
    }
}
