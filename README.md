# Actividad Sumativa - Semana 8: Gestionando datos mediante operaciones CRUD

## SpeedFast2

## Descripción
Este proyecto modela una empresa de delivery llamada SpeedFast. Permite registrar pedidos de 3 tipos (comida, encomienda y express) y sus respectivos repartidores, asignar un repartidor a un pedido para iniciar su entrega y consultar, editar o eliminar los registros desde una interfaz gráfica hecha con Java Swing.
Los datos se guardan en una base de datos MySQL a través de JDBC, usando el patrón DAO (interfaces CRUD). 

## Estructura del Proyecto
```
src/main/java/
|__ app/
|   |__ Main.java
|__ model/
|   |__ EstadoPedido.java
|   |__ TipoPedido.java
|   |__ Pedido.java
|   |__ PedidoComida.java
|   |__ PedidoEncomienda.java
|   |__ PedidoExpress.java
|   |__ Repartidor.java
|   |__ Entrega.java
|__ interfaces/
|   |__ Cancelable.java
|   |__ Despachable.java
|   |__ Rastreable.java
|__ dao/
|   |__ ConexionDB.java
|   |__ PedidoDAO.java
|   |__ RepartidorDAO.java
|   |__ EntregaDAO.java
|   |__ impl/
|       |__ PedidoDAOImpl.java
|       |__ RepartidorDAOImpl.java
|       |__ EntregaDAOImpl.java
|__ vista/
|   |__ VentanaPrincipal.java
|   |__ VentanaRegistroPedido.java
|   |__ VentanaListaPedidos.java
|   |__ VentanaRegistroRepartidor.java
|   |__ VentanaListaRepartidores.java
|   |__ VentanaListaEntregas.java
```

## Base de datos
El proyecto requiere una base de datos MySQL llamada speedfast_db. Ejecutar el siguiente script en MySQL Workbench antes de iniciar la aplicación: 

```
CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

CREATE TABLE repartidor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(100) NOT NULL,
    tipo ENUM('COMIDA','ENCOMIENDA','EXPRESS'),
    estado ENUM('PENDIENTE','EN_REPARTO','ENTREGADO')
);

CREATE TABLE entrega (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_repartidor INT,
    fecha DATE,
    hora TIME,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    FOREIGN KEY (id_repartidor) REFERENCES repartidor(id)
);
```

## Instrucciones para compilar y ejecutar

1. Crear la base de datos ejecutando el script descrito previamente en MySQL Workbench
2. Abrir el proyecto en el IDE (IntelliJ IDEA) y configurar la conexión con la base de datos
3. Verificar que la clase principal sea `app.Main`
4. Ejecutar con el botón "Run"

## Autor del Proyecto

**Nombre completo** | Eduardo Soto Estrada |
**Carrera** | Desarrollo de Aplicaciones |
**Sede** | Online |
**Fecha de entrega** | 04/10/2026 |
