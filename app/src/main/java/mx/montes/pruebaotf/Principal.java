package mx.montes.pruebaotf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import mx.montes.pruebaotf.Pojo.Activacion;
import mx.montes.pruebaotf.RespuestasHttp.RespuestaActivacion;
import mx.montes.pruebaotf.aSync.ConexionHTTP;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Principal extends AppCompatActivity {

    EditText editSerial;
    EditText editHostname;
    Button btnActivar;
    Activacion datosActivacion;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);

        editSerial = findViewById(R.id.editSerial);
        editHostname = findViewById(R.id.editHostname);
        btnActivar = findViewById(R.id.btnActivar);
        datosActivacion=null;

        btnActivar.setOnClickListener(yoBoton -> {
            validarFormularioRegistroLicencia();
        });

        inicializaPreferenciasCompartidasFIJOS();
        inicializaBaseDatos();

        if(isDispositivoRegistrado())
            navegarPantallaProductos();


    }


    private void validarFormularioRegistroLicencia()
    {
        String valorSerie=editSerial.getText().toString().trim();
        String valorHost=editHostname.getText().toString().trim();
        boolean b1=valorSerie.isEmpty();
        boolean b2=valorHost.isEmpty();

        if(b1){
            notificaUsuario(getString(R.string.texto_titulo_dialogo),getString(R.string.texto_serie_vacia));
            editSerial.requestFocus();
            return;
        }



        if(b2) {
            notificaUsuario(getString(R.string.texto_titulo_dialogo),getString(R.string.texto_hostname_vacio));
            editHostname.requestFocus();
            return;
        }


        solicitarActivacionLicencia(valorSerie,valorHost);
    }



    private void solicitarActivacionLicencia(String serial,String hostname) {
        ProgressDialog dialogo=new ProgressDialog(this);
        dialogo.setTitle(getString(R.string.titulo_dialogo));
        dialogo.show();

        Call<RespuestaActivacion> call = ConexionHTTP.provideApiService().activarLicencia(serial, hostname, getString(R.string.device_id), getString(R.string.device_information));
        call.enqueue(new Callback<RespuestaActivacion>() {
            @Override
            public void onResponse(@NotNull Call<RespuestaActivacion> call, @NotNull Response<RespuestaActivacion> response) {
                dialogo.dismiss();

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    RespuestaActivacion respuesta = response.body();
                    if(respuesta.getResponse().compareTo(getString(R.string.respuesta_exito))==0) {
                        datosActivacion=respuesta.getDatos().getActivacion();
                        if(datosActivacion!=null){
                            almacenaDatosLicencia();
                            guardaDatosLicenciaBaseDatos();
                            navegarPantallaProductos();
                        }



                    }
                    else {
                        notificaUsuario(getString(R.string.texto_titulo_dialogo), getString(R.string.respuesta_error));
                    }

                } else {
                    dialogo.dismiss();
                    notificaUsuario(getString(R.string.texto_titulo_dialogo), getString(R.string.error_conexion));
                }

            }

            @Override
            public void onFailure(@NotNull Call<RespuestaActivacion> call, @NotNull Throwable error) {
                notificaUsuario(getString(R.string.texto_titulo_dialogo), getString(R.string.error_conexion));
            }

        });

    }





    private boolean isDispositivoRegistrado() {
       boolean respuesta=true;

        SharedPreferences preferencias=getSharedPreferences(getString(R.string.app_paquete),MODE_PRIVATE);
        String api_key=preferencias.getString(getString(R.string.license_api_key),null);

        if(api_key==null)
            respuesta=false;


        return respuesta;
    }


    private void navegarPantallaProductos() {
        Intent intentoPantalla2=new Intent(this, ProductosVista.class);
        startActivity(intentoPantalla2);
    }


    private void inicializaBaseDatos() {
        //TODO Crear 1 tabla para los datos de la licencia : 4 campos

    }

    private void guardaDatosLicenciaBaseDatos(){
        //TODO Actualizar  1 tabla con 1 solo registro con los datos de la licencia : 4 campos

    }


    private void almacenaDatosLicencia() {
        // <-- Respuesta de Activacion ( api-key ,  device-id ,license_id ,terminal_id )

        SharedPreferences preferencias=getSharedPreferences(getString(R.string.app_paquete),MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString(getString(R.string.license_api_key),datosActivacion.getApi_key());
        editor.putString(getString(R.string.device_id_key),datosActivacion.getDevice_id());
        editor.putInt(getString(R.string.license_license_id_key),datosActivacion.getLicense_id());
        editor.putInt(getString(R.string.license_terminal_id_key),datosActivacion.getTerminal_id());
        editor.apply();
    }


    private void inicializaPreferenciasCompartidasFIJOS(){

        // Dispositivo: device_id, device_information + Usuario:    serial , hostname   --> Solicitud Activacion

        SharedPreferences preferencias=getSharedPreferences(getString(R.string.app_paquete),MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString(getString(R.string.device_id_key),getString(R.string.device_id));
        editor.putString(getString(R.string.device_information_key),getString(R.string.device_information));
        editor.apply();
    }



    private void notificaUsuario(String titulo, String mensaje)
    {
        AlertDialog.Builder constructorDialogo=new AlertDialog.Builder(this);
        constructorDialogo.setTitle(titulo);
        constructorDialogo.setMessage(mensaje);
        constructorDialogo.setPositiveButton(getString(R.string.texto_dialogo_ok),null);
        AlertDialog dialogo=constructorDialogo.create();
        dialogo.show();

    }


    @Override
    public void onBackPressed() {
        //No hacer nada :) para anular pila.
    }
}