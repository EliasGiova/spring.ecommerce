
package com.curso.ecommerce.controller;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.ProductoService;
import java.util.Optional;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired//inyecta al contenedor una instancia
    private ProductoService productoService;
    
    @GetMapping("")
    public String home(Model model){
        model.addAttribute("productos", productoService.findAll());
        return "usuario/home";
    }
    
    //busca el id del producto y lo manda hacia otra vista con informacion del producto
    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model){
        Producto producto = new Producto();
        Optional<Producto>productoOptional = productoService.get(id);
        producto = productoOptional.get();
        
        model.addAttribute("producto", producto);
        
        logger.info("Id producto mandado como parametro {}", id);
        return "usuario/productohome";
    }
}
