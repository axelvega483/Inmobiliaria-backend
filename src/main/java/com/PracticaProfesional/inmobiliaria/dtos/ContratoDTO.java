package com.PracticaProfesional.inmobiliaria.dtos;

import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContratoDTO {
    
    private Integer id;
    private EnumTipoContrato tipoOperacion;
    private String tipoCliente;
    private LocalDateTime fechaContrato;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal importe;
    private EnumEstadoContrato estado;
    
    private Map<String, Object> inmueble = new HashMap<>();
    private Map<String, Object> cliente = new HashMap<>();
    private Map<String, Object> agente = new HashMap<>();
    
    public ContratoDTO(Contrato contrato) {
        setId(contrato.getId());
        setTipoOperacion(contrato.getTipoContrato());
        setEstado(contrato.getEstado());
        setFechaContrato(contrato.getFechaContrato());
        setFechaFin(contrato.getFechaFin());
        setFechaInicio(contrato.getFechaInicio());
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
