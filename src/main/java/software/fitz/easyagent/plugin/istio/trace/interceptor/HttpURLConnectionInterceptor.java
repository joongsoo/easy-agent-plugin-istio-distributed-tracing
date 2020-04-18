package software.fitz.easyagent.plugin.istio.trace.interceptor;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import software.fitz.easyagent.api.interceptor.AroundInterceptor;
import software.fitz.easyagent.api.util.ReflectionUtils;
import software.fitz.easyagent.plugin.istio.trace.IstioTraceHeader;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Enumeration;

public class HttpURLConnectionInterceptor implements AroundInterceptor {

    @Override
    public Object[] before(Object targetObject, Method targetMethod, Object[] args) {

        if (targetObject instanceof HttpURLConnection) {
            HttpURLConnection target = (HttpURLConnection) targetObject;

            boolean connected = ReflectionUtils.getFieldBooleanValue(target,
                    ReflectionUtils.getField(target, "connected"));
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (!connected && requestAttributes != null) {

                Arrays.stream(IstioTraceHeader.values()).forEach(header -> {
                    Enumeration<String> values = requestAttributes.getRequest().getHeaders(header.getHeaderName());

                    while (values.hasMoreElements()) {
                        String value = values.nextElement();
                        target.setRequestProperty(header.getHeaderName(), value);
                    }
                });
            }
        }

        return args;
    }
}
