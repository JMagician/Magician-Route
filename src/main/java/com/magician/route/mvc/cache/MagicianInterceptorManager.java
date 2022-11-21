package com.magician.route.mvc.cache;

import com.magician.route.commons.util.MatchUtil;
import com.magician.route.commons.util.PathUtil;
import com.magician.route.mvc.enums.ReqMethod;
import com.magician.route.mvc.route.MagicianInterceptor;
import com.magician.route.mvc.route.MagicianRoute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interceptor Management
 *
 * For interceptor storage, adding, querying, collation
 */
public class MagicianInterceptorManager {

    /**
     * Interception to the collection
     */
    private static Map<String, MagicianInterceptor> magicianInterceptorMap = new ConcurrentHashMap<>();

    /**
     * Pair mapping of interceptors and routes
     */
    private static Map<String, List<MagicianInterceptor>> magicianInterceptor = new ConcurrentHashMap<>();

    public static void addInterceptor(String path, MagicianInterceptor magicianInterceptor) {
        magicianInterceptorMap.put(PathUtil.getPath(path), magicianInterceptor);
    }

    public static List<MagicianInterceptor> getInterceptorList(String routePath) {
        return magicianInterceptor.get(routePath);
    }

    public static Map<String, MagicianInterceptor> getMagicianInterceptorMap() {
        return magicianInterceptorMap;
    }

    /**
     * Collate interceptors, match interceptors to routes, and save mapping relationships
     * <p>
     * After collation, for each request, you only need to get the interceptor that needs to be executed from the mapping relationship, and you no longer need to do lookup matches
     */
    public static void collation() {
        Map<String, Map<ReqMethod, MagicianRoute>> magicianRouteMap = MagicianRouteManager.getMagicianRouteMap();

        if (magicianRouteMap == null || magicianRouteMap.size() == 0) {
            return;
        }

        for (String routePath : magicianRouteMap.keySet()) {
            for (String interPattern : magicianInterceptorMap.keySet()) {
                if (MatchUtil.isMatch(interPattern, routePath) == false) {
                    continue;
                }

                List<MagicianInterceptor> magicianInterceptors = magicianInterceptor.get(routePath);
                if (magicianInterceptors == null) {
                    magicianInterceptors = new ArrayList<>();
                }

                magicianInterceptors.add(magicianInterceptorMap.get(interPattern));

                magicianInterceptor.put(routePath, magicianInterceptors);
            }
        }
    }
}
