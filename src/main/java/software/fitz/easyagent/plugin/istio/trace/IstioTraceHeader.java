package software.fitz.easyagent.plugin.istio.trace;

public enum IstioTraceHeader {

    X_REQUEST_ID("x-request-id"),
    X_B3_TRACEID("x-b3-traceid"),
    X_B3_PARENTID("x-b3-parentspanid"),
    X_B3_SPANID("x-b3-spanid"),
    X_B3_SAMPLED("x-b3-sampled"),
    X_B3_FLAGS("x-b3-flags"),
    X_OT_SPAN_CONTEXT("x-ot-span-context"),
    X_CLOUD_TRACE_CONTEXT("x-cloud-trace-context"),
    TRACEPARENT("traceparent"),
    GRPC_TRACE_BIN("grpc-trace-bin");

    private final String headerName;

    IstioTraceHeader(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }
}
