/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.TransaccionServicios;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sofia
 */
@CrossOrigin("*")
@RestController
@RequestMapping("reportes")
public class ReporteControlador {

    @Autowired
    private InmuebleServicios inmuebleServicios;
    @Autowired
    private ClienteServicios clienteServicios;
    @Autowired
    private TransaccionServicios transaccionServicios;
    @Autowired
    private ContratoServicios contratoServicios;

    Map<String, Object> response, inmueble, transaccion, cliente, contrato;

    @GetMapping
    public ResponseEntity<Map<String, Object>> contrato() {
        try {
            response = new HashMap<>();
            inmueble = new HashMap<>();
            transaccion = new HashMap<>();
            cliente = new HashMap<>();
            contrato = new HashMap<>();

            long cantidadInmuebles = inmuebleServicios.cantidad();

            BigDecimal entradaVenta = BigDecimal.ZERO;
            BigDecimal entradaAlquiler = BigDecimal.ZERO;
            BigDecimal impuestosInmobiliarios = BigDecimal.ZERO;
            BigDecimal impuestosMunicipales = BigDecimal.ZERO;
            BigDecimal impuestosTotales;
            BigDecimal ingresoTotales = BigDecimal.ZERO;

            int cantidadPropietario = 0;
            int cantidadInquilino = 0;
            int cantidadComprador = 0;
            int cantidadCasas = 0;
            int cantidadDeptos = 0;
            int cantidadOficinas = 0;
            int cantidadCampos = 0;
            int cantidadIngresos = 0;
            int cantidadEgresos = 0;
            int cantidadContrato = 0;
            int cantContratoVenta = 0;
            int cantContratoAlquiler = 0;

            List<Contrato> contratos = contratoServicios.listar();
            List<Inmueble> inmuebles = inmuebleServicios.listar();
            List<Transaccion> transacciones = transaccionServicios.listar();
            List<Cliente> clientes = clienteServicios.listar();

            for (Contrato contratoFiltro : contratos) {
                if (contratoFiltro.getTipoContrato() == EnumTipoContrato.VENTA) {
                    entradaVenta = entradaVenta.add(contratoFiltro.getImporte());
                    cantContratoVenta++;
                } else if (contratoFiltro.getTipoContrato() == EnumTipoContrato.ALQUILER) {
                    entradaAlquiler = entradaAlquiler.add(contratoFiltro.getImporte());
                    cantContratoAlquiler++;
                }

                cantidadContrato++;
            }
            contrato.put("cantidadContratos", cantidadContrato);
            contrato.put("contratosVentas", cantContratoVenta);
            contrato.put("contratosAlquiler", cantContratoAlquiler);
            contrato.put("ingresosVentas", entradaVenta);
            contrato.put("ingresosAlquiler", entradaAlquiler);
            response.put("contratos", contrato);
            for (Inmueble inmuebleFiltro : inmuebles) {
                impuestosInmobiliarios = impuestosInmobiliarios.add(inmuebleFiltro.getImpInmobiliarios());
                impuestosMunicipales = impuestosMunicipales.add(inmuebleFiltro.getImpMunicipales());
                switch (inmuebleFiltro.getTipoInmueble()) {
                    case CASA:
                        cantidadCasas++;
                    case DEPARTAMENTO:
                        cantidadDeptos++;
                    case OFICINA:
                        cantidadOficinas++;
                    case CAMPO:
                        cantidadCampos++;
                }
            }
            impuestosTotales = impuestosInmobiliarios.add(impuestosMunicipales);

            inmueble.put("casas", cantidadCasas);
            inmueble.put("departamentos", cantidadDeptos);
            inmueble.put("oficinas", cantidadOficinas);
            inmueble.put("campo", cantidadCampos);
            inmueble.put("impuestosInmobiliarios", impuestosInmobiliarios);
            inmueble.put("inmpuestosMunicipales", impuestosMunicipales);
            inmueble.put("impuestosTotales", impuestosTotales);
            inmueble.put("cantidadInmueble", cantidadInmuebles);
            response.put("inmuebles", inmueble);

            for (Transaccion transaccionFiltro : transacciones) {
                if (transaccionFiltro.getTipoTransaccion().equals("INGRESO")) {
                    cantidadIngresos++;
                    ingresoTotales = ingresoTotales.add(transaccionFiltro.getImporte());
                } else {
                    cantidadEgresos++;
                }
               
            }
            transaccion.put("cantidadIngresos", cantidadIngresos);
            transaccion.put("cantidadEgresos", cantidadEgresos);
            transaccion.put("transaccionesTotales", ingresoTotales);
            response.put("transacciones", transaccion);

            for (Cliente clienteFiltro : clientes) {
                switch (clienteFiltro.getTipoCliente()) {
                    case COMPRADOR:
                        cantidadComprador++;
                    case INQUILINO:
                        cantidadInquilino++;
                    case PROPIETARIO:
                        cantidadPropietario++;

                }
            }
            cliente.put("cantidadPropietarios", cantidadPropietario);
            cliente.put("cantidadInquilinos", cantidadInquilino);
            cliente.put("cantidadComprador", cantidadComprador);
            response.put("clientes", cliente);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}