package controlador;

import dao.EstudianteDAO;
import dao.UsuarioDAO;
import dao.impl.EstudianteDAOImpl;
import dao.impl.UsuarioDAOImpl;
import model.Estudiante;
import model.Resultado;
import model.Usuario;
import java.sql.SQLException;


public class LoginControl {

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final EstudianteDAO estudianteDAO = new EstudianteDAOImpl();

    private Usuario usuarioActual;
    private Estudiante estudianteActual;

    public Resultado iniciarSesion(String correo, String contrasena) {
        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.isEmpty()) {
            return Resultado.error("Ingresa tu correo y tu contraseña.");
        }
        try {
            Usuario usuario = usuarioDAO.buscarPorCorreo(correo.trim());
            if (usuario == null || !contrasena.equals(usuario.getContrasena())) {
                return Resultado.error("Correo o contraseña incorrectos.");
            }

            Estudiante estudiante = null;
            if (usuario.esEstudiante()) {
                estudiante = estudianteDAO.buscarPorRut(usuario.getRut());
                if (estudiante == null) {
                    return Resultado.error("Tu usuario no tiene una ficha de estudiante. Avisa al bibliotecario.");
                }
            }

            usuarioActual = usuario;
            estudianteActual = estudiante;
            return Resultado.ok("Bienvenido(a), " + usuario.getNombre() + ".");
        } catch (SQLException e) {
            return Resultado.error("No se pudo conectar a la base de datos: " + e.getMessage());
        }
    }

    public void cerrarSesion() {
        usuarioActual = null;
        estudianteActual = null;
    }

    public Usuario getUsuarioActual() { return usuarioActual; }
    public Estudiante getEstudianteActual() { return estudianteActual; }
}