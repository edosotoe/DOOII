package dao.impl;

import dao.DatabaseConnection;
import dao.PrestamoDAO;
import model.Prestamo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAOImpl implements PrestamoDAO {

    private static final String SELECT_BASE =
            "SELECT p.id, p.id_estudiante, p.id_libro, p.fecha_prestamo, p.fecha_devolucion, p.devuelto, "
                    + "e.nombre AS nombre_estudiante, l.titulo AS titulo_libro "
                    + "FROM prestamos p "
                    + "JOIN estudiantes e ON e.id = p.id_estudiante "
                    + "JOIN libros l ON l.id = p.id_libro ";

    private Connection conexion() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Prestamo mapear(ResultSet rs) throws SQLException {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(rs.getInt("id"));
        prestamo.setIdEstudiante(rs.getInt("id_estudiante"));
        prestamo.setIdLibro(rs.getInt("id_libro"));
        prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
        prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion").toLocalDate());
        prestamo.setDevuelto(rs.getBoolean("devuelto"));
        prestamo.setNombreEstudiante(rs.getString("nombre_estudiante"));
        prestamo.setTituloLibro(rs.getString("titulo_libro"));
        return prestamo;
    }

    @Override
    public boolean registrarPrestamo(Prestamo prestamo) throws SQLException {
        return prestarSincronizado(conexion(), prestamo);
    }

    @Override
    public boolean registrarDevolucion(int idPrestamo, LocalDate fechaEntrega) throws SQLException {
        return devolverSincronizado(conexion(), idPrestamo, fechaEntrega);
    }

    private static synchronized boolean prestarSincronizado(Connection con, Prestamo prestamo)
            throws SQLException {
        con.setAutoCommit(false);
        try {
            int stock = 0;
            String sqlStock = "SELECT stock FROM libros WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlStock)) {
                ps.setInt(1, prestamo.getIdLibro());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        stock = rs.getInt("stock");
                    }
                }
            }
            if (stock <= 0) {
                con.rollback();
                return false;
            }

            String sqlPrestamo = "INSERT INTO prestamos "
                    + "(id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) "
                    + "VALUES (?, ?, ?, ?, FALSE)";
            try (PreparedStatement ps = con.prepareStatement(sqlPrestamo, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, prestamo.getIdEstudiante());
                ps.setInt(2, prestamo.getIdLibro());
                ps.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
                ps.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));
                ps.executeUpdate();
                try (ResultSet claves = ps.getGeneratedKeys()) {
                    if (claves.next()) {
                        prestamo.setId(claves.getInt(1));
                    }
                }
            }

            String sqlDescuento = "UPDATE libros SET stock = stock - 1 WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlDescuento)) {
                ps.setInt(1, prestamo.getIdLibro());
                ps.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    private static synchronized boolean devolverSincronizado(Connection con, int idPrestamo,
                                                             LocalDate fechaEntrega) throws SQLException {
        con.setAutoCommit(false);
        try {
            int idLibro = 0;
            String sqlBuscar = "SELECT id_libro FROM prestamos WHERE id = ? AND devuelto = FALSE";
            try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
                ps.setInt(1, idPrestamo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idLibro = rs.getInt("id_libro");
                    }
                }
            }
            if (idLibro == 0) {
                con.rollback();
                return false;
            }

            String sqlDevolver = "UPDATE prestamos SET devuelto = TRUE, fecha_devolucion = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlDevolver)) {
                ps.setDate(1, Date.valueOf(fechaEntrega));
                ps.setInt(2, idPrestamo);
                ps.executeUpdate();
            }

            String sqlReponer = "UPDATE libros SET stock = stock + 1 WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlReponer)) {
                ps.setInt(1, idLibro);
                ps.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }


    private List<Prestamo> consultar(String sql) throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Prestamo> listar() throws SQLException {
        return consultar(SELECT_BASE + "ORDER BY p.fecha_prestamo DESC, p.id DESC");
    }

    @Override
    public List<Prestamo> listarActivos() throws SQLException {
        return consultar(SELECT_BASE + "WHERE p.devuelto = FALSE ORDER BY p.fecha_devolucion");
    }


    @Override
    public List<Prestamo> listarPorEstudiante(int idEstudiante) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.id_estudiante = ? ORDER BY p.fecha_prestamo DESC, p.id DESC";
        List<Prestamo> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public Prestamo buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.id = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Object[]> librosMasPrestados() throws SQLException {
        String sql = "SELECT l.titulo, l.autor, COUNT(p.id) AS total "
                + "FROM prestamos p JOIN libros l ON l.id = p.id_libro "
                + "GROUP BY l.id, l.titulo, l.autor "
                + "ORDER BY total DESC, l.titulo";
        List<Object[]> filas = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new Object[]{rs.getString("titulo"), rs.getString("autor"), rs.getInt("total")});
            }
        }
        return filas;
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM prestamos WHERE id = ? AND devuelto = TRUE";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}