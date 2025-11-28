package org.example.repository.equipamento;

import org.example.database.Conexao;
import org.example.model.Equipamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoRepositoryImpl implements EquipamentoRepository {

    private Equipamento listaEquipamento(ResultSet rs) throws SQLException {
        Equipamento equipamento = new Equipamento(
                rs.getString("nome"),
                rs.getString("numeroDeSerie"),
                rs.getString("areaSetor"),
                rs.getString("statusOperacional")
        );

        equipamento.setId(rs.getLong("id"));

        return equipamento;
    }

    @Override
    public Equipamento save(Equipamento equipamento) throws SQLException {
        String query = """
                INSERT INTO
                Equipamento
                (nome, numeroDeSerie, areaSetor, statusOperacional)
                VALUES
                (?, ?, ?, ?)
                """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, equipamento.getNome());
            stmt.setString(2, equipamento.getNumeroDeSerie());
            stmt.setString(3, equipamento.getAreaSetor());
            stmt.setString(4, equipamento.getStatusOperacional());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                equipamento.setId(rs.getLong(1));
            }
        }

        return equipamento;
    }

    @Override
    public Equipamento findById(long id) throws SQLException, RuntimeException {
        List<Equipamento> equipamentos = new ArrayList<>();
        String query = """
                SELECT * FROM
                Equipamento
                WHERE id = ?
                """;

        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return listaEquipamento(rs);
            }
        }

        return null;
    }

    @Override
    public void updateStatus(long id, String novoStatus) throws SQLException {
        String query = """
                UPDATE
                Equipamento
                SET
                statusOperacional = ?
                WHERE
                id = ?
                """;

        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, novoStatus);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

}