package tw.edu.ncu.cc.course.server.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import tw.edu.ncu.cc.oauth.resource.filter.AccessTokenDecisionFilter
import tw.edu.ncu.cc.oauth.resource.filter.ApiTokenDecisionFilter

@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = true )
public class SecurityConfig {

    @Order( 1 )
    @Configuration
    public static class OauthGuard1 extends WebSecurityConfigurerAdapter {

        @Autowired
        def ApiTokenDecisionFilter apiTokenDecisionFilter

        @Override
        protected void configure( HttpSecurity http ) throws Exception {
            http.requestMatchers()
                    .antMatchers( "/v*/courses/**" )
                    .antMatchers( "/v*/departments/**" )
                    .antMatchers( "/v*/targets/**" )
                    .antMatchers( "/v*/summer/**" )
                    .antMatchers( "/v*/colleges/**" )
                    .antMatchers( "/v*/status/**" )
                .and()
                    .addFilterAfter( apiTokenDecisionFilter, UsernamePasswordAuthenticationFilter )
                    .csrf().disable()

        }
    }

    @Order( 2 )
    @Configuration
    public static class OauthGuard2 extends WebSecurityConfigurerAdapter {

        @Autowired
        def AccessTokenDecisionFilter accessTokenDecisionFilter

        @Override
        protected void configure( HttpSecurity http ) throws Exception {
            http.requestMatchers()
                    .antMatchers( "/v*/student/**" )
                .and()
                    .addFilterAfter( accessTokenDecisionFilter, UsernamePasswordAuthenticationFilter )
                    .csrf().disable()
        }
    }

    @Order( 3 )
    @Configuration
    public static class ManagementAPI extends WebSecurityConfigurerAdapter {

        @Value( '${custom.management.security.access}' )
        def String managementAccess

        @Override
        protected void configure( HttpSecurity http ) throws Exception {
            http.antMatcher( "/management/**" )
                    .authorizeRequests()
                        .anyRequest().access( managementAccess )
        }

    }

}
