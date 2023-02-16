
package com.curso.ecommerce.service;

import com.curso.ecommerce.model.Producto;
import java.util.List;
import java.util.Optional;


public interface ProductoService {
    public Producto save(Producto producto); //guarda un producto
    public Optional<Producto> get(Integer id); //trae un producto 
    public void update(Producto producto); //modifica un producto
    public void delete(Integer id); //elimina un producto
    public List<Producto> findAll(); //trae todos los productos guardados
        
}
