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


class UnitControllerTest extends IntegrationSpecification {

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
                            "name" : "文學院",
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
                            "name" :  "中國文學系",
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
                              "name" : "中國文學系[不分類]",
                              "id" : "cofuZdeptI1I1001I0ZcofgI0"
                            }
                        ]
                        '''
                )
        )
    }

    def "it can provide all colleges"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/unit/college" )
                                .with( apiToken() )
                                .header( "Accept-Language", "zh_TW" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].id == "deptI1I1000I0"
    }

    def "it can provide all departments in specified college"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/unit/college/deptI1I1000I0/department" )
                                    .with( apiToken() )
                                    .header( "Accept-Language", "zh_TW" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].id == "deptI1I1001I0"
    }

    def "it can provide all targets for specified department"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/unit/department/deptI1I1001I0/target" )
                                    .with( apiToken() )
                                    .header( "Accept-Language", "zh_TW" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].id == "cofuZdeptI1I1001I0ZcofgI0"
    }

    def "it cannot get any information if api token not provided"() {
        expect:
            server().perform(
                    get( "/v1/unit/department/deptI1I1001I0/target" )
                        .header( "Accept-Language", "zh_TW" )
            ).andExpect(
                    status().isBadRequest()
            )
    }

}
