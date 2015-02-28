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

    private Bitmap bmpBola, bmpDerecha, bmpIzqierda, bmpPlataforma, bmpLadrillo, bmpInicio;
    private int alto, ancho;
    private HebraJuego hebraJuego;
    Control izquierda, derecha;
    Plataforma plataforma;
    private static int limite;
    private Bola b = null;
    private Ladrillo[] pantalla;
    private static boolean funcionando = false;
    private int contador = 0;
    private int estadoJuego = 0; // 0 - Sin empezar | 1 - Jugando | 2 - Perdido
    public VistaJuego(Context context) {
        super(context);
        getHolder().addCallback(this);
        bmpBola = BitmapFactory.decodeResource(getResources(), R.drawable.bola);
        bmpDerecha = BitmapFactory.decodeResource(getResources(), R.drawable.der);
        bmpIzqierda = BitmapFactory.decodeResource(getResources(), R.drawable.izq);
        bmpPlataforma = BitmapFactory.decodeResource(getResources(), R.drawable.plataforma);
        bmpLadrillo = BitmapFactory.decodeResource(getResources(), R.drawable.ladrillo);
        bmpInicio = BitmapFactory.decodeResource(getResources(), R.drawable.titulo);
        pantalla = new Ladrillo[5];

        for (int i = 0; i < pantalla.length; i++) {
            pantalla[i] = new Ladrillo(bmpLadrillo);
        }
        hebraJuego = new HebraJuego(this);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        izquierda = new Control(this, bmpIzqierda, 0, canvas.getHeight() - bmpIzqierda.getHeight());
        derecha = new Control
                (this, bmpDerecha, canvas.getWidth() - bmpDerecha.getWidth(), canvas.getHeight() - bmpDerecha.getHeight());
        switch (estadoJuego){
            case 0:
                canvas.drawColor(Color.RED);
                canvas.drawBitmap(bmpInicio,
                        (canvas.getWidth()/2) - (bmpInicio.getWidth() / 2),
                        (canvas.getHeight()/2) - (bmpInicio.getHeight()/2),
                        null);
                break;
            case 1:
                limite = canvas.getHeight() - bmpIzqierda.getHeight();
                canvas.drawColor(Color.RED);
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
                    estadoJuego = 0;
                    //setFuncionando(false);
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
                    b.cambiaAngulo();
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
                break;
            case 2:
                break;
            case 3:

                break;
        }
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
        synchronized (getHolder()) {
            switch(estadoJuego){
                case 0:
                    if(x > (ancho/2)-(bmpInicio.getWidth()/2) &&
                            x < (ancho/2)-(bmpInicio.getWidth()/2) + bmpInicio.getWidth() &&
                            y > (alto/2)-(bmpInicio.getHeight()/2)&&
                            y < (alto/2) - (bmpInicio.getHeight()/2) + bmpInicio.getHeight()){
                        estadoJuego = 1;
                        contador = 0;
                        b = null;
                        for(Ladrillo l : pantalla){
                            l.setEliminado(false);
                        }
                    }
                    break;
                case 1:
                    if (derecha.tocado(x, y)) {
                        plataforma.moverDerecha();
                    } else if (izquierda.tocado(x, y)) {
                        plataforma.moverIzquierda();
                    }
                    break;
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

    public void setEstadoJuego(int estadoJuego) {
        this.estadoJuego = estadoJuego;
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
