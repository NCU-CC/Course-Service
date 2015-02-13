package tw.edu.ncu.cc.course.server.service;

import tw.edu.ncu.cc.course.server.helper.http.HttpClientSpring;

public class ConnectionServiceImpl implements ConnectionService {

    private String remotePrefix = "";

    public void setRemotePrefix( String remotePrefix ) {
        this.remotePrefix = remotePrefix;
    }

    public String getRemotePrefix() {
        return remotePrefix;
    }

    public HttpClientSpring connect( String url ) {
        return HttpClientSpring.connect( remotePrefix + url );
    }

}
