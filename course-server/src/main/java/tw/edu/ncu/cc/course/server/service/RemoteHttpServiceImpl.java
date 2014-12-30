package tw.edu.ncu.cc.course.server.service;

import org.springframework.web.client.RestClientException;
import tw.edu.ncu.cc.course.server.helper.ConnectionHelper;

public class RemoteHttpServiceImpl implements RemoteHttpService {

    private String remotePrefix = "";
    private ConnectionHelper connectionHelper;

    public RemoteHttpServiceImpl() {
        connectionHelper = new ConnectionHelper();
    }

    public void setRemotePrefix( String remotePrefix ) {
        this.remotePrefix = remotePrefix;
    }

    @Override
    public < T > T getObject( Class< T > responseType, String url, Object... urlVariables ) throws RestClientException {
        return connectionHelper.getForObject(  remotePrefix + url, responseType, urlVariables );
    }

}
