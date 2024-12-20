package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import com.PracticaProfesional.inmobiliaria.interfaz.TransaccionInterface;
import com.PracticaProfesional.inmobiliaria.repository.TransaccionInterfaceRepo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TransaccionServicios implements TransaccionInterface {

    @Autowired
    private TransaccionInterfaceRepo repo;

    @Override
    public Transaccion guardar(Transaccion transaccion) {
        transaccion.setActivo(true);
        return repo.save(transaccion);
    }

    @Override
    public void eliminar(Integer id) {
        Transaccion transaccion = obtener(id).get();
        transaccion.setActivo(false);
        repo.save(transaccion);
    }

    @Override
    public Optional<Transaccion> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Transaccion> listar() {
        Sort sort = Sort.by("fechaTransaccion").descending();
        return repo.findAll(sort);
    }

    public List<Transaccion> listarFiltrado(boolean estado, LocalDateTime fechaDesde, LocalDateTime fechaHasta, String TipoOperacion, String TipoTransaccion) {
        return repo.listarFiltrado(estado, fechaDesde, fechaHasta, TipoTransaccion, TipoOperacion);
    }

}
