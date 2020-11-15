package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Juego;

import java.util.ArrayList;

import actores.Capsula;
import actores.Enemigo;
import actores.Personaje;
import input.Teclado;
import objetos.Onda;

public class PrimerMundo implements Screen {

    private Juego juego;
    private World world;

    private Box2DDebugRenderer box2DDebugRenderer;

    private Body rectanguloSuelo;

    private TiledMap map;

    private Texture blank;

    private Personaje p1;
    private Enemigo e1, e2, e3;
    private Capsula c1;
    private ArrayList<Onda> ondasToDestroy;

    private Music musica;

    private Viewport viewport;
    private OrthographicCamera orthographicCamera;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private int personajeSeleccionado;
    private float velocidadRecarga, tiempoContador;

    float elapsedTime;

    private Teclado teclado;


    public PrimerMundo(Juego loc, int personajeSeleccionado){

        this.juego = loc;
        this.personajeSeleccionado = personajeSeleccionado;
        orthographicCamera = new OrthographicCamera(20,20);
        world = new World(new Vector2(0,-9.8f),true);
        map = new TmxMapLoader().load("mapa/mapav8.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map,1/16f);
        blank = new Texture("recursos/blank.png");

        tiempoContador = 0;

        p1 = new Personaje(world, 1);
        e1 = new Enemigo(world,30,43,1, 10, 2.6f);
        e2 = new Enemigo(world, 45, 43,1,50, 2.6f);
        e3 = new Enemigo(world, 65, 80,1,70,2.6f);
        c1 = new Capsula(world, p1,"Objetos/capsule.png", 11,11);
        ondasToDestroy = new ArrayList<>();

        Interface.SetSpriteBatch(juego.getSpriteBatch(), p1.getKi());

        musica = Gdx.audio.newMusic(Gdx.files.internal("sonido/musica/dbzInicio.mp3"));
        musica.play();
        musica.setVolume(0.03f);
        musica.setLooping(true);

        box2DDebugRenderer = new Box2DDebugRenderer();

        orthographicCamera.position.set(p1.getX(),p1.getY()+8,0);

        orthographicCamera.zoom = 1;

        teclado = new Teclado(world, p1);
        Gdx.input.setInputProcessor(teclado);

        for (MapObject objeto:map.getLayers().get("Suelo").getObjects()){
            BodyDef propiedadesRectangulo= new BodyDef();
            propiedadesRectangulo.type = BodyDef.BodyType.StaticBody;
            rectanguloSuelo = world.createBody(propiedadesRectangulo);
            FixtureDef propiedadesFisicasRectangulo=new FixtureDef();
            Shape formaRectanguloSuelo=getRectangle((RectangleMapObject)objeto);
            propiedadesFisicasRectangulo.shape = formaRectanguloSuelo;
            propiedadesFisicasRectangulo.density = 1f;
            rectanguloSuelo.createFixture(propiedadesFisicasRectangulo);

        }

        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {

                for(int i = 0; i<p1.getListaOndas().size();i++){

                    if( contact.getFixtureA().getBody() == p1.getListaOndas().get(i).getCuerpo() && contact.getFixtureB().getBody() == e1.getListaOndas().get(i).getCuerpo()){

                        ondasToDestroy.add(p1.getListaOndas().get(i));
                        ondasToDestroy.add(e1.getListaOndas().get(i));
                        e1.getListaOndas().remove(i);
                        p1.getListaOndas().remove(i);

                    }

                    if(contact.getFixtureA().getBody() == rectanguloSuelo && contact.getFixtureB().getBody() == p1.getListaOndas().get(i).getCuerpo()){
                        ondasToDestroy.add(p1.getListaOndas().get(i));
                        p1.getListaOndas().remove(i);
                    }

                    //METODO PARA ELIMINAR UNA ONDA Y LA ANTERIOR DENTRO DEL ARRAYLIST CUANDO COLISIONAN

                    if(i >= 1 &&  contact.getFixtureA().getBody()==p1.getListaOndas().get(i-1).getCuerpo()&&
                            contact.getFixtureB().getBody()==p1.getListaOndas().get(i).getCuerpo()){
                        System.out.println("SE HA BORRADO");
                        ondasToDestroy.add(p1.getListaOndas().get(i));
                        ondasToDestroy.add(p1.getListaOndas().get(i-1));
                        p1.getListaOndas().remove(i);
                        p1.getListaOndas().remove(i-1);
                    }

                    if(contact.getFixtureA().getBody() == e1.body && contact.getFixtureB().getBody() == p1.getListaOndas().get(i).getCuerpo()){
                        System.out.println("Impacto");
                        ondasToDestroy.add(p1.getListaOndas().get(i));
                        p1.getListaOndas().remove(i);
                        e1.vidas--;

                    }

                    if(contact.getFixtureA().getBody() == p1.getCuerpo() && contact.getFixtureB().getBody() == p1.getListaOndas().get(i).getCuerpo()){
                        System.out.println("Impacto");
                        ondasToDestroy.add(p1.getListaOndas().get(i));
                        p1.getListaOndas().remove(i);
                        e1.vidas--;

                    }



                }

                for(int j = 0 ; j<e1.getListaOndas().size();j++){

                    if(contact.getFixtureA().getBody() == p1.getCuerpo() && contact.getFixtureB().getBody() == e1.getListaOndas().get(j).getCuerpo()){

                        ondasToDestroy.add(e1.getListaOndas().get(j));
                        e1.getListaOndas().remove(j);
                    }


                    if(e1.getListaOndas().size() !=0 ){
                        if(e1.getListaOndas().get(j).getCuerpo().getPosition().x<3){
                            ondasToDestroy.add(e1.getListaOndas().get(j));
                            e1.getListaOndas().remove(j);
                        }
                    }


                    if(e1.getListaOndas().size() != 0){

                        if( contact.getFixtureA().getBody() == e1.getListaOndas().get(j).getCuerpo() && contact.getFixtureB().getBody() == p1.getListaOndas().get(j).getCuerpo()){

                            ondasToDestroy.add(e1.getListaOndas().get(j));
                            ondasToDestroy.add(p1.getListaOndas().get(j));
                            e1.getListaOndas().remove(j);
                            p1.getListaOndas().remove(j);

                        }

                    }


                    if(contact.getFixtureA().getBody() == rectanguloSuelo &&  contact.getFixtureB().getBody() == e1.getListaOndas().get(j).getCuerpo()){

                        ondasToDestroy.add(e1.getListaOndas().get(j));
                        e1.getListaOndas().remove(j);
                    }

                }


                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        for(Onda onda : ondasToDestroy){
                            onda.body.setActive(false);
                        }

                        if(e1.vidas==0){
                            e1.body.setActive(false);
                        }
                    }
                });

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        elapsedTime += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(Gdx.graphics.getDeltaTime(),6,2);

        orthographicCamera.update();
        orthographicCamera.position.x = p1.getCuerpo().getPosition().x;

        orthogonalTiledMapRenderer.setView(orthographicCamera);

        orthogonalTiledMapRenderer.render();

        box2DDebugRenderer.render(world, orthographicCamera.combined);
        juego.getSpriteBatch().setProjectionMatrix(orthographicCamera.combined);

        juego.getSpriteBatch().begin();

        Interface.draw(orthographicCamera);
        //Interface.muestraKi(p1.getKi(),p1.getCuerpo().getPosition().x, p1.getCuerpo().getPosition().y, orthographicCamera);

        p1.animaciones(elapsedTime);
        p1.draw(juego.getSpriteBatch(),0);

        System.out.println("Posicion d eY"+p1.getCuerpo().getPosition().y);

        e1.animacionAcciones(elapsedTime, p1);
        e1.draw(juego.getSpriteBatch(),0);
        e1.setDistanciaEnemigo(p1.getCuerpo().getPosition().x);


        e2.animacionAcciones(elapsedTime,p1);
        e2.draw(juego.getSpriteBatch(),0);
        e2.setDistanciaEnemigo(p1.getCuerpo().getPosition().x);

        e3.animacionAcciones(elapsedTime,p1);
        e3.draw(juego.getSpriteBatch(),0);
        e3.setDistanciaEnemigo(p1.getCuerpo().getPosition().x);

        System.out.println("Coordenadas del personaje X: "+p1.getCuerpo().getPosition().x+", Y: "+p1.getCuerpo().getPosition().y);

        c1.draw(juego.getSpriteBatch(),0);

        //DEBUGS

        /*System.out.println("Coordenadas del personaje X: "+p1.getCuerpo().getPosition().x+", Y: "+p1.getCuerpo().getPosition().y);

        System.out.println("Estado personaje:  "+p1.getEstado());

        System.out.println("Direccion personaje:  "+p1.getDireccion());

        System.out.println("EL SUELO ESTA AQUI: "+p1.getPosicionSuelo());

        System.out.println("POSICION DEL DICHOSO BODY Y: "+p1.getCuerpo().getPosition().y);

        System.out.println("CARGANDOOOOOOOOOOOOOOOOO: "+p1.getCargando());*/

        //System.out.println("Overlasps primer mundo"+p1.recoleccion(c1));

        c1.recoleccion(p1);

        /*System.out.println("KIKIKIKIKI"+p1.getKi());

        System.out.println("COLISION??????------------------>"+c1.getColision());

        System.out.println("Estado personaje:  "+p1.getEstado());*/

        teclado.entrada();

        juego.getSpriteBatch().setColor(Color.GRAY);
        juego.getSpriteBatch().draw(blank,p1.getCuerpo().getPosition().x-3,-0.5f, 8,0.2f);

        juego.getSpriteBatch().setColor(Color.YELLOW);

        juego.getSpriteBatch().draw(blank, p1.getCuerpo().getPosition().x-3, -0.5f, p1.getKi(), 0.2f);

        juego.getSpriteBatch().end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        orthogonalTiledMapRenderer.dispose();
        world.dispose();
        musica.dispose();

    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) /16f, (rectangle.y + rectangle.height * 0.5f ) / 16f);
        polygon.setAsBox(rectangle.width * 0.5f /16f, rectangle.height * 0.5f / 16f, size, 0.0f);
        return polygon;
    }

}
