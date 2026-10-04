package dao;

import model.Repartidor;
import java.util.List;

public interface RepartidorDAO {
    boolean create(Repartidor repartidor);
    List<Repartidor> readAll();
    boolean update(Repartidor repartidor);
    boolean delete(int id);
}