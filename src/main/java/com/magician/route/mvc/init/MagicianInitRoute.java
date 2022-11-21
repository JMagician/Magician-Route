package com.magician.route.mvc.init;

import com.magician.route.mvc.MagicianRouteCreate;

/**
 * Initializing routes
 */
public interface MagicianInitRoute {

    /**
     * This method is triggered when the first request comes in and is implemented by the developer himself to create and add routes
     * @param routeCreate
     */
    void initRoute(MagicianRouteCreate routeCreate);
}
