package com.magician.route.mvc.route;

import io.magician.application.request.MagicianRequest;

/**
 * Routing, Each interface corresponds to a
 */
public interface MagicianRoute {

    /**
     * This method is executed automatically when a request comes in and is implemented by the developer to handle the server-side logic
     * @param request
     * @return
     * @throws Exception
     */
    Object request(MagicianRequest request) throws Exception;
}
