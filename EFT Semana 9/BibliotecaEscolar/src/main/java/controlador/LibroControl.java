package controlador;

import dao.CategoriaDAO;
import dao.LibroDAO;
import dao.impl.CategoriaDAOImpl;
import dao.impl.LibroDAOImpl;
import model.Categoria;
import model.Libro;
import model.Resultado;
import model.Usuario;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class LibroControl {

    private static final String SIN_PERMISO = "Solo el bibliotecario puede modificar el inventario.";

    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
    private final Usuario usuario; // quien inició sesión

    public LibroControl(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Libro> listar() {
        try {
            return libroDAO.listar();
        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Libro> buscar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listar();
        }
        try {
            return libroDAO.buscar(texto);
        } catch (SQLException e) {
            System.err.println("Error al buscar libros: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void cargarTabla(DefaultTableModel modelo) {
        cargarTabla(modelo, "");
    }

    public void cargarTabla(DefaultTableModel modelo, String texto) {
        modelo.setRowCount(0); // limpia la tabla
        for (Libro libro : buscar(texto)) {
            modelo.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getIsbn(),
                    libro.getEditorial(),
                    libro.getStock(),
                    libro.getNombreCategoria()
            });
        }
    }

    public List<Categoria> listarCategorias() {
        try {
            return categoriaDAO.listar();
        } catch (SQLException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
            return new ArrayList<>();
        }
    }

// Habilidades unicas del bibliotecario

    public Resultado registrar(String titulo, String autor, String isbn, String editorial,
                               int stock, Categoria categoria) {
        if (!usuario.esBibliotecario()) {
            return Resultado.error(SIN_PERMISO);
        }
        String error = validar(titulo, autor, isbn, editorial, stock, categoria);
        if (error != null) {
            return Resultado.error(error);
        }
        Libro libro = new Libro(0, titulo.trim(), autor.trim(), isbn.trim(), editorial.trim(),
                stock, categoria.getId());
        try {
            libroDAO.insertar(libro);
            return Resultado.ok("Libro registrado con el id " + libro.getId() + ".");
        } catch (SQLIntegrityConstraintViolationException e) {
            return Resultado.error("Ya existe un libro con el ISBN " + isbn.trim() + ".");
        } catch (SQLException e) {
            return Resultado.error("Error al registrar el libro: " + e.getMessage());
        }
    }

    public Resultado modificar(int id, String titulo, String autor, String isbn, String editorial,
                               int stock, Categoria categoria) {
        if (!usuario.esBibliotecario()) {
            return Resultado.error(SIN_PERMISO);
        }
        if (id <= 0) {
            return Resultado.error("Selecciona un libro de la tabla.");
        }
        String error = validar(titulo, autor, isbn, editorial, stock, categoria);
        if (error != null) {
            return Resultado.error(error);
        }
        Libro libro = new Libro(id, titulo.trim(), autor.trim(), isbn.trim(), editorial.trim(),
                stock, categoria.getId());
        try {
            if (libroDAO.actualizar(libro)) {
                return Resultado.ok("Libro modificado.");
            }
            return Resultado.error("El libro ya no existe.");
        } catch (SQLIntegrityConstraintViolationException e) {
            return Resultado.error("Ya existe otro libro con el ISBN " + isbn.trim() + ".");
        } catch (SQLException e) {
            return Resultado.error("Error al modificar el libro: " + e.getMessage());
        }
    }

    public Resultado eliminar(int id) {
        if (!usuario.esBibliotecario()) {
            return Resultado.error(SIN_PERMISO);
        }
        if (id <= 0) {
            return Resultado.error("Selecciona un libro de la tabla.");
        }
        try {
            if (libroDAO.eliminar(id)) {
                return Resultado.ok("Libro eliminado.");
            }
            return Resultado.error("El libro ya no existe.");
        } catch (SQLIntegrityConstraintViolationException e) {
            // la clave foránea de prestamos impide borrar un libro con historial
            return Resultado.error("No se puede eliminar: el libro tiene préstamos registrados.");
        } catch (SQLException e) {
            return Resultado.error("Error al eliminar el libro: " + e.getMessage());
        }
    }

    private String validar(String titulo, String autor, String isbn, String editorial,
                           int stock, Categoria categoria) {
        if (estaVacio(titulo) || estaVacio(autor) || estaVacio(isbn) || estaVacio(editorial)) {
            return "Título, autor, ISBN y editorial son obligatorios.";
        }
        if (categoria == null) {
            return "Selecciona una categoría.";
        }
        if (stock < 0) {
            return "El stock no puede ser negativo.";
        }
        return null;
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}