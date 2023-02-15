
package com.curso.ecommerce.controller;



import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.ProductoService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    
    private final Logger logger = LoggerFactory.getLogger(ProductoController.class);//crea mensajes para el seguimiento o
                                                                                    //registro de la ejecución de una aplicación.
    @Autowired
    private ProductoService productoService;
    
    @GetMapping("")
    public String show(){
        return "productos/show";
    }
    
    @GetMapping("/create")
    public String create(){
        return "productos/create";
    }
    
    @PostMapping("/save")
    public String save(Producto producto){
        logger.info("Este es el objeto producto{}", producto);
        productoService.save(producto);
        return "redirect:/productos";
    }
    
}
