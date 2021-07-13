package com.ak47007.controller;

import com.ak47007.model.Links;
import com.ak47007.service.FriendsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

/**
 * @author ak47007
 * @date 2020/5/21
 * 描述： 友情链接接口
 */
@Controller
@RequestMapping(value = "/friends")
@AllArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("friends");
        List<Links> friends = friendsService.getFriends();
        modelAndView.addObject("linksList", friends);
        return modelAndView;
    }

    /**
     * 申请友链
     */
    @PostMapping(value = "applyFriend")
    public String applyFriend(Links links) {
        friendsService.applyFriend(links);
        return "redirect:https://www.ak47007.com/friends";
    }

}
