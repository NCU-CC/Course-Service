package tw.edu.ncu.cc.course.server.service;

import org.springframework.web.client.RestClientException;
import tw.edu.ncu.cc.course.server.helper.ConnectionHelper;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class RemoteHttpServiceImpl implements RemoteHttpService {

    private String remotePrefix = "";
//    private RestTemplate restTemplate;
    private ConnectionHelper connectionHelper;

    public RemoteHttpServiceImpl() {
        turnOffSSLChecking();
//        restTemplate = new RestTemplate();
//        restTemplate.setMessageConverters( RestTemplateHelper.createConverters() );
        connectionHelper = new ConnectionHelper();
    }

    public void setRemotePrefix( String remotePrefix ) {
        this.remotePrefix = remotePrefix;
    }

    @Override
    public < T > T getObject( Class< T > responseType, String url, Object... urlVariables ) throws RestClientException {
//        return restTemplate.getForObject( remotePrefix + url, responseType, urlVariables );
        return connectionHelper.getForObject(  remotePrefix + url, responseType, urlVariables );
    }

    private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted( X509Certificate[] certs, String authType ) {
                }
                public void checkServerTrusted( X509Certificate[] certs, String authType ) {
                }
            }
    };

    public static void turnOffSSLChecking() {
        final SSLContext sc;
        try {
            sc = SSLContext.getInstance( "SSL" );
            sc.init( null, UNQUESTIONING_TRUST_MANAGER, null );
            HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );
        } catch ( NoSuchAlgorithmException | KeyManagementException e ) {
            throw new RuntimeException( e );
        }
    }

}
