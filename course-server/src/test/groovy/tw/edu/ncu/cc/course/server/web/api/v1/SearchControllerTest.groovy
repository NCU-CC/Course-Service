package tw.edu.ncu.cc.course.server.web.api.v1

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.Parameter
import resource.ServerResource
import specification.IntegrationSpecification
import spock.lang.Shared

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static tw.edu.ncu.cc.oauth.resource.test.ApiAuthMockMvcRequestPostProcessors.apiToken

class SearchControllerTest extends IntegrationSpecification {

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
                        "teachers": [  "錢夫人", "阿土伯" ],
                        "credit": 2,
                        "classRooms": [ "C2-209", "C2-209" ],
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
                        .withPath( "/courses" )
                        .withQueryStringParameter( new Parameter( "deptId", "deptI1I1000I0" ) )
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

    def "it can provide searching result of courses"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/courses" )
                                    .with( apiToken() )
                                    .header( "Accept-Language", "zh_TW" )
                                    .param( "deptId", "deptI1I1000I0" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].no == "EL5001"
    }

    def "it can provide searching result of courses of specified department"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/departments/deptI1I1000I0/courses" )
                                    .with( apiToken() )
                                    .header( "Accept-Language", "zh_TW" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].no == "EL5001"
    }

    def "it can provide searching result of courses for specified target"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/targets/cofuZdeptI1I1001I0ZcofgI0/courses" )
                                    .with( apiToken() )
                                    .header( "Accept-Language", "zh_TW" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].no == "EL5001"
    }

    def "it can provide searching result of courses of specified summer stage"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/summer/105/courses" )
                                    .with( apiToken() )
                                    .header( "Accept-Language", "zh_TW" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].no == "EL5001"
    }

    def "it cannot provide any information if api token not provided"() {
        expect:
            server().perform(
                    get( "/v1/summer/105/courses" )
                            .header( "Accept-Language", "zh_TW" )
            ).andExpect(
                    status().isBadRequest()
            )
    }

}
