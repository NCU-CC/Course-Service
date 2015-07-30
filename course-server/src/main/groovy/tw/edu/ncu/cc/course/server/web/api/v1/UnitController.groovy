package tw.edu.ncu.cc.course.server.web.api.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.course.data.v1.Unit
import tw.edu.ncu.cc.course.server.service.UnitService

@RestController
@RequestMapping( value = "v1/colleges", method = RequestMethod.GET )
class UnitController {

    @Autowired
    def UnitService unitService

    @RequestMapping
    public Unit[] findAllColleges( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language ) {

        unitService.findAllColleges( language )
    }

    @RequestMapping( value = "{collegeId}/departments" )
    public Unit[] findAllDepartmentsByCollegeId( @RequestHeader( value = "Accept-Language", defaultValue = "zh_TW" ) String language,
                                                 @PathVariable( "collegeId" ) String collegeId ) {

        unitService.findAllDepartmentsByCollegeId( language, collegeId )
    }

}
