package tw.edu.ncu.cc.course.server.web.api.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.course.data.v1.Course
import tw.edu.ncu.cc.course.server.service.SearchService

@RestController
@RequestMapping( value = "v1/search", method = RequestMethod.GET )
class SearchController {

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

    @RequestMapping( value = "department/{departmentId}" )
    public Course[] findByDepartmentId( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                        @PathVariable( "departmentId" ) String departmentId ) {

        searchService.findByDepartmentId( language, departmentId )
    }

    @RequestMapping( value = "target/{targetId}" )
    public Course[] findByTargetId( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                    @PathVariable( "targetId" ) String targetId ) {

        searchService.findByTargetId( language, targetId )
    }

    @RequestMapping( value = "summer/{stage}" )
    public Course[] findBySummerStage( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                       @PathVariable( "stage" ) String stage ) {

        searchService.findBySummerStage( language, stage )
    }

}
