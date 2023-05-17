
package com.curso.ecommerce.service;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import java.util.List;
import java.util.Optional;


public interface IOrdenService {
    
    List<Orden> findAll();//obtiene todas las ordenes
    Orden save (Orden orden);
    String generarNumeroOrden();
    List<Orden> findByUsuario(Usuario usuario);
    Optional<Orden> findById(Integer id);
}
