package com.java.mod.service.impl;

import com.java.mod.dto.Game;
import com.java.mod.mapper.GameMapper;
import com.java.mod.service.GameService;
import com.java.mod.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private ProductService productService;

    @Override
    public Game insertGame(Game game) {
        if (productService.getProductByProductId(game.getProductid()) == null) {
            return null;
        }
        int i = gameMapper.insertGame(game);
        if (i <= 0) {
            return null;
        }
        return game;
    }

    @Override
    public Game updateGame(String gamename, Game game) {
        if (productService.getProductByProductId(game.getProductid()) == null) {
            return null;
        }
        Game updated = gameMapper.getGameByGamename(gamename);
        if (updated == null){
            return null;
        }
        updated.setGamename(game.getGamename());
        updated.setModId(game.getModId());
        updated.setProductid(game.getProductid());
        gameMapper.updateGame(updated);
        return updated;
    }

    @Override
    public Boolean deleteGame(String gamename) {
        if (gameMapper.deleteGame(gamename) > 0){
            return true;
        }
        return false;
    }

    @Override
    public Game getGameByGamename(String gamename) {
        return gameMapper.getGameByGamename(gamename);
    }

    @Override
    public List<Game> getAllGames() {
        return gameMapper.getAllGames();
    }
}
