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
                        "name" : "文學\\/文化理論導讀",
                        "isClosed" : false,
                        "memo": "限三、四年級",
                        "isMasterDoctor": false,
                        "language": "國語",
                        "passwordCard": "不使用",
                        "isFirstRun": true,
                        "isPreSelect": true,
                        "teachers": "錢夫人,阿土伯",
                        "credit": 2,
                        "classRooms": "C2-209,C2-209",
                        "times": {
                                  "0": ["5"],
                                  "2": ["3", "4"]
                                },
                        "type": "必修",
                        "fullHalf": "全",
                        "maxStudents": 0
                      }
                    ]
                    '''

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/courses" ) //TODO ADD PARAMS
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
                        .withPath( "/departments/deptI1I1000I0/courses" )
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
                        .withPath( "/targets/cofuZdeptI1I1001I0ZcofgI0/courses" )
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
                        .withPath( "/summer/105/courses" )
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
