package tw.edu.ncu.cc.course.server.service

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import resource.ServerResource
import specification.SpringSpecification
import spock.lang.Shared

class SearchServiceImplTest extends SpringSpecification {

    @Autowired
    SearchService searchService

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    @Shared
    String courses = '''
                    [
                      {
                        "serialNo" : 12034,
                        "no" : "EL5001",
                        "classNo" : "*",
                        "name" : "���\\/��Ʋz�׾�Ū",
                        "isCanceled" : false,
                        "memo": "���T�B�|�~��",
                        "isMasterDoctor": false,
                        "language": "��y",
                        "usePasswordCard": "���ϥ�",
                        "isFirstRun": true,
                        "isPreSelect": true,
                        "teachers": "���ҤH,���g�B",
                        "credit": 2,
                        "classRooms": "C2-209,C2-209",
                        "time": {
                                  "0": ["5"],
                                  "2": ["3", "4"]
                                },
                        "type": "����",
                        "fullHalf": "��",
                        "limit": 0
                      }
                    ]
                    '''

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/search" ) //TODO ADD PARAMS
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody( courses )
        )

        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/search/department/deptI1I1000I0" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody( courses )
        )

        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/search/target/cofuZdeptI1I1001I0ZcofgI0" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody( courses )
        )

        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/search/summer/105" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody( courses )
        )
    }

    def "it can search course by params"() {
        when:
            def courses = searchService.search( "zh_TW", "deptI1I1000I0", null, null, null, null )
        then:
            courses.size() == 1
            courses[0].no == "EL5001"
    }

    def "it can find courses of specified department"() {
        when:
            def courses = searchService.findByDepartmentId( "zh_TW", "deptI1I1000I0" )
        then:
            courses.size() == 1
            courses[0].no == "EL5001"
    }

    def "it can find courses for specified target"() {
        when:
            def courses = searchService.findByTargetId( "zh_TW", "cofuZdeptI1I1001I0ZcofgI0" )
        then:
            courses.size() == 1
            courses[0].no == "EL5001"
    }

    def "it can find courses of specified summer stage"() {
        when:
            def courses = searchService.findBySummerStage( "zh_TW", "105" )
        then:
            courses.size() == 1
            courses[0].no == "EL5001"
    }

}
