package tw.edu.ncu.cc.course.server.interceptor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
public class ApplicationInterceptor extends HandlerInterceptorAdapter {

    private Set< String > blackList = new HashSet<>()

    private Logger logger = LoggerFactory.getLogger( this.getClass() )

    public ApplicationInterceptor() {
        blackList.add( "accept-encoding" )
        blackList.add( "accept-language" )
        blackList.add( "cache-control" )
        blackList.add( "content-type" )
        blackList.add( "cookie" )
        blackList.add( "accept" )
        blackList.add( "connection" )
        blackList.add( "host" )
    }

    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
        logger.info(
                String.format( "REQUEST[%s], HEADER[%s], FROM[%s]",
                    getRequestPath( request ),
                    getHeaders( request ),
                    request.getRemoteAddr()
                )
        )
        return true
    }

    private static String getRequestPath( HttpServletRequest request ) {
        return request.getRequestURI().substring( request.getContextPath().length() )
    }

    private String getHeaders( HttpServletRequest request ) {
        Enumeration< String > headers = request.getHeaderNames()
        if( headers == null ) {
            String authorization = request.getHeader( "Authorization" )
            return authorization == null ? "" : String.format( "{Authorization:%s}", authorization )
        } else {
            String headerString = ""
            while( headers.hasMoreElements() ) {
                String header = headers.nextElement().toLowerCase()
                if( ! blackList.contains( header ) ) {
                    headerString += String.format( "{%s:%s}", header, request.getHeader( header ) )
                }
            }
            return headerString
        }
    }
}
