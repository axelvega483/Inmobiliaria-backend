package com.PracticaProfesional.inmobiliaria.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaccion")
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tipo_transaccion")
    private String tipoTransaccion;

    @Column(name = "tipo_operacion")
    private String tipoOperacion;

    @Column(name = "importe")
    private BigDecimal importe;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_transaccion")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaTransaccion;

    @Column(name = "estado")
    private Boolean estado;

    private boolean activo;

    @JsonIgnoreProperties(value = {"transacciones"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "id_agente")
    private Usuario agente;

}
