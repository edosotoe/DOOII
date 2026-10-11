package controlador;

import dao.EstudianteDAO;
import dao.UsuarioDAO;
import dao.impl.EstudianteDAOImpl;
import dao.impl.UsuarioDAOImpl;
import model.Estudiante;
import model.Resultado;
import model.Usuario;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteControl {

    public static final String CLAVE_INICIAL = "clave123";
    private static final String SIN_PERMISO = "Solo el bibliotecario puede gestionar estudiantes.";

    private final EstudianteDAO estudianteDAO = new EstudianteDAOImpl();
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final Usuario usuario;

    public EstudianteControl(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Estudiante> listar() {
        try {
            return estudianteDAO.listar();
        } catch (SQLException e) {
            System.err.println("Error al listar estudiantes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void cargarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Estudiante estudiante : listar()) {
            modelo.addRow(new Object[]{
                    estudiante.getId(),
                    estudiante.getNombre(),
                    estudiante.getRut(),
                    estudiante.getCurso(),
                    estudiante.getCorreo()
            });
        }
    }

    public Resultado registrar(String nombre, String rut, String curso, String correo) {
        if (!usuario.esBibliotecario()) {
            return Resultado.error(SIN_PERMISO);
        }
        String error = validar(nombre, rut, curso, correo);
        if (error != null) {
            return Resultado.error(error);
        }
        Estudiante estudiante = new Estudiante(0, nombre.trim(), rut.trim(), curso.trim(), correo.trim());


        try {
            estudianteDAO.insertar(estudiante);
        } catch (SQLIntegrityConstraintViolationException e) {
            return Resultado.error("Ya existe un estudiante con el RUT " + rut.trim() + ".");
        } catch (SQLException e) {
            return Resultado.error("Error al registrar el estudiante: " + e.getMessage());
        }

        try {
            if (usuarioDAO.buscarPorRut(estudiante.getRut()) != null) {
                return Resultado.ok("Estudiante registrado. Ya tenía una cuenta de usuario con ese RUT.");
            }
            usuarioDAO.insertar(new Usuario(0, estudiante.getNombre(), estudiante.getRut(),
                    estudiante.getCorreo(), CLAVE_INICIAL, Usuario.ROL_ESTUDIANTE));
            return Resultado.ok("Estudiante registrado. Ingresa con su correo y la clave inicial " + CLAVE_INICIAL + ".");
        } catch (SQLException e) {

            try {
                estudianteDAO.eliminar(estudiante.getId());
            } catch (SQLException ignorada) {
                System.err.println("No se pudo deshacer el registro: " + ignorada.getMessage());
            }
            return Resultado.error("No se pudo crear la cuenta del estudiante: " + e.getMessage());
        }
    }

    public Resultado modificar(int id, String nombre, String rut, String curso, String correo) {
        if (!usuario.esBibliotecario()) {
            return Resultado.error(SIN_PERMISO);
        }
        if (id <= 0) {
            return Resultado.error("Selecciona un estudiante de la tabla.");
        }
        String error = validar(nombre, rut, curso, correo);
        if (error != null) {
            return Resultado.error(error);
        }
        try {
            Estudiante anterior = estudianteDAO.buscarPorId(id);
            if (anterior == null) {
                return Resultado.error("El estudiante ya no existe.");
            }
            Estudiante nuevo = new Estudiante(id, nombre.trim(), rut.trim(), curso.trim(), correo.trim());
            estudianteDAO.actualizar(nuevo);

            Usuario cuenta = usuarioDAO.buscarPorRut(anterior.getRut());
            if (cuenta != null) {
                cuenta.setNombre(nuevo.getNombre());
                cuenta.setRut(nuevo.getRut());
                cuenta.setCorreo(nuevo.getCorreo());
                usuarioDAO.actualizar(cuenta);
            }
            return Resultado.ok("Estudiante modificado.");
        } catch (SQLIntegrityConstraintViolationException e) {
            return Resultado.error("Ya existe otra persona con el RUT " + rut.trim() + ".");
        } catch (SQLException e) {
            return Resultado.error("Error al modificar el estudiante: " + e.getMessage());
        }
    }

    public Resultado eliminar(int id) {
        if (!usuario.esBibliotecario()) {
            return Resultado.error(SIN_PERMISO);
        }
        if (id <= 0) {
            return Resultado.error("Selecciona un estudiante de la tabla.");
        }
        try {
            Estudiante estudiante = estudianteDAO.buscarPorId(id);
            if (estudiante == null) {
                return Resultado.error("El estudiante ya no existe.");
            }
            estudianteDAO.eliminar(id);

            Usuario cuenta = usuarioDAO.buscarPorRut(estudiante.getRut());
            if (cuenta != null && cuenta.esEstudiante()) {
                usuarioDAO.eliminar(cuenta.getId());
            }
            return Resultado.ok("Estudiante eliminado junto con su cuenta de usuario.");
        } catch (SQLIntegrityConstraintViolationException e) {
            return Resultado.error("No se puede eliminar: el estudiante tiene préstamos registrados.");
        } catch (SQLException e) {
            return Resultado.error("Error al eliminar el estudiante: " + e.getMessage());
        }
    }

    private String validar(String nombre, String rut, String curso, String correo) {
        if (estaVacio(nombre) || estaVacio(rut) || estaVacio(curso) || estaVacio(correo)) {
            return "Nombre, RUT, curso y correo son obligatorios.";
        }
        String rutLimpio = rut.trim();
        if (!rutLimpio.contains("-") || rutLimpio.contains(".")
                || rutLimpio.length() < 9 || rutLimpio.length() > 10) {
            return "El RUT debe tener el formato 12345678-9, sin puntos.";
        }
        if (!correo.contains("@")) {
            return "El correo no es válido.";
        }
        return null;
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}