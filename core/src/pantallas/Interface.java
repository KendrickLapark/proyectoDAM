package pantallas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;

import java.text.DecimalFormat;

public class Interface {

    private static BitmapFont bitmapFont1 = new BitmapFont();
    private static SpriteBatch spriteBatch;
    public static float tiempo, ki;
    public static int tiempototal;
    private static Vector3 vector3 = new Vector3();
    private static Vector3 vector31 = new Vector3();
    private static Vector3 vector32 = new Vector3();
    private static Vector3 vector33 = new Vector3();


    public static void SetSpriteBatch(SpriteBatch batch, float cantKi){
        spriteBatch = batch;
        ki = cantKi;
    }

    public static void draw (OrthographicCamera camara, float ki, int salud, int puntuacion){

        tiempototal = 300;

        tiempo += Gdx.graphics.getDeltaTime();
        double tiemp = Math.ceil(tiempo);

        DecimalFormat decimalFormat = new DecimalFormat("#");

        tiempototal -=tiemp;

        vector3 = new Vector3(100,200,0);
        camara.unproject(vector3);
        bitmapFont1.getData().scaleX = 0.03f;
        bitmapFont1.getData().scaleY = 0.05f;
        bitmapFont1.setUseIntegerPositions(false);

        bitmapFont1.draw(spriteBatch, "Tiempo: "+tiempototal , vector3.x, vector3.y);

        vector31 = new Vector3(850, 200, 0);
        camara.unproject(vector31);
        bitmapFont1.getData().scaleX = 0.03f;
        bitmapFont1.getData().scaleY = 0.05f;
        bitmapFont1.setUseIntegerPositions(false);

        bitmapFont1.draw(spriteBatch, "Salud: "+salud , vector31.x, vector31.y);

        int kiEntero;

        kiEntero = Math.round(ki);

        vector32 = new Vector3(480,1120,0);
        camara.unproject(vector32);

        bitmapFont1.getData().scaleX = 0.035f;
        bitmapFont1.getData().scaleY = 0.05f;
        bitmapFont1.draw(spriteBatch, "Ki:   "+kiEntero , vector32.x, vector32.y);

        vector33 = new Vector3(1500,200,0);
        camara.unproject(vector33);

        bitmapFont1.getData().scaleX = 0.035f;
        bitmapFont1.getData().scaleY = 0.05f;
        bitmapFont1.draw(spriteBatch, "Puntuacion:   "+puntuacion , vector33.x, vector33.y);

    }

    public static BitmapFont getBitmapFont1() {
        return bitmapFont1;
    }

    public static float getTiempo() {
        return tiempo;
    }
}
