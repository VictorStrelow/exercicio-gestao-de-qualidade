package org.example.service.acaocorretiva;

import org.example.model.AcaoCorretiva;
import org.example.model.Falha;
import org.example.repository.acaocorretiva.AcaoCorretivaRepository;
import org.example.repository.acaocorretiva.AcaoCorretivaRepositoryImpl;
import org.example.repository.equipamento.EquipamentoRepository;
import org.example.repository.equipamento.EquipamentoRepositoryImpl;
import org.example.repository.falha.FalhaRepository;
import org.example.repository.falha.FalhaRepositoryImpl;

import java.sql.SQLException;

public class AcaoCorretivaServiceImpl implements AcaoCorretivaService {

    private AcaoCorretivaRepository repository;
    private FalhaRepository falhaRepository;
    private EquipamentoRepository equipamentoRepository;

    public AcaoCorretivaServiceImpl() {
        this.repository = new AcaoCorretivaRepositoryImpl();
        this.falhaRepository = new FalhaRepositoryImpl();
        this.equipamentoRepository = new EquipamentoRepositoryImpl();
    }

    @Override
    public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acao) throws SQLException {
        AcaoCorretiva acaoSalva;

        try {
            acaoSalva = repository.register(acao);

        } catch (SQLException e) {
            throw new RuntimeException("Falha não encontrada!");
        }

        long falhaId = acaoSalva.getFalhaId();
        String novoStatus = "RESOLVIDA";

        falhaRepository.updateStatus(falhaId, "RESOLVIDA");

        Falha falha;
        try {
            falha = falhaRepository.findById(falhaId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (falha != null && "CRITICA".equals(falha.getCriticidade())) {
            long equipamentoId = falha.getEquipamentoId();

            equipamentoRepository.updateStatus(equipamentoId, "OPERACIONAL");
        }

        return acaoSalva;
    }

}