package dao;

import model.Entrega;
import java.util.List;

public interface EntregaDAO {
    boolean create(Entrega entrega);
    List<Entrega> readAll();
    boolean update(Entrega entrega);
    boolean delete(int id);
}