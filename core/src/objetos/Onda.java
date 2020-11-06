package objetos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import actores.Enemigo;
import actores.Personaje;

public class Onda extends Actor {

    private Sprite sprite;
    private World world;

    private Personaje pj1;

    private boolean colision;

    public Body body;

    public float velocidadOriginal;


    public Onda(World m, Personaje p1){

        this.world = m;
        this.pj1 = p1;


        fisica();

        if (pj1.getDireccion() == Personaje.Direccion.DERECHA) {
            body.setLinearVelocity(8, 0);
            sprite = new Sprite(new Texture("Objetos/ondaR.png"));
        } else {
            body.setLinearVelocity(-8, 0);
            sprite = new Sprite(new Texture("Objetos/ondaL.png"));
        }

        velocidadOriginal = body.getLinearVelocity().x;

        if (this.body.getLinearVelocity().x != velocidadOriginal) {
            this.body.setActive(false);
        }


        System.out.println("La velocidad original de la onda es: "+velocidadOriginal);

    }

    public void fisica(){
        BodyDef bodyDef = new BodyDef();
        if(pj1.getDireccion() == Personaje.Direccion.DERECHA){
            bodyDef.position.set(pj1.getCuerpo().getPosition().x+0.7f,pj1.getCuerpo().getPosition().y);
        }else{
            bodyDef.position.set(pj1.getCuerpo().getPosition().x-0.7f,pj1.getCuerpo().getPosition().y);
        }

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.1f);
        fixtureDef.shape = circleShape;
        fixtureDef.density=1f;
        body.createFixture(fixtureDef);
        body.setGravityScale(0.0f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        sprite.setOrigin(body.getPosition().x,body.getPosition().y);
        sprite.setBounds(body.getPosition().x-0.2f,body.getPosition().y-0.2f,0.4f,0.4f);
        sprite.draw(batch);
    }


    public boolean colisionSaibaman(Enemigo enemigo){
        boolean overlaps=getHitBox().overlaps(enemigo.getHitBox());
        if(overlaps&&colision==false){
            colision=true;
            Gdx.app.log("Colisionando","con "+enemigo.getClass().getName());
        }else if(!overlaps){
            colision=false;
        }
        return colision;
    }

    public boolean colisionOnda(Onda o){
        boolean overlaps=getHitBox().overlaps(o.getHitBox());
        if(overlaps&&colision==false){
            colision=true;
            Gdx.app.log("Colisionando","con "+o.getClass().getName());
        }else if(!overlaps){
            colision=false;
        }
        return colision;
    }

    public Rectangle getHitBox(){
        return sprite.getBoundingRectangle();
    }

    public Body getCuerpo(){
        return body;
    }

}
