package actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

import objetos.Onda;

public class Personaje extends Actor {

    public enum Direccion{ DERECHA, IZQUIERDA}
    public enum Estado{QUIETO, ANDANDO, AIRE, RAFAGA, CARGANDO, MUERTO}

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

    private ArrayList <Onda> listaOndas;

    private Direccion direccion;
    private Estado estado;

    private float posicionSuelo, ki, velocidadRecarga;

    //private ArrayList <Onda> listaOndas;

    private float animationTime;

    private Boolean cayendo, rafagazo, bkhamehameha, loop, cargando, colision;

    private int personajeNumero, indexk, salud, contador;

    public Personaje(World mundo, int personajeElegido){

        this.world = mundo;


        personajeNumero = 1;
        salud = 2;
        ki = 8;

        listaOndas = new ArrayList<>();

        cayendo = false;
        colision = false;

        direccion = Direccion.DERECHA;
        estado = Estado.QUIETO;
        //listaOndas = new ArrayList<>();

        salto = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/salto.mp3"));
        caida = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/caidatrassalto.mp3"));
        kamehamehaSound = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/kamehameha.mp3"));
        recargaKi = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/recargaki.mp3"));

        sprite = new Sprite(new Texture("personajes/Goku/gstandr.png"));

        propiedadesFisicas();
        posicionSuelo = 2.6f;

        contador = 0;

        eleccionPersonaje();

        loop = true;
        cargando = false;
        rafagazo = false;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        for (Onda onda : listaOndas){
            onda.draw(batch,parentAlpha);
        }

        actualizarEstado();

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

    public void actualizarEstado(){

        if(body.getPosition().y<0){
            estado = Estado.MUERTO;
        }

        if(body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0){
            estado = Estado.QUIETO;
        }

        if(body.getPosition().y>posicionSuelo){
            estado = Estado.AIRE;
        }

        if(body.getLinearVelocity().y>0){
            cayendo = false;
        }else if(body.getLinearVelocity().y<0){
            cayendo = true;
        }

        if(body.getLinearVelocity().x>0){
            direccion =  Direccion.DERECHA;
        }else if(body.getLinearVelocity().x<0)
            direccion = Direccion.IZQUIERDA;

        if(estado != Estado.AIRE && body.getLinearVelocity().x!=0){
            estado = Estado.ANDANDO;

        }

        if(Gdx.input.isKeyPressed(Input.Keys.F)){
            estado = Estado.RAFAGA;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.X)){
            if(this.getKi()<=7.9f){
                velocidadRecarga = 0.1f;

                this.setKi(velocidadRecarga);
                estado = Estado.CARGANDO;
            }

        }

    }

    public void animaciones(float elapsedTime){

        if(estado == Estado.MUERTO){
            System.exit(0);
        }

        if(estado == Estado.QUIETO && rafagazo==false){
            if(direccion == Direccion.DERECHA){
                sprite = new Sprite(standr);
            }else{
                sprite = new Sprite(standl);
            }
        }

        if(estado == Estado.RAFAGA){
            if(direccion == Direccion.DERECHA){
                sprite = new Sprite(rafaga1);
            }else{
                sprite = new Sprite(rafaga2);
            }
        }

        if(estado == Estado.ANDANDO){

            if(direccion == Direccion.DERECHA){

                int a = 3;

                tmp = TextureRegion.split(andando1,37,58);
                walkFrames = new TextureRegion[a];

                int index = 0;

                for(int i = 0; i<a;i++){
                    for(int j = 0; j<1;j++){
                        walkFrames[index++] = tmp[j][i];
                    }
                }

                walkAnimation = new Animation(body.getLinearVelocity().x*(1/(7*body.getLinearVelocity().x)),walkFrames); // a mas número la animacion va mas lento , a 1 va muy lento a 0.001 muy rapido()

                currentWalkFrame =  (TextureRegion)walkAnimation.getKeyFrame((elapsedTime),loop);

                sprite = new Sprite(currentWalkFrame);

            }else{

                int a = 3;

                tmp = TextureRegion.split(andando2,37,58);
                walkFrames = new TextureRegion[a];

                int index2 = 0;

                for(int i = 0; i<a;i++){
                    for(int j = 0; j<1;j++){
                        walkFrames[index2++] = tmp[j][i];
                    }
                }

                walkAnimation = new Animation(body.getLinearVelocity().x*(1/(7*body.getLinearVelocity().x)),walkFrames);

                currentWalkFrame =  (TextureRegion)walkAnimation.getKeyFrame((elapsedTime),loop);

                sprite = new Sprite(currentWalkFrame);

            }
        }

        if(estado == Estado.AIRE){

            if(cayendo == false){
                if(direccion == Direccion.DERECHA){
                    sprite = new Sprite(jumpr);
                }else{
                    sprite = new Sprite(jumpl);
                }
            }else{
                if(direccion == Direccion.DERECHA){
                    sprite = new Sprite(fallr);
                }else{
                    sprite = new Sprite(falll);
                }
            }

            salto.play();
            salto.setVolume(0.02f);
        }

        if(estado == Estado.CARGANDO){
            body.setLinearVelocity(0,0);
            if(direccion == Direccion.DERECHA){
                sprite = new Sprite(new Texture("personajes/Goku/cargaR2.png"));
                sprite.setBounds(body.getPosition().x+4, body.getPosition().y,3f , 3f );
            }else{
                sprite = new Sprite(new Texture("personajes/Goku/cargaL.png"));
            }
            recargaKi.play();
            recargaKi.setVolume(0.03f);
        }else{
            recargaKi.stop();
        }

    }

    public void eleccionPersonaje(){

        if(personajeNumero==1){
            standr = new Texture("personajes/Goku/gstandr.png");
            standl = new Texture("personajes/Goku/gstandl.png");
            jumpr = new Texture("personajes/Goku/gjumpr.png");
            jumpl = new Texture("personajes/Goku/gjumpl.png");
            fallr = new Texture("personajes/Goku/gfallr.png");
            falll = new Texture("personajes/Goku/gfalll.png");
            rafaga1 = new Texture("personajes/Goku/gokurafaga.png");
            rafaga2 = new Texture("personajes/Goku/gokurafagaL.png");

            lanzaR = new Texture("personajes/Goku/kamehameha/lanzandoR.png");
            lanzaL = new Texture("personajes/Goku/kamehameha/lanzandoL.png");

            salto = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/salto.mp3"));
            caida = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/caidatrassalto.mp3"));

            andando1 = new Texture("personajes/Goku/animacion/wg.png");
            andando2 = new Texture("personajes/Goku/animacion2/walkinggoku2.png");
            kamehamehaTextureR = new Texture("personajes/Goku/kamehameha/kamehamehaR.png");
            kamehamehaTextureL = new Texture("personajes/Goku/kamehameha/kamehamehaL.png");
            cargandoR = new Texture("personajes/Goku/animacioncargaR/cargando.png");

            sprite = new Sprite(standr);

            currentWalkFrame = new TextureRegion(standr);
        }

        if(personajeNumero==2){
            standr = new Texture("personajes/Vegeta/vstandr.png");
            standl = new Texture("personajes/Vegeta/vstandl.png");
            jumpr = new Texture("personajes/Vegeta/vjumpr.png");
            jumpl = new Texture("personajes/Vegeta/vjumpl.png");
            fallr = new Texture("personajes/Vegeta/vfallr.png");
            falll = new Texture("personajes/Vegeta/vfalll.png");
            rafaga1 = new Texture("personajes/Vegeta/vrafagar.png");
            rafaga2 = new Texture("personajes/Vegeta/vrafagal.png");
            andando1 = new Texture("personajes/Vegeta/animacion1/vandandor.png");
            andando2 = new Texture("personajes/Vegeta/animacion2/vandandol.png");

            salto = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/salto.mp3"));
            caida = Gdx.audio.newMusic(Gdx.files.internal("sonido/efectos/caidatrassalto.mp3"));

            sprite = new Sprite(standr);

            currentWalkFrame = new TextureRegion(standr);
        }

        if(personajeNumero==3){
            standr = new Texture("personajes/Piccolo/pstandr.png");
            standl = new Texture("personajes/Piccolo/pstandl.png");
            jumpr = new Texture("personajes/Piccolo/pjumpr.png");
            jumpl = new Texture("personajes/Piccolo/pjumpl.png");
            fallr = new Texture("personajes/Piccolo/pfallr.png");
            falll = new Texture("personajes/Piccolo/pfalll.png");
            rafaga1 = new Texture("personajes/Piccolo/prafagar.png");
            rafaga2 = new Texture("personajes/Piccolo/prafagal.png");
            andando1 = new Texture("personajes/Piccolo/animacion1/pandandor.png");
            andando2 = new Texture("personajes/Piccolo/animacion2/pandandol.png");

            sprite = new Sprite(standr);

            currentWalkFrame = new TextureRegion(standr);
        }

    }

    public Body getCuerpo(){
        return body;
    }

    public Estado getEstado(){
        return estado;
    }

    public Direccion getDireccion(){
        return direccion;
    }

    public float getPosicionSuelo(){
        return posicionSuelo;
    }

    public void setRafagazo(boolean rafagazo){
         this.rafagazo = rafagazo;
    }

    public void setCargando(boolean cargando){
        this.cargando = cargando;
    }

    public boolean getCargando(){
        return cargando;
    }

    public boolean getRafagazo(){
        return rafagazo;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public ArrayList <Onda> getListaOndas(){
        return listaOndas;
    }

    public Boolean getcolision() {
        return colision;
    }

    public void setKi(float cantKi){
        if(ki>=0){
            ki+=cantKi;
        }
    }

    public float getKi() {
        return ki;
    }

    public Rectangle getHitBox(){
        return sprite.getBoundingRectangle();
    }
}
