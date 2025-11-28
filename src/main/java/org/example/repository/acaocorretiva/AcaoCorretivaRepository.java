package org.example.repository.acaocorretiva;

import org.example.model.AcaoCorretiva;

import java.sql.SQLException;

public interface AcaoCorretivaRepository {

    AcaoCorretiva register(AcaoCorretiva acaoCorretiva) throws SQLException;

}