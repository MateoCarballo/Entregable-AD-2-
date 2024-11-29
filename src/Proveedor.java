public class Proveedor {
    private int idProveedor;
    private String nombreProveedor;
    private Contacto contacto;

    // Constructor
    public Proveedor(int idProveedor, String nombreProveedor, Contacto contacto) {
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
        this.contacto = contacto;
    }

    // Getters y setters
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    // Clase interna Contacto
    public static class Contacto {
        private String nombreContacto;
        private String nif;
        private String telefono;
        private String email;

        public Contacto(String nombreContacto, String nif, String telefono, String email) {
            this.nombreContacto = nombreContacto;
            this.nif = nif;
            this.telefono = telefono;
            this.email = email;
        }
        @Override
        public String toString() {
            return "Contacto " + "\n" +
                    "\u2550" + "\u2550" + "\u2550" + "\u2550" + "\n" +
                    "Nombre del Contacto -> " + nombreContacto + "\n" +
                    "NIF -> " + nif + "\n" +
                    "Telefono -> " + telefono + "\n" +
                    "Email -> " + email + "\n" +
                    "\u2550" + "\u2550" + "\u2550" + "\u2550" + "\n";
        }

        // Getters y setters
        public String getNombreContacto() {
            return nombreContacto;
        }

        public void setNombreContacto(String nombreContacto) {
            this.nombreContacto = nombreContacto;
        }

        public String getNif() {
            return nif;
        }

        public void setNif(String nif) {
            this.nif = nif;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @Override
    public String toString() {
        return "\u2550" + "\u2550" + "\u2550" + "\u2550" + "\n" +
                "Proveedor " + "\n" +
                "ID proveedor -> " + idProveedor + "\n" +
                "Nombre proveedor -> " + nombreProveedor + "\n"
                + contacto + "\n";
    }
}
