package com.gmail.breninsul.jd2.config;

import com.gmail.breninsul.jd2.service.impl.UserDetailsServiceImpl;
import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF handled by Vaadin
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .accessDeniedPage("/accessDenied")
                .and()
                .authorizeRequests()
                .regexMatchers("/frontend/.*", "/VAADIN/.*", "/login.*", "/accessDenied.*","/search.*").permitAll()
                .regexMatchers("/cabinet.*").hasAnyAuthority(UserDetailsServiceImpl.UserRoleEnum.ROLE_USER.name(),UserDetailsServiceImpl.UserRoleEnum.ROLE_ADMIN.name())
                .regexMatchers("/panel.*", "/log.*").hasAnyAuthority(UserDetailsServiceImpl.UserRoleEnum.ROLE_ADMIN.name())
                .and()
                .logout().deleteCookies("JSESSIONID")
                .and()
                .rememberMe().key("uniqueAndSecret")
        ;

        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider()).
                jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery("select F_name, F_passward, F_enabled"
                + " from T_User where F_name=?")
                .passwordEncoder(passwordEncoder);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(ServletHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }
}

