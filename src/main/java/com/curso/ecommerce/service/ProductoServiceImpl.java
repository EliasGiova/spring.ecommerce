
package com.curso.ecommerce.service;

import com.curso.ecommerce.model.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.curso.ecommerce.repository.IProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService{

    @Autowired
    private IProductoRepository productoRepo;
        
    @Override
    public Producto save(Producto producto) {
        return productoRepo.save(producto);
    }

    @Override
    public Optional<Producto> get(Integer id) {
        return productoRepo.findById(id);
    }

    @Override
    public void update(Producto producto) {
        productoRepo.save(producto);
    }

    @Override
    public void delete(Integer id) {
        productoRepo.deleteById(id);
    }
    
    @Override
    public List<Producto> findAll(){
        return productoRepo.findAll();
    }
    
}
