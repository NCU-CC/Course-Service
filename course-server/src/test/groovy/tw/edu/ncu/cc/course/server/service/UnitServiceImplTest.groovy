package tw.edu.ncu.cc.course.server.service

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import resource.ServerResource
import specification.SpringSpecification
import spock.lang.Shared


class UnitServiceImplTest extends SpringSpecification {

    @Autowired
    UnitService unitService

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/unit/college" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody(
                        '''
                        [
                          {
                            "name" : "��ǰ|",
                            "id" : "deptI1I1000I0"
                          }
                        ]
                        '''
                )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/unit/college/deptI1I1000I0/department" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody(
                        '''
                        [
                          {
                            "name" :  "�����Ǩt",
                            "id" : "deptI1I1001I0"
                          }
                        ]
                        '''
                )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/unit/department/deptI1I1001I0/target" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody(
                        '''
                        [
                            {
                              "name" : "�����Ǩt[������]",
                              "id" : "cofuZdeptI1I1001I0ZcofgI0"
                            },
                            {
                              "name" : "�����Ǩt[�@�~��]",
                              "id" : "cofuZdeptI1I1001I0ZcofgI1"
                            }
                        ]
                        '''
                )
        )
    }

    def "it can find all colleges"() {
        when:
            def colleges = unitService.findAllColleges( "zh_TW" )
        then:
            colleges.size() == 1
            colleges[0].id == "deptI1I1000I0"
    }

    def "it can find all departments in specified college"() {
        when:
            def departments = unitService.findAllDepartmentsByCollegeId( "zh_TW", "deptI1I1000I0" )
        then:
            departments.size() == 1
            departments[0].id == "deptI1I1001I0"
    }

    def "it can find all targets for specified department"() {
        when:
            def targets = unitService.findAllTargetsByDepartmentId( "zh_TW", "deptI1I1001I0" )
        then:
            targets.size() == 2
    }

}
