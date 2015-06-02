package tw.edu.ncu.cc.course.server.web.api.v1

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import resource.ServerResource
import specification.IntegrationSpecification
import spock.lang.Shared

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static tw.edu.ncu.cc.oauth.resource.test.ApiAuthMockMvcRequestPostProcessors.apiToken


class CourseControllerTest extends IntegrationSpecification {

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/course/12034" )
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
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/course/91001/limit" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody(
                        '''
                        {
                          "serialNo" : "91001",
                          "no" : "LA4001",
                          "name" : "文學與劇場",
                          "memo" : "some memo here"
                        }
                        '''
                )
        )
    }

    def "it can provide course information by serialNo"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/course/12034" )
                                    .with( apiToken() )
                                    .header( "Accept-Language", "zh_TW" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.no == "EL5001"
    }

    def "it can provide course limit by serialNo"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/course/91001/limit" )
                                    .with( apiToken() )
                                    .header( "Accept-Language", "zh_TW" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.no == "LA4001"
    }

    def "it cannot provide any information if api token not provided"() {
        expect:
            server().perform(
                    get( "/v1/course/91001/limit" )
                            .header( "Accept-Language", "zh_TW" )
            ).andExpect(
                    status().isBadRequest()
            )
    }

}
