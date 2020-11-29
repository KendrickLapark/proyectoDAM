package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pantallas.PantallaMenu;
import pantallas.PantallaSeleccion;
import pantallas.PantallaGameOver;
import pantallas.PrimerMundo;

public class Juego extends Game {

	private Screen pantallaActual;
	public SpriteBatch spriteBatch;

	@Override
	public void create () {

		spriteBatch = new SpriteBatch();
		setScreen(new PrimerMundo(this,1));

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}


}
