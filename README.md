# Evaluación Final Transversal - Semana 9: Sistema de Gestión de Biblioteca Escolar

## BibliotecaEscolar

## Descripción
Este proyecto modela el sistema de gestión de una biblioteca escolar. Permite iniciar sesión con dos roles distintos (bibliotecario y estudiante), administrar el inventario de libros y el registro de estudiantes, registrar préstamos y devoluciones con cálculo de las fechas de vencimiento y detección de atrasos, y consultar reportes desde una interfaz gráfica hecha con JavaSwing.
Los datos se guardan en una base de datos MySQL a través de JDBC, usando los patrones MVC, DAO (interfaces CRUD) y Singleton para la conexión.
El préstamo y la devolución se ejecutan en un hilo independiente (Runnable y Thread) para no bloquear la interfaz, y las operaciones que modifican el stock están protegidas con métodos synchronized.

## Estructura del Proyecto
```
src/main/java/
|__ app/
|   |__ Main.java
|__ model/
|   |__ Persona.java
|   |__ Usuario.java
|   |__ Estudiante.java
|   |__ Libro.java
|   |__ Categoria.java
|   |__ Prestamo.java
|   |__ Resultado.java
|__ dao/
|   |__ DatabaseConnection.java
|   |__ UsuarioDAO.java
|   |__ EstudianteDAO.java
|   |__ LibroDAO.java
|   |__ CategoriaDAO.java
|   |__ PrestamoDAO.java
|   |__ impl/
|       |__ UsuarioDAOImpl.java
|       |__ EstudianteDAOImpl.java
|       |__ LibroDAOImpl.java
|       |__ CategoriaDAOImpl.java
|       |__ PrestamoDAOImpl.java
|__ controlador/
|   |__ LoginControl.java
|   |__ LibroControl.java
|   |__ EstudianteControl.java
|   |__ PrestamoControl.java
|   |__ ReporteControl.java
|__ vista/
|   |__ LoginVista.java
|   |__ MenuVista.java
|   |__ LibroVista.java
|   |__ EstudianteVista.java
|   |__ PrestamoVista.java
|   |__ ReporteVista.java
```

## Base de datos
El proyecto requiere una base de datos MySQL llamada biblioteca. Ejecutar el siguiente script en MySQL Workbench antes de iniciar la aplicación:

```
CREATE DATABASE IF NOT EXISTS biblioteca;
USE biblioteca;

CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    rut VARCHAR(12) UNIQUE,
    correo VARCHAR(100),
    contraseña VARCHAR(100),
    rol ENUM('bibliotecario', 'estudiante') NOT NULL
);

CREATE TABLE estudiantes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    rut VARCHAR(12) UNIQUE,
    curso VARCHAR(20),
    correo VARCHAR(100)
);

CREATE TABLE categorias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100)
);

CREATE TABLE libros (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(200),
    autor VARCHAR(100),
    isbn VARCHAR(20) UNIQUE,
    editorial VARCHAR(100),
    stock INT,
    id_categoria INT,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id)
);

CREATE TABLE prestamos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_estudiante INT,
    id_libro INT,
    fecha_prestamo DATE,
    fecha_devolucion DATE,
    devuelto BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id),
    FOREIGN KEY (id_libro) REFERENCES libros(id)
);
```

Luego, ejecutar el script `Script_poblado_tablas_biblioteca.sql`, incluido en la carpeta adjunta del proyecto, para cargar los datos de prueba (usuarios, estudiantes, categorías, libros y préstamos).

Como alternativa, el archivo `sql/biblioteca_pruebas.sql` contiene la base de datos completa (estructura y datos, incluidos los registros generados durante las pruebas de la aplicación) y puede importarse desde MySQL Workbench con Server > Data Import.

## Instrucciones para compilar y ejecutar

1. Crear la base de datos ejecutando el script descrito previamente en MySQL Workbench y luego el script de poblado (o importando `biblioteca_pruebas.sql`)
2. Abrir el proyecto en el IDE (IntelliJ IDEA) y configurar la conexión con la base de datos en la clase `dao.DatabaseConnection` (usuario y contraseña de MySQL)
3. Verificar que la clase principal sea `app.Main`
4. Ejecutar con el botón "Run"
5. Iniciar sesión con uno de los usuarios de prueba:
   - Bibliotecario: `antonia@correo.cl` / `clave123`
   - Estudiante: `carlos@correo.cl` / `clave123`

## Autor del Proyecto

**Nombre completo** | Eduardo Soto Estrada |
**Carrera** | Desarrollo de Aplicaciones |
**Sede** | Online |
**Fecha de entrega** | 11/10/2026 |
