package com.magician.route.mvc;

import com.magician.route.mvc.cache.MagicianRouteManager;
import com.magician.route.mvc.enums.ReqMethod;
import com.magician.route.mvc.route.MagicianRoute;

/**
 * Route creator
 *
 * When the MagicianInitRoute -> initRoute() method is triggered, the object of this class will be passed in and the developer can call the methods inside it to add the route
 */
public class MagicianRouteCreate {
    public void post(String path, MagicianRoute magicianRoute){
        addRoute(path, ReqMethod.POST, magicianRoute);
    }

    public void get(String path, MagicianRoute magicianRoute){
        addRoute(path, ReqMethod.GET, magicianRoute);
    }

    public void put(String path, MagicianRoute magicianRoute){
        addRoute(path, ReqMethod.PUT, magicianRoute);
    }

    public void head(String path, MagicianRoute magicianRoute){
        addRoute(path, ReqMethod.HEAD, magicianRoute);
    }

    public void patch(String path, MagicianRoute magicianRoute){
        addRoute(path, ReqMethod.PATCH, magicianRoute);
    }

    public void delete(String path, MagicianRoute magicianRoute){
        addRoute(path, ReqMethod.DELETE, magicianRoute);
    }

    public void trace(String path, MagicianRoute magicianRoute){
        addRoute(path, ReqMethod.TRACE, magicianRoute);
    }

    public void connect(String path, MagicianRoute magicianRoute){
        addRoute(path, ReqMethod.CONNECT, magicianRoute);
    }

    public void addAny(String path, MagicianRoute magicianRoute){
        post(path, magicianRoute);
        get(path, magicianRoute);
        put(path, magicianRoute);
        head(path, magicianRoute);
        patch(path, magicianRoute);
        delete(path, magicianRoute);
        trace(path, magicianRoute);
        connect(path, magicianRoute);
    }

    public void addRoute(String path, ReqMethod reqMethod, MagicianRoute magicianRoute){
        MagicianRouteManager.addRoute(path, reqMethod, magicianRoute);
    }
}
