package input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import actores.Personaje;
import objetos.Onda;

public class Teclado implements InputProcessor {

    private Personaje personaje;
    private World world;

    public Teclado(World world,Personaje personaje) {
        this.personaje = personaje;
        this.world = world;
    }

    @Override
    public boolean keyDown(int keycode) {

       // Gdx.app.log("eventoDown","Input "+keycode);
        switch (keycode) {
            case Input.Keys.D:
                personaje.getCuerpo().applyLinearImpulse(new Vector2(7,0),personaje.getCuerpo().getWorldCenter(),true);
                break;
            case Input.Keys.A:
                personaje.getCuerpo().applyLinearImpulse(new Vector2(-7,0),personaje.getCuerpo().getWorldCenter(),true);
                break;
            case Input.Keys.W:
                personaje.getCuerpo().applyLinearImpulse(new Vector2(0,10),personaje.getCuerpo().getWorldCenter(),true);
                break;
            case Input.Keys.S:
                personaje.getCuerpo().setLinearVelocity(0,0);
                break;
            case Input.Keys.E:

                break;

            case Input.Keys.X:

                break;
        }
        return true;

    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode){

            case Input.Keys.F:
                personaje.setRafagazo(false);
                break;

            case Input.Keys.X:
                personaje.setCargando(false);
                break;

        }

        return false;
    }

    public void entrada(){

        if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
            personaje.setRafagazo(true);
            personaje.getListaOndas().add(new Onda(world, personaje));
            personaje.setEstado(Personaje.Estado.RAFAGA);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.X)){
            personaje.setCargando(true);
        }

    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


}
