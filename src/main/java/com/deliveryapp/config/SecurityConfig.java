package com.deliveryapp.config;

import com.deliveryapp.security.UserDetailsServiceImpl;
import com.deliveryapp.services.ClienteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private ClienteServiceImpl clienteServiceImpl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/cliente/cadCliente").permitAll()
                .antMatchers(HttpMethod.POST, "/cliente/cadCliente").permitAll()
                .antMatchers(HttpMethod.GET, "/cliente/cadEndereco/*").permitAll()
                .antMatchers(HttpMethod.POST, "/cliente/cadEndereco/*").permitAll()
                .antMatchers(HttpMethod.GET, "/cliente/dadosCliente").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/cliente/dadosCliente").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/cliente/pedidosCliente").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/cliente/pedidosCliente").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/cliente/enderecosCliente").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/cliente/enderecosCliente").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/sacola/index").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/sacola/add/*").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/sacola/remove/*").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/pedido/finalizar").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/gerente/addPrato").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/gerente/addPrato").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/gerente/listarPratos").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/gerente/editPrato/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/gerente/editPrato/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/gerente/excluirPrato/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/gerente/listarPedidosPendentes").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/gerente/listarPedidosEnviados").hasRole("ADMIN")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/logar")
                .permitAll()
                .defaultSuccessUrl("/")

                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logar")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/fonts/**","/js/**","/img/**","/images/**");
    }
}
