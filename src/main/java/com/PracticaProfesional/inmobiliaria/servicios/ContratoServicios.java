package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.PracticaProfesional.inmobiliaria.interfaz.ContratoInterface;
import com.PracticaProfesional.inmobiliaria.repository.ContratoInterfaceRepo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ContratoServicios implements ContratoInterface {

    @Autowired
    private ContratoInterfaceRepo repo;

    @Override
    public Contrato guardar(Contrato contrato) {
        contrato.setActivo(true);
        return repo.save(contrato);
    }

    @Override
    public void eliminar(Integer id) {
        Contrato contrato = obtener(id).get();
        contrato.setActivo(false);
        repo.save(contrato);
    }

    @Override
    public Optional<Contrato> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Contrato> listar() {
        return repo.findAll();
    }

    public List<Contrato> listarFiltrados(boolean activo, EnumEstadoContrato estado, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer cliente, EnumTipoContrato tipoContrato) {
        return repo.filtrarContratos(activo, estado, fechaDesde, fechaHasta, cliente, tipoContrato);
    }
}
