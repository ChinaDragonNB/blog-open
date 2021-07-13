package com.ak47007.controller;

import com.ak47007.model.vo.Result;
import com.ak47007.model.vo.TagVO;
import com.ak47007.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author AK47007
 * @date 2020/5/20
 */
@RestController
@RequestMapping(value = "/tag")
@AllArgsConstructor
@CrossOrigin
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        List<TagVO> tagList = tagService.getTagList();
        modelAndView.addObject("tagList",tagList);
        modelAndView.setViewName("tag");
        return modelAndView;
    }

    /**
     * 标签数据
     */
    @GetMapping(value = "/listTag", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result listTag() {
        List<TagVO> tagList = tagService.getTagList();
        return Result.success(tagList);
    }
}
