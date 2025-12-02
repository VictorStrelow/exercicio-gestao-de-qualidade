package org.example.service.relatorioservice;

import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;
import org.example.repository.relatorioservice.RelatorioRepository;
import org.example.repository.relatorioservice.RelatorioRepositoryImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RelatorioServiceImpl implements RelatorioService{

    private final RelatorioRepository repository;

    public RelatorioServiceImpl() {
        this.repository = new RelatorioRepositoryImpl();
    }

    @Override
    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException {
        return repository.calcularTotalTempoParada();
    }

    @Override
    public List<Equipamento> buscarEquipamentosSemFalhasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws SQLException {
        if (dataInicio == null || dataFim == null || dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Período de datas inválido.");
        }
        return repository.findEquipamentosSemFalhasNoPeriodo(dataInicio, dataFim);
    }

    @Override
    public Optional<FalhaDetalhadaDTO> buscarDetalhesCompletosFalha(long falhaId) throws SQLException {
        if (falhaId <= 0) {
            throw new RuntimeException("ID da falha inválido.");
        }

        Optional<FalhaDetalhadaDTO> detalhes = repository.findDetalhesCompletosFalha(falhaId);

        return detalhes;
    }

    @Override
    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException {
        if (contagemMinimaFalhas <= 0) {
            throw new RuntimeException("Contagem mínima de falhas deve ser maior que zero.");
        }

        return repository.findEquipamentosComAltaFrequenciaDeFalhas(contagemMinimaFalhas);
    }

}