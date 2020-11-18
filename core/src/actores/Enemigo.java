package actores;

import com.badlogic.gdx.Gdx;
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

import java.util.ArrayList;
import java.util.Timer;

import objetos.Onda;

public class Enemigo extends Actor {

    public enum Direccion{ DERECHA, IZQUIERDA}
    public enum Estado{QUIETO, ANDANDO, AIRE, ALERTA, RAFAGA, CARGANDO, MUERTO}

    private World world;
    public Body body;
    private Sprite sprite;
    private Texture standR, standL, animacionWalking1,animacionWalking2,animacionFalling1,animacionFalling2, rafagaR, rafagaL, deadR, deadL;

    private Enemigo.Direccion direccion, direccionPrevia;
    private Enemigo.Estado estado;

    private ArrayList <Onda> listaOndas;

    private Animation walkAnimation;
    private TextureRegion[]walkFrames;
    private TextureRegion[][]tmp;
    TextureRegion currentWalkFrame;

    public int vidas, distanciaEnemigo, idEnemigo, x, y;

    private float posInicialX, crono, tiempo, tiempoRafaga, positionX, positionY;

    public Enemigo(World mundo, int x, int y, int idEnemigo, float posX, float posY){

        this.world = mundo;

        this.idEnemigo = idEnemigo;

        this.x = x;
        this.y = y;
        this.positionX = posX;
        this.positionY = posY;

        direccion = Direccion.DERECHA;
        estado = Estado.ANDANDO;

        crono = 0;
        tiempo = 1;

        direccionPrevia = null;

        listaOndas =new ArrayList<>();

        cargaTexturas();
        propiedadesFisicas();

        vidas = 3;

        posInicialX = body.getPosition().x;

    }

    public void propiedadesFisicas(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(positionX,positionY);
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

        for (Onda onda : listaOndas){
            onda.draw(batch,parentAlpha);
        }

        if(vidas == 0){
            sprite.setBounds(body.getPosition().x+4, body.getPosition().y,1.1f , 1.1f );
            sprite.setPosition(body.getPosition().x-0.6f , body.getPosition().y - 1);
            sprite.draw(batch);
        }else{
            if(estado != Estado.RAFAGA){
                if(estado!=Estado.ALERTA){
                    patrullar(x,y);
                }

            }
            sprite.setBounds(body.getPosition().x+4, body.getPosition().y,1.3f , 1.3f );
            sprite.setPosition(body.getPosition().x-0.6f , body.getPosition().y - sprite.getHeight() / 2 - 0.1f);
            sprite.draw(batch);
        }

    }

    public void patrullar(int x, int y) {

        int a = x;
        int b = y;

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



        if(estado == Estado.ANDANDO && direccion == Direccion.DERECHA){

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

        }
        if(estado == Estado.ANDANDO && direccion == Direccion.IZQUIERDA){

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

        if(vidas == 0 && direccion == Direccion.IZQUIERDA){

            sprite = new Sprite(deadL);

            body.setLinearVelocity(0,0);

        }

        if(vidas == 0 && direccion == Direccion.DERECHA){


            sprite = new Sprite(deadR);

            body.setLinearVelocity(0,0);

        }

        if(distanciaEnemigo > -10 && distanciaEnemigo<=0 && vidas != 0){

            tiempo += Gdx.graphics.getDeltaTime();
            direccionPrevia = direccion;
            System.out.println("Distancia al enemigo ===="+distanciaEnemigo);

            estado = Estado.ALERTA;
            body.setLinearVelocity(0,0);
            if(personaje.getCuerpo().getPosition().x > body.getPosition().x){
                    sprite = new Sprite(standR);
                if(tiempo>3){
                    direccion = Direccion.DERECHA;
                    listaOndas.add(new Onda(world, this));
                    estado = Enemigo.Estado.RAFAGA;
                    tiempo-=tiempo;
                    sprite = new Sprite(rafagaR);
                }
                direccion = direccionPrevia;
            }
        }else if(distanciaEnemigo<10 && distanciaEnemigo>0 && vidas != 0){

            tiempo += Gdx.graphics.getDeltaTime();
            direccionPrevia = direccion;
            estado = Estado.ALERTA;
            body.setLinearVelocity(0,0);
            if(personaje.getCuerpo().getPosition().x < body.getPosition().x){
                sprite = new Sprite(standL);
                if(tiempo>3){
                    direccion = Direccion.IZQUIERDA;
                    listaOndas.add(new Onda(world, this));
                    estado = Enemigo.Estado.RAFAGA;
                    tiempo-=tiempo;
                    sprite = new Sprite(rafagaL);
                }

                direccion = direccionPrevia;
            }
        }else{
            estado = Estado.ANDANDO;
        }

    }

    public void setDistanciaEnemigo(float posicionEnemigo){

            distanciaEnemigo = Math.round(this.body.getPosition().x - posicionEnemigo);

    }

    public Rectangle getHitBox(){ return sprite.getBoundingRectangle(); }

    public void cargaTexturas(){

        if(idEnemigo == 1){
            standR = new Texture("personajes/Saibaman/sai.png");
            standL = new Texture("personajes/Saibaman/saiL.png");
            animacionWalking1 = new Texture("personajes/Saibaman/saibamanwalking1.png");
            animacionWalking2 = new Texture("personajes/Saibaman/saibamanwalking2.png");
            animacionFalling1 = new Texture("personajes/Saibaman/saibamanfallingR2.png");
            animacionFalling2 = new Texture("personajes/Saibaman/saibamanfallingL.png");
            rafagaR = new Texture("personajes/Saibaman/saibamanRafagaR.png");
            rafagaL = new Texture("personajes/Saibaman/saibamanRafagaL.png");
            deadR = new Texture("personajes/Saibaman/deadR.png");
            deadL = new Texture("personajes/Saibaman/deadL.png");
        }

    }

    public Estado getEstado() {
        return estado;
    }

    public int getDistanciaEnemigo() {
        return distanciaEnemigo;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public Body getBody() {
        return body;
    }

    public ArrayList<Onda> getListaOndas() {
        return listaOndas;
    }
}
