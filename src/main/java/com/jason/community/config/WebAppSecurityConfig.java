package com.jason.community.config;

import com.jason.community.common.CommunityConstant;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jason
 * @version 1.0
 */
@Configuration // 标识为配置类
@EnableWebSecurity // 开启Web环境下权限控制功能
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启全局方法权限控制
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailService;

    @Resource // 使用BCryptPasswordEncoder加密规则
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 注册过滤器Filter
    public FilterRegistrationBean<Filter> someFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();

        // SpringSecurity使用FilterChain控制二十多个Filter进行权限管理
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        registration.setFilter(delegatingFilterProxy);
        registration.setName("springSecurityFilterChain");
        registration.addUrlPatterns("/*");
        registration.setOrder(10);
        return registration;
    }

    /**
     * 配置权限管理
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder
                // 基于数据库完成账号、密码的检查
                .userDetailsService(userDetailService)
                // 指定密码加密规则
                .passwordEncoder(passwordEncoder);
    }

    /**
     * 配置访问请求
     */
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()     // 对请求授权
                .antMatchers("/css/**","/fileupload/**","/images/**","/js/**","/lib/**","/ueditor/**","/jquery/**")
                .permitAll()             // 访问静态资源请求：无条件访问
                .antMatchers("/login.html", "/register.html")
                .permitAll()             // 访问登录注册页面：无条件访问
                .antMatchers("/admin/send/short/message", "/admin/do/register")
                .permitAll()             // 请求发送短信注册：无条件访问

                // 访问小区模块 相关页面
                .antMatchers("/community_list.html")
                .hasAuthority("community:read")
                .antMatchers("/community_add.html")
                .hasAuthority("community:write")
                // 访问房产模块 相关页面
                .antMatchers("/building_list.html")
                .hasAuthority("house:read")
                .antMatchers("/building_add.html")
                .hasAuthority("house:write")
                .antMatchers("/house_list.html")
                .hasAuthority("house:read")
                .antMatchers("/house_add.html")
                .hasAuthority("house:write")
                // 访问业主信息模块 相关页面
                .antMatchers("/owner_list.html")
                .hasAuthority("owner:read")
                .antMatchers("/owner_add.html")
                .hasAuthority("owner:write")
                .antMatchers("/car_list.html")
                .hasAuthority("owner:read")
                .antMatchers("/car_add.html")
                .hasAuthority("owner:write")
                .antMatchers("/pet_list.html")
                .hasAuthority("owner:read")
                .antMatchers("/pet_add.html")
                .hasAuthority("owner:write")
                // 访问停车位模块 相关页面
                .antMatchers("/parking_list.html")
                .hasAuthority("parking:read")
                .antMatchers("/parking_add.html")
                .hasAuthority("parking:write")
               .antMatchers("/parking_use_list.html")
                .hasAuthority("parking:read")
                .antMatchers("/parking_use_add.html")
                .hasAuthority("parking:write")
                // 访问服务模块 相关页面
                .antMatchers("/activity_list.html")
                .hasAuthority("service:read")
                .antMatchers("/activity_add.html")
                .hasAuthority("service:write")
                .antMatchers("/repair_list.html")
                .hasAuthority("service:read")
                .antMatchers("/repair_add.html")
                .hasAuthority("service:write")
                .antMatchers("/complaint_list.html")
                .hasAuthority("service:read")
                .antMatchers("/complaint_add.html")
                .hasAuthority("service:write")
                .antMatchers("/letter_list.html")
                .hasAuthority("service:read")
                .antMatchers("/letter_add.html")
                .hasAuthority("service:write")
                // 访问资产模块 相关页面
                .antMatchers("/asset_list.html")
                .hasAuthority("asset:read")
                .antMatchers("/asset_add.html")
                .hasAuthority("asset:write")
                // 访问收费模块 相关页面
                .antMatchers("/charge_item_list.html")
                .hasAuthority("charge:read")
                .antMatchers("/charge_item_add.html")
                .hasAuthority("charge:write")
                // 访问管理员模块 相关页面
                .antMatchers("/admin_list.html")
                .hasAuthority("admin:read")
                .antMatchers("/grant_role.html")
                .hasAuthority("admin:write")
                .antMatchers("/role_list.html")
                .hasAuthority("admin:read")
                .antMatchers("/role_add.html")
                .hasAuthority("admin:write")
                .antMatchers("/grant_auth.html")
                .hasAuthority("admin:write")
                .antMatchers("/auth_list.html")
                .hasAuthority("admin:read")
                .antMatchers("/auth_add.html")
                .hasAuthority("admin:write")
                // 访问系统模块 相关页面
                .antMatchers("/system_list.html")
                .hasAuthority("system:read")
                .antMatchers("/system_add.html")
                .hasAuthority("system:write")

                .anyRequest()            // 对其他的任意请求
                .authenticated()         // 需要登录才可以访问

                .and()
                .formLogin()                            // 开启表单登录功能
                .loginPage("/login.html")               // 指定访问登录的页面（没有指定就访问默认登录页）
                .loginProcessingUrl("/admin/do/login")  // 指定处理登录的请求（即登录表单的提交地址）

                .defaultSuccessUrl("/index.html") // 指定登录成功后页面
                .failureUrl("/login.html")        // 指定登录失败后页面

                .and()
                .csrf().disable()       // 禁用CSRF功能，表单则可以使用post以外的请求，也不用带令牌参数
                /*
                    HTTP响应头信息中的X-Frame-Options，用于指示浏览器是否应该加载一个iframe中的页面
                    X-Frame-Options有以下配置项：
                        1.DENY：不能被嵌入到任何iframe或者frame中。
                        2.SAMEORIGIN：页面只能被本站页面嵌入到iframe或者frame中
                        3.ALLOW-FROM uri：只能被嵌入到指定域名的框架中。
                */
                .headers().frameOptions().sameOrigin()

                .and()
                .logout()                        // 开启注销功能
                .logoutUrl("/do/logout")         // 指定处理注销的请求
                .logoutSuccessUrl("/login.html") // 指定注销成功后页面

                .and()
                .exceptionHandling()                                 // 开启异常处理
                .accessDeniedPage("/system_exception.html")          // 指定访问拒绝请求的地址
                .accessDeniedHandler(new AccessDeniedHandlerImpl() { // 指定处理访问拒绝的servlet
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        request.setAttribute("exception", new Exception(CommunityConstant.MESSAGE_ACCESS_DENIED));
                        request.getRequestDispatcher("/system_exception.html").forward(request,response);
                    }
                })
//                .and()
//                .rememberMe()                           // 开启"记住我"功能
        ;
    }
}
