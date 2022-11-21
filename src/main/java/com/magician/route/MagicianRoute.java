package com.magician.route;

import com.magician.route.commons.annotation.Interceptor;
import com.magician.route.commons.annotation.Route;
import com.magician.route.mvc.MagicianInterceptorCreate;
import com.magician.route.mvc.MagicianRouteCreate;
import com.magician.route.mvc.init.MagicianInitInterceptor;
import com.magician.route.mvc.init.MagicianInitRoute;
import com.magician.route.mvc.cache.MagicianInterceptorManager;
import com.magician.route.mvc.execute.MvcExecute;
import io.magician.application.request.MagicianRequest;
import io.magician.common.cache.MagicianCacheManager;

import java.util.Set;

/**
 * MagicianRoute's entrance
 */
public class MagicianRoute {

    private static boolean first = true;

    /**
     * Forward all the requests from Magician to this method to enjoy all the features of Magician-Route
     *
     * @param request
     * @throws Exception
     */
    public static void request(MagicianRequest request) throws Exception {
        load();

        MvcExecute.execute(request);
    }

    /**
     * Load the required resources, triggered only when the first request comes in
     *
     * @throws Exception
     */
    private static void load() throws Exception {
        if (first == false) {
            return;
        }

        MagicianRouteCreate magicianRouteCreate = new MagicianRouteCreate();
        MagicianInterceptorCreate magicianInterceptorCreate = new MagicianInterceptorCreate();

        /* Trigger Add route and add interceptor methods */
        Set<String> scanClassList = MagicianCacheManager.getScanClassList();
        for (String className : scanClassList) {
            Class<?> cls = Class.forName(className);

            Route clsRoute = cls.getAnnotation(Route.class);
            Interceptor interceptor = cls.getAnnotation(Interceptor.class);
            if (clsRoute == null && interceptor == null) {
                continue;
            }
            if (clsRoute != null && interceptor != null) {
                throw new Exception("It is not possible to add both Route and Interceptor annotations, className:[" + className + "]");
            }

            if (clsRoute != null) {
                MagicianInitRoute magicianInitRoute = (MagicianInitRoute) cls.getDeclaredConstructor().newInstance();
                magicianInitRoute.initRoute(magicianRouteCreate);
            }

            if (interceptor != null) {
                MagicianInitInterceptor magicianInitInterceptor = (MagicianInitInterceptor) cls.getDeclaredConstructor().newInstance();
                magicianInitInterceptor.initInterceptor(magicianInterceptorCreate);
            }
        }

        /* Collate interceptors, match interceptors to routes, and save mapping relationships */
        MagicianInterceptorManager.collation();

        first = false;
    }
}
