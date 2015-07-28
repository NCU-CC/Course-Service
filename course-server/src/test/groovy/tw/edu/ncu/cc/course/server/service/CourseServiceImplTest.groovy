package tw.edu.ncu.cc.course.server.service

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import resource.ServerResource
import specification.SpringSpecification
import spock.lang.Shared


class CourseServiceImplTest extends SpringSpecification {

    @Autowired
    CourseService courseService

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/courses/12034" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody(
                        '''
                        {
                            "serialNo" : 12034,
                            "no" : "EL5001",
                            "classNo" : "*",
                            "name" : "文學/文化理論導讀",
                            "isCanceled" : false,
                            "memo": "限三、四年級",
                            "isMasterDoctor": false,
                            "language": "國語",
                            "usePasswordCard": "不使用",
                            "isFirstRun": true,
                            "isPreSelect": true,
                            "teachers": "錢夫人,阿土伯",
                            "credit": 2,
                            "classRooms": "C2-209,C2-209",
                            "time": {
                                      "0": ["5"],
                                      "2": ["3", "4"]
                                    },
                            "type": "必修",
                            "fullHalf": "全",
                            "limit": 0
                        }
                        '''
                )
        )
    }

    def "it can find course information by serialNo"() {
        when:
            def course = courseService.findBySerialNo( "zh_TW", "12034" )
        then:
            course.no == "EL5001"
    }

}
