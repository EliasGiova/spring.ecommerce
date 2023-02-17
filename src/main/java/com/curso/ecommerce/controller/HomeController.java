
package com.curso.ecommerce.controller;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HomeController {
    
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired//inyecta al contenedor una instancia
    private ProductoService productoService;
    
    //para almacenar los detalles de la orden
    private List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
    
    //datos de la orden
    Orden orden = new Orden();
    
    @Autowired
    private IUsuarioService usuarioService;
    
    @GetMapping("")
    public String home(Model model){
        model.addAttribute("productos", productoService.findAll());
        return "usuario/home";
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////
    
    
    
    
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
    
    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model){
        DetalleOrden  detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal=0;
        
        
        Optional<Producto>optionalProducto = productoService.get(id);
        logger.info("Producto añadido {}", optionalProducto.get());
        logger.info("Cantidad : {}", cantidad);
        producto=optionalProducto.get();
        
        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio()*cantidad);
        detalleOrden.setProducto(producto);
        
        //validar que el producto no se aña dos veces a la lista
        Integer idProducto = producto.getId();
        boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
        if (!ingresado) {
            detalles.add(detalleOrden);
        }
        
        sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
        
        orden.setTotal(sumaTotal);
        
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        
        
        return "usuario/carrito";
    }
    
    //quitar producto del carrito
    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Integer id, Model model){
        
        //lista nueva de productos
        List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();
        
        for (DetalleOrden detalleOrden: detalles) {
            if (detalleOrden.getProducto().getId() != id) {
                ordenesNuevas.add(detalleOrden);
            }
        }
        //poner la nueva lista con los productos restantes
        detalles = ordenesNuevas;
        
        double sumaTotal=0;
        
        sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
        
        orden.setTotal(sumaTotal);
        
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        
        return "usuario/carrito";
    }
    
    @GetMapping("/getCart")
    public String getCart(Model model){
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        return "/usuario/carrito";
    }
    
    @GetMapping("/order")
    public String order(Model model){
        
        Usuario usuario = usuarioService.findById(1).get();
        
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        model.addAttribute("usuario", usuario);
        
        return "/usuario/resumenorden";
    }
}
