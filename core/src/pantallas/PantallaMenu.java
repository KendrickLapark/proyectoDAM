package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Juego;

public class PantallaMenu implements Screen {

    private Stage stage;
    int opcionSeleccionada, cont; // Variable con el personaje sobre el que recae el foco de elección y un contador.
    private Juego juego; // Juego en el que se muestra la  pantalla.
    private Music musica; // Música de la pantalla
    TextButton startButton, optionsButton, exitButton;
    Texture texture1, texture2, texture3, texture4;
    Image fondo;
    private TextButton.TextButtonStyle textButtonStyle, textButtonStyle2;

    void preparaPantalla(){

        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("recursos/impact.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        freeTypeFontParameter.size = 12;
        freeTypeFontParameter.borderWidth = 1;

        BitmapFont bitmapFont = freeTypeFontGenerator.generateFont(freeTypeFontParameter);



        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitmapFont;
        textButtonStyle.fontColor = Color.GRAY;

        textButtonStyle2 =new TextButton.TextButtonStyle();
        textButtonStyle2.font = bitmapFont;
        textButtonStyle2.fontColor = Color.WHITE;

        startButton = new TextButton("Nueva Partida", textButtonStyle);
        startButton.setPosition(stage.getWidth() - startButton.getWidth()/0.75f, stage.getHeight() - startButton.getHeight()/0.6f);

        optionsButton = new TextButton("Opciones", textButtonStyle);
        optionsButton.setPosition(stage.getWidth() - optionsButton.getWidth()/0.55f, stage.getHeight() - optionsButton.getHeight()/0.26f);

        exitButton = new TextButton("Salir", textButtonStyle);
        exitButton.setPosition(stage.getWidth() - exitButton.getWidth()/0.33f, stage.getHeight() - exitButton.getHeight()/0.17f);

        opcionSeleccionada=1;

        texture4 = new Texture("recursos/fondops2.jpg");

        fondo = new Image(texture4);
        fondo.setSize(stage.getWidth(),stage.getHeight());

        stage.addActor(fondo);

    }

    void controlador(){

        stage.addActor(startButton);
        stage.addActor(optionsButton);
        stage.addActor(exitButton);

        if(Gdx.input.isKeyJustPressed(Input.Keys.S )||(Gdx.input.isKeyPressed(Input.Keys.DOWN))){

            if(opcionSeleccionada==3){
                opcionSeleccionada=1;
            }else{
                opcionSeleccionada++;
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.W )||(Gdx.input.isKeyPressed(Input.Keys.UP))){

            if(opcionSeleccionada==1){
                opcionSeleccionada=3;
            }else{
                opcionSeleccionada--;
            }
        }

        if(Gdx.input.isKeyJustPressed((Input.Keys.ENTER))){

            if(opcionSeleccionada==1){

                juego.setScreen(new PantallaSeleccion(juego));

            }

            if(opcionSeleccionada==2){



            }

            if(opcionSeleccionada==3){

                System.exit(0);

            }

        }

        if(opcionSeleccionada==1){
            startButton.setStyle(textButtonStyle2);
            optionsButton.setStyle(textButtonStyle);
            exitButton.setStyle(textButtonStyle);
        }

        if(opcionSeleccionada==2){
            startButton.setStyle(textButtonStyle);
            optionsButton.setStyle(textButtonStyle2);
            exitButton.setStyle(textButtonStyle);
        }

        if(opcionSeleccionada==3){
            startButton.setStyle(textButtonStyle);
            optionsButton.setStyle(textButtonStyle);
            exitButton.setStyle(textButtonStyle2);
        }

    }

    public PantallaMenu(Juego j){
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
}
