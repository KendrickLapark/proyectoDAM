package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pantallas.PantallaMenu;

public class Juego extends Game {

	public SpriteBatch spriteBatch;

	@Override
	public void create () {

		spriteBatch = new SpriteBatch();
		setScreen(new PantallaMenu(this));

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
