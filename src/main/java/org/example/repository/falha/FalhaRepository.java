package org.example.repository.falha;

import org.example.model.Falha;

import java.sql.SQLException;
import java.util.List;

public interface FalhaRepository {

    Falha save(Falha falha) throws SQLException;

    List<Falha> findByFailure() throws SQLException;
}