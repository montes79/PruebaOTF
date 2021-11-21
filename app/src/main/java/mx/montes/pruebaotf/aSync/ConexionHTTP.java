package mx.montes.pruebaotf.aSync;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
//import javax.security.cert.CertificateException;

import mx.montes.pruebaotf.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConexionHTTP {


    public static OkHttpClient clienteHTTP() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder constructor=new OkHttpClient.Builder();
        constructor.readTimeout(2, TimeUnit.MINUTES);
        constructor.connectTimeout(2, TimeUnit.MINUTES);
        constructor.callTimeout(2, TimeUnit.MINUTES);
        constructor.writeTimeout(2, TimeUnit.MINUTES);


        return constructor.addInterceptor(interceptor).addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).cache(null)
                .build();
    }




    public static Retrofit objetoRetroFit(String baseURL, OkHttpClient clienteHttp) {

        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(clienteHttp)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }




    public static OTF provideApiService() {
        return objetoRetroFit(BuildConfig.API_URL, clienteHTTP()).create(OTF.class);
    }


}
