package com.magician.route.mvc.cache;


import com.magician.route.commons.util.PathUtil;
import com.magician.route.mvc.enums.ReqMethod;
import com.magician.route.mvc.route.MagicianRoute;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Route Management
 *
 * For route storage, adding, querying
 */
public class MagicianRouteManager {

    /**
     * Route to the collection
     */
    private static Map<String, Map<ReqMethod, MagicianRoute>> magicianRouteMap = new ConcurrentHashMap<>();

    public static void addRoute(String path, ReqMethod reqMethod, MagicianRoute magicianRoute) {
        path = PathUtil.getPath(path);

        Map<ReqMethod, MagicianRoute> routeMap = magicianRouteMap.get(path);
        if (routeMap == null) {
            routeMap = new HashMap<>();
        }

        routeMap.put(reqMethod, magicianRoute);
        magicianRouteMap.put(path, routeMap);
    }

    public static MagicianRoute getRoute(String path, ReqMethod reqMethod) {
        Map<ReqMethod, MagicianRoute> routeMap = magicianRouteMap.get(path);
        if (routeMap == null || routeMap.size() == 0) {
            return null;
        }
        return routeMap.get(reqMethod);
    }

    public static Map<String, Map<ReqMethod, MagicianRoute>> getMagicianRouteMap() {
        return magicianRouteMap;
    }
}
