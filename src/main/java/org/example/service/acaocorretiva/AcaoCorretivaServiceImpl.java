package org.example.service.acaocorretiva;

import org.example.model.AcaoCorretiva;
import org.example.repository.acaocorretiva.AcaoCorretivaRepository;
import org.example.repository.acaocorretiva.AcaoCorretivaRepositoryImpl;

import java.sql.SQLException;

public class AcaoCorretivaServiceImpl implements AcaoCorretivaService {

    private AcaoCorretivaRepository repository;

    public AcaoCorretivaServiceImpl() {
        this.repository = new AcaoCorretivaRepositoryImpl();
    }

    @Override
    public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acao) throws SQLException {
        return repository.register(acao);
    }

}