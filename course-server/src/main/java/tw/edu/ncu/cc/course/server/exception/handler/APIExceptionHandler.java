package tw.edu.ncu.cc.course.server.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import tw.edu.ncu.cc.course.data.v1.message.Error;
import tw.edu.ncu.cc.course.data.v1.message.ErrorCode;

import javax.servlet.http.HttpServletRequest;

@RestController
public class APIExceptionHandler {

    private Logger logger = LoggerFactory.getLogger( this.getClass() );

    @ExceptionHandler( { AccessDeniedException.class } )
    public ResponseEntity accessDenied( HttpServletRequest request ) {
        logger.warn( "ACCESS DENIED FROM {} FOR {} ", request.getRemoteAddr(), request.getRequestURI() );
        return new ResponseEntity<>(
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler( { HttpStatusCodeException.class, HttpServerErrorException.class } )
    public ResponseEntity remoteResponseError( HttpStatusCodeException e ) {
        switch ( e.getStatusCode() ) {
            case NOT_FOUND:
                return new ResponseEntity<>(
                        new Error(
                            ErrorCode.NOT_EXIST, "required resource not exist"
                        ), HttpStatus.NOT_FOUND
                );
            default:
                return new ResponseEntity<>(
                        new Error(
                                ErrorCode.RAW, e.getMessage() + " ->> " + e.getResponseBodyAsString()
                        ), e.getStatusCode()
                );
        }
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity exceptionHandler( Exception e ) {
        logger.error( "SEVERE INTERNAL ERROR", e );
        return new ResponseEntity<>(
                new Error(
                        ErrorCode.SERVER_ERROR, e.getMessage()
                ), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
