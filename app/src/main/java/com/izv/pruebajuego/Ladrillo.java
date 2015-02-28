package com.izv.pruebajuego;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by rober on 26/02/2015.
 */
public class Ladrillo {

    private VistaJuego vista;
    private Bitmap bmp;
    private int ancho, alto;
    private int posX, posY;
    private boolean eliminado;

    public Ladrillo(VistaJuego vista, Bitmap bmp, int posX, int posY) {
        this.vista = vista;
        this.bmp = bmp;
        this.posX = posX;
        this.posY = posY;
        ancho = bmp.getWidth();
        alto = bmp.getHeight();
        eliminado = false;
    }

    public Ladrillo(Bitmap bmp){
        this.bmp = bmp;
        ancho = bmp.getWidth();
        alto = bmp.getHeight();
        eliminado = false;
    }

    public void dibujar(Canvas canvas) {
        canvas.drawBitmap(bmp, posX, posY, null);
    }

    public void eliminar(){
        posX = 0-ancho;
        posY = 0-alto;
    }

    public VistaJuego getVista() {
        return vista;
    }

    public void setVista(VistaJuego vista) {
        this.vista = vista;
    }

    public void setEliminado(boolean eliminado){
        this.eliminado = eliminado;
    }

    public boolean isEliminado(){
        return eliminado;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
