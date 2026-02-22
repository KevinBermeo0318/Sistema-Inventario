# 🗂️ Sistema de Inventario

![Java](https://img.shields.io/badge/Java-17-blue)
![Maven](https://img.shields.io/badge/Maven-Build-orange)
![SQLite](https://img.shields.io/badge/Database-SQLite-lightgrey)
![Status](https://img.shields.io/badge/Status-Completed-success)

Sistema de Inventario desarrollado en **Java** con arquitectura por capas y conexión a **SQLite**, diseñado para gestionar productos, usuarios y movimientos de inventario de forma eficiente.

🔗 **Repositorio:**  
https://github.com/KevinBermeo0318/Sistema-Inventario

---

## 📌 Descripción

Este proyecto implementa una aplicación de escritorio utilizando **Java Swing** que permite:

- Gestionar productos
- Administrar usuarios
- Registrar entradas y salidas de inventario
- Persistir datos mediante SQLite
- Aplicar el patrón DAO para separación de responsabilidades

Está estructurado siguiendo buenas prácticas de organización y modularidad.

---

## 🚀 Funcionalidades

✔️ Autenticación de usuarios  
✔️ CRUD de productos  
✔️ CRUD de usuarios  
✔️ Registro de movimientos (entradas y salidas)  
✔️ Base de datos local automática  
✔️ Interfaz gráfica intuitiva  
✔️ Arquitectura en capas  

---

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Maven**
- **Java Swing**
- **SQLite**
- **JDBC**
- Patrón **DAO**

---

## 🏗️ Arquitectura del proyecto

El proyecto está organizado en capas:

```
com.inventario
│
├── dao        → Acceso a datos (DAO)
├── db         → Gestión de conexión a base de datos
├── model      → Entidades del sistema
├── ui         → Interfaz gráfica (Swing)
└── Main.java  → Punto de entrada
```

Este diseño permite:

- Separación de responsabilidades
- Mejor mantenimiento
- Escalabilidad futura
- Código más limpio y estructurado

---

## ⚙️ Instalación

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/KevinBermeo0318/Sistema-Inventario.git
```

### 2️⃣ Acceder al directorio

```bash
cd Sistema-Inventario
```

### 3️⃣ Compilar el proyecto

```bash
mvn clean install
```

### 4️⃣ Ejecutar la aplicación

```bash
mvn exec:java
```

También puedes ejecutar `Main.java` directamente desde tu IDE.

---

## 🗄️ Base de datos

El sistema utiliza una base de datos local SQLite:

```
inventario.db
```

Se genera automáticamente si no existe.

---

## 📈 Posibles mejoras futuras

- Implementación de roles (admin / empleado)
- Exportación de reportes en PDF o Excel
- Migración a arquitectura web (Spring Boot)
- Implementación de pruebas unitarias
- Dashboard con estadísticas

---

## 🎯 Objetivo del proyecto

Proyecto desarrollado con fines académicos y de práctica para reforzar:

- Programación orientada a objetos
- Arquitectura por capas
- Manejo de bases de datos
- Desarrollo de aplicaciones de escritorio
- Buenas prácticas con Maven

---

## 👨‍💻 Autor

**Kevin Rico Bermeo**  
Desarrollador en formación  

🔗 GitHub: https://github.com/KevinBermeo0318

---

## 📄 Licencia

Este proyecto es de uso académico y demostrativo.
