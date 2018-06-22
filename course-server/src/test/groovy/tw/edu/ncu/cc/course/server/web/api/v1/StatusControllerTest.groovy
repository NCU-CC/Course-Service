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

class StatusControllerTest extends IntegrationSpecification {

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "en_US" ) )
                        .withPath( "/status" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody(
                            '''
                            {
                              "semester" : 1021 ,
                              "stage" : "�[�h�ﶥ�q"
                            }
                            '''
                        )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/status" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody(
                        '''
                            {
                              "semester" : 1022 ,
                              "stage" : "�[�h�ﶥ�q"
                            }
                            '''
                )
        )
    }

    def "it can provide current status with api token 1"() {
        when:
            def response = JSON(
                server().perform(
                        get( "/v1/status" )
                            .with( apiToken() )
                            .header( "Accept-Language", "en_US" )
                ).andExpect(
                        status().isOk()
                ).andReturn()
            )
        then:
            response.semester == 1021
    }

    def "it can provide current status with api token 2"() {
        when:
            def response = JSON(
                server().perform(
                        get( "/v1/status" )
                            .with( apiToken() )
                ).andExpect(
                        status().isOk()
                ).andReturn()
            )
        then:
            response.semester == 1022
    }

    def "it cannot provide any information if api token not provided"() {
        expect:
            server().perform(
                    get( "/v1/status" )
            ).andExpect(
                    status().isBadRequest()
            ).andReturn()
    }

}
