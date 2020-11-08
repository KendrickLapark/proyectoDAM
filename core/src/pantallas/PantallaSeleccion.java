package com.mygdx.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Juego;

import pantallas.PrimerMundo;


public class PantallaSeleccion implements Screen {


    private Stage stage;
    int personajeActual, cont; // Variable con el personaje sobre el que recae el foco de elección y un contador.
    private Juego juego; // Juego en el que se muestra la  pantalla.
    private Music musica; // Música de la pantalla
    TextButton startButton;
    Texture texture1, texture2, texture3, texture4;
    Image goku, vegeta, piccolo, fondo;

    void preparaPantalla(){


        musica = Gdx.audio.newMusic(Gdx.files.internal("sonido/musica/menumusic.mp3"));
        musica.play();
        musica.setVolume(0.03f);
        musica.setLooping(true);

        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("recursos/impact.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        freeTypeFontParameter.size = 15;
        freeTypeFontParameter.borderWidth = 1;
        freeTypeFontParameter.borderColor = Color.PURPLE;

        BitmapFont bitmapFont = freeTypeFontGenerator.generateFont(freeTypeFontParameter);


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitmapFont;
        textButtonStyle.fontColor = Color.WHITE;

        startButton = new TextButton("START", textButtonStyle);
        startButton.setPosition(stage.getWidth() - startButton.getWidth()/0.45f, stage.getHeight() - startButton.getHeight());

        texture1 = new Texture("personajes/Goku/gstandr.png");
        texture2 = new Texture("personajes/Vegeta/vstandr.png");
        texture3 = new Texture("personajes/Piccolo/pstandr.png");
        texture4 = new Texture("recursos/fondops2.jpg");

        fondo = new Image(texture4);
        fondo.setSize(stage.getWidth(),stage.getHeight());

        goku = new Image(texture1);
        goku.setBounds(16,16,20,20);
        goku.setPosition(30,50);

        vegeta = new Image(texture2);
        vegeta.setBounds(16,16,20,20);
        vegeta.setPosition(70,50);

        piccolo = new Image(texture3);
        piccolo.setBounds(16,16,20,20);
        piccolo.setPosition(110,50);

        stage.addActor(fondo);
        stage.addActor(goku);
        stage.addActor(vegeta);
        stage.addActor(piccolo);

        personajeActual = 1;

    }

    void controlador(){

        if(cont==0){
            startButton.addListener( new ClickListener(){
                @Override
                public void touchUp(InputEvent inputEvent,float x, float y, int pointer, int button){
                    //dispose();
                    juego.setScreen(new PrimerMundo(juego, personajeActual));
                    dispose();
                }
            });

            cont++;
            stage.addActor(startButton);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.A )||(Gdx.input.isKeyPressed(Input.Keys.LEFT))){

            if(personajeActual==1){
                personajeActual=3;
            }else{
                personajeActual--;
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.D )||(Gdx.input.isKeyPressed(Input.Keys.RIGHT))){

            if(personajeActual==3){
                personajeActual=1;
            }else{
                personajeActual++;
            }
        }

        if(personajeActual==1){
            goku.setColor(Color.WHITE);
            vegeta.setColor(Color.GRAY);
            piccolo.setColor(Color.GRAY);
        }

        if(personajeActual==2){
            goku.setColor(Color.GRAY);
            vegeta.setColor(Color.WHITE);
            piccolo.setColor(Color.GRAY);
        }

        if(personajeActual==3){
            goku.setColor(Color.GRAY);
            vegeta.setColor(Color.GRAY);
            piccolo.setColor(Color.WHITE);
        }

    }

    public PantallaSeleccion(Juego j){
        cont = 0;

        this.juego = j;
        FitViewport fitViewport = new FitViewport(160,120);
        stage = new Stage(fitViewport);
        Gdx.input.setInputProcessor(stage);

        preparaPantalla();

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        controlador();

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

        stage.dispose();
        musica.dispose();
    }

    public int getPersonajeActual(){
        return  personajeActual;
    }
}
