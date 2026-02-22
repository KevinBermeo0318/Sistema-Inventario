# 🗂️ Sistema de Inventario

![Java](https://img.shields.io/badge/Java-17-blue)
![Maven](https://img.shields.io/badge/Maven-Build-orange)
![SQLite](https://img.shields.io/badge/Database-SQLite-lightgrey)
![Status](https://img.shields.io/badge/Status-Completed-success)

Sistema de Inventario desarrollado en **Java** utilizando arquitectura en capas (DAO + Model + UI) y base de datos **SQLite** para la persistencia de datos.

🔗 Repositorio:  
https://github.com/KevinBermeo0318/Sistema-Inventario

---

# 📌 Descripción General

La aplicación permite administrar productos y usuarios, así como registrar movimientos de inventario (entradas y salidas).

Está diseñada como una aplicación de escritorio utilizando **Java Swing**, aplicando buenas prácticas como:

- Separación de responsabilidades
- Patrón DAO
- Persistencia con SQLite
- Organización modular del código

---

# 🧠 Flujo del Sistema

1. El usuario inicia sesión.
2. Se valida contra la base de datos.
3. Se accede al panel principal.
4. Desde el menú principal se puede:
   - Gestionar productos
   - Gestionar usuarios
   - Registrar movimientos
5. Cada acción se comunica con la base de datos a través de los DAO.

---

# 🏗️ Arquitectura del Proyecto

```
com.inventario
│
├── dao        → Lógica de acceso a datos
├── db         → Conexión a SQLite
├── model      → Clases que representan entidades
├── ui         → Interfaz gráfica
└── Main.java  → Punto de entrada
```

---

# 📂 Detalle del Código por Módulo

---

## 🔹 1. model (Entidades del sistema)

Contiene las clases que representan las tablas de la base de datos.

### 📦 Producto.java
Representa un producto del inventario.

Atributos principales:
- id
- nombre
- descripcion
- cantidad
- precio

Responsabilidad:
- Actuar como contenedor de datos.
- Ser utilizado por DAO y UI para manipular información.

---

### 👤 Usuario.java
Representa un usuario del sistema.

Atributos:
- id
- username
- password
- rol (si está implementado)

Responsabilidad:
- Gestionar autenticación.
- Representar los usuarios almacenados en la base de datos.

---

## 🔹 2. dao (Data Access Object)

Encargado de interactuar con la base de datos.

Aquí se encuentra la lógica SQL del sistema.

---

### 📦 ProductoDAO.java

Funciones principales:

- insertarProducto()
- actualizarProducto()
- eliminarProducto()
- obtenerProductos()

Responsabilidad:
- Ejecutar consultas SQL.
- Convertir resultados en objetos Producto.
- Separar la lógica de base de datos del resto del sistema.

---

### 👤 UsuarioDAO.java

Funciones principales:

- insertarUsuario()
- validarUsuario()
- obtenerUsuarios()
- eliminarUsuario()

Responsabilidad:
- Gestionar autenticación.
- Controlar la persistencia de usuarios.

---

## 🔹 3. db (Conexión a base de datos)

### 🗄️ DatabaseManager.java

Responsabilidad:
- Crear la conexión con SQLite.
- Inicializar la base de datos si no existe.
- Centralizar la configuración de conexión.

Se utiliza JDBC para conectar con:

```
inventario.db
```

---

## 🔹 4. ui (Interfaz Gráfica)

Desarrollada con Java Swing.

---

### 🔐 LoginFrame.java

Función:
- Mostrar formulario de inicio de sesión.
- Validar credenciales usando UsuarioDAO.
- Redirigir al panel principal si son correctas.

---

### 🖥️ MainFrame.java

Función:
- Ventana principal del sistema.
- Permite navegar hacia:
  - Gestión de productos
  - Gestión de usuarios
  - Movimientos

---

### 📦 UsuariosFrame.java

Permite:
- Ver lista de usuarios.
- Agregar nuevos usuarios.
- Eliminar usuarios.

---

### 📦 MovimientosFrame.java

Permite:
- Registrar entradas de inventario.
- Registrar salidas.
- Actualizar automáticamente la cantidad disponible.

---

### 📦 ProductoDialog.java / UsuarioDialog.java

Ventanas emergentes para:
- Crear
- Editar
- Confirmar información

---

# 🗄️ Base de Datos

Base de datos local SQLite:

```
inventario.db
```

Tablas principales:

- usuarios
- productos
- movimientos (si está implementado)

La base de datos se crea automáticamente al ejecutar el sistema.

---

# 🛠️ Tecnologías Utilizadas

- Java 17
- Maven
- Java Swing
- SQLite
- JDBC
- Patrón DAO

---

# 📈 Posibles Mejoras

- Implementar encriptación de contraseñas
- Implementar roles y permisos
- Añadir reportes en PDF
- Migrar a versión web (Spring Boot)
- Implementar pruebas unitarias (JUnit)

---

# 🎯 Objetivo Académico

Este proyecto demuestra conocimientos en:

- Programación Orientada a Objetos
- Arquitectura en capas
- Gestión de bases de datos
- Desarrollo de aplicaciones de escritorio
- Organización profesional de proyectos

---

# 👨‍💻 Autor

Kevin Rico Bermeo  
Desarrollador en formación  

GitHub:  
https://github.com/KevinBermeo0318
