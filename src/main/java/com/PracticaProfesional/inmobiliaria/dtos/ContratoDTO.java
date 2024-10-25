/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.dtos;

import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Exequiel
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContratoDTO {

    private Integer id;
    private String tipoOperacion;
    private String tipoCliente;
    private Date fechaContrato;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer cantCuota;
    private BigDecimal importe;
    private String estado;

    private Map<String, Object> inmueble = new HashMap<>();
    private Map<String, Object> cliente = new HashMap<>();
    private Map<String, Object> agente = new HashMap<>();

    public ContratoDTO(Contrato contrato) {
        setId(contrato.getId());
        setTipoOperacion(contrato.getTipoOperacion());
        setTipoCliente(contrato.getTipoCliente());
        setFechaContrato(contrato.getFechaContrato());
        setFechaFin(contrato.getFechaFin());
        setFechaInicio(contrato.getFechaInicio());
        setEstado(contrato.getEstado());
        setCantCuota(contrato.getCantCuota());
        setImporte(contrato.getImporte());
        inmueble.put("id", contrato.getInmueble().getId());
        inmueble.put("titulo", contrato.getInmueble().getTitulo());

        cliente.put("id", contrato.getCliente().getId());
        cliente.put("nombre", contrato.getCliente().getNombre());
        cliente.put("apellido", contrato.getCliente().getApellido());

        agente.put("id", contrato.getAgente().getId());
        agente.put("nombre", contrato.getAgente().getNombre());
        agente.put("apellido", contrato.getAgente().getApellido());

    }
}
