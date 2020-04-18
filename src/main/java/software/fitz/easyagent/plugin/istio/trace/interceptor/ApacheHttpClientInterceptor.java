package software.fitz.easyagent.plugin.istio.trace.interceptor;

import org.apache.http.HttpMessage;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import software.fitz.easyagent.api.interceptor.AroundInterceptor;
import software.fitz.easyagent.plugin.istio.trace.IstioTraceHeader;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;

public class ApacheHttpClientInterceptor implements AroundInterceptor {

    @Override
    public Object[] before(Object targetObject, Method targetMethod, Object[] args) {

        Arrays.stream(args)
                .filter(x -> x instanceof HttpMessage)
                .map(x -> (HttpMessage) x)
                .forEach(httpMessage -> {
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                    if (attributes != null) {

                        // Find istio header.
                        Arrays.stream(IstioTraceHeader.values()).forEach(header -> {
                            Enumeration<String> values = attributes.getRequest().getHeaders(header.getHeaderName());

                            // Change header value.
                            while (values.hasMoreElements()) {
                                String value = values.nextElement();
                                httpMessage.setHeader(header.getHeaderName(), value);
                            }
                        });
                    }
                });

        return args;
    }
}
