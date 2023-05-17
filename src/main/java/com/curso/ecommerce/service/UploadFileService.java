/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {

    private String folder = "images//";//va contenes la ubicacion en nuestro proyecto donde se van a cargar las imagenes

    public String saveImage(MultipartFile file) throws IOException {//es un objeto de tipo imagen que viene desde donde lo cargamos
        if (!file.isEmpty()) {//file no esta vacio
            byte[] bytes = file.getBytes();//la imagen se pasa en byte desde el cliente hacia el servidor
            Path path = Paths.get(folder + file.getOriginalFilename());//en este path se guarda la imagen 
            Files.write(path, bytes);
            return file.getOriginalFilename();
        }
        return "default.jpg";
    }
    
    public void deleteImage(String nombre){
        String ruta ="images//";
        File file = new File(ruta+nombre);
        file.delete();
    }
}
