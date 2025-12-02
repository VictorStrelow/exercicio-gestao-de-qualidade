package org.example.repository.relatorioservice;

import org.example.database.Conexao;
import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.AcaoCorretiva;
import org.example.model.Equipamento;
import org.example.model.Falha;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelatorioRepositoryImpl implements RelatorioRepository{

    private FalhaDetalhadaDTO mapFalhaDetalhada(ResultSet rs) throws SQLException {
        if (!rs.next()) return null;

        Equipamento equipamento = new Equipamento(
                rs.getString("nome_equip"),
                rs.getString("numeroDeSerie"),
                rs.getString("areaSetor"),
                rs.getString("statusOperacional")
        );

        equipamento.setId(rs.getLong("equipamentoId"));

        Falha falha = new Falha();
        falha.setId(rs.getLong("falhaId"));
        falha.setDescricao(rs.getString("descricao_falha"));
        falha.setCriticidade(rs.getString("criticidade"));
        falha.setStatus(rs.getString("status"));

        AcaoCorretiva acao = null;

        if (rs.getLong("acaoId") != 0) {
            acao = new AcaoCorretiva(
                    falha.getId(),
                    rs.getTimestamp("dataHoraInicio").toLocalDateTime(),
                    rs.getTimestamp("dataHoraFim").toLocalDateTime(),
                    rs.getString("responsavel"),
                    rs.getString("descricaoAcao")
            );

            acao.setId(rs.getLong("acaoId"));
        }

        return new FalhaDetalhadaDTO(falha, equipamento, (List<String>) acao);
    }

    @Override
    public List<RelatorioParadaDTO> calcularTotalTempoParada() throws SQLException {
        List<RelatorioParadaDTO> lista = new ArrayList<>();

        String query = """
                SELECT
                    E.nome AS nomeEquipamento,
                    SUM(F.tempoParadaHoras) AS TotalHorasParadas
                FROM Equipamento E
                JOIN Falha F ON E.id = F.equipamentoId
                GROUP BY E.id, E.nome
                HAVING SUM(F.tempoParadaHoras) IS NOT NULL;
                """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new RelatorioParadaDTO(
                        rs.getString("nomeEquipamento"),
                        rs.getDouble("TotalHorasParadas")
                ));
            }
        }
        return lista;
    }

    @Override
    public List<Equipamento> findEquipamentosSemFalhasNoPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        List<Equipamento> lista = new ArrayList<>();

        String query = """
                SELECT E.*
                FROM Equipamento E
                LEFT JOIN Falha F 
                    ON E.id = F.equipamentoId 
                    AND F.dataHoraOcorrencia BETWEEN ? AND ?
                WHERE F.id IS NULL;
                """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim.plusDays(1)));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Equipamento equipamento = new Equipamento(
                            rs.getString("nome"),
                            rs.getString("numeroDeSerie"),
                            rs.getString("areaSetor"),
                            rs.getString("statusOperacional")
                    );
                    equipamento.setId(rs.getLong("id"));
                    lista.add(equipamento);
                }
            }
        }
        return lista;
    }

    @Override
    public Optional<FalhaDetalhadaDTO> findDetalhesCompletosFalha(long falhaId) throws SQLException {

        String query = """
                SELECT 
                    F.id AS falhaId, F.descricao AS descricao_falha, F.criticidade, F.status, F.equipamentoId, 
                    E.nome AS nome_equip, E.numeroDeSerie, E.areaSetor, E.statusOperacional,
                    A.id AS acaoId, A.dataHoraInicio, A.dataHoraFim, A.responsavel, A.descricaoAcao
                FROM Falha F
                JOIN Equipamento E ON F.equipamentoId = E.id
                LEFT JOIN AcaoCorretiva A ON F.id = A.falhaId
                WHERE F.id = ?;
                """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, falhaId);

            try (ResultSet rs = stmt.executeQuery()) {
                FalhaDetalhadaDTO dto = mapFalhaDetalhada(rs);
                return Optional.ofNullable(dto);
            }
        }
    }

    @Override
    public List<EquipamentoContagemFalhasDTO> findEquipamentosComAltaFrequenciaDeFalhas(int contagemMinima) throws SQLException {
        List<EquipamentoContagemFalhasDTO> lista = new ArrayList<>();

        String query = """
                SELECT
                    E.nome AS nomeEquipamento,
                    COUNT(F.id) AS TotalFalhas
                FROM Equipamento E
                JOIN Falha F ON E.id = F.equipamentoId
                GROUP BY E.id, E.nome
                HAVING COUNT(F.id) >= ?;
                """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, contagemMinima);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new EquipamentoContagemFalhasDTO(
                            rs.getString("nomeEquipamento"),
                            rs.getInt("TotalFalhas")
                    ));
                }
            }
        }
        return lista;
    }

}