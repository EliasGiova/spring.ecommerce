
package com.curso.ecommerce.controller;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
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
    
    @Autowired
    private IOrdenService ordenService;
            
    @Autowired
    private IDetalleOrdenService detalleOrdenService;
    
    @Autowired
    private IUsuarioService usuarioService;
    
    //para almacenar los detalles de la orden
    private List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
    
    //datos de la orden
    Orden orden = new Orden();
    
     ///////////////////////////////////////////////////////////////////////////////////
    
    @GetMapping("")
    public String home(Model model, HttpSession session){
        logger.info("Session del usuario : {}", session.getAttribute("idusuario"));
        model.addAttribute("productos", productoService.findAll());
        
        //session
        model.addAttribute("sesion", session.getAttribute("idusuario"));
        
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
    public String getCart(Model model, HttpSession session){
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        
        //sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));
                
        return "/usuario/carrito";
    }
    
    @GetMapping("/order")
    public String order(Model model, HttpSession session){
        
        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        model.addAttribute("usuario", usuario);
        
        return "/usuario/resumenorden";
    }
    
    //guardar la orden
    @GetMapping("/saveOrden")
    public String saveOrden(HttpSession session){
        Date fechaCreacion = new Date();
        orden.setFechaCreacion(fechaCreacion);
        orden.setNumero(ordenService.generarNumeroOrden());
        
        //usuario
        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        orden.setUsuario(usuario);
        ordenService.save(orden);
        
        //guardarDetaller
        for(DetalleOrden dt : detalles){
            dt.setOrden(orden);
            detalleOrdenService.save(dt);
        }
        
        ///limpiar list y otden
        Orden orden = new Orden();
        detalles.clear();
        
        return "redirect:/";
    }
    
    //funcionalidad para buscar un producto
    @PostMapping("/search")
    public String searchProduct(@RequestParam String nombre, Model model){
        String N = nombre.substring(0, 1).toUpperCase();
        String ombre = nombre.substring(1,nombre.length()).toLowerCase();
        String Nombre = N+ombre;
        logger.info("nombre del Producto: {}", Nombre);
        List<Producto> productos = productoService.findAll().stream().filter(p -> p.getNombre().contains(Nombre)).collect(Collectors.toList());
        model.addAttribute("productos", productos);
        return "usuario/home";
    }
    
}
