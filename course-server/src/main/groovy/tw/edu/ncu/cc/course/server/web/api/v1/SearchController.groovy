package tw.edu.ncu.cc.course.server.web.api.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.course.data.v1.Course
import tw.edu.ncu.cc.course.data.v1.Unit
import tw.edu.ncu.cc.course.server.service.SearchService
import tw.edu.ncu.cc.course.server.service.UnitService

@RestController
@RequestMapping( value = "v1", method = RequestMethod.GET )
class SearchController {

    @Autowired
    def UnitService unitService

    @Autowired
    def SearchService searchService

    @RequestMapping( value = "departments/{departmentId}/courses" )
    public Course[] findByDepartmentId( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                        @PathVariable( "departmentId" ) String departmentId ) {

        searchService.findByDepartmentId( language, departmentId )
    }

    @RequestMapping( value = "departments/{departmentId}/targets" )
    public Unit[] findAllTargetsByDepartmentId( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                                @PathVariable( "departmentId" ) String departmentId ) {

        unitService.findAllTargetsByDepartmentId( language, departmentId )
    }

    @RequestMapping( value = "targets/{targetId}/courses" )
    public Course[] findByTargetId( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                    @PathVariable( "targetId" ) String targetId ) {

        searchService.findByTargetId( language, targetId )
    }

    @RequestMapping( value = "summer/{stage}/courses" )
    public Course[] findBySummerStage( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                       @PathVariable( "stage" ) String stage ) {

        searchService.findBySummerStage( language, stage )
    }

}
