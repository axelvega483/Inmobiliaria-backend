package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Imagen;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoInmueble;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ImagenServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("*")
@RestController
@RequestMapping("inmuebles")
public class InmuebleControlador {

    private Map<String, Object> response;

    @Autowired
    private InmuebleServicios inmuebleServicio;
    @Autowired
    private ClienteServicios clienteServicio;
    @Autowired
    private ImagenServicios imagenService;

    private final String RUTA_IMAGENES = System.getProperty("user.dir") + "/imagenes/";

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar(
            @RequestParam(name = "tipoInmueble", required = false) String tipoInmueble,
            @RequestParam(name = "direccion", required = false) String direccion,
            @RequestParam(name = "estado", required = false) String estado
    ) {
        try {
            response = new HashMap<>();
            response.put("data", inmuebleServicio.listar(tipoInmueble, direccion, estado));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("imagen/{id}")
    @ResponseBody
    public ResponseEntity<?> mostrarImagen(@PathVariable Integer id) {
        try {
            Imagen imagenBD = imagenService.obtener(id).orElse(null);
            if (imagenBD == null) {

                return new ResponseEntity<>("No se encontro la imagen", HttpStatus.NOT_FOUND);
            } else {
                Path imagenPath = Paths.get(RUTA_IMAGENES).resolve(imagenBD.getNombre());
                Resource resource = new UrlResource(imagenPath.toUri());

                if (resource.exists() || resource.isReadable()) {
                    String contentType;
                    contentType = Files.probeContentType(imagenPath);
                    if (contentType == null) {
                        contentType = "application/octet-stream";
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                } else {
                    throw new RuntimeException("No se puede leer el archivo: " + id);
                }
            }
        } catch (RuntimeException | MalformedURLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Integer id) {
        try {
            response = new HashMap<>();

            Inmueble inmueble = inmuebleServicio.obtener(id).orElse(null);
            if (inmueble == null) {
                response.put("data", "No se encontro el inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("data", inmueble);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> guardar(
            @Valid @RequestBody Inmueble inmueble) {
        try {
            response = new HashMap<>();
            Cliente propietario = clienteServicio.obtener(inmueble.getPropietario().getId()).orElse(null);
            if (propietario == null) {
                response.put("data", "Propietario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            inmueble.setFechaRegistro(LocalDateTime.now());
            inmueble.setEstado(EnumEstadoInmueble.MANTENIMIENTO);
            propietario.agregarInmueble(inmueble);

            response.put("data", inmuebleServicio.guardar(inmueble));
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "subir-imagen/{idInmueble}", consumes = {"multipart/form-data"})
    private ResponseEntity<Map<String, Object>> subirImagenes(
            @PathVariable(name = "idInmueble") Integer id,
            @RequestParam(name = "imagenes") MultipartFile[] imagenes) {
        LocalDateTime fechaRegistro = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        Path directorioPath = Paths.get(RUTA_IMAGENES);
        try {
            response = new HashMap<>();
            Inmueble inmueble = inmuebleServicio.obtener(id).orElse(null);
            if (inmueble == null) {
                response.put("data", "No se encontro el inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (!Files.exists(directorioPath)) {
                Files.createDirectories(directorioPath);
            }

            for (Imagen imagene : inmueble.getImagenes()) {
                imagenService.eliminar(imagene.getId());
                Files.deleteIfExists(directorioPath.resolve(imagene.getNombre()));
            }

            inmueble.getImagenes().clear();
            for (MultipartFile image : imagenes) {
                String nombreImagen = dtf.format(fechaRegistro) + "_" + image.getOriginalFilename();
                Path rutaImagen = directorioPath.resolve(nombreImagen);

                Files.write(rutaImagen, image.getBytes());

                Imagen imagen = new Imagen();
                imagen.setActivo(true);
                imagen.setNombre(dtf.format(fechaRegistro) + "_" + image.getOriginalFilename());
                inmueble.addImagen(imagen);
            }
            inmueble = inmuebleServicio.guardar(inmueble);
            response.put("data", "imagenes subida");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IOException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Metodo para dar de alta un inmueble y colocarle fecha de publicacion
     *
     * @param id id de inmueble
     * @return
     *
     */
    @PostMapping("/publicar/{id}")
    public ResponseEntity<Map<String, Object>> publicarInmueble(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Inmueble inmueble = inmuebleServicio.obtener(id).orElse(null);
            if (inmueble == null) {
                response.put("data", "El inmueble no se encontro");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            inmueble.setEstado(EnumEstadoInmueble.DISPONIBLE);
            response.put("data", inmuebleServicio.guardar(inmueble));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Inmueble inmu = this.inmuebleServicio.obtener(id).orElse(null);
            if (inmu == null) {
                response.put("data", "No se encontro Inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (inmu.getEstado().equals(EnumEstadoInmueble.ALQUILADO)) {
                response.put("data", "El inmueble se encuentra alquilado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            inmuebleServicio.eliminar(inmu.getId());
            response.put("data", "Se elimino el inmueble id " + id);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> modificar(@RequestBody Inmueble inmueble,
            @PathVariable(required = true) Integer id) {
        try {
            response = new HashMap<>();

            Inmueble inmuebleBD = inmuebleServicio.obtener(id).orElse(null);
            if (inmuebleBD == null) {
                response.put("data", "no se encontro Inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            actualizar(inmuebleBD, inmueble);

            response.put("data", inmuebleServicio.guardar(inmuebleBD));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (HibernateException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("inmueble", inmuebleServicio.obtener(id).orElse(null));
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void actualizar(Inmueble viejo, Inmueble nuevo) {
        if (viejo.isVenta()) {
            viejo.setPrecioVenta(nuevo.getPrecioVenta());
        } else {
            viejo.setPrecioAlquilerDia(nuevo.getPrecioAlquilerDia());
            viejo.setPrecioAlquilerMes(nuevo.getPrecioAlquilerMes());
        }
        viejo.setTitulo(nuevo.getTitulo());
        viejo.setEstado(nuevo.getEstado());
        viejo.setCantAmbientes(nuevo.getCantAmbientes());
        viejo.setDescripcion(nuevo.getDescripcion());
        viejo.setDireccion(nuevo.getDireccion());
        viejo.setExpensas(nuevo.getExpensas());
        viejo.setHectareas(nuevo.getHectareas());
        viejo.setBanios(nuevo.getBanios());
        viejo.setAccesoRuta(nuevo.isAccesoRuta());
        viejo.setImpInmobiliarios(nuevo.getImpInmobiliarios());
        viejo.setImpMunicipales(nuevo.getImpMunicipales());
        viejo.setMts2(nuevo.getMts2());
        viejo.setVidrieraCalle(nuevo.isVidrieraCalle());
        viejo.setVenta(nuevo.isVenta());
        viejo.setTipoOperacion(nuevo.getTipoOperacion());
        viejo.setTipoInmueble(nuevo.getTipoInmueble());
        viejo.setRiego(nuevo.isRiego());
    }
}
