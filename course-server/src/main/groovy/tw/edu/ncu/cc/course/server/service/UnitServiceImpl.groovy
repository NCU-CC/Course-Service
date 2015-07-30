package tw.edu.ncu.cc.course.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.course.data.v1.Unit

@Service
class UnitServiceImpl implements UnitService {

    @Autowired
    def ConnectionService connectionService

    @Override
    Unit[] findAllColleges( String language ) {
        connectionService
                .connect( "/colleges" )
                .header( "Accept-Language", language )
                .get( Unit[] )
    }

    @Override
    Unit[] findAllDepartmentsByCollegeId( String language, String collegeId ) {
        connectionService
                .connect( "/colleges/{collegeId}/departments" )
                .variables( collegeId )
                .header( "Accept-Language", language )
                .get( Unit[] )
    }

    @Override
    Unit[] findAllTargetsByDepartmentId( String language, String departmentId ) {
        connectionService
                .connect( "/departments/{departmentId}/targets" )
                .variables( departmentId )
                .header( "Accept-Language", language )
                .get( Unit[] )
    }

}
