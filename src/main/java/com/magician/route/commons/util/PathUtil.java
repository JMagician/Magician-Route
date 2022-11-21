package com.magician.route.commons.util;

/**
 * Process uri to get the required parts
 */
public class PathUtil {

    /**
     * handle slashes before and after
     * @param path
     * @return
     */
    public static String getPath(String path){
        if(path.startsWith("/") == false){
            path = "/" + path;
        }
        if(path.endsWith("/")){
            path = path.substring(0, path.length() - 1);
        }
        return path.toLowerCase();
    }

    /**
     * Remove parameters from uri and handle slashes before and after
     * @param uri
     * @return
     */
    public static String getUri(String uri){

        if(uri.lastIndexOf("?") > -1){
            return getPath(uri.substring(0, uri.lastIndexOf("?")));
        }

        return getPath(uri);
    }
}
