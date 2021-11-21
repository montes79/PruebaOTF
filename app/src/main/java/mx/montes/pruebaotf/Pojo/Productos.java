package mx.montes.pruebaotf.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Productos implements Serializable {

    @SerializedName("product_id")
    @Expose
    private int product_id;


    @SerializedName("lang_id")
    @Expose
    private int lang_id;


    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("short_description")
    @Expose
    private String short_description;

    public Productos(int product_id, int lang_id, String name, String short_description) {
        this.product_id = product_id;
        this.lang_id = lang_id;
        this.name = name;
        this.short_description = short_description;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getLang_id() {
        return lang_id;
    }

    public String getName() {
        return name;
    }

    public String getShort_description() {
        return short_description;
    }
}
