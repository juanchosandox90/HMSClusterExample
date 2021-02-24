package com.clusterexample.huawei.utils;

import com.clusterexample.huawei.models.PuntoVO;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton INSTANCE = new Singleton();

    private Singleton() {

    }

    public static Singleton getInstance() {
        return INSTANCE;
    }

    public String posicion_sel = "-";
    private List<PuntoVO> arrayMapa;
    public String ciudad= "Seleccione/-";
    public String soluciones= "Seleccione/-";
    public Boolean touch_map = false;
    public String datosUrl="";
    public Boolean  hacerFiltro = false;
    public String nombreBusqueda = "";

    public String tipoProductoNew ="0";
    public String abiertoNew = "0";
    public String filtroPorCiudad = "";
    public String nombreCiudadNew = "";
    public int pasoZoom = 0;

    public String mostrarMenuIniciarSesion = "";
    public String mostrarMenuCrearCuenta = "";
    public String vieneDeRecolecion = "no";
    public String pasarArecoleccion = "no";

    public String getPasarArecoleccion() {
        return pasarArecoleccion;
    }

    public void setPasarArecoleccion(String pasarArecoleccion) {
        this.pasarArecoleccion = pasarArecoleccion;
    }

    public String getVieneDeRecolecion() {
        return vieneDeRecolecion;
    }

    public void setVieneDeRecolecion(String vieneDeRecolecion) {
        this.vieneDeRecolecion = vieneDeRecolecion;
    }

    public String getMostrarMenuCrearCuenta() {
        return mostrarMenuCrearCuenta;
    }

    public void setMostrarMenuCrearCuenta(String mostrarMenuCrearCuenta) {
        this.mostrarMenuCrearCuenta = mostrarMenuCrearCuenta;
    }

    public String getMostrarMenuIniciarSesion() {
        return mostrarMenuIniciarSesion;
    }

    public void setMostrarMenuIniciarSesion(String mostrarMenuIniciarSesion) {
        this.mostrarMenuIniciarSesion = mostrarMenuIniciarSesion;
    }

    public int getPasoZoom() {
        return pasoZoom;
    }

    public void setPasoZoom(int pasoZoom) {
        this.pasoZoom = pasoZoom;
    }

    public String getTipoDocumentoBusqueda() {
        return tipoDocumentoBusqueda;
    }

    public void setTipoDocumentoBusqueda(String tipoDocumentoBusqueda) {
        this.tipoDocumentoBusqueda = tipoDocumentoBusqueda;
    }

    public String tipoDocumentoBusqueda = "";

    public String getNombreCiudadNew() {
        return nombreCiudadNew;
    }

    public void setNombreCiudadNew(String nombreCiudadNew) {
        this.nombreCiudadNew = nombreCiudadNew;
    }

    public String idCiudadNew = "0";

    public String getIdCiudadNew() {
        return idCiudadNew;
    }

    public void setIdCiudadNew(String idCiudadNew) {
        this.idCiudadNew = idCiudadNew;
    }

    public float zoomMap = 17.0f;

    public float getZoomMap() {
        return zoomMap;
    }

    public void setZoomMap(float zoomMap) {
        this.zoomMap = zoomMap;
    }

    public String getFiltroPorCiudad() {
        return filtroPorCiudad;
    }

    public void setFiltroPorCiudad(String filtroPorCiudad) {
        this.filtroPorCiudad = filtroPorCiudad;
    }

    public String getTipoProductoNew() {
        return tipoProductoNew;
    }

    public void setTipoProductoNew(String tipoProductoNew) {
        this.tipoProductoNew = tipoProductoNew;
    }

    public String getAbiertoNew() {
        return abiertoNew;
    }

    public void setAbiertoNew(String abiertoNew) {
        this.abiertoNew = abiertoNew;
    }



    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }

    public Boolean getHacerFiltro() {
        return hacerFiltro;
    }

    public void setHacerFiltro(Boolean hacerFiltro) {
        this.hacerFiltro = hacerFiltro;
    }

    public String getCiudadFiltroNuevo() {
        return ciudadFiltroNuevo;
    }

    public void setCiudadFiltroNuevo(String ciudadFiltroNuevo) {
        this.ciudadFiltroNuevo = ciudadFiltroNuevo;
    }

    public String ciudadFiltroNuevo="";

    public List<PuntoVO> getArrayMapaCopia() {
        return arrayMapaCopia;
    }

    public void setArrayMapaCopia(List<PuntoVO> arrayMapaCopia) {
        this.arrayMapaCopia = arrayMapaCopia;
    }

    private List<PuntoVO> arrayMapaCopia;

    // cambio 15 de septiembre

    private List<PuntoVO> lista_puntos = new ArrayList<PuntoVO>();
    private List<PuntoVO> lista_puntos_copia = new ArrayList<PuntoVO>();

    public List<PuntoVO> getLista_puntos_copia() {
        return lista_puntos_copia;
    }

    public void setLista_puntos_copia(List<PuntoVO> lista_puntos_copia) {
        this.lista_puntos_copia = lista_puntos_copia;
    }

    private String total_pag;


    public List<PuntoVO> getLista_puntos() {
        return lista_puntos;
    }

    public void setLista_puntos(List<PuntoVO> lista_puntos) {
        this.lista_puntos = lista_puntos;
    }

    public String getTotal_pag() {
        return total_pag;
    }

    public void setTotal_pag(String total_pag) {
        this.total_pag = total_pag;
    }

    public String getDatosUrl() {
        return datosUrl;
    }

    public void setDatosUrl(String datosUrl) {
        this.datosUrl = datosUrl;
    }

    public List<PuntoVO> getArrayMapa() {
        return arrayMapa;
    }

    public void setArrayMapa(List<PuntoVO> arrayMapa) {
        this.arrayMapa = arrayMapa;
    }

    public String getPosicion_sel() {
        return posicion_sel;
    }

    public void setPosicion_sel(String posicion_sel) {
        this.posicion_sel = posicion_sel;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getSoluciones() {
        return soluciones;
    }

    public void setSoluciones(String soluciones) {
        this.soluciones = soluciones;
    }

    public Boolean getTouch_map() {
        return touch_map;
    }

    public void setTouch_map(Boolean touch_map) {
        this.touch_map = touch_map;
    }
}