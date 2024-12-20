package com.PracticaProfesional.inmobiliaria.entidades;

import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.*;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "pagos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaPago;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "monto")
    private BigDecimal monto;

    @Column(name = "gananciaAgente")
    private BigDecimal gananciaAgente;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaRegistro;

    private boolean activo;

    @JsonIgnoreProperties(value = {"pagos"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "id_contrato")
    private Contrato contrato;

    public void confirmarPago() {
        this.estado = "PAGADO";
        this.contrato.actualizarSaldo();
        calcularComision();
    }

    private void calcularComision() {
        BigDecimal gananciasPorPagos;
        Usuario agente = contrato.getAgente();

        if (contrato.getTipoContrato().equals(EnumTipoContrato.VENTA)) {
            if (agente.getComisionVenta().compareTo(BigDecimal.ZERO) > 0) {
                gananciasPorPagos = getMonto().multiply(agente.getComisionVenta().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            } else {
                gananciasPorPagos = BigDecimal.ZERO;
            }
        } else {
            if (agente.getComisionAlquiler().compareTo(BigDecimal.ZERO) > 0) {
                gananciasPorPagos = getMonto().multiply(agente.getComisionAlquiler().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            } else {
                gananciasPorPagos = BigDecimal.ZERO;
            }
        }
        this.gananciaAgente=gananciasPorPagos;
        agente.setTotalGanancias(agente.getTotalGanancias().add(gananciasPorPagos));
    }

}
