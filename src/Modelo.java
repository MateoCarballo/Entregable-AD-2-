import java.sql.*;
import java.util.ArrayList;

import static java.lang.System.*;

public record Modelo(Connection modeloConnectionMySQL, Connection modeloConnectionPostgre) {

    /*
1 -> Crear una nueva categoría (PostgreSQL).
Se implementará una función con la siguiente cabecera: void crearCategoria(String nombreCategoria).
Se recibirá un String que será el nombreCategoria y se añadirá a la base de datos.
* */

    public void crearCategoria(String nombreCategoria) {
        int idGenerado = -1;
        try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement
                ("""
                        INSERT INTO categorias(nombre_categoria)
                        VALUES (?)""", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, nombreCategoria);
            if (preparedStatement.executeUpdate() == 1) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        idGenerado = resultSet.getInt(1);
                    }
                }
            }
            if (idGenerado != -1)
                out.println("Categoría creada con exito! El nuevo id para la categoría es -> " + idGenerado);
        } catch (SQLException e) {
            out.println("Error en la creacion de la categoría " + e.getMessage());
            e.printStackTrace();
        }

    }

    /*
    * 2 -> Crear un nuevo proveedor (PostgreSQL)
    Se implementará una función con la siguiente cabecera: void crearNuevoProveedor(String nombreProveedor, String nif, int telefono, String email).
    Se recibirá todos los datos del proveedor y se añadirán en la base de datos.
    * */
    public void crearProveedor(String nombreProveedor, String nif, int telefono, String email) {
        long idGenerado = -1;
        try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement
                ("""
                        INSERT
                        INTO proveedores(nombre_proveedor, contacto)
                        VALUES (?, ROW(?,?,?,?))""", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, nombreProveedor);
            preparedStatement.setString(2, nombreProveedor);
            preparedStatement.setString(3, nif);
            preparedStatement.setString(4, String.valueOf(telefono));
            preparedStatement.setString(5, email);
            if (preparedStatement.executeUpdate() == 1) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        idGenerado = resultSet.getInt(1);
                    }
                }
            }
            if (idGenerado != 1)
                out.println("Proveedor creado con exito! El nuevo id para el proveedor es -> " + idGenerado);

        } catch (SQLException e) {
            out.println("Error durante la creacion del proveedor " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    3 -> Eliminar un nuevo proveedor (PostgreSQL)
    Se implementará una función con la siguiente cabecera: void eliminarProveedor(int id).
    Se tendrá que comprobar si el id indicado existe y si es así, eliminarlo de la base de datos.
     */
    public void eliminarProveedor(int id) {

        // ELIMINAR LA CLAVE FORANEA DE LA TABLA 'productos'
        try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement
                ("""
                        UPDATE productos
                        SET id_proveedor = null
                        WHERE id_proveedor = ?""")) {

            preparedStatement.setInt(1, id);
            int filasAfectadas = preparedStatement.executeUpdate();
            if ( filasAfectadas > 0) {
                out.println("La referencia al proveedor con id " + id + " en la tabla 'productos' ha sido eliminada en " + filasAfectadas + "tuplas");
            } else {
                out.println("El proveedor no se ha eliminado como clave foranea de la tabla productos");
            }

        } catch (SQLException e) {
            out.println("Error durante la eliminacion del proveedor como clave foranea (tabla productos) " + e.getMessage());
            e.printStackTrace();
        }
        // ELIMINAR DE LA TABLA 'proveedores'

        try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement
                ("""
                        DELETE
                        FROM proveedores
                        WHERE id_proveedor = ?""")) {

            preparedStatement.setInt(1, id);
            if (preparedStatement.executeUpdate() == 1) {
                out.println("Proveedor eliminado con exito!");
            } else {
                out.println("El proveedor no se ha eliminado como clave primaria de la tabla proveedores");
            }

        } catch (SQLException e) {
            out.println("Error durante la eliminacion del proveedor " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    4 -> Crear un nuevo usuario (MySQL)
    Se implementará una función con la siguiente cabecera: void crearUsuario(String nombre, String email, int anho_nacimiento).
    Se recibirán todos los datos del usuario.
     */
    public void crearUsuario(String nombre, String email, int anhoNacimiento) {
        long idGenerado = -1;
        try (PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement
                ("""
                        INSERT
                        INTO usuarios (nombre,email,ano_nacimiento)
                        VALUES (?,?,?)""", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, anhoNacimiento);
            if (preparedStatement.executeUpdate() == 1) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        idGenerado = resultSet.getInt(1);
                    }
                }
            }
            if (idGenerado != 1)
                out.println("Usuario creado con exito! El nuevo id para el usuario es -> " + idGenerado);

        } catch (SQLException e) {
            out.println("Error durante la creacion de un nuevo usario " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    5 -> Eliminar un usuario (MySQL)
    Se implementará una función con la siguiente cabecera: void eliminarUsuario(int id).
    Se tendrá que comprobar si el id indicado existe y si es así, eliminarlo de la base de datos.
     */
    public void eliminarUsuario(int id) {

        // PONE A NULL EL VALOR DE LA CLAVE FORANEA QUE CONTIENE EL ID DEL USUARIO QUE VAMOS A ELIMINAR
        if (comprobarUsuarioExiste(id)) {
            try (PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement
                    ("""
                            UPDATE pedidos
                            SET id_usuario = null
                            WHERE id_usuario = ?""")) {
                preparedStatement.setInt(1, id);
                int filasAfectadas = preparedStatement.executeUpdate();
                if (filasAfectadas > 0)
                    out.println("La referencia al usuario con id " + id + " en la tabla 'pedidos' ha sido puesta a null en " + filasAfectadas + "tuplas");

            } catch (SQLException e) {
                out.println("Error durante la eliminacion como clave foránea en la tabla pedidos " + e.getMessage());
            }
        }

        // BORRAMOS EL USUARIO EN LA TABLA 'usuarios'
        if (comprobarUsuarioExiste(id)) {
            try (PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement
                    ("""
                            DELETE
                            FROM usuarios
                            WHERE id_usuario = ?""")) {
                preparedStatement.setInt(1, id);
                if (preparedStatement.executeUpdate() == 1) out.println("Usuario eliminado con exito!");

            } catch (SQLException e) {
                out.println("Error durante la eliminacion de un nuevo usuario " + e.getMessage());
            }
        }


    }

    /*
    Metodo añadido por mi cuenta para asegurarme de que el usuario existe antes de intentar eliminarlo de la base de datos
     */
    private boolean comprobarUsuarioExiste(int idParaComprobar) {
        boolean existeUsuario = false;
        try (PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement
                ("""
                        SELECT nombre
                        FROM usuarios
                        WHERE id_usuario = ?""")) {
            preparedStatement.setInt(1, idParaComprobar);
            ResultSet resultSet = preparedStatement.executeQuery();

            existeUsuario = resultSet.next();

        } catch (SQLException e) {
            out.println("Error durante la busqueda de usuario por id " + e.getMessage());
        }
        return existeUsuario;
    }

    /*
    6 -> Crear nuevo producto (nombre, precio, stock, categoria, proveedor) (MySQL + PostgreSQL)
    Se implementará una función con la siguiente cabecera:
    void crearProducto(String nombre, Double precio, int stock, String nombre_categoria, String nif).
    Se tendrá que obtener el id de la categoría y el id del proveedor a partir del nombre y del nif.
    Se añadirá en la base de datos MySQL y en la base de datos PostgreSQL.
    El identificador del producto tendrá que ser el mismo en ambas bases de datos.
     */
    public void crearProducto(String nombre, Double precio, int stock, String nombreCategoria, String nif) {

        int idProductoGenerada = -1;
        int idProveedor = -1;
        int idCategoria = -1;
        /*
        Pasos seguidos para llevar a cabo la insercion de un nuevo producto:
            1 -> INSERTAR EL PRODUCTO EN DB MYSQL Y OBTENER EL ID (del nuevo producto creado).
            2 -> OBTENEMOS EL id_categoria DESDE EL DATO 'nombre_categoria' DE LA TABLA CATEGORIAS (POSTGRE)
            3 -> OBTENER EL id_proveedor DESDE EL DATO 'nif' DEL TIPO DE DATO 'Contacto' DESDE LA TABLA PROVEEDORES (POSTGRE)
            4 -> INSERTAR EN LA DB POSTGRE EL NUEVO PRODUCTO INTRODUCIDO EN LA DB MYSQL,
                TRAYENDO DESDE AHI LA 'id_producto' Y BUSCANDO 'id_proveedor' e 'id_categoria' EN LAS TABLAS DE LA BASE DE DATOS POSTGRE
         */
        boolean error = false;
        try {
            modeloConnectionMySQL.setAutoCommit(false);
            modeloConnectionPostgre.setAutoCommit(false);

            //1.INSERTAMOS EN LA BASE DE DATOS MYSQL EL NUEVO PRODUCTO PARA GENERAR UN ID Y RECUPERAR EL VALOR DEL ULTIMO ID
            try (PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement
                    ("INSERT INTO productos (nombre_producto,precio,stock) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, nombre);
                preparedStatement.setDouble(2, precio);
                preparedStatement.setInt(3, stock);

                if (preparedStatement.executeUpdate() == 1) {
                    try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                        while (resultSet.next()) {
                            idProductoGenerada = resultSet.getInt(1);
                        }
                    }
                }
            } catch (SQLException e) {
                error = true;
                out.println("Error en la creacion de un producto en la DB MySQL " + e.getMessage());
                e.printStackTrace();
            }

            //  2.OBTENEMOS EL id_categoria DESDE EL DATO 'nombre_categoria' DE LA TABLA CATEGORIAS
            try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement
                    ("SELECT id_categoria FROM categorias WHERE nombre_categoria = ?")) {
                preparedStatement.setString(1, nombreCategoria);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        idCategoria = resultSet.getInt(1);
                    }
                }
            } catch (SQLException e) {
                out.println("Error durante la busqueda del id_producto en la tabla proveedores " + e.getMessage());
                e.printStackTrace();
            }

            // 3.OBTENEMOS EL id_proveedor DESDE EL DATO 'nif' DEL TIPO DE DATO 'Contacto' DESDE LA TABLA PROVEEDORES
            try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement
                    ("SELECT id_proveedor FROM proveedores WHERE (contacto).nif = ?")) {
                preparedStatement.setString(1, nif);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        idProveedor = resultSet.getInt(1);
                    }
                }
            } catch (SQLException e) {
                out.println("Error al obtener el id del proveedor desde el nif " + e.getMessage());
                e.printStackTrace();
            }

        /*
        4.INSERTAR EN LA DB POSTGRE EL NUEVO PRODUCTO INTRODUCIDO EN LA DB MYSQL,
         TRAYENDO DESDE AHI LA 'id_producto' Y BUSCANDO 'id_proveedor' e 'id_categoria' EN LAS TABLAS DE LA BASE DE DATOS POSTGRE
         */
            try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement(
                    "INSERT INTO productos (id_producto,id_proveedor,id_categoria) " +
                            "VALUES (?,?,?)")) {
                preparedStatement.setInt(1, idProductoGenerada);
                preparedStatement.setInt(2, idProveedor);
                preparedStatement.setInt(3, idCategoria);
                if (preparedStatement.executeUpdate() == 1) {
                    out.println("Producto creado con exito! \n"
                            + "El nuevo id para el producto es -> " + idProductoGenerada + "\n"
                            + "Su proveedor tiene el id -> " + idProveedor + "\n"
                            + "Su categorias tiene el id -> " + idCategoria + "\n");
                }
            } catch (SQLException e) {
                out.println("Error durante la insercion de los datos en la tabla productos de postgre " + e.getMessage());
                e.printStackTrace();
            }

            try {
                if (!error) {
                    modeloConnectionPostgre.commit();
                    modeloConnectionMySQL.commit();
                } else {
                    modeloConnectionPostgre.rollback();
                    modeloConnectionMySQL.rollback();
                }

            } catch (SQLException e) {
                out.println("Error al intentar commitear los cambios para cerrar la transaccion " + e.getMessage());
            }

        } catch (SQLException e) {
            out.println("Error al intentar para la opcion de autocommit para realizar una transaccion");
            try {
                modeloConnectionPostgre.rollback();
                modeloConnectionMySQL.rollback();
            } catch (SQLException ex) {
                out.println("Error en el rollback :" + e.getMessage());
            }

            e.printStackTrace();
        }

        try {
            modeloConnectionMySQL.setAutoCommit(true);
            modeloConnectionPostgre.setAutoCommit(true);
        } catch (SQLException e) {
            out.println("Error al devolver el estado del autocommit a true " + e.getMessage());
            e.printStackTrace();
        }
    }
/*
7 -> Eliminar un producto por su nombre (MySQL + PostgreSQL)
Se implementará una función con la siguiente cabecera:
void eliminarProductoPorNombre(String nombre).
Se tendrá que eliminar el producto de ambas bases de datos.
*/

    public void eliminarProductoPorNombre(String nombre) {
        //TODO falta probar
        int idProducto = buscarProducto(nombre);

        if (idProducto != -1) {
            out.println("Producto encontrado con id: " + idProducto);
            //Lllamos a un metodo para ejecutar el borrado de las tablas como una transacción
            eliminarRegistrosDelProducto(idProducto);
        }


    }

    public int buscarProducto(String nombre) {
        int idProducto = -1;
        //BUSCAMOS EL 'id_producto' EN LA BASE DE DATOS MYSQL
        try (PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement(
                """
                        SELECT id_producto
                        FROM productos
                        WHERE nombre_producto= ?""")) {
            preparedStatement.setString(1, nombre);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    idProducto = resultSet.getInt(1);
                }
            } catch (SQLException e) {
                out.println("Error al leer los resultados con resulset de la DB MySQL " + e.getMessage());
            }
        } catch (SQLException e) {
            out.println("Error en la busqueda del producto por nombre " + e.getMessage());
        }
        return idProducto;
    }

    private void eliminarRegistrosDelProducto(int idProducto) {
        // EMPEZAMOS LA TRANSACCION
        try {
            modeloConnectionMySQL.setAutoCommit(false);
            modeloConnectionPostgre.setAutoCommit(false);
            // 1 -> BORRADO EN MYSQL

            // 1.1 -> BORRADO de la tupla que contiene id_producto en la tabla 'pedidos_productos'
            try (PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement(
                    """
                            DELETE
                            FROM pedidos_productos
                            WHERE id_producto = ?""")) {

                preparedStatement.setInt(1, idProducto);
                if (preparedStatement.executeUpdate() == 1)
                    out.println("Borrado producto como clave foranea en la tabla 'pedidos_productos' ## MySQL");

            } catch (SQLException e) {
                out.println("Error durante el borrado del producto en la tabla 'pedidos_productos' en MySQL: " + e.getMessage());
                e.printStackTrace();
                try {
                    modeloConnectionMySQL.rollback();
                } catch (SQLException ex) {
                    out.println("Error al hacer rollback en MySQL: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            // 1.2 -> Borrado en la tabla 'productos' de la tupla con el nombre introducido
            try (PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement(
                    """
                            DELETE
                            FROM productos
                            WHERE id_producto = ?""")) {
                preparedStatement.setInt(1, idProducto);
                if (preparedStatement.executeUpdate() == 1)
                    out.println("Borrado producto como clave primaria en la tabla 'productos' ## MySQL");

            } catch (SQLException e) {
                out.println("Error durante el borrado del producto en la tabla productos en MySQL: " + e.getMessage());
                e.printStackTrace();
                try {
                    modeloConnectionMySQL.rollback();
                } catch (SQLException ex) {
                    out.println("Error al hacer rollback en MySQL: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            // 2 -> BORRADO EN POSTGRE

            // 2.1 -BORRADO de la tupla que contiene id_producto en la tabla 'alamacenes_productos'
            try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement(
                    """
                            DELETE
                            FROM almacenes_productos
                            WHERE id_producto = ?""")) {
                preparedStatement.setInt(1, idProducto);
                if (preparedStatement.executeUpdate() == 1)
                    out.println("Borrado producto como clave foránea en la tabla 'almacenes_productos' ## POSTGRE");
            } catch (SQLException e) {
                out.println("Error durante el borrado del producto en la tabla alamacenes_productos en Postgre: " + e.getMessage());
                try {
                    modeloConnectionPostgre.rollback();
                } catch (SQLException ex) {
                    out.println("Error al hacer rollback en Postgre: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            // 2.2 -> Borrado en la tabla 'productos' de la tupla con el nombre introducido
            try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement(
                    "DELETE FROM productos WHERE id_producto = ?")) {
                preparedStatement.setInt(1, idProducto);
                if (preparedStatement.executeUpdate() == 1)
                    out.println("Borrado producto como clave primaria en la tabla 'productos' ## POSTGRE");
            } catch (SQLException e) {
                out.println("Error durante el borrado del producto en la tabla productos en Postgre: " + e.getMessage());
                try {
                    modeloConnectionPostgre.rollback();
                } catch (SQLException ex) {
                    out.println("Error al hacer rollback en Postgre: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            //REALIZAMOS COMMITS
            try {
                modeloConnectionMySQL.commit();
                modeloConnectionPostgre.commit();
            } catch (SQLException e) {
                out.println("Error al realizar el commit de la transaccion " + e.getMessage());
            }

        } catch (SQLException e) {
            out.println("Error al cambiar el auto commit a false " + e.getMessage());
        } finally {
            // CERRAMOS LA TRANSACCION
            try {
                modeloConnectionMySQL.setAutoCommit(true);
                modeloConnectionPostgre.setAutoCommit(true);
            } catch (SQLException e) {
                out.println("Error al cambiar el auto commit a true " + e.getMessage());
            }
        }
    }

/*
8 -> Listar los productos con bajo stock (menos de X unidades disponibles) (MySQL)
Se implementará una función con la siguiente cabecera: void listarProductosBajoStock(int stock) .
Mediante una única consulta se tendrá que obtener el conjunto de filas resultante y mostrar el nombre de los productos junto con su stock.
 */

    public void listarProductosBajoStock(int stock) {
        String nombre = "";
        int stockEncontrado;
        ArrayList<Producto> productosFiltrados = new ArrayList<>();
        try (PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement(
                """
                        SELECT nombre_producto, stock
                        FROM productos 
                        WHERE stock <= ?""")) {
            preparedStatement.setInt(1, stock);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    nombre = resultSet.getString(1);
                    stockEncontrado = resultSet.getInt(2);
                    productosFiltrados.add(new Producto(nombre, stockEncontrado));
                }

                for (Producto p : productosFiltrados) {
                    out.println(p.toStringTuneado());
                }
            }

        } catch (SQLException e) {
            out.println("Error al buscar los productos por stock " + e.getMessage());
        }
    }

    /*
    9 -> Obtener el total de pedidos realizados por cada usuario (MySQL)
    Se implementará una función con la siguiente cabecera:
    void obtenerTotalPedidosUsuarios().
    Mediante una consulta se tendrá que obtener toda la información e
    imprimir por pantalla: el nombre del usuario y el total de pedidos que ha hecho.
    */
    public void obtenerTotalPedidosUsuarios() {
        ArrayList<Usuario> usuariosFiltrados = new ArrayList<>();
        try (PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement(
                """
                        SELECT u.nombre as Usuario, COUNT(p.id_pedido) as Total 
                        FROM pedidos as p
                        INNER JOIN usuarios as u 
                        WHERE p.id_usuario = u.id_usuario 
                        GROUP BY u.nombre""")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nombre = resultSet.getString(1);
                    int numeroPedidos = resultSet.getInt(2);
                    usuariosFiltrados.add(new Usuario(nombre, numeroPedidos));
                }

                for (Usuario u : usuariosFiltrados) {
                    out.println(u.toStringTuneado());
                }
            }
        } catch (SQLException e) {
            out.println(" Error al buscar la informacion necesaria para printear los pedidos registrados para cada usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    10 -> Obtener la cantidad de productos almacenados por cada almacén (PostgreSQL)
    Se implementará una función con la siguiente cabecera: void obtenerCantidadProductosEnCadaAlmacen().
    Mediante una consulta se tendrá que obtener toda la información solicitada y pintar por pantalla el
    nombre del almacén y el total de productos de los que dispone.
    */
    public void obtenerCantidadProductosEnCadaAlmacen() {
        try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement(
                """
                        SELECT a.nombre_almacen, SUM(ap.cantidad) 
                        FROM almacenes as a INNER JOIN almacenes_productos as ap ON a.id_almacen = ap.id_almacen 
                        GROUP BY a.nombre_almacen""")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nombreAlmacen = resultSet.getString(1);
                    int cantidadProductos = resultSet.getInt(2);
                    out.println("Almacen  " + nombreAlmacen + "\n" +
                            "Cantidad de productos -> " + cantidadProductos + "\n");
                }
            } catch (SQLException e) {
                out.println("Error al recuperar los datos de la DB Postgre " + e.getMessage());
            }

        } catch (SQLException e) {
            out.println("Error durante la obtencion de la cantidad de productos por alamacen " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    11 -> Listar todos los productos con sus respectivas categorías y proveedores (PostgreSQL)
    Se implementará una función con la siguiente cabecera: void listarTodosProductosConCategoriaYProveedor().
    Se realizará una primera consulta en PostgreSQL que permita obtener toda la información del producto:
     id, nombre, nif, teléfono e email del proveedor así como el nombre de la categoría.
    Se realizará una consulta en MySQL para obtener el nombre, precio y stock del producto
    Se concatenará y se mostrarán todos los datos indicados anteriormente usando Java.
    */
    public void listarTodosProductosConCategoriaYProveedor() {
        int idProducto;
        String nombreProducto;
        String nif;
        String telefono;
        String email;
        String nombreCategoria;
        ArrayList<Producto> listaProductoDesdePostgre = new ArrayList<>();

        // TODO cargarme esta mierda de metodo para hacerlo, iterar dos veces las consultas.

        try (PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement
                ("""
                        SELECT prod.id_producto, prov.nombre_proveedor, (prov.contacto).nif, (prov.contacto).telefono, (prov.contacto).email, cate.nombre_categoria 
                        FROM productos as prod 
                        INNER JOIN categorias as cate ON prod.id_categoria = cate.id_categoria 
                        INNER JOIN proveedores as prov ON prod.id_proveedor = prov.id_proveedor""")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    idProducto = resultSet.getInt(1);
                    nombreProducto = resultSet.getString(2);
                    nif = resultSet.getString(3);
                    telefono = resultSet.getString(4);
                    email = resultSet.getString(5);
                    nombreCategoria = resultSet.getString(6);
                    listaProductoDesdePostgre.add(new Producto(idProducto, nombreProducto, nif, telefono,
                            email, nombreCategoria));
                }
                for (Producto p : listaProductoDesdePostgre) {
                    out.println(p.toStringTuneadoMetodo11A());
                }
            } catch (Exception e) {
                out.println("Error al recuperar los datos de la DB mediante ' Result Set ' ");
            }
        } catch (SQLException e) {
            out.println("Error durante la consulta a la DB Postgre");
        }

        try (PreparedStatement preparedStatement =
                     modeloConnectionMySQL.prepareStatement("""
                             SELECT nombre_producto, precio, stock
                             FROM productos WHERE id_producto = ?""")) {
            for (Producto p : listaProductoDesdePostgre) {
                preparedStatement.setInt(1, p.getId_producto());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        p.setNombreProducto(resultSet.getString(1));
                        p.setPrecio(resultSet.getDouble(2));
                        p.setStock(resultSet.getInt(3));
                        out.println(p.toStringTuneadoMetodo11B());
                    }
                } catch (SQLException e) {
                    out.println("Error al obtner los datos desde la DB MySQL");
                }
            }
        } catch (SQLException e) {
            out.println("Error al conectar con la DB de MySQL");
        }
    }

/*
12 -> Obtener todos los usuarios que han comprado algún producto de una categoria dada (MySQL + PostgreSQL).
Se implementará una función con la siguiente cabecera: void obtenerUsuariosCompraronProductosCategoria(int idCategoria).
Se recibirá el id de la categoría y se obtendrá en PostgreSQL el id de los productos que pertenezcan a esa categoría.
En MySQL se obtendrá el nombre de los usuarios que han comprado algún producto de los indicados anteriormente.
Se mostrará por pantalla el nombre de los usuarios.
*/

    public void obtenerUsuariosCompraronProductosCategoria(int idCategoria) {
        int idProducto = -1;

        //SACAR EL id_producto DESDE LA DB POSTGRE
        try (PreparedStatement preparedStatementPostgre = modeloConnectionPostgre.
                prepareStatement("""
                        SELECT id_producto 
                        FROM productos 
                        WHERE id_categoria = ?""")) {
            preparedStatementPostgre.setInt(1, idCategoria);
            try (ResultSet resultSetPostgre = preparedStatementPostgre.executeQuery()) {
                while (resultSetPostgre.next()) {
                    idProducto = resultSetPostgre.getInt(1);
                    /*
                    usr es la tabla         'usuarios'
                    ped es la tabla         'pedidos'
                    ped_prod es la tabla    'pedidos_productos'
                     */
                    try (PreparedStatement preparedStatementMySQL = modeloConnectionMySQL.
                            prepareStatement("""
                                    SELECT usr.nombre
                                    FROM usuarios as usr
                                    INNER JOIN pedidos as ped
                                    ON usr.id_usuario = ped.id_usuario
                                    INNER JOIN pedidos_productos as ped_prod
                                    ON ped_prod.id_pedido = ped.id_pedido
                                    WHERE ped_prod.id_producto = ?
                                    """)) {
                        preparedStatementMySQL.setInt(1, idProducto);
                        try (ResultSet resultSetMySQL = preparedStatementMySQL.executeQuery()) {
                            while (resultSetMySQL.next()) {
                                out.println(resultSetMySQL.getString(1));
                            }
                        } catch (Exception e) {
                            out.println("Error en el reesul set de MySQL");
                        }
                    } catch (Exception e) {
                        out.println("Error en el prepared statement de MySQL");
                    }
                }
            } catch (Exception e) {
                out.println("Error en el resul set de Postgre");
            }
        } catch (SQLException e) {
            out.println("Error en el prepared statement de Postgre");
        }

    }

    public ArrayList mostrasCategorias() {
        ArrayList <Categoria> categoriasInscritasDB = new ArrayList<>();
        try(PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement(
                "SELECT * FROM categorias")){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    int idCategoria = resultSet.getInt(1);
                    String nombreCategoria = resultSet.getString(2);
                    categoriasInscritasDB.add(new Categoria(idCategoria,nombreCategoria));
                }
            } catch (SQLException e) {
                out.println("Error al mostras las categorias de la tabla 'categorias' en la DB POSTGRE " + e.getMessage());
            }
        } catch (SQLException e) {
            out.println("Error en la consulta a la tabla 'categorias' en la DB POSTGRE " + e.getMessage());        }
        return categoriasInscritasDB;
    }

    public ArrayList mostrarProveedores(){
        ArrayList<Proveedor> proveedoresInscritosDB = new ArrayList<>();

        try(PreparedStatement preparedStatement = modeloConnectionPostgre.prepareStatement(
                "SELECT id_proveedor, nombre_proveedor, (contacto).nombre_contacto, (contacto).nif, (contacto).telefono, (contacto).email FROM proveedores")){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    //Parte del proveedor
                    int id = resultSet.getInt(1);
                    String nombre = resultSet.getString(2);
                    //Parte del contacto
                    String nombreContacto = resultSet.getString(3);
                    String nif = resultSet.getString(4);
                    String telefono = resultSet.getString(5);
                    String email = resultSet.getString(6);
                    proveedoresInscritosDB.add(new Proveedor(id,nombre,new Proveedor.Contacto(nombreContacto,nif,telefono,email)));
                }
            }catch (SQLException e) {
                out.println("Error al mostras los proveedores de la tabla 'proveedores' en la DB POSTGRE " + e.getMessage());
            }
        }catch (SQLException e) {
            out.println("Error en la consulta a la tabla 'proveedores' en la DB POSTGRE " + e.getMessage());
        }
        return proveedoresInscritosDB;
    }

    public ArrayList mostrarProductos(){
        ArrayList<Producto> productosInscritosDB = new ArrayList<>();

        try(PreparedStatement preparedStatement = modeloConnectionMySQL.prepareStatement(
                "SELECT nombre_producto FROM productos")){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String nombreProducto = resultSet.getString(1);
                    productosInscritosDB.add(new Producto(nombreProducto));
                }
            }catch (SQLException e) {
                out.println("Error al mostras los proveedores de la tabla 'proveedores' en la DB POSTGRE " + e.getMessage());
            }
        }catch (SQLException e) {
            out.println("Error en la consulta a la tabla 'proveedores' en la DB POSTGRE " + e.getMessage());
        }
        return productosInscritosDB;
    }
}


