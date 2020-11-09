package actores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Timer;

public class Enemigo1 extends Actor {

    public enum Direccion{ DERECHA, IZQUIERDA}
    public enum Estado{QUIETO, ANDANDO, AIRE, RAFAGA, CARGANDO, MUERTO}

    private World world;
    public Body body;
    private Sprite sprite;
    private Texture staticSaibaman, animacionWalking1,animacionWalking2,animacionFalling1,animacionFalling2, rafagaR, rafagaL;

    private Direccion direccion;
    private Estado estado;

    private Animation walkAnimation;
    private TextureRegion[]walkFrames;
    private TextureRegion[][]tmp;
    TextureRegion currentWalkFrame;

    private boolean ida;

    public int vidas, distanciaEnemigo, idEnemigo, x, y;

    public static float tiempo;

    public static int tiempototal;

    private Timer timer;

    private float posInicialX, crono, controladorTiempo;

    public Enemigo1(World mundo, int x, int y, int idEnemigo){
        this.world = mundo;

        this.idEnemigo = idEnemigo;

        this.x = x;
        this.y = y;

        direccion = Direccion.DERECHA;

        crono = 0;
        controladorTiempo = 1;

        cargaTexturas();
        propiedadesFisicas();

        ida = true;

        vidas = 3;

        posInicialX = body.getPosition().x;

    }

    public void propiedadesFisicas(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(10,2.6f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(1/2f, 1/2f);
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if(vidas == 0){
            sprite.setBounds(body.getPosition().x-0.6f, body.getPosition().y-1,1.3f , 1.3f );
            sprite.draw(batch);

        }else{
            if(distanciaEnemigo>=5 ||distanciaEnemigo<=-5){
                patrullar(x, y);
            }
            sprite.setBounds(body.getPosition().x+4, body.getPosition().y,1.3f , 1.3f );
            sprite.setPosition(body.getPosition().x-0.6f , body.getPosition().y - sprite.getHeight() / 2);
            sprite.draw(batch);


        }


    }

    public void patrullar(int x, int y) {

        int a = 30;
        int b = 43;

        if(body.getPosition().x<164){

            if(body.getPosition().x < b && direccion == Direccion.DERECHA){
                body.setLinearVelocity(3,0);
            }else{
                direccion = Direccion.IZQUIERDA;
            }
            if(body.getPosition().x>posInicialX && direccion == Direccion.IZQUIERDA){
                body.setLinearVelocity(-3,0);
            }else{
                direccion = Direccion.DERECHA;
            }

        }else{
            if(body.getPosition().x<b   && direccion == Direccion.DERECHA){
                body.setLinearVelocity(3,0);
            }else{
                direccion = Direccion.IZQUIERDA;
            }

            if(body.getPosition().x>a && direccion == Direccion.IZQUIERDA){
                body.setLinearVelocity(-3,0);
            }else{
                direccion = Direccion.DERECHA;
            }
        }

    }

    public void animacionAcciones(float elapsedTime, Personaje personaje){

        if(direccion == Direccion.DERECHA && vidas>0){

            tmp = TextureRegion.split(animacionWalking1,37,58);

            walkFrames = new TextureRegion[6];

            int index = 0;

            for(int i = 0; i<6;i++){
                for(int j = 0; j<1;j++){
                    walkFrames[index++] = tmp[j][i];
                }
            }

            walkAnimation = new Animation(0.09f,walkFrames);

            currentWalkFrame =  (TextureRegion)walkAnimation.getKeyFrame((elapsedTime),true);

            sprite = new Sprite(currentWalkFrame);

        }else if(direccion == Direccion.IZQUIERDA && vidas<0){

            tmp = TextureRegion.split(animacionWalking2,37,58);

            walkFrames = new TextureRegion[6];

            int index = 0;

            for(int i = 0; i<6;i++){
                for(int j = 0; j<1;j++){
                    walkFrames[index++] = tmp[j][i];
                }
            }

            walkAnimation = new Animation(0.09f,walkFrames);

            currentWalkFrame =  (TextureRegion)walkAnimation.getKeyFrame((elapsedTime),true);

            sprite = new Sprite(currentWalkFrame);

        }

        if(vidas == 0 && direccion == Direccion.DERECHA){

            tmp = TextureRegion.split(animacionFalling1,48,58);

            walkFrames = new TextureRegion[4];

            int index = 0;

            for(int i = 0; i<4;i++){
                for(int j = 0; j<1;j++){
                    walkFrames[index++] = tmp[j][i];
                }
            }

            walkAnimation = new Animation(0.00009f,walkFrames);

            currentWalkFrame = (TextureRegion)walkAnimation.getKeyFrame((elapsedTime),false);

            sprite = new Sprite(currentWalkFrame);

            body.setLinearVelocity(0,0);

        }

        if(vidas == 0 && direccion == Direccion.IZQUIERDA){

            tmp = TextureRegion.split(animacionFalling2,56,62);

            walkFrames = new TextureRegion[4];

            int index = 0;

            for(int i = 3; i>=0;i--){
                for(int j = 0; j<1;j++){
                    walkFrames[index++] = tmp[j][i];
                }
            }

            walkAnimation = new Animation(0.02f,walkFrames);

            currentWalkFrame = (TextureRegion)walkAnimation.getKeyFrame((elapsedTime),false);

            sprite = new Sprite(currentWalkFrame);

            body.setLinearVelocity(0,0);

        }

        if(distanciaEnemigo<5 || distanciaEnemigo<-5){

            System.out.println("Distancia al enemigo ===="+distanciaEnemigo);

            body.setLinearVelocity(0,0);

            if(personaje.getCuerpo().getPosition().x > body.getPosition().x){
                sprite = new Sprite(rafagaR);
            }

            if(personaje.getCuerpo().getPosition().x < body.getPosition().x){
                sprite = new Sprite(rafagaL);
            }

        }

    }

    public void setDistanciaEnemigo(float posicionEnemigo){

        distanciaEnemigo = Math.round(this.body.getPosition().x - posicionEnemigo);

    }

    public Rectangle getHitBox(){ return sprite.getBoundingRectangle(); }

    public void cargaTexturas(){

        if(idEnemigo == 1){
            staticSaibaman = new Texture("personajes/Saibaman/sai.png");
            animacionWalking1 = new Texture("personajes/Saibaman/saibamanwalking1.png");
            animacionWalking2 = new Texture("personajes/Saibaman/saibamanwalking2.png");
            animacionFalling1 = new Texture("personajes/Saibaman/saibamanfallingR2.png");
            animacionFalling2 = new Texture("personajes/Saibaman/saibamanfallingL.png");
            rafagaR = new Texture("personajes/Saibaman/saibamanRafagaR.png");
            rafagaL = new Texture("personajes/Saibaman/saibamanRafagaL.png");
        }

    }

}

