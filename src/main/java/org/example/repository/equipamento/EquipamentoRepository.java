package org.example.repository.equipamento;

import org.example.model.Equipamento;

import java.sql.SQLException;

public interface EquipamentoRepository {

    Equipamento save(Equipamento equipamento) throws SQLException;

    Equipamento findById(long id) throws SQLException, RuntimeException;

}