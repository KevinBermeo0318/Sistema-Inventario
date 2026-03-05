# Sistema de Inventario - Documentación del Código

## Descripción General

Este proyecto es un **Sistema de Inventario desarrollado en Java** que
permite gestionar productos, categorías y movimientos de inventario. El
sistema está organizado usando **arquitectura por capas**, separando
claramente:

-   Modelos (entidades del sistema)
-   DAO (acceso a datos)
-   Configuración de base de datos
-   Interfaz de usuario

Esto permite que el sistema sea **más fácil de mantener, escalar y
comprender**.

------------------------------------------------------------------------

# Arquitectura del Proyecto

El proyecto sigue la siguiente estructura:

    src
    │
    ├── dao
    │   ├── ProductoDAO.java
    │   ├── CategoriaDAO.java
    │   └── MovimientoDAO.java
    │
    ├── models
    │   ├── Producto.java
    │   ├── Categoria.java
    │   ├── Movimiento.java
    │   └── AlertaStock.java
    │
    ├── database
    │   └── DatabaseConfig.java
    │
    ├── ui
    │   ├── MainWindow.java
    │   ├── ProductosWindow.java
    │   ├── CategoriasWindow.java
    │   ├── MovimientosWindow.java
    │   ├── AlertasWindow.java
    │   └── ReportesWindow.java
    │
    └── Main.java

Cada carpeta tiene una responsabilidad específica.

------------------------------------------------------------------------

# 1. Main.java

Esta clase es el **punto de entrada del sistema**.

## Función principal

    public static void main(String[] args)

Responsabilidades:

-   Iniciar la aplicación.
-   Crear las instancias de los DAO.
-   Inicializar el sistema.
-   Mostrar el menú o interfaz principal.

También gestiona el **flujo principal del programa**.

### Flujo del sistema

1.  Se inicia el programa.
2.  Se cargan las conexiones a la base de datos.
3.  Se abre la ventana principal.
4.  El usuario interactúa con el sistema.
5.  Las acciones se procesan mediante los DAO.

------------------------------------------------------------------------

# 2. Configuración de la Base de Datos

## DatabaseConfig.java

Esta clase es responsable de **gestionar la conexión con la base de
datos MySQL**.

### Variables principales

    private static final String URL = "jdbc:mysql://localhost:3306/inventario_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

Explicación:

-   **URL**: dirección del servidor de base de datos.
-   **inventario_db**: nombre de la base de datos.
-   **USER**: usuario de MySQL.
-   **PASSWORD**: contraseña.

### Método principal

    public static Connection getConnection()

Este método:

1.  Crea una conexión con MySQL.
2.  Retorna un objeto `Connection`.
3.  Permite que los DAO ejecuten consultas SQL.

------------------------------------------------------------------------

# 3. Modelos del Sistema

Los modelos representan **las entidades del sistema**.

Se encuentran en la carpeta:

    src/models

------------------------------------------------------------------------

## Producto.java

Representa un producto dentro del inventario.

### Atributos

-   id
-   nombre
-   precio
-   stock
-   categoria

### Responsabilidad

-   Almacenar información de productos.
-   Transportar datos entre DAO y UI.

------------------------------------------------------------------------

## Categoria.java

Representa una categoría de productos.

### Atributos

-   id
-   nombre
-   descripcion

### Función

Permite organizar los productos por tipo.

Ejemplo:

  id   nombre
  ---- -------------
  1    Electrónica
  2    Ropa
  3    Alimentos

------------------------------------------------------------------------

## Movimiento.java

Representa un movimiento del inventario.

### Atributos

-   producto
-   cantidad
-   tipoMovimiento
-   fecha
-   usuario

### TipoMovimiento (Enum)

Define el tipo de movimiento:

-   ENTRADA → aumenta el stock
-   SALIDA → reduce el stock

Esto permite llevar **control del inventario**.

------------------------------------------------------------------------

## AlertaStock.java

Se utiliza para detectar problemas de inventario.

Tipos de alerta:

-   STOCK_BAJO
-   STOCK_CRITICO

Esto permite generar advertencias cuando un producto está por agotarse.

------------------------------------------------------------------------

# 4. DAO (Data Access Object)

Los DAO se encargan de **toda la interacción con la base de datos**.

Se encuentran en:

    src/dao

------------------------------------------------------------------------

# ProductoDAO.java

Gestiona todas las operaciones relacionadas con los productos.

### obtenerProductos()

Consulta todos los productos de la base de datos.

SQL utilizado:

    SELECT * FROM productos

------------------------------------------------------------------------

### insertarProducto()

Agrega un nuevo producto.

SQL típico:

    INSERT INTO productos (nombre, precio, stock, categoria_id)
    VALUES (?, ?, ?, ?)

------------------------------------------------------------------------

### actualizarProducto()

Modifica la información de un producto existente.

Se utiliza para:

-   cambiar precio
-   actualizar stock
-   modificar nombre

------------------------------------------------------------------------

### eliminarProducto()

Elimina un producto de la base de datos.

------------------------------------------------------------------------

# CategoriaDAO.java

Gestiona las categorías.

Funciones:

-   obtenerCategorias()
-   insertarCategoria()
-   eliminarCategoria()

Permite mantener organizada la clasificación de productos.

------------------------------------------------------------------------

# MovimientoDAO.java

Gestiona los movimientos del inventario.

------------------------------------------------------------------------

### registrarMovimiento()

Registra una entrada o salida de inventario.

Ejemplo:

-   ENTRADA → cuando llegan productos.
-   SALIDA → cuando se venden productos.

------------------------------------------------------------------------

### obtenerMovimientos()

Devuelve el historial completo de movimientos.

Esto permite realizar auditorías y reportes.

------------------------------------------------------------------------

# 5. Base de Datos

El sistema utiliza **MySQL**.

La estructura se encuentra en:

    sql/base.sql

------------------------------------------------------------------------

## Tabla productos

Almacena todos los productos.

Campos típicos:

-   id_producto
-   nombre
-   precio
-   stock
-   categoria_id

------------------------------------------------------------------------

## Tabla categorias

Almacena las categorías de productos.

Campos:

-   id_categoria
-   nombre
-   descripcion

------------------------------------------------------------------------

## Tabla movimientos

Registra todas las operaciones del inventario.

Campos:

-   id_movimiento
-   producto_id
-   tipo_movimiento
-   cantidad
-   fecha
-   usuario

------------------------------------------------------------------------

# Flujo Completo del Sistema

    Usuario
       │
       ▼
    Interfaz gráfica (UI)
       │
       ▼
    DAO
       │
       ▼
    DatabaseConfig
       │
       ▼
    MySQL Database

Esto significa:

1.  El usuario realiza una acción en la interfaz.
2.  La interfaz llama a un DAO.
3.  El DAO ejecuta una consulta SQL.
4.  La base de datos devuelve el resultado.

------------------------------------------------------------------------

# Conceptos de Programación Aplicados

Este proyecto implementa:

-   Programación Orientada a Objetos
-   Arquitectura por capas
-   Patrón DAO
-   JDBC
-   Modelado de base de datos
-   Separación de responsabilidades

------------------------------------------------------------------------

# Autor

Kevin Rico Bermeo

