package itmo.java.advanced_124_31.config;

import itmo.java.advanced_124_31.auth.AuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${auth.header}")
	private String principalRequestHeader;

	@Value("${auth.token}")
	private String principalRequestValue;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		AuthFilter filter = new AuthFilter(principalRequestHeader);
		filter.setAuthenticationManager(authentication -> {
			String principal = (String) authentication.getPrincipal();
			if (!principalRequestValue.equals(principal)) {
				throw new BadCredentialsException(
						"The API key was not found or not the expected value.");
			}
			authentication.setAuthenticated(true);
			return authentication;
		});

		httpSecurity
				.addFilter(filter)
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/cars/all").permitAll()
				 .antMatchers(HttpMethod.GET, "/drivers/all").permitAll()
				 .antMatchers(HttpMethod.GET, "/workshifts/all").permitAll()
				 .antMatchers(HttpMethod.GET, "/licenses/all").permitAll()
				.antMatchers("/cars/**", "/drivers/**", "/workshifts/**", "/licenses/**")
				.authenticated()
				.and().csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
