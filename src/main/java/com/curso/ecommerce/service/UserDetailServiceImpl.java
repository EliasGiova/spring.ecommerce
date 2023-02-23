
package com.curso.ecommerce.service;

import com.curso.ecommerce.model.Usuario;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService{//es una interface de Spring Security

    @Autowired
    private IUsuarioService usuarioService;
    
    @Autowired
    private BCryptPasswordEncoder bCrypt;
    
    @Autowired
    HttpSession session;
    
    private final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//esta clase sirve para validar el usuario y la clave si son las registradas con anterioridad
        logger.info("Esto es el username");
        Optional <Usuario> optionalUser = usuarioService.findByEmail(username);
        if (optionalUser.isPresent()) {
            logger.info("esto es el id del usuario: {}", optionalUser.get().getId());
                session.setAttribute("idusuario", optionalUser.get().getId());
                Usuario usuario = optionalUser.get();
                return User.builder().username(usuario.getNombre()).
                        password(bCrypt.encode(usuario.getPassword())).
                        roles(usuario.getTipo()).build();
        }else{
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
    
}
