public class Producto {
    private int id_producto;
    private String nombreProducto;
    private double precio;
    private int stock;
    private String nombreProveedor;
    private String nifProveedor;
    private String telefonoProveedor;
    private String emailProveedor;
    private String nombreCategoria;

    public Producto (){

    }

    public Producto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Producto(String nombreProducto, int stock) {
        this.nombreProducto = nombreProducto;
        this.stock = stock;
    }

    public Producto(int id_producto, String nombreProducto, double precio, int stock) {
        this.id_producto = id_producto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto(int id_producto, String nombreProveedor, String nifProveedor, String telefonoProveedor, String emailProveedor, String nombreCategoria) {
        this.id_producto = id_producto;
        this.nombreProveedor = nombreProveedor;
        this.nifProveedor = nifProveedor;
        this.telefonoProveedor = telefonoProveedor;
        this.emailProveedor = emailProveedor;
        this.nombreCategoria = nombreCategoria;
    }

    public Producto(int id_producto, String nombreProducto, double precio, int stock, String nombreProveedor, String nifProveedor, String telefonoProveedor, String emailProveedor, String nombreCategoria) {
        this.id_producto = id_producto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
        this.nombreProveedor = nombreProveedor;
        this.nifProveedor = nifProveedor;
        this.telefonoProveedor = telefonoProveedor;
        this.emailProveedor = emailProveedor;
        this.nombreCategoria = nombreCategoria;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getNifProveedor() {
        return nifProveedor;
    }

    public void setNifProveedor(String nifProveedor) {
        this.nifProveedor = nifProveedor;
    }

    public String getTelefonoProveedor() {
        return telefonoProveedor;
    }

    public void setTelefonoProveedor(String telefonoProveedor) {
        this.telefonoProveedor = telefonoProveedor;
    }

    public String getEmailProveedor() {
        return emailProveedor;
    }

    public void setEmailProveedor(String emailProveedor) {
        this.emailProveedor = emailProveedor;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String toStringTuneado(){
       return "Nombre -> " + getNombreProducto() + "\n" + "Stock -> " + getStock() + "\n";
   }
    public String toStringTuneadoMetodo11A(){
        return "ID Producto -> " + getId_producto() + "\n" +
                "Nombre proveedor  -> " + getNombreProveedor() + "\n" +
                "NIF proveedor -> " + getNifProveedor() +"\n" +
                "Telefono proveedor -> " + getTelefonoProveedor() +"\n" +
                "Email -> " + getEmailProveedor() + "\n" +
                "Nombre categoría -> " + getNombreCategoria() + "\n"
                ;
    }
    public String toStringTuneadoMetodo11B(){
        return "ID Producto -> " + getId_producto() + "\n" +
                "Nombre producto -> " + getNombreProducto() + "\n" +
                "Precio producto -> " + getPrecio() + "\n" +
                "Stock producto -> " + getStock() + "\n" +
                "Nombre proveedor  -> " + getNombreProveedor() + "\n" +
                "NIF proveedor -> " + getNifProveedor() +"\n" +
                "Telefono proveedor -> " + getTelefonoProveedor() +"\n" +
                "Email -> " + getEmailProveedor() + "\n" +
                "Nombre categoría -> " + getNombreCategoria() + "\n"
                ;
    }

}
