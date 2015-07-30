package tw.edu.ncu.cc.course.server.service;

import tw.edu.ncu.cc.course.server.helper.http.HttpClientSpring;

public interface ConnectionService {
    public HttpClientSpring connect( String url );
}
