package org.example.repository.falha;

import org.example.database.Conexao;
import org.example.model.Falha;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FalhaRepositoryImpl implements FalhaRepository {

    private Falha listaFalha(ResultSet rs) throws SQLException {
        Falha falha = new Falha(
                rs.getLong("equipamentoId"),
                rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime(),
                rs.getString("descricao"),
                rs.getString("criticidade"),
                rs.getString("status"),
                rs.getBigDecimal("tempoParadaHoras")
        );

        falha.setId(rs.getLong("id"));

        return falha;
    }

    @Override
    public Falha save(Falha falha) throws SQLException {
        String query = """
                INSERT INTO
                Falha
                (equipamentoId, descricao, criticidade, dataHoraOcorrencia, tempoParadaHoras, status)
                VALUES
                (?, ?, ?, ?, ?, ?)
               """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, falha.getEquipamentoId());
            stmt.setString(2, falha.getDescricao());
            stmt.setString(3, falha.getCriticidade());
            stmt.setTimestamp(4, Timestamp.valueOf(falha.getDataHoraOcorrencia()));
            stmt.setBigDecimal(5, falha.getTempoParadaHoras());
            stmt.setString(6, falha.getStatus());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                falha.setId(rs.getLong(1));
            }

        }

        return falha;
    }

    @Override
    public List<Falha> findByFailure( ) throws SQLException {
        List<Falha> falhas = new ArrayList<>();

        String query = """
                SELECT * FROM
                Falha
                WHERE criticidade = 'CRITICA'
                AND status = 'ABERTA'
                """;

        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs  = stmt.executeQuery()) {


            while (rs.next()) {
                falhas.add(listaFalha(rs));
            }
        }

        return falhas;
    }

    @Override
    public Falha updateStatus(long falhaId, String novoStatus) throws SQLException {
        String query = """
                UPDATE
                Falha
                SET status = ?
                WHERE id = ?
                """;

        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, novoStatus);
            stmt.setLong(2, falhaId);
            stmt.executeUpdate();
        }
        return null;
    }

    @Override
    public Falha findById(long id) throws SQLException {
        String query = """
                SELECT * FROM Falha
                WHERE id = ?
                """;

        try (Connection conn = org.example.database.Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return listaFalha(rs);
                }
            }
        }

        return null;
    }

}