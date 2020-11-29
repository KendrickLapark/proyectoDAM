package actores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Checkpoint extends Actor {

    private World world;
    private Sprite sprite;
    public Body body;

    public Checkpoint(World world, float x, float y) {
        this.world = world;
        sprite = new Sprite(new Texture("Objetos/sign.png"));

        propiedadesFisicas(187,2.6f);

    }

    public void propiedadesFisicas(float x, float y){

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x,y); //x=2090 y=69, zona inicial
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(1/2f, 1/2f);
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        sprite.setBounds(body.getPosition().x-0.5f, body.getPosition().y-0.4f, 1f,0.75f);
        sprite.draw(batch);

    }

    public Body getCuerpo(){
        return body;
    }

}
