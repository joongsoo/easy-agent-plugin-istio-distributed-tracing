package software.fitz.easyagent.plugin.istio.trace;

import software.fitz.easyagent.api.Plugin;
import software.fitz.easyagent.api.TransformDefinition;
import software.fitz.easyagent.api.TransformerRegistry;
import software.fitz.easyagent.plugin.istio.trace.interceptor.ApacheHttpClientInterceptor;
import software.fitz.easyagent.plugin.istio.trace.interceptor.HttpURLConnectionInterceptor;
import software.fitz.easyagent.plugin.istio.trace.interceptor.HystrixInterceptor;

import static software.fitz.easyagent.api.MethodDefinition.all;
import static software.fitz.easyagent.api.MethodDefinition.matchArgs;
import static software.fitz.easyagent.api.strategy.TransformStrategy.className;

public class IstioDistributedTracePlugin implements Plugin {

    @Override
    public void setup(TransformerRegistry transformerRegistry) {

        transformerRegistry.register(
                TransformDefinition.builder()
                        .transformStrategy(className("java.net.HttpURLConnection+"))
                        .addTargetMethod(all("connect"))
                        .addInterceptor(new HttpURLConnectionInterceptor())
                        .build()
        );

        transformerRegistry.register(
                TransformDefinition.builder()
                        .transformStrategy(className("org.apache.http.client.HttpClient+"))
                        .addTargetMethod(matchArgs("execute"))
                        .addInterceptor(new ApacheHttpClientInterceptor())
                        .build()
        );

        transformerRegistry.register(
                TransformDefinition.builder()
                        .transformStrategy(className("com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy+"))
                        .addTargetMethod(all("wrapCallable"))
                        .addInterceptor(new HystrixInterceptor())
                        .build()
        );
    }
}
