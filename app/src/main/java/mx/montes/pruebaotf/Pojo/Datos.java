package mx.montes.pruebaotf.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Datos implements Serializable {

    @SerializedName("activation")
    @Expose
    private Activacion activacion;


    @SerializedName("product_lang")
    @Expose
    private List<Productos> productos;


    public Datos(Activacion activacion) {
        this.activacion = activacion;
    }

    public Datos(List<Productos> productos) {
        this.productos = productos;
    }

    public Activacion getActivacion() {
        return activacion;
    }


    public List<Productos> getProductos() {
        return productos;
    }
}
