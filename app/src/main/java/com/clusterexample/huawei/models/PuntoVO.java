package com.clusterexample.huawei.models;

import java.util.ArrayList;
import java.util.List;

public class PuntoVO {

    public String ID_PAIS = "";
    public String ID_CIUDAD = "";
    public String ID_DIVISION = "";
    public String NOMBRE = "";
    public String DIRECCION = "";
    public String LONGITUDE = "";
    public String LATITUDE = "";
    public String TELEFONOS = "";
    public String ID_PARQUEADERO = "";
    public List<HorariosVO> HORARIOS = new ArrayList<HorariosVO>();
    public List<ProductosVO> PRODUCTOS = new ArrayList<ProductosVO>();

    public String getID_PAIS() {
        return ID_PAIS;
    }

    public void setID_PAIS(String ID_PAIS) {
        this.ID_PAIS = ID_PAIS;
    }

    public String getID_CIUDAD() {
        return ID_CIUDAD;
    }

    public void setID_CIUDAD(String ID_CIUDAD) {
        this.ID_CIUDAD = ID_CIUDAD;
    }

    public String getID_DIVISION() {
        return ID_DIVISION;
    }

    public void setID_DIVISION(String ID_DIVISION) {
        this.ID_DIVISION = ID_DIVISION;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public List<HorariosVO> getHORARIOS() {
        return HORARIOS;
    }

    public void setHORARIOS(List<HorariosVO> HORARIOS) {
        this.HORARIOS = HORARIOS;
    }

    public List<ProductosVO> getPRODUCTOS() {
        return PRODUCTOS;
    }

    public void setPRODUCTOS(List<ProductosVO> PRODUCTOS) {
        this.PRODUCTOS = PRODUCTOS;
    }

    public String getTELEFONOS() {
        return TELEFONOS;
    }

    public void setTELEFONOS(String TELEFONOS) {
        this.TELEFONOS = TELEFONOS;
    }

    public String getID_PARQUEADERO() {
        return ID_PARQUEADERO;
    }

    public void setID_PARQUEADERO(String ID_PARQUEADERO) {
        this.ID_PARQUEADERO = ID_PARQUEADERO;
    }
}
