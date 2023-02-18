
package com.curso.ecommerce.service;

import com.curso.ecommerce.model.Orden;
import java.util.List;


public interface IOrdenService {
    
    List<Orden> findAll();//obtiene todas las ordenes
    Orden save (Orden orden);
    String generarNumeroOrden();
}
