package tw.edu.ncu.cc.course.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.course.data.v1.Course

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    private ConnectionService connectionService;

    @Autowired
    public void setConnectionService( ConnectionService connectionService ) {
        this.connectionService = connectionService;
    }

    @Override
    public Course[] readSelectedCourses( String studentID, String language ) {
        return connectionService
                .connect( "/student/{studentID}/selected" )
                .variables( studentID )
                .header( "Accept-Language", language )
                .get( Course[] );
    }

    @Override
    public Course[] readTrackedCourses( String studentID, String language ) {
        return connectionService
                .connect( "/student/{studentID}/tracking" )
                .variables( studentID )
                .header( "Accept-Language", language )
                .get( Course[] );
    }

}
