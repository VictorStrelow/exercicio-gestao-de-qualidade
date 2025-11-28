package org.example.service.equipamento;

import org.example.model.Equipamento;
import org.example.repository.equipamento.EquipamentoRepository;
import org.example.repository.equipamento.EquipamentoRepositoryImpl;

import java.sql.SQLException;

public class EquipamentoServiceImpl implements EquipamentoService {

    private EquipamentoRepository repository;

    public EquipamentoServiceImpl() {
        this.repository = new EquipamentoRepositoryImpl();
    }

    @Override
    public Equipamento criarEquipamento(Equipamento equipamento) throws SQLException {
        equipamento.setStatusOperacional("OPERACIONAL");

        return repository.save(equipamento);
    }

    @Override
    public Equipamento buscarEquipamentoPorId(Long id) throws RuntimeException {
        Equipamento equipamento;

        try {
            equipamento = repository.findById(id);

        } catch (SQLException e) {
            throw new RuntimeException("Equipamento não encontrado!" + e.getMessage());
        }

        if (equipamento == null) {
            throw new RuntimeException("Equipamento não encontrado!");
        }

        return equipamento;
    }

}