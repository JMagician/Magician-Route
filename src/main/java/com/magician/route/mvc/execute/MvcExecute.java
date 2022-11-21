package com.magician.route.mvc.execute;

import com.magician.route.commons.util.JSONUtil;
import com.magician.route.commons.util.MsgUtil;
import com.magician.route.commons.util.PathUtil;
import com.magician.route.mvc.cache.MagicianRouteManager;
import com.magician.route.mvc.enums.ReqMethod;
import com.magician.route.mvc.execute.model.ResponseInputStream;
import com.magician.route.mvc.route.MagicianInterceptor;
import com.magician.route.mvc.route.MagicianRoute;
import io.magician.application.request.MagicianRequest;

import java.util.List;

/**
 * Routing actuators
 */
public class MvcExecute {

    /**
     * Execution routes and other logic
     * @param request
     * @throws Exception
     */
    public static void execute(MagicianRequest request) throws Exception {
        try {
            String uri = PathUtil.getUri(request.getUrl());

            /* Get the corresponding route according to uri */
            MagicianRoute magicianRoute = MagicianRouteManager.getRoute(uri, ReqMethod.valueOf(request.getMethod().name()));
            if (magicianRoute == null) {
                request.getResponse().sendErrorMsg(404, "Could not find this route [" + uri + "] or request method mismatch");
                return;
            }

            /* Get the interceptor corresponding to this route */
            List<MagicianInterceptor> magicianInterceptorList = InterceptorExecute.getInterceptorList(uri);

            /* Execute the before method of the interceptor */
            Object interResult = InterceptorExecute.before(magicianInterceptorList, request);
            if (!MagicianInterceptor.SUCCESS.equals(String.valueOf(interResult))) {
                request.getResponse().sendJson(String.valueOf(interResult));
                return;
            }

            /* Execute Route */
            Object result = magicianRoute.request(request);

            /* Execute the after method of the interceptor */
            interResult = InterceptorExecute.after(magicianInterceptorList, request, result);
            if (!MagicianInterceptor.SUCCESS.equals(String.valueOf(interResult))) {
                request.getResponse().sendJson(String.valueOf(interResult));
                return;
            }

            // If it returns null, it means that the controller has already responded, so no response is required
            if (result == null) {
                return;
            }

            /* If it returns ResponseInputStream, it responds directly to the stream */
            if (result instanceof ResponseInputStream) {
                ResponseInputStream inputStream = (ResponseInputStream) result;
                request.getResponse().sendStream(inputStream.getName(), inputStream.getBytes());
                return;
            }

            /* If the type of the return value is not ResponseInputStream, respond directly */
            request.getResponse().sendJson(JSONUtil.toJSONString(result));
        } catch (Exception e) {
            request.getResponse().sendJson(MsgUtil.getMsg(500, e.getMessage()));
            throw e;
        }
    }

}
