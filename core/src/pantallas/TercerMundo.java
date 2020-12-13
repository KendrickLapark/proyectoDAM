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
import com.mygdx.game.Juego;

import java.util.ArrayList;

import actores.Capsula;
import actores.Checkpoint;
import actores.Enemigo;
import actores.Personaje;
import actores.Plataforma;
import input.Teclado;
import objetos.Onda;

public class TercerMundo implements Screen {

    private Juego juego;
    private World world;

    private Box2DDebugRenderer box2DDebugRenderer;

    private TiledMap map;

    private Body rectanguloSuelo;

    private Personaje p1;

    private Plataforma pt1;

    private Checkpoint checkpoint;

    private OrthographicCamera orthographicCamera;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private int personajeSeleccionado, puntuacion;

    private float timerTransicion;

    private Texture blank;

    private Enemigo e1, e2, e3, e4, e5, e6;

    private ArrayList<Enemigo> listaEnemigos;
    private ArrayList<Enemigo> enemigosDestroy;
    private ArrayList<Onda> ondasToDestroy;
    private ArrayList<Capsula> listaCapsulas;

    private Capsula c1, c2, c3, c4, c5, c6;

    private Music musica;


    private float elapsedTime;

    private Teclado teclado;

    public TercerMundo(Juego juego, int personajeSeleccionado) {
        this.juego = juego;
        this.personajeSeleccionado = personajeSeleccionado;

        orthographicCamera = new OrthographicCamera(20,20);
        world = new World(new Vector2(0,-9.8f),true);
        map = new TmxMapLoader().load("mapa/tercermapa.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map,1/16f);

        blank = new Texture("recursos/blank.png");

        timerTransicion = 0;

        listaEnemigos = new ArrayList<>();
        enemigosDestroy = new ArrayList<>();
        ondasToDestroy = new ArrayList<>();
        listaCapsulas = new ArrayList<>();

        checkpoint = new Checkpoint(world, 147,2.6f);

        p1 = new Personaje(world, personajeSeleccionado,146,2.6f);

        pt1 = new Plataforma(world, 113,1.5f, 115, 136);

        c1 = new Capsula(world, p1,"objetos/capsule.png", 27,7);
        c2 = new Capsula(world, p1,"objetos/capsule.png", 65.5f,13);
        c3 = new Capsula(world, p1,"objetos/capsule.png", 86,17);
        c4 = new Capsula(world, p1,"objetos/capsule.png", 109,7);
        c5 = new Capsula(world, p1,"objetos/capsule.png", 123,7);
        c6 = new Capsula(world, p1,"objetos/capsule.png", 140,7);

        listaCapsulas.add(c1);
        listaCapsulas.add(c2);
        listaCapsulas.add(c3);
        listaCapsulas.add(c4);
        listaCapsulas.add(c5);
        listaCapsulas.add(c6);

        box2DDebugRenderer = new Box2DDebugRenderer();

        musica = Gdx.audio.newMusic(Gdx.files.internal("sonido/musica/cyberpunk.mp3"));
        musica.play();
        musica.setVolume(0.03f);
        musica.setLooping(true);

        Interface.SetSpriteBatch(juego.getSpriteBatch(), p1.getKi());

        orthographicCamera.position.set(p1.getX(),p1.getY()+8,0);

        e1 = new Enemigo(world,18,26,1, 21, 2.6f);
        e2 = new Enemigo(world,29,33,1, 30, 6.6f);
        e3 = new Enemigo(world,41,46,1, 43, 9.6f);
        e4 = new Enemigo(world,70,75,1, 72, 10.6f);
        e5 = new Enemigo(world,84,88,1, 86, 14.6f);
        e6 = new Enemigo(world,92,104,1, 100, 2.6f);

        listaEnemigos.add(e1);
        listaEnemigos.add(e2);
        listaEnemigos.add(e3);
        listaEnemigos.add(e4);
        listaEnemigos.add(e5);
        listaEnemigos.add(e6);

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

                for(Enemigo e: listaEnemigos){

                    if(contact.getFixtureA().getBody() == p1.getCuerpo() && contact.getFixtureB().getBody() == e.getBody()){
                        p1.setSalud(-1);
                    }

                    for(int i = 0; i<e.getListaOndas().size();i++){

                        for(int l = 0; l<p1.getListaOndas().size();l++){

                            if(contact.getFixtureA().getBody() == p1.getListaOndas().get(l).getCuerpo() && contact.getFixtureB().getBody() == e.getListaOndas().get(i).getCuerpo()){
                                ondasToDestroy.add(e.getListaOndas().get(i));
                                ondasToDestroy.add(p1.getListaOndas().get(l));
                                e.getListaOndas().remove(i);
                                p1.getListaOndas().remove(l);
                            }

                        }

                        if(contact.getFixtureA().getBody() == p1.getCuerpo() && contact.getFixtureB().getBody() == e.getListaOndas().get(i).getCuerpo()){
                            System.out.println("Impacto");
                            ondasToDestroy.add(e.getListaOndas().get(i));
                            e.getListaOndas().remove(i);
                            p1.setSalud(-1);

                        }

                    }

                }

                for(int i = 0; i<p1.getListaOndas().size();i++){

                    if(p1.getListaOndas().get(i).getCuerpo().getPosition().x < 3){
                        ondasToDestroy.add(p1.getListaOndas().get(i));
                        p1.getListaOndas().remove(i);
                    }

                    for(int k = 0; k<listaEnemigos.size();k++){

                        if(contact.getFixtureA().getBody() == listaEnemigos.get(k).body && contact.getFixtureB().getBody() == p1.getListaOndas().get(i).getCuerpo()){
                            System.out.println("Impacto");
                            ondasToDestroy.add(p1.getListaOndas().get(i));
                            p1.getListaOndas().remove(i);
                            listaEnemigos.get(k).vidas--;

                            if(listaEnemigos.get(k).getVidas()<=0){

                                enemigosDestroy.add(listaEnemigos.get(k));

                            }

                        }

                    }

                    if(contact.getFixtureA().getBody() == p1.getCuerpo() && contact.getFixtureB().getBody() == p1.getListaOndas().get(i).getCuerpo()){
                        System.out.println("Impacto");
                        ondasToDestroy.add(p1.getListaOndas().get(i));
                        p1.getListaOndas().remove(i);
                        e1.vidas--;

                    }

                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        for(Onda onda : ondasToDestroy){
                            onda.body.setActive(false);
                        }

                        for(Enemigo enemigo : enemigosDestroy){
                            enemigo.body.setActive(false);
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

        System.out.println("Coordenadas del personaje X: "+p1.getCuerpo().getPosition().x+", Y: "+p1.getCuerpo().getPosition().y);

        orthographicCamera.update();

        orthographicCamera.position.x = p1.getCuerpo().getPosition().x;

        orthogonalTiledMapRenderer.setView(orthographicCamera);

        orthogonalTiledMapRenderer.render();

        juego.getSpriteBatch().setProjectionMatrix(orthographicCamera.combined);

        juego.getSpriteBatch().begin();

        Interface.draw(orthographicCamera, p1.getKi(), p1.getSalud(), puntuacion);

        p1.animaciones(elapsedTime);
        p1.draw(juego.getSpriteBatch(),0);

        pt1.draw(juego.getSpriteBatch(),0);

        checkpoint.draw(juego.getSpriteBatch(),0);

        for(Enemigo e:listaEnemigos){

            e.animacionAcciones(elapsedTime, p1);
            e.draw(juego.getSpriteBatch(), 0);
            e.setDistanciaEnemigo(p1.getCuerpo().getPosition().x);

            for(Onda o: e.getListaOndas()){

                o.cicloVida();
            }

        }

        for (Enemigo e: listaEnemigos){
            for (int j = 0; j<e.getListaOndas().size();j++){
                if(e.getListaOndas().get(j).tiempoVida>1f){
                    ondasToDestroy.add(e.getListaOndas().get(j));
                    e.getListaOndas().remove(j);
                }
            }
        }

        for(Onda o: ondasToDestroy){

            o.body.setActive(false);

        }

        for(Capsula c : listaCapsulas){

            c.draw(juego.getSpriteBatch(),0);

            if(c.getContadorColision()==1){

                puntuacion+=10;

            }

            c.recoleccion(p1);

        }

        if(p1.getSalud() == 0 || Interface.tiempototal<0){

            juego.setScreen(new PantallaGameOver(juego, personajeSeleccionado, 3));
            Interface.tiempo=0;
            dispose();

        }

        teclado.entrada();

        if(p1.getEstado()!= Personaje.Estado.TRANSICION){
            juego.getSpriteBatch().setColor(Color.GRAY);
            juego.getSpriteBatch().draw(blank,p1.getCuerpo().getPosition().x-3,-0.5f, 8,0.2f);

            juego.getSpriteBatch().setColor(Color.YELLOW);

            juego.getSpriteBatch().draw(blank, p1.getCuerpo().getPosition().x-3, -0.5f, p1.getKi(), 0.2f);

        }else{

            timerTransicion+= Gdx.graphics.getDeltaTime();

            juego.getSpriteBatch().setColor(Color.GRAY);
            juego.getSpriteBatch().draw(blank,184,-0.5f, 8,0.2f);

            juego.getSpriteBatch().setColor(Color.YELLOW);

            juego.getSpriteBatch().draw(blank, 184, -0.5f, p1.getKi(), 0.2f);

            if(timerTransicion>3){

                juego.setScreen(new PantallaTransicion(juego, puntuacion, personajeSeleccionado, 2, Interface.getTiempototal()));
                dispose();

            }

        }

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
