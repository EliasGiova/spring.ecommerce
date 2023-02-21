package com.curso.ecommerce.controller;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;
import java.io.IOException;
import java.util.Optional;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger logger = LoggerFactory.getLogger(ProductoController.class);//crea mensajes para el seguimiento o
    //registro de la ejecución de una aplicación.

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UploadFileService upload;
    
    @Autowired
    private IUsuarioService usuarioService;

    //muestra la interface de la vista de los productos
    @GetMapping("")
    public String show(Model model) {//objeto de tipo model lleva informacion desde el back-end hacia la vista
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    //recibe los atributos para crear el producto
    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    //guarda el producto y redirecciona a la lista con el producto para mostrarlo por pantalla
    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        logger.info("Este es el objeto producto{}", producto);
        
        Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        producto.setUsuario(u);

        //imagen
        if (producto.getId() == null) {//esta validacion es cuando se crea un producto
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        } else {

        }

        productoService.save(producto);
        return "redirect:/productos";
    }

    //recibe para modificar
    @GetMapping("/edit/{id}")//se pone entre llave para recibir el id para poder modificarlo
    public String edit(@PathVariable Integer id, Model model) {//se declara PathVariable para mapear la id que viene en la url para guardarla en la id 
        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.get(id);
        producto = optionalProducto.get();

        logger.info("Producto Buscado: {}", producto);
        model.addAttribute("producto", producto);

        return "productos/edit";
    }

    //realiza las modificaciones y las manda a la vista
    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        Producto p = new Producto();
        p = productoService.get(producto.getId()).get();

        if (file.isEmpty()) {// editamos el producto pero no cambiamos la imagen

            producto.setImagen(p.getImagen());
        } else {//cuando se edita tambien la imagen

            //para eliminar cuando no sea la imagen por defecto
            if (!p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getImagen());
            }
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/productos";
    }

    //recibe el id que quiere eliminar y luego redirecciona a la lista sin el producto eliminado
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        Producto p = new Producto();
        p = productoService.get(id).get();

        //para eliminar cuando no sea la imagen por defecto
        if (!p.getImagen().equals("default.jpg")) {
            upload.deleteImage(p.getImagen());
        }

        productoService.delete(id);
        return "redirect:/productos";
    }
}
