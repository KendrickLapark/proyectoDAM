package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    private Personaje p1;
    private Enemigo e1;
    private Capsula c1;
    private ArrayList<Onda> ondasToDestroy;

    private Music musica;

    private Viewport viewport;
    private OrthographicCamera orthographicCamera;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private int personajeSeleccionado;
    private float velocidadRecarga;

    float elapsedTime;

    private Texture blank;

    private Teclado teclado;


    public PrimerMundo(Juego loc){

        this.juego = loc;
        orthographicCamera = new OrthographicCamera(20,20);
        world = new World(new Vector2(0,-9.8f),true);
        map = new TmxMapLoader().load("mapa/mapav2.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map,1/16f);

        p1 = new Personaje(world, 1);
        e1 = new Enemigo(world,30,43,1);
        c1 = new Capsula(world, p1,"Objetos/capsule.png", 6,6);
        ondasToDestroy = new ArrayList<>();

        musica = Gdx.audio.newMusic(Gdx.files.internal("sonido/musica/dbzInicio.mp3"));
        musica.play();
        musica.setVolume(0.03f);
        musica.setLooping(true);

        box2DDebugRenderer = new Box2DDebugRenderer();

        orthographicCamera.position.set(p1.getX(),p1.getY()+8,0);


        orthographicCamera.zoom = 0.9f;

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

                    if(contact.getFixtureA().getBody() == rectanguloSuelo && contact.getFixtureB().getBody() == p1.getListaOndas().get(i).getCuerpo()){
                        ondasToDestroy.add(p1.getListaOndas().get(i));
                        p1.getListaOndas().remove(i);
                    }

                }

                /*if(contact.getFixtureA().getBody() == p1.getCuerpo() && contact.getFixtureB().getBody() == plataforma.getCuerpo()){
                    System.out.println("En la plataforma");
                    p1.getEstado() = Personaje.Estado.ENPLATAFORMA;
                }*/

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

        p1.animaciones(elapsedTime);
        p1.draw(juego.getSpriteBatch(),0);
        p1.recoleccion(c1);

        e1.animacionAcciones(elapsedTime);
        e1.draw(juego.getSpriteBatch(),0);

        if(p1.recoleccion(c1)){
            c1.draw(juego.getSpriteBatch(),0);
        }



        //DEBUGS

        /*System.out.println("Coordenadas del personaje X: "+p1.getCuerpo().getPosition().x+", Y: "+p1.getCuerpo().getPosition().y);

        System.out.println("Estado personaje:  "+p1.getEstado());

        System.out.println("Direccion personaje:  "+p1.getDireccion());

        System.out.println("EL SUELO ESTA AQUI: "+p1.getPosicionSuelo());

        System.out.println("POSICION DEL DICHOSO BODY Y: "+p1.getCuerpo().getPosition().y);

        System.out.println("CARGANDOOOOOOOOOOOOOOOOO: "+p1.getCargando());*/

        //System.out.println("Overlasps primer mundo"+p1.recoleccion(c1));

        teclado.entrada();

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
