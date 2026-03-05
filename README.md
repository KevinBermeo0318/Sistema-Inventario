# 📊 Diagramas del Sistema

Para facilitar la comprensión del proyecto, a continuación se muestran algunos diagramas que explican la arquitectura, base de datos y funcionamiento del sistema.

---

# 🏗️ Arquitectura del Sistema

Este diagrama muestra cómo interactúan las diferentes capas del proyecto.

```mermaid
flowchart TD

A[👤 Usuario] --> B[🖥️ Interfaz UI]

B --> C[📦 DAO Layer]

C --> D[⚙️ DatabaseConfig]

D --> E[(🗄️ MySQL Database)]

subgraph UI
B
end

subgraph Lógica de Negocio
C
end

subgraph Persistencia
D
E
end
```

### Explicación

👤 **Usuario**
Interactúa con el sistema mediante la interfaz.

🖥️ **UI (Interfaz)**
Ventanas del sistema donde el usuario realiza acciones.

📦 **DAO**
Contiene la lógica que ejecuta consultas SQL.

⚙️ **DatabaseConfig**
Administra la conexión con la base de datos.

🗄️ **MySQL**
Almacena todos los datos del sistema.

---

# 🗄️ Diagrama de Base de Datos

Este diagrama representa la estructura principal de la base de datos del sistema.

```mermaid
erDiagram

CATEGORIAS {
int id_categoria
string nombre
string descripcion
}

PRODUCTOS {
int id_producto
string nombre
double precio
int stock
int categoria_id
}

MOVIMIENTOS {
int id_movimiento
int producto_id
string tipo_movimiento
int cantidad
date fecha
string usuario
}

CATEGORIAS ||--o{ PRODUCTOS : clasifica
PRODUCTOS ||--o{ MOVIMIENTOS : genera
```

### Explicación

📂 **Categorías**
Permiten organizar los productos.

📦 **Productos**
Contienen la información del inventario.

🔄 **Movimientos**
Registran entradas y salidas de productos.

Relaciones:

* Una **categoría** puede tener muchos productos.
* Un **producto** puede tener muchos movimientos.

---

# 🧩 Diagrama de Clases

Este diagrama muestra la estructura de las clases principales del sistema.

```mermaid
classDiagram

class Producto {
+int id
+String nombre
+double precio
+int stock
+int categoria
}

class Categoria {
+int id
+String nombre
+String descripcion
}

class Movimiento {
+int id
+Producto producto
+int cantidad
+TipoMovimiento tipo
+Date fecha
+String usuario
}

class ProductoDAO {
+obtenerProductos()
+insertarProducto()
+actualizarProducto()
+eliminarProducto()
}

class CategoriaDAO {
+obtenerCategorias()
+insertarCategoria()
+eliminarCategoria()
}

class MovimientoDAO {
+registrarMovimiento()
+obtenerMovimientos()
}

ProductoDAO --> Producto
CategoriaDAO --> Categoria
MovimientoDAO --> Movimiento
Movimiento --> Producto
Producto --> Categoria
```

### Explicación

📦 **Producto**
Representa un artículo del inventario.

📂 **Categoria**
Clasifica los productos.

🔄 **Movimiento**
Registra entradas y salidas de inventario.

📦 **DAO**
Manejan la comunicación con la base de datos.

---

# 🔄 Flujo de Funcionamiento del Sistema

Este diagrama explica cómo se procesa una acción dentro del sistema.

```mermaid
sequenceDiagram

actor Usuario
participant UI
participant DAO
participant DB

Usuario->>UI: Realiza una acción
UI->>DAO: Solicita operación
DAO->>DB: Ejecuta consulta SQL
DB-->>DAO: Devuelve datos
DAO-->>UI: Resultado
UI-->>Usuario: Muestra información
```

### Ejemplo de flujo

1️⃣ El usuario registra un producto
2️⃣ La interfaz envía la solicitud al **ProductoDAO**
3️⃣ El DAO ejecuta un **INSERT en la base de datos**
4️⃣ MySQL guarda el producto
5️⃣ La interfaz muestra el resultado

---

# 🚀 Beneficios de esta Arquitectura

Este sistema utiliza buenas prácticas de desarrollo:

✔️ Separación de responsabilidades
✔️ Arquitectura por capas
✔️ Código modular
✔️ Fácil mantenimiento
✔️ Escalabilidad

---

# 📌 Autor

👨‍💻 **Kevin Rico Bermeo**
Desarrollador en formación

🔗 GitHub
https://github.com/KevinBermeo0318
