/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sofia
 */
@CrossOrigin("*")
@RestController
@RequestMapping("usuarios")
public class UsuarioControlador {

    Map<String, Object> response;

    @Autowired
    UsuarioServicios userService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> cargaUsuario() {
        try {
            response = new HashMap<>();
            response.put(("data"), userService.listar());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtner(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            response.put(("data"), userService.obtener(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario user = userService.guardar(usuario);
            response = new HashMap<>();
            response.put("data", user);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Usuario user = userService.obtener(id).orElse(null);
            if (user == null) {
                response.put("data", "No se Encontro Usuario");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            userService.eliminar(user.getId());
            response.put("data", "Se elimino usuario id " + id);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> modificar(@RequestBody Usuario usuario, @PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Usuario user = userService.obtener(id).orElse(null);
            if (user == null) {
                response.put("data", "No se encontro Usuario");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            actualizarDatos(user, usuario);
            response.put("data", userService.guardar(usuario));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void actualizarDatos(Usuario viejo, Usuario nuevo) {
        viejo.setNombre(nuevo.getNombre());
        viejo.setApellido(nuevo.getApellido());
        viejo.setCorreo(nuevo.getCorreo());
        viejo.setPassword(nuevo.getPassword());
        viejo.setProvincia(nuevo.getProvincia());
        viejo.setRol(nuevo.getRol());
        viejo.setComisionAlquiler(nuevo.getComisionAlquiler());
        viejo.setComisionVenta(nuevo.getComisionVenta());
        viejo.setFechaRegistro(nuevo.getFechaRegistro());
    }
//
//    @GetMapping("/filtrar")
//    public String filtrarUsuario(
//            @RequestParam(required = false) String rol,
//            @RequestParam(required = false) String provincia,
//            Model model) {
//
//        // Filtrar la lista de clientes según los parámetros de búsqueda
//        List<Usuario> usuarioFiltrados = userService.filtrarUsuario(rol, provincia);
//        model.addAttribute("usuario", new Usuario());
//        // Añadir los filtros actuales al modelo para mantener el valor en los inputs
//        model.addAttribute("rolFiltro", rol);
//        model.addAttribute("provinciaFiltro", provincia);
//
//        // Enviar la lista filtrada a la vista
//        model.addAttribute("listado_usuario", usuarioFiltrados);
//        return "users";
//    }

}
