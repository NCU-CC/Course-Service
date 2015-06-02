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
                        .withPath( "/search" )  //TODO ADD PARAMS
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

    def "it can provide searching result of courses"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/search" )
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
                            get( "/v1/search/department/deptI1I1000I0" )
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
                            get( "/v1/search/target/cofuZdeptI1I1001I0ZcofgI0" )
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
                            get( "/v1/search/summer/105" )
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
                    get( "/v1/search/summer/105" )
                            .header( "Accept-Language", "zh_TW" )
            ).andExpect(
                    status().isBadRequest()
            )
    }

}
