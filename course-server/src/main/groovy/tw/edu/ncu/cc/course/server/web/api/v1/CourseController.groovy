package tw.edu.ncu.cc.course.server.web.api.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.course.data.v1.Course
import tw.edu.ncu.cc.course.data.v1.Limit
import tw.edu.ncu.cc.course.server.service.CourseService
import tw.edu.ncu.cc.course.server.service.LimitService
import tw.edu.ncu.cc.course.server.service.SearchService

@RestController
@RequestMapping( value = "v1/courses", method = RequestMethod.GET )
class CourseController {

    @Autowired
    def CourseService courseService

    @Autowired
    def LimitService limitService

    @Autowired
    def SearchService searchService

    @RequestMapping
    public Course[] find( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                          @RequestParam ( value = "deptId" , required = false ) String departmentId,
                          @RequestParam ( value = "week"   , required = false ) Integer week,
                          @RequestParam ( value = "period" , required = false ) String period,
                          @RequestParam ( value = "limit"  , required = false ) Integer limit,
                          @RequestParam ( value = "keyword", required = false ) String keyword ) {

        searchService.search( language, departmentId, week, period, keyword, limit )
    }

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
