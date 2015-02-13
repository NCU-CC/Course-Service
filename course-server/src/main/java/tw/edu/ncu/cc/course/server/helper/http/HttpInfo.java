package tw.edu.ncu.cc.course.server.helper.http;

import java.util.HashMap;
import java.util.Map;

public class HttpInfo {

    private String url;
    private String method;
    private String content;
    private String[] variables;
    private Map< String, String > parameters;
    private Map< String, String > headers;

    public HttpInfo() {
        headers = new HashMap<>();
        parameters = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent( String content ) {
        this.content = content;
    }

    public String[] getVariables() {
        return variables;
    }

    public void setVariables( String...variables ) {
        this.variables = variables;
    }

    public Map< String, String > getParameters() {
        return parameters;
    }

    public void setParameter( String key, String value ) {
        parameters.put( key, value );
    }

    public String getMethod() {
        return method;
    }

    public void setMethod( String method ) {
        this.method = method;
    }

    public void setHeader( String key, String value ) {
        headers.put( key, value );
    }

    public Map< String, String > getHeaders() {
        return headers;
    }

}
