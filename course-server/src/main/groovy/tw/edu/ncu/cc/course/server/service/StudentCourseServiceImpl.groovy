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
                .connect( "/students/{studentID}/courses?filter=selected" )
                .variables( studentId )
                .header( "Accept-Language", language )
                .get( Course[] )
    }

    @Override
    public Course[] readTrackedCourses( String studentId, String language ) {
        connectionService
                .connect( "/students/{studentID}/courses?filter=tracking" )
                .variables( studentId )
                .header( "Accept-Language", language )
                .get( Course[] )
    }

    @Override
    Course[] readRejectedCourses( String studentId, String language ) {
        connectionService
                .connect( "/students/{studentID}/courses?filter=rejected" )
                .variables( studentId )
                .header( "Accept-Language", language )
                .get( Course[] )
    }

}
