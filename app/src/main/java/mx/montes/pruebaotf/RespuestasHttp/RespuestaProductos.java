package mx.montes.pruebaotf.RespuestasHttp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import mx.montes.pruebaotf.Pojo.Datos;

public class RespuestaProductos implements Serializable {

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("data")
    @Expose
    private Datos datos;


    @SerializedName("error")
    @Expose
    private String error;


    public RespuestaProductos(String response, Datos datos, String error) {
        this.response = response;
        this.datos = datos;
        this.error = error;
    }

    public String getResponse() {
        return response;
    }

    public Datos getDatos() {
        return datos;
    }

    public String getError() {
        return error;
    }
}
