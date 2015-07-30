package tw.edu.ncu.cc.course.server.helper.http;

public class HttpException extends RuntimeException {

    private int code;

    public HttpException( int code, String message ) {
        super( message );
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
