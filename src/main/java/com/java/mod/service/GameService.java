package com.java.mod.service;

import com.java.mod.dto.Game;

import java.util.List;

public interface GameService {
    Game insertGame(Game game);

    Game updateGame(String gamename, Game game);

    Boolean deleteGame(String gamename);

    Game getGameByGamename(String gamename);

    List<Game> getAllGames();
}
