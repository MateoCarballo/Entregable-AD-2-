public class Usuario {
    private String nombreUsuario;
    private int numeroDePedidos;

    public Usuario(){

    }

    public Usuario(String nombreUsuario, int numeroDePedidos) {
        this.nombreUsuario = nombreUsuario;
        this.numeroDePedidos = numeroDePedidos;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String usuario) {
        nombreUsuario = usuario;
    }

    public int getNumeroDePedidos() {
        return numeroDePedidos;
    }

    public void setNumeroDePedidos(int numeroDePedidos) {
        this.numeroDePedidos = numeroDePedidos;
    }

    public String toStringTuneado(){
        return "Nombre -> " + getNombreUsuario() + "\n" + "Número de pedidos -> " + getNumeroDePedidos() + "\n";
    }
}
