package tw.edu.ncu.cc.course.server.controller.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tw.edu.ncu.cc.course.data.v1.Course;
import tw.edu.ncu.cc.course.server.controller.ApplicationController;
import tw.edu.ncu.cc.course.server.service.StudentCourseService;

@RestController
@RequestMapping( value = "api/v1/student" )
public class StudentCourseController extends ApplicationController {

    private StudentCourseService studentCourseService;

    @Autowired
    public void setStudentCourseService( StudentCourseService studentCourseService ) {
        this.studentCourseService = studentCourseService;
    }

    @PreAuthorize( "hasRole('CLASS_READ')" )
    @RequestMapping( value = "selected", method = RequestMethod.GET )
    public Course[] searchSelectedCourse( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                          Authentication authentication ) {

        return studentCourseService.readSelectedCourses( authentication.getName(), language );
    }

    @PreAuthorize( "hasRole('CLASS_READ')" )
    @RequestMapping( value = "tracking", method = RequestMethod.GET )
    public Course[] searchTrackedCourse( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                         Authentication authentication ) {

        return studentCourseService.readTrackedCourses( authentication.getName(), language );
    }

}
