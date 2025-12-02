package org.example.repository.relatorioservice;

import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RelatorioRepository {

    List<RelatorioParadaDTO> calcularTotalTempoParada() throws SQLException;

    List<Equipamento> findEquipamentosSemFalhasNoPeriodo(LocalDate inicio, LocalDate fim) throws SQLException;

    Optional<FalhaDetalhadaDTO> findDetalhesCompletosFalha(long falhaId) throws SQLException;

    List<EquipamentoContagemFalhasDTO> findEquipamentosComAltaFrequenciaDeFalhas(int contagemMinima) throws SQLException;

}