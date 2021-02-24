package com.clusterexample.huawei.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.clusterexample.huawei.ActivityMapaClusterHuawei;
import com.clusterexample.huawei.MainActivity;
import com.clusterexample.huawei.models.HorariosVO;
import com.clusterexample.huawei.models.ProductosVO;
import com.clusterexample.huawei.models.PuntoVO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Servicios {

    private Activity activity;
    private AsyncHttpClient mClient;
    private String url = "http://app.servientrega.com/wsPuntosServicio/api/";
    private String urlPuntos = "http://app.servientrega.com/co/rest/locations/v1.0/"; //cambio 25 de mayo
    private static Singleton singleton = Singleton.getInstance();
    private Context context;

    private String urlGeneral = "http://app.servientrega.com/co/locations/v1.0/api/";

    public Servicios(AppCompatActivity activity) {
        mClient = new AsyncHttpClient();
        this.activity = activity;
    }

    public Servicios() {
        mClient = new AsyncHttpClient();
    }

    public Servicios(Context context) {
        mClient = new AsyncHttpClient();
        this.context = context;
    }

    public void ServicioPuntoCercanosHomeHuawei(final MainActivity activity, String lat, String lon, String distancia, String id_producto, String abierto, int pagina) {
        singleton.setDatosUrl(lat + "," + lon + "," + distancia + "," + id_producto + "," + abierto);
        final String url_ = urlGeneral + lat + "/" + lon + "/" + distancia + "/" + id_producto + "/" + abierto + "/" + pagina + "/200";
        Log.e("URL: ", url_);
        mClient.setTimeout(5000);
        mClient.get(url_, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jArray = response.getJSONArray("Results");
                    String total_paginas = response.getString("TotalPages");
                    if (jArray.length() == 0) {
                        Toast.makeText(context, "No hay puntos para agregar al mapa", Toast.LENGTH_LONG).show();
                    } else {
                        final List<PuntoVO> puntos = new ArrayList<PuntoVO>();
                        puntos.clear();
                        for (int j = 0; j < jArray.length(); j++) {
                            final List<HorariosVO> horarios = new ArrayList<HorariosVO>();
                            horarios.clear();
                            PuntoVO punto = new PuntoVO();
                            try {
                                JSONObject jObjt = jArray.getJSONObject(j);
                                punto.setID_PAIS(jObjt.getString("ID_PAIS"));
                                punto.setID_CIUDAD(jObjt.getString("ID_CIUDAD"));
                                punto.setID_DIVISION(jObjt.getString("ID_DIVISION"));
                                punto.setNOMBRE(jObjt.getString("NOMBRE"));
                                punto.setDIRECCION(jObjt.getString("DIRECCION"));
                                punto.setLONGITUDE(jObjt.getString("LONGITUDE"));
                                punto.setLATITUDE(jObjt.getString("LATITUDE"));
                                punto.setID_PARQUEADERO(jObjt.getString("ID_PARQUEADERO"));
                                JSONArray jArraytel = jObjt.getJSONArray("TELEFONOS");
                                if (jArraytel.length() == 0) {
                                    punto.setTELEFONOS("-");
                                } else {
                                    if (jArraytel.length() >= 2) {
                                        String telefonos = "";
                                        for (int tel = 0; tel < 2; tel++) {
                                            JSONObject jObjtCol = jArraytel.getJSONObject(tel);
                                            telefonos += jObjtCol.getString("NUMERO_TELE_DIVISION") + " ";
                                        }
                                        punto.setTELEFONOS(telefonos);
                                    } else {
                                        JSONObject jObjtCol = jArraytel.getJSONObject(0);
                                        punto.setTELEFONOS(jObjtCol.getString("NUMERO_TELE_DIVISION"));
                                    }

                                }
                                JSONArray jArrayHorarios = jObjt.getJSONArray("HORARIOS");
                                if (jArrayHorarios.length() != 0) {
                                    for (int i = 0; i < jArrayHorarios.length(); i++) {
                                        HorariosVO horario = new HorariosVO();
                                        try {
                                            JSONObject jObjth = jArrayHorarios.getJSONObject(i);
                                            horario.setDIA_SEMANA(jObjth.getString("DIA_SEMANA"));
                                            horario.setHORA_INICIAL_AM(jObjth.getString("HORA_INICIAL_AM"));
                                            horario.setHORA_FINAL_AM(jObjth.getString("HORA_FINAL_AM"));
                                            horario.setHORA_FINAL_PM(jObjth.getString("HORA_FINAL_PM"));
                                        } catch (Exception e) {

                                        }
                                        horarios.add(horario);
                                    }
                                    punto.setHORARIOS(horarios);
                                }
                                JSONArray jArrayPorductos = jObjt.getJSONArray("PRODUCTOS");
                                final List<ProductosVO> productos = new ArrayList<ProductosVO>();
                                productos.clear();
                                if (jArrayPorductos.length() != 0) {
                                    for (int k = 0; k < jArrayPorductos.length(); k++) {
                                        ProductosVO producto = new ProductosVO();
                                        try {
                                            JSONObject jObjth = jArrayPorductos.getJSONObject(k);
                                            producto.setID_PRODUCTO(jObjth.getInt("ID_PRODUCTO") + "");
                                            producto.setNOMBRE_PRODUCTO(jObjth.getString("NOMBRE_PRODUCTO"));
                                        } catch (Exception e) {

                                        }
                                        productos.add(producto);
                                    }
                                    punto.setPRODUCTOS(productos);
                                }
                            } catch (Exception e) {
                                Log.e("Exception: ", e.getLocalizedMessage());
                            }
                            puntos.add(punto);
                        }
                        activity.agregarPinesHuawei(puntos, total_paginas);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONExepction: ", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable e) {
                Toast.makeText(context, "Failure: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void ServicioPuntoCercanosHuawei(final ActivityMapaClusterHuawei activity, String lat, String lon, String distancia, String id_producto, String abierto, int pagina) {
        singleton.setDatosUrl(lat + "," + lon + "," + distancia + "," + id_producto + "," + abierto);
        mClient.setTimeout(5000);
        //  http://app.servientrega.com/findlocations/api/{latitude}/{longitude}/{distancia}/{idproducto}/{abiertos}/{pagina}/{resultadosXpagina}
        String url_ = urlGeneral + lat + "/" + lon + "/" + distancia + "/" + id_producto + "/" + abierto + "/" + pagina + "/200";
        // String url_ = "http://app.servientrega.com/wsPuntosServicio/api/PuntosServicio/1/0/5000";
        Log.e("URL: ", url_);
        mClient.get(url_, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jArray = response.getJSONArray("Results");
                    String total_paginas = response.getString("TotalPages");
                    if (jArray.length() == 0) {
                        Toast.makeText(context, "No hay puntos para agregar al mapa", Toast.LENGTH_LONG).show();
                    } else {
                        final List<PuntoVO> puntos = new ArrayList<PuntoVO>();
                        puntos.clear();
                        for (int j = 0; j < jArray.length(); j++) {
                            final List<HorariosVO> horarios = new ArrayList<HorariosVO>();
                            horarios.clear();
                            PuntoVO punto = new PuntoVO();
                            try {
                                JSONObject jObjt = jArray.getJSONObject(j);
                                punto.setID_PAIS(jObjt.getString("ID_PAIS"));
                                punto.setID_CIUDAD(jObjt.getString("ID_CIUDAD"));
                                punto.setID_DIVISION(jObjt.getString("ID_DIVISION"));
                                punto.setNOMBRE(jObjt.getString("NOMBRE"));
                                punto.setDIRECCION(jObjt.getString("DIRECCION"));
                                punto.setLONGITUDE(jObjt.getString("LONGITUDE"));
                                punto.setLATITUDE(jObjt.getString("LATITUDE"));
                                punto.setID_PARQUEADERO(jObjt.getString("ID_PARQUEADERO"));
                                JSONArray jArraytel = jObjt.getJSONArray("TELEFONOS");
                                if (jArraytel.length() == 0) {
                                    punto.setTELEFONOS("-");
                                } else {
                                    if (jArraytel.length() >= 2) {
                                        String telefonos = "";
                                        for (int tel = 0; tel < 2; tel++) {
                                            JSONObject jObjtCol = jArraytel.getJSONObject(tel);
                                            telefonos += jObjtCol.getString("NUMERO_TELE_DIVISION") + " ";
                                        }
                                        punto.setTELEFONOS(telefonos);
                                    } else {
                                        JSONObject jObjtCol = jArraytel.getJSONObject(0);
                                        punto.setTELEFONOS(jObjtCol.getString("NUMERO_TELE_DIVISION"));
                                    }

                                }
                                JSONArray jArrayHorarios = jObjt.getJSONArray("HORARIOS");
                                if (jArrayHorarios.length() != 0) {
                                    for (int i = 0; i < jArrayHorarios.length(); i++) {
                                        HorariosVO horario = new HorariosVO();
                                        try {
                                            JSONObject jObjth = jArrayHorarios.getJSONObject(i);
                                            horario.setDIA_SEMANA(jObjth.getString("DIA_SEMANA"));
                                            horario.setHORA_INICIAL_AM(jObjth.getString("HORA_INICIAL_AM"));
                                            horario.setHORA_FINAL_AM(jObjth.getString("HORA_FINAL_AM"));
                                            horario.setHORA_FINAL_PM(jObjth.getString("HORA_FINAL_PM"));
                                        } catch (Exception e) {

                                        }
                                        horarios.add(horario);
                                    }
                                    punto.setHORARIOS(horarios);
                                }
                                JSONArray jArrayPorductos = jObjt.getJSONArray("PRODUCTOS");
                                final List<ProductosVO> productos = new ArrayList<ProductosVO>();
                                productos.clear();
                                if (jArrayPorductos.length() != 0) {
                                    for (int k = 0; k < jArrayPorductos.length(); k++) {
                                        ProductosVO producto = new ProductosVO();
                                        try {
                                            JSONObject jObjth = jArrayPorductos.getJSONObject(k);
                                            producto.setID_PRODUCTO(jObjth.getInt("ID_PRODUCTO") + "");
                                            producto.setNOMBRE_PRODUCTO(jObjth.getString("NOMBRE_PRODUCTO"));
                                        } catch (Exception e) {

                                        }
                                        productos.add(producto);
                                    }
                                    punto.setPRODUCTOS(productos);
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "Exception: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                            puntos.add(punto);
                        }

                        singleton.setLista_puntos(puntos);
                        singleton.setTotal_pag(total_paginas);
                        singleton.setZoomMap(13);
                        activity.agregarPines(puntos, total_paginas);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "JSONExceptio " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable e) {
                Toast.makeText(context, "Failure " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
