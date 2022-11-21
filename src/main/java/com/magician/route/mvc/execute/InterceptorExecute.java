package com.magician.route.mvc.execute;

import com.magician.route.mvc.cache.MagicianInterceptorManager;
import com.magician.route.mvc.route.MagicianInterceptor;
import io.magician.application.request.MagicianRequest;

import java.util.List;

/**
 * execute interceptor
 */
public class InterceptorExecute {

    /**
     * Get the interceptor corresponding to this route
     * @param route
     * @return
     */
    public static List<MagicianInterceptor> getInterceptorList(String route){
        return MagicianInterceptorManager.getInterceptorList(route);
    }

    /**
     * Execute the before method of the interceptor
     * @param interceptorModelList
     * @param request
     * @return
     * @throws Exception
     */
    public static Object before(List<MagicianInterceptor> interceptorModelList, MagicianRequest request) throws Exception {
        if(interceptorModelList == null || interceptorModelList.size() < 1){
            return MagicianInterceptor.SUCCESS;
        }

        for(MagicianInterceptor magicianInterceptor : interceptorModelList){
            Object result = magicianInterceptor.before(request);
            if(!MagicianInterceptor.SUCCESS.equals(String.valueOf(result))){
                return result;
            }
        }

        return MagicianInterceptor.SUCCESS;
    }

    /**
     * Execute the after method of the interceptor
     * @param interceptorModelList
     * @param request
     * @param apiResult
     * @return
     * @throws Exception
     */
    public static Object after(List<MagicianInterceptor> interceptorModelList, MagicianRequest request, Object apiResult) throws Exception {
        if(interceptorModelList == null || interceptorModelList.size() < 1){
            return MagicianInterceptor.SUCCESS;
        }

        for(MagicianInterceptor magicianInterceptor : interceptorModelList){
            Object result = magicianInterceptor.after(request, apiResult);
            if(!MagicianInterceptor.SUCCESS.equals(String.valueOf(result))){
                return result;
            }
        }

        return MagicianInterceptor.SUCCESS;
    }
}
