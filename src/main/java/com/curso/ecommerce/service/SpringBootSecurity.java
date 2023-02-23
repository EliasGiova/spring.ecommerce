package com.curso.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringBootSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(getEnecoder());//es un metodo que se le pasa como para parametro un objeto que es de arriba que busca el metodo para validar el usuario y clave
    }

    @Bean// es de configuracion de codificacion de la clave
    public BCryptPasswordEncoder getEnecoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {//este metodo restringe cierto datos al usuario
        http.csrf().disable().authorizeRequests()//http es argumento, csrf es un metodo que ayude a que no se inyecte codigo malisioso disable. authorizeRequest son otros metodos,
        .antMatchers("/administrador/**").hasRole("ADMIN")// antMatchers indica que controladores tienen acceso de acuerdo el rol que tengan, este caso Administrador ** eso significa que da acceso a todo la vista, "template" como rol de admin.    
        .antMatchers("/productos/**").hasRole("ADMIN").and()//le da permiso tambien al directorio producto, o sea todo la vista como rol de admin
        .formLogin().loginPage("/usuario/login")//formLogin es el formulario donde se loguea es a traves de una ruta que se establece con login.page
        .permitAll()//el resto de cosas permite todo.
        .defaultSuccessUrl("/usuario/acceder");//cuando ya se haya logueado utiliza esta funcion para mandarlo a un lugar
    }*/

}
