package tw.edu.ncu.cc.course.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.course.data.v1.Course

@Service
class SearchServiceImpl implements SearchService {

    @Autowired
    def ConnectionService connectionService

    @Override
    Course[] search( String language, String departmentId, Integer week, String period, String keyword, Integer limit ) {
        connectionService
                .connect( "/search" )
                .parameter( "deptId", departmentId )
                .parameter( "keyword", keyword )
                .parameter( "period", period )
                .parameter( "week", week as String )
                .parameter( "limit", limit as String )
                .header( "Accept-Language", language )
                .get( Course[] )
    }

    @Override
    Course[] findByDepartmentId( String language, String departmentId ) {
        connectionService
                .connect( "/search/department/{departmentId}" )
                .variables( departmentId )
                .header( "Accept-Language", language )
                .get( Course[] )
    }

    @Override
    Course[] findByTargetId( String language, String targetId ) {
        connectionService
                .connect( "/search/target/{targetId}" )
                .variables( targetId )
                .header( "Accept-Language", language )
                .get( Course[] )
    }

    @Override
    Course[] findBySummerStage( String language, String stage ) {
        connectionService
                .connect( "/search/summer/{stage}" )
                .variables( stage )
                .header( "Accept-Language", language )
                .get( Course[] )
    }

}
