package com.ak47007.controller;

import com.ak47007.model.vo.ArticleVO;
import com.ak47007.model.vo.Result;
import com.ak47007.service.ArticleService;
import com.ak47007.util.LocationUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

/**
 * @author AK47007
 * @date 2020/5/18
 */
@RestController
@RequestMapping(value = "/article")
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 文章首页
     */
    @GetMapping
    public ModelAndView index(String tag,String title) {
        return articleService.getData(1,tag,title);
    }

    /**
     * 文章首页 带页数
     */
    @GetMapping(value = "/{pageNum}")
    public ModelAndView index(@PathVariable int pageNum) {
        return articleService.getData(pageNum,null,null);
    }

    /**
     * 文章详情
     */
    @GetMapping(value = "/detail/{uuid}")
    public ModelAndView articleDetail(@PathVariable String uuid,HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("article/articleDetail");

        // 获取IP地址
        String ipAddr = LocationUtils.getIpAddr(request);
        ArticleVO article = articleService.articleDetail(uuid,ipAddr);
        if (article == null) {
            modelAndView.setViewName("error/404");
        }
        modelAndView.addObject("article",article);

        return modelAndView;
    }


    /**
     * 获取文章内容
     */
    @GetMapping(value = "getArticleContent", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getArticleContent(String uuid) {
        ArticleVO article = articleService.getArticleInfo(uuid);
        if (article == null) {
            return Result.error("获取文章内容失败");
        }
        return Result.success(article.getArticleContent());
    }



}
