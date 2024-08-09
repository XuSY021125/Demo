package com.java.mod.mapper;

import com.java.mod.dto.Game;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GameMapper {
    @Insert("INSERT INTO table_games (gamename, modId, productid) VALUES (#{gamename}, #{modId}, #{productid})")
    int insertGame(Game game);

    @Delete("DELETE FROM table_games WHERE gamename = #{gamename}")
    int deleteGame(String gamename);

    @Update("UPDATE table_games SET modId = #{modId}, productid = #{productid} WHERE gamename = #{gamename}")
    int updateGame(Game game);

    @Select("SELECT * FROM table_games WHERE gamename = #{gamename}")
    Game getGameByGamename(String gamename);

    @Select("SELECT * FROM table_games WHERE productid = #{productid}")
    List<Game> getGameByModidAndProductid(Integer productid);

    @Select("SELECT * FROM table_games")
    List<Game> getAllGames();
}

