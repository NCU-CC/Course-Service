package tw.edu.ncu.cc.course.server.service

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.Parameter
import org.springframework.beans.factory.annotation.Autowired
import resource.ServerResource
import specification.SpringSpecification
import spock.lang.Shared

class StudentCourseServiceImplTest extends SpringSpecification {

    @Autowired
    private StudentCourseService studentCourseService

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    @Shared
    private String serverResponse =
                        '''
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
                        .withPath( "/students/101502549/courses" )
                        .withQueryStringParameter( new Parameter( "filter", "selected" ) )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody( serverResponse )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/students/101502550/courses" )
                        .withQueryStringParameter( new Parameter( "filter", "tracking" ) )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody( serverResponse )
        )
    }

    def "it can read selected courses from remote server"() {
        when:
            def response = studentCourseService.readSelectedCourses( "101502549", "zh_TW" )
        then:
            response[0].serialNo == 12034
    }

    def "it can read tracking courses from remote server"() {
        when:
            def response = studentCourseService.readTrackedCourses( "101502550", "zh_TW" )
        then:
            response[0].serialNo == 12034
    }

}
