package com.java.mod.controller;

import com.java.mod.apiResponse.ApiResponse;
import com.java.mod.dto.Game;
import com.java.mod.service.GameService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GameController 类用于处理与游戏相关的 HTTP 请求
 */
@RestController
@RequestMapping("/games")
@Slf4j
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * 创建新游戏的 POST 请求处理器
     *
     * @param game 要创建的游戏对象
     * @return 如果创建成功，返回 201 CREATED 状态码和包含游戏对象的响应体；
     *         如果游戏已存在，则返回 409 CONFLICT 状态码和错误信息；
     *         如果创建失败，则返回 400 BAD_REQUEST 状态码和错误信息。
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Game>> createGame(@RequestBody @Valid Game game) {
        if (gameService.getGameByGamename(game.getGamename()) != null){
            log.warn("Attempted to create an existing game with gamename: {}", game.getGamename());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(409, "Game already exists", null));
        }
        if (gameService.insertGame(game) == null){
            log.error("Failed to create game with gamename: {}", game.getGamename());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Failed to create game", null));
        }
        log.info("Successfully created game with gamename: {}", game.getGamename());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(201, "Game created successfully", game));
    }

    /**
     * 根据游戏名称获取游戏的 GET 请求处理器
     *
     * @param gamename 游戏名称
     * @return 如果找到游戏，返回 200 OK 和游戏对象；
     *         如果未找到游戏，则返回 404 NOT_FOUND 和错误信息
     */
    @GetMapping("/{gamename}")
    public ResponseEntity<ApiResponse<Game>> getGameByGamename(@PathVariable String gamename) {
        Game game = gameService.getGameByGamename(gamename);
        if (game == null){
            log.warn("Attempted to retrieve non-existent game with gamename: {}", gamename);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Game not found", null));

        }
        log.info("Query game with gamename: {}", gamename);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", game));
    }

    /**
     * 更新游戏的 PUT 请求处理器
     *
     * @param gamename 游戏名称。
     * @param game     包含更新信息的游戏对象
     * @return 200 OK 和更新后的游戏对象
     */
    @PutMapping("/{gamename}")
    public ResponseEntity<ApiResponse<Game>> updateGame(@PathVariable String gamename, @RequestBody Game game) {
        game.setGamename(gamename);
        Game beforeUp = gameService.getGameByGamename(gamename);
        Game updated = gameService.updateGame(gamename,game);
        if (updated == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Failed to update game. No game or product found", null));
        }
        if (beforeUp.equals(game)){
            return ResponseEntity.badRequest().body(new ApiResponse<>(400,"Game updated failed. The updated gama already exists", updated));
        }
        return ResponseEntity.ok(new ApiResponse<>(200,"Game updated successfully", updated));
    }

    /**
     * 删除游戏的 DELETE 请求处理器
     *
     * @param gamename 游戏名称
     * @return 如果删除成功，返回 200 OK 和确认消息；
     *         如果未找到游戏，则返回 404 NOT_FOUND 和错误信息
     */
    @DeleteMapping("/{gamename}")
    public ResponseEntity<String> delete(@PathVariable String gamename) {
        boolean deleted = gameService.deleteGame(gamename);
        if (deleted) {
            return ResponseEntity.ok("Deleted successfully");
        }
        return ResponseEntity.status(404).body("Game not found");
    }

    /**
     * 获取所有游戏的 GET 请求处理器
     *
     * @return 200 OK 和包含所有游戏列表的响应体
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Game>>> getAllGame() {
        List<Game> gameList = gameService.getAllGames();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", gameList));
    }
}