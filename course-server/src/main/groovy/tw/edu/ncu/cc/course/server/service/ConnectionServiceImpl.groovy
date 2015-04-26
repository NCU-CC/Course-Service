package tw.edu.ncu.cc.course.server.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.course.server.helper.http.HttpClientSpring

@Service
public class ConnectionServiceImpl implements ConnectionService {

    @Value( '${custom.connection.remote.prefix}' )
    def String remotePrefix

    public HttpClientSpring connect( String url ) {
        HttpClientSpring.connect( remotePrefix + url )
    }

}
