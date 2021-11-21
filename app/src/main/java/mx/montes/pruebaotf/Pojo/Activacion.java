package mx.montes.pruebaotf.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Activacion implements Serializable {

    @SerializedName("device_id")
    @Expose
    private String device_id;

    @SerializedName("api_key")
    @Expose
    private String api_key;

    @SerializedName("license_id")
    @Expose
    private int license_id;


    @SerializedName("terminal_id")
    @Expose
    private int terminal_id;


    public Activacion(String device_id, String api_key, int license_id, int terminal_id) {
        this.device_id = device_id;
        this.api_key = api_key;
        this.license_id = license_id;
        this.terminal_id = terminal_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getApi_key() {
        return api_key;
    }

    public int getLicense_id() {
        return license_id;
    }

    public int getTerminal_id() {
        return terminal_id;
    }
}
