package tw.edu.ncu.cc.course.server.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionHelper {

    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    public < T > T getForObject( String url, Class< T > responseType, Object... urlVariables ) {
        try {
            HttpURLConnection connectionURL = doConnection( prepareURL( url, urlVariables ), null, GET );
            connectionURL.connect();
            if ( connectionURL.getResponseCode() == 200 ) {
                return convert( getStringFromConnection( connectionURL ), responseType );
            } else {
                throw new HttpServerErrorException( HttpStatus.valueOf( connectionURL.getResponseCode() ) );
            }
        } catch ( IOException e ) {
            throw new HttpServerErrorException( HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() );
        }
    }

    private HttpURLConnection doConnection( URL url, String content, String method ) throws IOException {

        HttpURLConnection httpConn = ( HttpURLConnection ) url.openConnection();

        httpConn.setRequestMethod( method );
        httpConn.setDoOutput( true );
        httpConn.setDoInput( true );
        httpConn.setRequestProperty( "Content-Type", "application/json;charset=UTF-8 " );

        if ( content != null && content.length() > 0 ) {
            try ( OutputStream outputStream = httpConn.getOutputStream() ) {
                DataOutputStream dataStream = new DataOutputStream( outputStream );
                BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( dataStream, "UTF-8" ) );
                writer.write( content );
                writer.flush();
            }
        }
        return httpConn;
    }

    private String getStringFromConnection( HttpURLConnection connection ) throws IOException {
        assert connection != null;
        try ( InputStream inputStream = connection.getInputStream() ) {
            return getStringFromConnection( inputStream );
        }
    }

    private < T > T convert( String message, Class<T> type ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue( message, type );
        } catch ( IOException e ) {
            throw new HttpServerErrorException( HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() );
        }
    }

    private URL prepareURL( String url, Object... urlVariables ) {
        try {
            return new UriTemplate( url ).expand( urlVariables ).toURL();
        } catch ( MalformedURLException e ) {
            throw new HttpServerErrorException( HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() );
        }
    }

    private String getStringFromErrorConnection( HttpURLConnection connection ) throws IOException {
        assert connection != null;
        try ( InputStream inputStream = connection.getErrorStream() ) {
            return getStringFromConnection( inputStream );
        }
    }

    private String getStringFromConnection( InputStream inputStream ) throws IOException {
        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream, "UTF-8" ) ) ) {
            StringBuilder body = new StringBuilder( 255 );
            String inputLine;
            while ( ( inputLine = reader.readLine() ) != null )
                body.append( inputLine ).append( "\n" );
            return body.toString();
        }
    }

}