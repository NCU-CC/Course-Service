package tw.edu.ncu.cc.course.server.exception.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.HttpStatusCodeException
import tw.edu.ncu.cc.course.data.v1.message.Error
import tw.edu.ncu.cc.course.data.v1.message.ErrorCode

import javax.servlet.http.HttpServletRequest

@ControllerAdvice
public class ApplicationExceptionHandler {

    private Logger logger = LoggerFactory.getLogger( this.getClass() );

    @ExceptionHandler( AccessDeniedException.class )
    public ResponseEntity< Error > accessDenied( HttpServletRequest request ) {
        logger.warn( "ACCESS DENIED FOR {} FROM {}", request.getRequestURI(), request.getRemoteAddr() );
        return new ResponseEntity<>(
                new Error(
                        ErrorCode.ACCESS_DENIED, "access is denied"
                ), HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler( [ HttpStatusCodeException.class, HttpServerErrorException.class ] )
    public ResponseEntity< Error > remoteResponseError( HttpStatusCodeException e ) {
        switch ( e.getStatusCode() ) {
            case HttpStatus.NOT_FOUND:
                return new ResponseEntity<>(
                        new Error(
                            ErrorCode.NOT_EXIST, "required resource not exist"
                        ), HttpStatus.NOT_FOUND
                );
            default:
                logger.error( "REQUEST FAILED FROM REMOTE SERVICE" );
                return new ResponseEntity<>(
                        new Error(
                                ErrorCode.RAW, e.getMessage() + " ->> " + e.getResponseBodyAsString()
                        ), e.getStatusCode()
                );
        }
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity< Error > exceptionHandler( Exception e ) {
        logger.error( "UNEXPECTED ERROR", e );
        return new ResponseEntity<>(
                new Error(
                        ErrorCode.SERVER_ERROR, e.getMessage()
                ), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}