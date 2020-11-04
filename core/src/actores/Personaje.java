package actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public class Personaje extends Actor {

    private World world;
    private Sprite sprite;
    private Music salto, caida, recargaKi, kamehamehaSound;
    private Texture standr, standl, jumpr, fallr, jumpl, falll, andando1, andando2, rafaga1, rafaga2, kamehamehaTextureR, kamehamehaTextureL, lanzaR, lanzaL, cargandoR, cargandoL;

    private Body body;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;

    private Animation walkAnimation;
    private TextureRegion [] walkFrames;
    private TextureRegion[][] tmp;
    private TextureRegion currentWalkFrame;

    //private ArrayList <Onda> listaOndas;

    private float animationTime, ki;

    private Boolean rafagazo, bkhamehameha, loop, cargando;

    private int personajeNumero, indexk, salud;

    public Personaje(World mundo, int personajeElegido){

        this.world = mundo;

        personajeNumero = personajeElegido;

        salud = 2;
        ki = 0;

        //listaOndas = new ArrayList<>();

        salto = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/salto.mp3"));
        caida = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/caidatrassalto.mp3"));
        kamehamehaSound = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/kamehameha.mp3"));
        recargaKi = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/recargaki.mp3"));

        sprite = new Sprite(new Texture("personajes/Goku/gstandr.png"));

        propiedadesFisicas();

        loop = true;
        cargando = false;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        sprite.setBounds(body.getPosition().x+4, body.getPosition().y,1.3f , 1.3f );
        sprite.setPosition(body.getPosition().x-0.6f , body.getPosition().y - sprite.getHeight() / 2);
        sprite.draw(batch);

    }

    public void propiedadesFisicas(){

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(sprite.getX()+2,sprite.getY()+2.3f);
        body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();

        fixtureDef = new FixtureDef();
        polygonShape.setAsBox(1/2f,1/2f);
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef);
        fixtureDef.shape.dispose();

    }

    public Body getCuerpo(){
        return body;
    }

}
