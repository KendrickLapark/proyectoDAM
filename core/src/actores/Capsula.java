package actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Capsula extends Actor {

    private Sprite sprite; //Sprite que simboliza al actor
    private World world; //Mundo de nuestro juego
    private BodyDef propiedadesCuerpo; //Definidor de las propiedades del body
    private Body cuerpo; //Cuerpo del objeto
    private FixtureDef propiedadesFisicasCuerpo;//Definidor de las propiedades físicas del body
    private Boolean colision; // Boolean para detectar si colisiona con otros cuerpos
    private Personaje personaje;
    private int contadorColision, contPuntuacion;


    public Capsula(World world, Personaje personaje, String ruta, float x, float y){

        this.world = world;
        this.personaje = personaje;
        sprite = new Sprite(new Texture(ruta));

        contadorColision = 0;
        contPuntuacion = 0;


        colision = false;

        propiedadesFisicas(x,y);

    }

    public void propiedadesFisicas(float x, float y){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x,y);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        cuerpo = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(1/3f, 1/3f);
        fixtureDef.shape = polygonShape;
        fixtureDef.isSensor= true;
        cuerpo.createFixture(fixtureDef);


    }

    public Rectangle getHitBox(){
        return sprite.getBoundingRectangle();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if(contadorColision==0){
            sprite.setOrigin(cuerpo.getPosition().x,cuerpo.getPosition().y);
            sprite.setBounds(cuerpo.getPosition().x - 0.35f,cuerpo.getPosition().y - 0.35f,0.7f,0.7f);
            sprite.draw(batch);
        }

    }


    public void recoleccion(Personaje personaje){

                boolean overlaps=getHitBox().overlaps(personaje.getHitBox());
                if(overlaps){
                    contadorColision++;
                }

    }

    public int getContadorColision() {
        return contadorColision;
    }
}
