package tw.edu.ncu.cc.course.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.course.data.v1.Course

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Autowired
    def ConnectionService connectionService;

    @Override
    public Course[] readSelectedCourses( String studentId, String language ) {
        connectionService
                .connect( "/student/{studentID}/selected" )
                .variables( studentId )
                .header( "Accept-Language", language )
                .get( Course[] )
    }

    @Override
    public Course[] readTrackedCourses( String studentId, String language ) {
        connectionService
                .connect( "/student/{studentID}/tracking" )
                .variables( studentId )
                .header( "Accept-Language", language )
                .get( Course[] )
    }

    @Override
    Course[] readRejectedCourses( String studentId, String language ) {
        connectionService
                .connect( "/student/{studentID}/rejected" )
                .variables( studentId )
                .header( "Accept-Language", language )
                .get( Course[] )
    }

}
