package tw.edu.ncu.cc.course.data.v1.message;

public class ErrorCode {

    public static final int RAW = -1;
    public static final int NOT_EXIST = RAW + 1;
    public static final int ACCESS_DENIED = NOT_EXIST + 1;
    public static final int SERVER_ERROR = ACCESS_DENIED + 1;

}
