package com.magician.route.mvc;

import com.magician.route.mvc.cache.MagicianInterceptorManager;
import com.magician.route.mvc.route.MagicianInterceptor;

/**
 * Interceptor creator
 */
public class MagicianInterceptorCreate {

    /**
     * When the MagicianInitInterceptor -> initInterceptor() method is triggered, the object of this class is passed in and the developer calls this method to add the interceptor
     * @param path
     * @param magicianInterceptor
     */
    public void addInterceptor(String path, MagicianInterceptor magicianInterceptor){
        MagicianInterceptorManager.addInterceptor(path, magicianInterceptor);
    }

}
