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
import static tw.edu.ncu.cc.oauth.resource.test.ApiAuthMockMvcRequestPostProcessors.accessToken


class CourseControllerTest extends IntegrationSpecification {

    def token = accessToken().user( "user-uid" ).scope( "user.info.basic.read" )

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
                            "name" : "����/���Ʋz�׾�Ū",
                            "isClosed" : false,
                            "memo": "���T�B�|�~��",
                            "isMasterDoctor": false,
                            "language": "���y",
                            "passwordCard": "���ϥ�",
                            "isFirstRun": true,
                            "isPreSelect": true,
                            "teachers": [  "���ҤH", "���g�B" ],
                            "credit": 2,
                            "classRooms": [ "C2-209", "C2-209" ],
                            "times": {
                                      "0": ["5"],
                                      "2": ["3", "4"]
                                    },
                            "type": "����",
                            "fullHalf": "��",
                            "maxStudents": 0
                        }
                        '''
                )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withHeader( new Header( "Accept-Language", "zh_TW" ) )
                        .withPath( "/courses/91001/limit" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json;charset=UTF-8" ) )
                        .withBody(
                        '''
                        {
                          "serialNo" : "91001",
                          "no" : "LA4001",
                          "name" : "���ǻP�@��",
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
                            get( "/v1/courses/12034" )
                                    .with( token )
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
                            get( "/v1/courses/91001/limit" )
                                    .with( token )
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
                    get( "/v1/courses/91001/limit" )
                            .header( "Accept-Language", "zh_TW" )
            ).andExpect(
                    status().isBadRequest()
            )
    }

}
