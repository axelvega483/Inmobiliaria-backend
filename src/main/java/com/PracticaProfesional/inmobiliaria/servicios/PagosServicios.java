package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import com.PracticaProfesional.inmobiliaria.interfaz.PagosInterface;
import com.PracticaProfesional.inmobiliaria.repository.PagosInterfaceRepo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PagosServicios implements PagosInterface {

    @Autowired
    private PagosInterfaceRepo repo;

    @Override
    @Transactional
    public Pago guardar(Pago pago) {
        pago.setActivo(true);
        return repo.save(pago);
    }

    @Override
    public void eliminar(Integer id) {
        Pago pago = obtener(id).get();
        pago.setActivo(false);
        repo.save(pago);
    }

    @Override
    public Optional<Pago> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Pago> listar() {
        Sort sort = Sort.by("fechaPago").ascending();
        return repo.findAll(sort);
    }

    public List<Pago> listarFiltro(LocalDateTime fechaDesde, LocalDateTime fechaHasta, String estado, Integer agenteId) {
        return repo.listarFiltro(fechaDesde, fechaHasta, estado, agenteId);
    }

    public Object listarFiltroFechaRegistro(LocalDateTime fechaDesde, LocalDateTime fechaHasta, String estado, Integer agenteId) {
        return repo.listarFiltroFechaRegistro(fechaDesde, fechaHasta, estado, agenteId);
    }

}
