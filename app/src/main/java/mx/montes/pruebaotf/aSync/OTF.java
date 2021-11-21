package mx.montes.pruebaotf.aSync;

import java.util.Map;

import mx.montes.pruebaotf.RespuestasHttp.RespuestaActivacion;
import mx.montes.pruebaotf.RespuestasHttp.RespuestaProductos;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OTF {

    @FormUrlEncoded
    @POST("api/v2/license/activate")
    Call<RespuestaActivacion> activarLicencia(
            @Field("serial") String serial,
            @Field("hostname") String hostname,
            @Field("device_id") String device_id,
            @Field("device_information") String device_information
    );



    @GET("api/v2/products/menu")
    Call<RespuestaProductos> recuperaProductos(
            @HeaderMap Map<String, String> headers

    );

}
