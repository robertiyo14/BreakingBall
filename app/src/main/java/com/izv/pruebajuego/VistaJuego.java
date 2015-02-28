package com.izv.pruebajuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

/**
 * Created by rober on 23/02/2015.
 */
public class VistaJuego extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap bmpBola, bmpDerecha, bmpIzqierda, bmpPlataforma, bmpLadrillo;
    private int alto, ancho;
    private HebraJuego hebraJuego;
    Control izquierda, derecha;
    Plataforma plataforma;
    private static int limite;
    private Bola b = null;
    private Ladrillo[] pantalla;
    private static boolean funcionando = false;
    private int contador = 0;

    public VistaJuego(Context context) {
        super(context);
        getHolder().addCallback(this);
        bmpBola = BitmapFactory.decodeResource(getResources(), R.drawable.bola);
        bmpDerecha = BitmapFactory.decodeResource(getResources(), R.drawable.der);
        bmpIzqierda = BitmapFactory.decodeResource(getResources(), R.drawable.izq);
        bmpPlataforma = BitmapFactory.decodeResource(getResources(), R.drawable.plataforma);
        bmpLadrillo = BitmapFactory.decodeResource(getResources(), R.drawable.ladrillo);
        pantalla = new Ladrillo[5];
        for (int i = 0; i < pantalla.length; i++) {
            pantalla[i] = new Ladrillo(bmpLadrillo);
        }
        hebraJuego = new HebraJuego(this);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        limite = canvas.getHeight() - bmpIzqierda.getHeight();
        canvas.drawColor(Color.RED);
        izquierda = new Control(this, bmpIzqierda, 0, canvas.getHeight() - bmpIzqierda.getHeight());
        derecha = new Control
                (this, bmpDerecha, canvas.getWidth() - bmpDerecha.getWidth(), canvas.getHeight() - bmpDerecha.getHeight());
        if (plataforma == null) {
            plataforma = new Plataforma(this, bmpPlataforma, canvas.getWidth() / 2, limite - bmpPlataforma.getHeight());
        }
        izquierda.dibujar(canvas);
        plataforma.dibujar(canvas);
        derecha.dibujar(canvas);
        if (b == null) {
            b = new Bola(this, bmpBola);
        }
        if (pantallaTerminada(pantalla)) {
            setFuncionando(false);
        }
        for (int i = 0; i < pantalla.length; i++) {
            if (contador == 0) {
                int margen = (canvas.getWidth() / 2) - (pantalla.length * bmpLadrillo.getWidth() / 2);
                pantalla[i].setPosX(i * pantalla[i].getAncho() + margen);
                pantalla[i].setPosY(canvas.getHeight() / 4);
            }
                pantalla[i].dibujar(canvas);
        }
        contador++;
        b.dibujar(canvas);
        if (b.colisiona(plataforma, 0)) {
            b.cambiarDireccion();
        }
        for (Ladrillo l : pantalla) {
            if (b.colisiona(l, 0)) {
                l.eliminar();
                l.setEliminado(true);
                b.cambiarDireccion();
                break;
            }
        }


//        if (posY >= getHeight() - bmp.getHeight()) {
//            direccionY = -10;
//        }else if(posY <= 0){
//            direccionY = 10;
//        }
//        if (posX >= getWidth() - bmp.getWidth()) {
//            direccionX = -10;
//        } else if (posX <= 0) {
//            direccionX = 10;
//        }
//        posX = posX + direccionX;
//        posY = posY + direccionY;
//        canvas.drawBitmap(bmp, posX, posY, null);
    }

    public boolean pantallaTerminada(Ladrillo[] pantalla) {
        for (Ladrillo l : pantalla) {
            if (!l.isEliminado()) {
                return false;
            }
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x, y;
        boolean toc = false;
        x = event.getX();
        y = event.getY();
        Log.v("AAAAAAAAAAAAA", "Estoy en on touch event");
        synchronized (getHolder()) {
            if (derecha.tocado(x, y)) {
                plataforma.moverDerecha();
                Log.v("AAAAAAAAAAAA", "estoy moviendo");
            } else if (izquierda.tocado(x, y)) {
                plataforma.moverIzquierda();
            }
        }

        return true;
    }

    public static int getLimite() {
        return limite;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setFuncionando(true);
        hebraJuego.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        alto = height;
        ancho = width;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean reintentar = true;
        setFuncionando(false);
        while (reintentar) {
            try {
                hebraJuego.join();
                reintentar = false;
            } catch (InterruptedException e) {
            }
        }

    }

    public void setFuncionando(boolean f) {
        funcionando = f;
    }

    /* ************************************************************************************** */
    /*                                                                                        */
    /*                                   HEBRA DEL JUEGO                                      */
    /*                                                                                        */
    /* ************************************************************************************** */

    public class HebraJuego extends Thread {

        private VistaJuego vista;

        private static final long FPS = 30;

        public HebraJuego(VistaJuego vj) {
            this.vista = vj;
        }

        @Override
        public void run() {
            long inicio;
            long ticksPS = 1000 / FPS;
            long tiempoEspera;
            while (funcionando) {
                Canvas canvas = null;
                inicio = System.currentTimeMillis();
                try {
                    canvas = vista.getHolder().lockCanvas();
                    synchronized (vista.getHolder()) {
                        vista.draw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        vista.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
                tiempoEspera = ticksPS - (System.currentTimeMillis() - inicio);
                try {
                    if (tiempoEspera > 0)
                        sleep(tiempoEspera);
                    else
                        sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
