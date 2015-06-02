package tw.edu.ncu.cc.course.server.web.api.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.course.data.v1.Course
import tw.edu.ncu.cc.course.data.v1.Limit
import tw.edu.ncu.cc.course.server.service.CourseService
import tw.edu.ncu.cc.course.server.service.LimitService

@RestController
@RequestMapping( value = "v1/course", method = RequestMethod.GET )
class CourseController {

    @Autowired
    def CourseService courseService

    @Autowired
    def LimitService limitService

    @RequestMapping( value = "{serialNo}" )
    public Course findBySerialNo( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                  @PathVariable( "serialNo" ) String serialNo ) {

        courseService.findBySerialNo( language, serialNo )
    }

    @RequestMapping( value = "{serialNo}/limit" )
    public Limit findLimitBySerialNo( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                      @PathVariable( "serialNo" ) String serialNo ) {

        limitService.findByCourseSerialNo( language, serialNo )
    }

}
