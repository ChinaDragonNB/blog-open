package com.ak47007.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.ak47007.enums.LinkStatusEnum;
import com.ak47007.model.Link;
import com.ak47007.model.base.Result;
import com.ak47007.model.dto.LinkLineDTO;
import com.ak47007.model.query.LinkQuery;
import com.ak47007.model.vo.main.LinkLineVO;
import com.ak47007.service.LinksService;
import com.ak47007.utils.AssertUtil;
import com.ak47007.utils.ElementUISortUtil;
import com.ak47007.utils.SendEmailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.ak47007.mapper.LinkMapper;

/**
 * @author AK47007
 * @date 2019/7/13
 */
@Service
@AllArgsConstructor
public class LinksServiceImpl implements LinksService {

    private final LinkMapper linkMapper;

    private final SendEmailUtil sendEmailUtil;

    @Override
    public List<Link> listLink(LinkQuery query) {
        Integer type = query.getType();
        String title = query.getTitle();
        Long userId = query.getUserId();
        String columnName = query.getColumnName();
        String order = query.getOrder();

        LambdaQueryWrapper<Link> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Link::getUserId, userId);

        if (StrUtil.isNotBlank(title)) {
            wrapper.like(Link::getTitle, title);
        }
        if (type.equals(1)) {
            Integer status = LinkStatusEnum.PASS.getStatus();
            wrapper.eq(Link::getCheckStatus, status);
        } else {
            Integer notPass = LinkStatusEnum.NOT_PASS.getStatus();
            Integer checking = LinkStatusEnum.CHECKING.getStatus();
            wrapper.in(Link::getCheckStatus, checking, notPass);
        }
        String sql = ElementUISortUtil.sortSql(columnName, order, " `check_status` DESC , id ASC");

        wrapper.last(sql);

        query.startPage();
        List<Link> links = linkMapper.selectList(wrapper);
        return links;
    }

    @Override
    public Link linkInfo(Long id) {
        Link link = linkMapper.selectById(id);
        AssertUtil.notNull(link, "该链接不存在");
        return link;
    }

    @Override
    public Result editLink(Link link) {
        String title = link.getTitle();
        if (StrUtil.isBlank(title.trim())) {
            return Result.error("请输入网站标题");
        }
        String url = link.getLink();
        if (StrUtil.isBlank(url.trim())) {
            return Result.error("请输入网站地址");
        }
        String logo = link.getLogo();
        if (StrUtil.isBlank(logo.trim())) {
            return Result.error("请上传网站logo");
        }
        linkMapper.updateById(link);
        return Result.success("保存成功");
    }

    @Override
    public Result delLink(Long id) {
        linkMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @Override
    public int getCount(long userId) {
        LambdaQueryWrapper<Link> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Link::getUserId, userId);
        return linkMapper.selectCount(wrapper);
    }

    @Override
    public Result checkLink(Long linksId, int result) throws Exception {
        Link linkInfo = linkMapper.selectById(linksId);
        AssertUtil.notNull(linkInfo, "审核失败,该链接不存在");

        Link data = new Link();
        data.setId(linksId);
        Integer status = null;
        switch (result) {
            // 不通过
            case 0:
                status = LinkStatusEnum.NOT_PASS.getStatus();
                break;
            // 待审核
            case 1:
                status = LinkStatusEnum.CHECKING.getStatus();
                break;
            // 通过
            case 2:
                status = LinkStatusEnum.PASS.getStatus();
                data.setPassTime(LocalDateTime.now());
                String email = linkInfo.getEmail();
                if (Strings.isNotBlank(email)) {
                    sendEmailUtil.sendMail(email, "", "审核通过", "您的网站已经添加到AK47凌凌漆博客友情链接页面");
                }
                break;
        }
        data.setCheckStatus(status);

        linkMapper.updateById(data);
        return Result.success("审核成功");
    }

    @Override
    public Result<LinkLineVO> linkLine(Long userId) {
        List<Integer> pass = null;
        List<Integer> noPass = null;
        try {
            // 已通过的友情链接
            LinkLineDTO passLink = linkMapper.linkLine(2, userId);
            // 未通过的友情链接
            LinkLineDTO noPassLink = linkMapper.linkLine(0, userId);
            pass = new ArrayList<>();
            noPass = new ArrayList<>();
            setLinkLine(pass, passLink);
            setLinkLine(noPass, noPassLink);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("友情链接获取失败");
        }

        return Result.success(new LinkLineVO(pass, noPass, "<b>" + LocalDate.now().getYear() + "年各月份申请友情链接人数</b>"));
    }

    /**
     * 链接折线图转换
     */
    private void setLinkLine(List<Integer> list, LinkLineDTO dto) throws Exception {
        if (dto != null) {
            Class<?> clz = dto.getClass();
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                Method m = (Method) dto.getClass().getMethod("get" + getMethodName(field.getName()));
                Integer val = (Integer) m.invoke(dto);
                list.add(val);
            }
        } else {
            // 每个月的数量都赋值为0
            for (int i = 0; i < 12; i++) {
                list.add(0);
            }
        }

    }

    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fieldName) throws Exception {
        byte[] items = fieldName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

}


