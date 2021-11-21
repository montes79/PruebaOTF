package mx.montes.pruebaotf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


import mx.montes.pruebaotf.RespuestasHttp.RespuestaProductos;
import mx.montes.pruebaotf.aSync.ConexionHTTP;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import mx.montes.pruebaotf.Pojo.Productos;

public class ProductosVista extends AppCompatActivity {

    TextView txtLicencia;
    Button btnMostrar;
    Button btnQuitar;
    String device_id;
    String api_key;
    List<Productos> listaTotalProductos;
    List<Productos> listaFiltrada;
    TableLayout tablaProductos;
    Context contexto;
    TextView txtTituloListado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_productosvista);
        txtLicencia=findViewById(R.id.txtLicencia);
        device_id=null;
        api_key=null;
        contexto=this;
        listaFiltrada=null;
        listaTotalProductos=null;
        btnMostrar=findViewById(R.id.btnMostrar);
        btnQuitar=findViewById(R.id.btnQuitar);
        tablaProductos=findViewById(R.id.tablaProductos);
        txtTituloListado=findViewById(R.id.txtTituloListado);

        setLicencia();

        btnMostrar.setOnClickListener(yoBoton -> {
            mostrarProductos();
        });




        btnQuitar.setOnClickListener(yoBoton -> {
            removerLicenciaDispositivo();
        });


    }



    private void setLicencia()
    {
        SharedPreferences preferencias=getSharedPreferences(getString(R.string.app_paquete),MODE_PRIVATE);
         api_key=preferencias.getString(getString(R.string.license_api_key),null);
         device_id=preferencias.getString(getString(R.string.device_id_key),null);

        if(api_key!=null)
            txtLicencia.setText(api_key);
    }



    private void mostrarProductos(){

        ProgressDialog dialogo=new ProgressDialog(this);
        dialogo.setTitle(getString(R.string.titulo_dialogo_productos));
        dialogo.show();

        HashMap<String,String> mapa= new HashMap<>();
        mapa.put("api-key", api_key);
        mapa.put("device-id", device_id);

        Call<RespuestaProductos> call = ConexionHTTP.provideApiService().recuperaProductos(mapa);
        call.enqueue(new Callback<RespuestaProductos>() {
            @Override
            public void onResponse(@NotNull Call<RespuestaProductos> call, @NotNull Response<RespuestaProductos> response) {
                dialogo.dismiss();


                if (response.isSuccessful()) {
                    assert response.body() != null;
                    RespuestaProductos respuesta = response.body();
                    if(respuesta.getResponse().compareTo(getString(R.string.respuesta_exito))==0) {
                        listaTotalProductos=respuesta.getDatos().getProductos();
                        if(listaTotalProductos!=null){
                            System.out.println("Productos Cantidad de recibidos:"+listaTotalProductos.size());
                            listaFiltrada=listaTotalProductos.stream().filter(p->p.getLang_id()==1).collect(Collectors.toList());
                            listaTotalProductos.removeIf(p->p.getLang_id()!=1);
                            System.out.println("Productos Cantidad despues de eliminar:"+listaTotalProductos.size());
                            System.out.println("Productos Cantidad de elementos de la lista filtrada :"+listaFiltrada.size());

                            // construirTablaProductos(listaFiltrada); // Es indistinto.. listaTotalProductos o  listaFiltrada
                            construirTablaProductos(listaFiltrada);
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
            public void onFailure(@NotNull Call<RespuestaProductos> call, @NotNull Throwable error) {
                notificaUsuario(getString(R.string.texto_titulo_dialogo), getString(R.string.error_conexion));
            }

        });

    }



    private void construirTablaProductos(List<Productos> lista) {
        tablaProductos.removeAllViews();
        LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout_producto;
        RelativeLayout fila_divisor;
        RelativeLayout fila_encabezado;
        fila_encabezado = (RelativeLayout) inflater.inflate(R.layout.encabezado_productos,null);
        fila_divisor = (RelativeLayout) inflater.inflate(R.layout.divisor_productos,null);
        tablaProductos.addView(fila_encabezado);
        tablaProductos.addView(fila_divisor);

        txtTituloListado.setVisibility(View.VISIBLE);

        for(int i=0;i<lista.size();i++) {
            int enumerador=i+1;
            Productos producto=lista.get(i);
            layout_producto=(RelativeLayout) inflater.inflate(R.layout.datos_producto,tablaProductos, false);
            fila_divisor = (RelativeLayout) inflater.inflate(R.layout.divisor_productos,null);

            TextView tv1=layout_producto.findViewById(R.id.txtNombre);
            TextView tv2=layout_producto.findViewById(R.id.txtDescripcion);
            tv1.setText(enumerador+".- "+producto.getName());

            if(producto.getShort_description()!=null){
                if(producto.getShort_description().length()!=0)
                    tv2.setText(producto.getShort_description());
                else
                    tv2.setText(getString(R.string.sin_descripcion));
            } else
                tv2.setText(getString(R.string.sin_descripcion));

            tablaProductos.addView(layout_producto);
            tablaProductos.addView(fila_divisor);

        }




    }



    private void removerLicenciaDispositivo() {
        AlertDialog dialogo;
        AlertDialog.Builder constructor=new AlertDialog.Builder(this);
        constructor.setTitle(getString(R.string.texto_titulo_dialogo));
        constructor.setMessage(getString(R.string.texto_confirmar_quitar_licencia));

        constructor.setPositiveButton(getString(R.string.texto_respuesta_SI), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {
                remueveLicencia();
            }
        });

        constructor.setNegativeButton(getString(R.string.texto_respuesta_NO), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        });

        dialogo=constructor.create();
        dialogo.show();
    }


    private void remueveLicencia(){
        SharedPreferences.Editor preferencias=getSharedPreferences(getString(R.string.app_paquete),MODE_PRIVATE).edit();
        preferencias.clear();
        preferencias.commit();
        preferencias.apply();
        navegarPantallaPrincipal();

    }


    private void navegarPantallaPrincipal() {
        Intent intentoPantalla1=new Intent(this,Principal.class);
        startActivity(intentoPantalla1);
    }


    @Override
    public void onBackPressed() {
        //No hacer nada :) para anular pila.
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



}