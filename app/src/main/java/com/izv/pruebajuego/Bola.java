package com.izv.pruebajuego;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by rober on 24/02/2015.
 */
public class Bola {

    private VistaJuego vista;
    private Bitmap bmp;
    private int ancho, alto;
    private int posX, posY;
    private int direccionX, direccionY;

    public Bola(VistaJuego vista, Bitmap bmp) {
        this.vista = vista;
        this.bmp = bmp;
        this.ancho=bmp.getWidth();
        this.alto=bmp.getHeight();
        Random rnd = new Random();
        posX=vista.getWidth()/2;
        posY=0;
        direccionX=15;
        direccionY=15;
    }

    public Bola(VistaJuego vista, Bitmap bmp, int ancho, int alto, int posX, int posY, int direccionX, int direccionY) {
        this.vista = vista;
        this.bmp = bmp;
        this.ancho = ancho;
        this.alto = alto;
        this.posX = posX;
        this.posY = posY;
        this.direccionX = direccionX;
        this.direccionY = direccionY;
    }

    public void dibujar(Canvas canvas) {
        movimiento();
        canvas.drawBitmap(bmp, posX, posY, null);
    }

    private void movimiento(){
        if (posX > vista.getWidth() - ancho - direccionX || posX + direccionX < 0) {
            direccionX = -direccionX;
        }
        posX = posX + direccionX;
        if (posY + direccionY < 0) {
            direccionY = -direccionY;
        }
        posY = posY + direccionY;
        if(posY > VistaJuego.getLimite() - alto - direccionY){
            vista.setFuncionando(false);
        }
    }

    public boolean colisiona(Plataforma p,int delta){
        if(this.posX + this.ancho - delta < p.getPosX()){
            return false;
        }
        if(this.posY + this.alto -delta < p.getPosY()){
            return false;
        }
        if(this.posX > p.getPosX() + p.getAncho() -delta){
            return false;
        }
        if(this.posY > p.getPosY() + p.getAlto() -delta){
            return false;
        }
        return true;
    }

    public boolean colisiona(Ladrillo l,int delta){
        if(this.posX + this.ancho - delta < l.getPosX()){
            return false;
        }
        if(this.posY + this.alto -delta < l.getPosY()){
            return false;
        }
        if(this.posX > l.getPosX() + l.getAncho() -delta){
            return false;
        }
        if(this.posY > l.getPosY() + l.getAlto() -delta){
            return false;
        }
        return true;
    }

    public void cambiarDireccion(){
        direccionY = -direccionY;
        //direccionX = -direccionX;
    }

    public void eliminar(){
        direccionX = 0;
        direccionY = 0;
        posX = 0-bmp.getWidth();
        posY = 0-bmp.getHeight();
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

    public int getDireccionX() {
        return direccionX;
    }

    public void setDireccionX(int direccionX) {
        this.direccionX = direccionX;
    }

    public int getDireccionY() {
        return direccionY;
    }

    public void setDireccionY(int direccionY) {
        this.direccionY = direccionY;
    }
}
