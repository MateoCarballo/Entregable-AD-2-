public class Categoria {
    private final int idCategoria;
    private final String nombreCategoria;

    public Categoria(int idCategoria, String nombreCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    @Override
    public String toString() {
        return  "ID de la categoria -> " + idCategoria + "\n" +
                "Nombre de la categoria -> " + nombreCategoria ;
    }
}
