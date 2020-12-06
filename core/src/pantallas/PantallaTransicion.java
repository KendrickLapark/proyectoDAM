package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Juego;

public class PantallaTransicion implements Screen {

    private Texture texture;

    private Image fondo;

    private Juego juego;

    private Stage stage;

    private BitmapFont bitmapFont;

    private int score, personajeOpcion, mundoPrevio;

    private float tiempoMapa;

    private Label label, label2, label3, label4;

    public PantallaTransicion(Juego juego, int puntuacion, int personajeSeleccionado, int numMundo, float tiempo) {

        this.juego = juego;

        personajeOpcion = personajeSeleccionado;

        tiempoMapa = tiempo;

        mundoPrevio = numMundo;

        FitViewport fitViewport = new FitViewport(160,120);

        this.stage = new Stage(fitViewport);

        this.score = puntuacion;

        preparaPantalla();

    }

    void preparaPantalla(){

        fondo = new Image(new Texture("recursos/black.png"));

        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("recursos/impact.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        freeTypeFontParameter.size = 12;
        freeTypeFontParameter.borderWidth = 1;

        bitmapFont = freeTypeFontGenerator.generateFont(freeTypeFontParameter);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font =  bitmapFont;
        labelStyle.fontColor = Color.WHITE;

        label = new Label("Estadisticas.",  labelStyle);
        label.setPosition(30,90);

        label2 = new Label("Score: "+score, labelStyle);
        label2.setPosition(50,70);

        label3 = new Label("Tiempo: "+tiempoMapa, labelStyle);
        label3.setPosition(30,50);

        label4 = new Label("Pulsa para continuar", labelStyle);
        label4.setPosition( 10, 20);

        stage.addActor(label);
        stage.addActor(label2);
        stage.addActor(label3);
        stage.addActor(label4);
        stage.addActor(fondo);

    }

    void controlador(){

        if(mundoPrevio==1){
            if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()){

                juego.setScreen(new SegundoMundo(juego, personajeOpcion));

            }
        }



    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controlador();

        stage.draw();

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

    }
}
