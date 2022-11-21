package com.magician.route.mvc.init;

import com.magician.route.mvc.MagicianInterceptorCreate;

/**
 * Initializing Interceptors
 */
public interface MagicianInitInterceptor {

    /**
     * This method is triggered when the first request comes in, and is implemented by the developer himself to create and add interceptors
     * @param interceptorCreate
     */
    void initInterceptor(MagicianInterceptorCreate interceptorCreate);
}
