package com.clusterexample.huawei.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String APP_SETTINGS = "APP_SETTINGS";
    private static final String USUARIO_INFO = "USUARIO_INFO";
    private static final String INFO_LOGIN = "INFO_LOGIN";

    static SharedPreferences userInfo;
    static SharedPreferences.Editor editorInfo;

    private SharedPreferencesManager() {

    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getSharedPreferencesInfo(Context context) {
        return context.getSharedPreferences(USUARIO_INFO, Context.MODE_PRIVATE);
    }

    public static String getEntro(Context context) {
        return getSharedPreferences(context).getString("entro", "no");
    }

    public static void setEntro(Context context, String entro) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("entro", entro);
        editor.commit();
    }

    public static String getDistancia(Context context) {
        return getSharedPreferences(context).getString("Distancia", "1");
    }

    public static void setDistancia(Context context, String Distancia) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("Distancia", Distancia);
        editor.commit();
    }

    public static String getBusqueda(Context context) {
        return getSharedPreferences(context).getString("busqueda", "0");
    }

    public static void setBusqueda(Context context, String Distancia) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("busqueda", Distancia);
        editor.commit();
    }

    public static String getLatitud(Context context) {
        return getSharedPreferences(context).getString("latitud", "0");
    }

    public static void setLatitud(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("latitud", token);
        editor.commit();
    }

    public static String getLongitud(Context context) {
        return getSharedPreferences(context).getString("longitud", "0");
    }

    public static void setLongitud(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("longitud", token);
        editor.commit();
    }

    public static String getUserData(Context context) {
        return getSharedPreferences(context).getString("user_data", "0");
    }

    public static void setUserData(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("user_data", token);
        editor.commit();
    }

    public static String getVersionAppStr(Context context) {
        return getSharedPreferences(context).getString("v_app", "2.6");
    }

    public static void setVersionAppStr(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("v_app", token);
        editor.commit();
    }

    public static String getIdPais(Context context) {
        return getSharedPreferences(context).getString("id_pais", "1");
    }

    public static void setIdPais(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("id_pais", token);
        editor.commit();
    }

    public static String getUrlCuentas(Context context) {
        return getSharedPreferences(context).getString("url_cuentas", "http://app.servientrega.com/co/dev/account/v1.0/");

    }

    public static void setUrlCuentas(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("url_cuentas", token);
        editor.commit();
    }

    public static String getUrlDominioGeneral(Context context) {
        return getSharedPreferences(context).getString("url_dominio", "http://app.servientrega.com/co/locations/v1.0/");
    }

    public static void setUrlDominioGeneral(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("url_dominio", token);
        editor.commit();
    }

    public static String getUrlRastreo(Context context) {
        return getSharedPreferences(context).getString("url_rastreo", "http://app.servientrega.com/co/dev/tracking/v1.1/");

    }

    public static void setUrlRatequote(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("url_ratequote", token);
        editor.commit();
    }

    public static String getUrlRatequote(Context context) {
        return getSharedPreferences(context).getString("url_ratequote", "0"); // estos funcionan al reves se supone que arranca sin datos pues no se utiliza dentro del app sino web entonces se usa para validar que el modulo exista
    }

    public static void setUrlPickup(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("url_pickup", token);
        editor.commit();
    }

    public static String getUrlPickup(Context context) {
        return getSharedPreferences(context).getString("url_pickup", "0"); //
    }

    public static void setUrlWeb(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("url_web", token);
        editor.commit();
    }

    public static String getUrlWeb(Context context) {
        return getSharedPreferences(context).getString("url_web", "http://app.servientrega.com/"); //
    }

    public static void setUrlRastreo(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("url_rastreo", token);
        editor.commit();
    }

    public static String getNombrePais(Context context) {
        return getSharedPreferences(context).getString("nombre_pais", "Colombia");
    }

    public static void setNombrePais(Context context, String token) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("nombre_pais", token);
        editor.commit();
    }

    public static String getConfigInicial(Context context) {
        return getSharedPreferences(context).getString("configini", "no");
    }

    public static void setConfigInicial(Context context, String entro) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("configini", entro);
        editor.commit();
    }



}
