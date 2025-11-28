package org.example.service.falha;

import org.example.model.Equipamento;
import org.example.model.Falha;
import org.example.repository.equipamento.EquipamentoRepositoryImpl;
import org.example.repository.falha.FalhaRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class FalhaServiceImpl implements FalhaService {

    private FalhaRepositoryImpl falhaRepository;
    private EquipamentoRepositoryImpl equipamentoRepository;

    public FalhaServiceImpl() {
        this.falhaRepository = new FalhaRepositoryImpl();
        this.equipamentoRepository = new EquipamentoRepositoryImpl();
    }

    @Override
    public Falha registrarNovaFalha(Falha falha) throws SQLException {

        try {
            Long equipamentoId = falha.getEquipamentoId();
            Equipamento equipamento = equipamentoRepository.findById(equipamentoId);

            if (equipamento == null) {
                throw new IllegalArgumentException("Equipamento não encontrado!");
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }

        falha.setStatus("ABERTA");

        Falha falhaSalva = falhaRepository.save(falha);

        if ("CRITICA".equals(falha.getCriticidade())) {
            long id = falhaSalva.getEquipamentoId();
            String novoStatus = "EM_MANUTENCAO";

            equipamentoRepository.updateStatus(id, novoStatus);
        }

        return falhaSalva;
    }

    @Override
    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {
        Falha falha;

        try {
            return falhaRepository.findByFailure();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar falhas críticas abertas.", e);
        }
    }

}