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
import actores.Plataforma;
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
    private Enemigo e1, e2, e3, e4, e5, e6;
    private Capsula c1,c2,c3,c4,c5,c6;
    private Plataforma pt1;
    private ArrayList<Onda> ondasToDestroy;
    private ArrayList<Onda> ondasMundo;
    private ArrayList<Enemigo> listaEnemigos;
    private ArrayList<Enemigo> enemigosDestroy;
    private ArrayList<Capsula> listaCapsulas;

    private Music musica;

    private Viewport viewport;
    private OrthographicCamera orthographicCamera;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private int personajeSeleccionado, puntuacion;
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
        puntuacion = 0;

        listaEnemigos =  new ArrayList<>();
        listaCapsulas = new ArrayList<>();
        enemigosDestroy = new ArrayList<>();

        p1 = new Personaje(world, personajeSeleccionado,105,5.6f);

        pt1 = new Plataforma(world, 149,13.5f);

        e1 = new Enemigo(world,8,12,1, 15, 2.6f);
        e2 = new Enemigo(world, 45, 43,1,50, 2.6f);
        e3 = new Enemigo(world, 65, 80,1,70,2.6f);
        e4 = new Enemigo(world, 124, 133,1,130,9.6f);
        e5 = new Enemigo(world,148,162, 1,150,2.6f);
        e6 = new Enemigo(world,165,180,1,171, 14.6f);

        listaEnemigos.add(e1);
        listaEnemigos.add(e2);
        listaEnemigos.add(e3);
        listaEnemigos.add(e4);
        listaEnemigos.add(e5);
        listaEnemigos.add(e6);

        c1 = new Capsula(world, p1,"objetos/capsule.png", 11,11);
        c2 = new Capsula(world,p1,"objetos/capsule.png",33,7);
        c3 = new Capsula(world,p1,"objetos/capsule.png",54,7);
        c4 = new Capsula(world,p1,"objetos/capsule.png",119,10);

        listaCapsulas.add(c1);
        listaCapsulas.add(c2);
        listaCapsulas.add(c3);
        listaCapsulas.add(c4);

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

        orthographicCamera.update();

        orthographicCamera.position.x = p1.getCuerpo().getPosition().x;

        orthogonalTiledMapRenderer.setView(orthographicCamera);

        orthogonalTiledMapRenderer.render();

        box2DDebugRenderer.render(world, orthographicCamera.combined);

        juego.getSpriteBatch().setProjectionMatrix(orthographicCamera.combined);

        juego.getSpriteBatch().begin();

        Interface.draw(orthographicCamera, p1.getKi(), p1.getSalud(), puntuacion);

        p1.animaciones(elapsedTime);
        p1.draw(juego.getSpriteBatch(),0);

        System.out.println("Posicion d eY"+p1.getCuerpo().getPosition().y);

        pt1.draw(juego.getSpriteBatch(),0);

        System.out.println("WIDTH "+Gdx.graphics.getWidth()+" HEIGHT "+Gdx.graphics.getHeight()); //  1920 1017

        for(Enemigo e:listaEnemigos){

            e.animacionAcciones(elapsedTime, p1);
            e.draw(juego.getSpriteBatch(), 0);
            e.setDistanciaEnemigo(p1.getCuerpo().getPosition().x);

            for(Onda o: e.getListaOndas()){

                o.cicloVida();
            }

        }

        for (int x = 0; x<p1.getListaOndas().size();x++){

            p1.getListaOndas().get(x).cicloVida();

            if(p1.getListaOndas().get(x).tiempoVida>1f){
                ondasToDestroy.add(p1.getListaOndas().get(x));
                p1.getListaOndas().remove(x);
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

        System.out.println("Coordenadas del personaje X: "+p1.getCuerpo().getPosition().x+", Y: "+p1.getCuerpo().getPosition().y);

        for(Capsula c : listaCapsulas){

            c.draw(juego.getSpriteBatch(),0);

            if(c.getContadorColision()==1){

                puntuacion+=10;

            }

            c.recoleccion(p1);

        }

        //DEBUGS

        /*System.out.println("Coordenadas del personaje X: "+p1.getCuerpo().getPosition().x+", Y: "+p1.getCuerpo().getPosition().y);

        System.out.println("Estado personaje:  "+p1.getEstado());

        System.out.println("Direccion personaje:  "+p1.getDireccion());

        System.out.println("EL SUELO ESTA AQUI: "+p1.getPosicionSuelo());

        System.out.println("POSICION DEL DICHOSO BODY Y: "+p1.getCuerpo().getPosition().y);

        System.out.println("CARGANDOOOOOOOOOOOOOOOOO: "+p1.getCargando());*/

        //System.out.println("Overlasps primer mundo"+p1.recoleccion(c1));

        //c1.recoleccion(p1);

        /*System.out.println("KIKIKIKIKI"+p1.getKi());

        System.out.println("COLISION??????------------------>"+c1.getColision());

        System.out.println("Estado personaje:  "+p1.getEstado());*/

        System.out.println("VIDAS DEL ENEMIGO "+e4.getVidas());


        if(p1.getSalud() == 0 || Interface.tiempototal<0){

            juego.setScreen(new PantallaGameOver(juego, personajeSeleccionado));
            Interface.tiempo=0;
            dispose();

        }

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
