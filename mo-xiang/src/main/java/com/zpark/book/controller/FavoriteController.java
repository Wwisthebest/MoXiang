package com.zpark.book.controller;

import com.zpark.book.enums.ErrorMsg;
import com.zpark.book.entity.Favorite;
import com.zpark.book.service.FavoriteService;
import com.zpark.book.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@RequestMapping("/favorite")

public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public ResultVo addFavorite(@CookieValue("shUserId")
                                    @NotNull(message = "登录异常 请重新登录")
                                    @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                    @RequestBody Favorite favorite){
        favorite.setUserId(Long.valueOf(shUserId));
        favorite.setCreateTime(new Date());
        if(favoriteService.addFavorite(favorite)){
            return ResultVo.success(favorite.getId());
        }
        return ResultVo.fail(ErrorMsg.FAVORITE_EXIT);
    }

    @GetMapping("/delete")
    public ResultVo deleteFavorite(@CookieValue("shUserId")
                                       @NotNull(message = "登录异常 请重新登录")
                                       @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                       @RequestParam Long id){
        if(favoriteService.deleteFavorite(id)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("/check")
    public ResultVo checkFavorite(@CookieValue("shUserId")
                                      @NotNull(message = "登录异常 请重新登录")
                                      @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                  @RequestParam Long idleId){
        return ResultVo.success(favoriteService.isFavorite(Long.valueOf(shUserId),idleId));
    }

    @GetMapping("/my")
    public ResultVo getMyFavorite(@CookieValue("shUserId")
                                    @NotNull(message = "登录异常 请重新登录")
                                    @NotEmpty(message = "登录异常 请重新登录") String shUserId){
        return ResultVo.success(favoriteService.getAllFavorite(Long.valueOf(shUserId)));
    }
}
