package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.repository.ClienteInterfaceRepo;
import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoCliente;
import com.PracticaProfesional.inmobiliaria.interfaz.ClienteInterface;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Sofia
 */
@Service
public class ClienteServicios implements ClienteInterface {

    @Autowired
    private ClienteInterfaceRepo repo;

    @Override
    public Cliente guardar(Cliente cliente) {
        cliente.setFechaRegistro(new Date());
        cliente.setActivo(true);
        return repo.save(cliente);
    }

    @Override
    public void eliminar(Integer id) {
        Cliente cliente = obtener(id).get();
        cliente.setActivo(false);
        repo.save(cliente);
    }

    @Override
    public Optional<Cliente> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Cliente> listar() {
        return repo.findAll();
    }

    public List<Cliente> listarPorTipoCliente(String tipo) {
        return repo.findByTipoCliente(EnumTipoCliente.valueOf(tipo.toUpperCase()));
    }
}
