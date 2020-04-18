package software.fitz.easyagent.plugin.istio.trace.interceptor;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import software.fitz.easyagent.api.interceptor.AroundInterceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class HystrixInterceptor implements AroundInterceptor {

    @Override
    public Object[] before(Object targetObject, Method targetMethod, Object[] args) {

        for (int i=0; i<args.length; i++) {

            if (args[i] instanceof Callable) {
                Callable c = (Callable) args[i];
                RequestAttributes currentAttributes = RequestContextHolder.getRequestAttributes();

                args[i] = (Callable) () -> {
                    RequestAttributes hystrixThreadAttributes = RequestContextHolder.getRequestAttributes();

                    try {
                        RequestContextHolder.setRequestAttributes(currentAttributes);
                        return c.call();
                    } finally {
                        RequestContextHolder.setRequestAttributes(hystrixThreadAttributes);
                    }
                };
            }
        }

        return args;
    }
}
