package tw.edu.ncu.cc.course.server.web.api.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import tw.edu.ncu.cc.course.data.v1.Course
import tw.edu.ncu.cc.course.server.service.StudentCourseService

@RestController
@RequestMapping( value = "v1/student" )
public class StudentCourseController {

    @Autowired
    def StudentCourseService studentCourseService

    @PreAuthorize( "hasAuthority('CLASS_READ')" )
    @RequestMapping( value = "selected", method = RequestMethod.GET )
    public Course[] searchSelectedCourse( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                          Authentication authentication ) {

        studentCourseService.readSelectedCourses( authentication.name, language )
    }

    @PreAuthorize( "hasAuthority('CLASS_READ')" )
    @RequestMapping( value = "tracking", method = RequestMethod.GET )
    public Course[] searchTrackedCourse( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                         Authentication authentication ) {

        studentCourseService.readTrackedCourses( authentication.name, language )
    }

}
