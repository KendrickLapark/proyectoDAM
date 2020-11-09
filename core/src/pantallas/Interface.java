package pantallas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.text.DecimalFormat;

public class Interface {

    private static BitmapFont bitmapFont1 = new BitmapFont();
    private static SpriteBatch spriteBatch;
    public static float tiempo, ki;
    public static int tiempototal;

    public static void SetSpriteBatch(SpriteBatch batch, float cantKi){
        spriteBatch = batch;
        ki = cantKi;
    }

    public static void draw (OrthographicCamera camara){

        tiempototal = 300;

        tiempo += Gdx.graphics.getDeltaTime();
        double tiemp = Math.ceil(tiempo);

        DecimalFormat decimalFormat = new DecimalFormat("#");

        tiempototal -=tiemp;

        Vector3 vector3 = new Vector3(0,80,0);
        camara.unproject(vector3);
        bitmapFont1.getData().scaleX = 0.03f;
        bitmapFont1.getData().scaleY = 0.05f;
        bitmapFont1.setUseIntegerPositions(false);

        bitmapFont1.draw(spriteBatch, "Tiempo: "+tiempototal , vector3.x, vector3.y);

        /*int kiEntero;

        kiEntero = Math.round(ki);

        Vector3 vector31 = new Vector3(0,500,0);
        camara.unproject(vector31);

        bitmapFont1.getData().scaleX = 0.035f;
        bitmapFont1.getData().scaleY = 0.05f;
        bitmapFont1.draw(spriteBatch, "Ki:   "+kiEntero , vector31.x, vector31.y);*/

    }

}
