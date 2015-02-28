package com.izv.pruebajuego;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by rober on 25/02/2015.
 */
public class Plataforma {

    private VistaJuego vista;
    private Bitmap bmp;
    private int ancho, alto;
    private int posX, posY;

    public Plataforma(VistaJuego vista, Bitmap bmp, int posX, int posY) {
        this.vista = vista;
        this.bmp = bmp;
        this.posX = posX;
        this.posY = posY;
        ancho = bmp.getWidth();
        alto = bmp.getHeight();
    }

    public void dibujar(Canvas canvas) {
        canvas.drawBitmap(bmp, posX, posY, null);
    }

    public void moverDerecha(){
        if(posX + bmp.getWidth() < vista.getWidth()){
            posX = posX + 10;
        }
    }

    public void moverIzquierda(){
        if(posX>0){
            posX = posX - 10;
        }
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
