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
        AssertUtil.notNull(link, "??????????????????");
        return link;
    }

    @Override
    public Result editLink(Link link) {
        String title = link.getTitle();
        if (StrUtil.isBlank(title.trim())) {
            return Result.error("?????????????????????");
        }
        String url = link.getLink();
        if (StrUtil.isBlank(url.trim())) {
            return Result.error("?????????????????????");
        }
        String logo = link.getLogo();
        if (StrUtil.isBlank(logo.trim())) {
            return Result.error("???????????????logo");
        }
        linkMapper.updateById(link);
        return Result.success("????????????");
    }

    @Override
    public Result delLink(Long id) {
        linkMapper.deleteById(id);
        return Result.success("????????????");
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
        AssertUtil.notNull(linkInfo, "????????????,??????????????????");

        Link data = new Link();
        data.setId(linksId);
        Integer status = null;
        switch (result) {
            // ?????????
            case 0:
                status = LinkStatusEnum.NOT_PASS.getStatus();
                break;
            // ?????????
            case 1:
                status = LinkStatusEnum.CHECKING.getStatus();
                break;
            // ??????
            case 2:
                status = LinkStatusEnum.PASS.getStatus();
                data.setPassTime(LocalDateTime.now());
                String email = linkInfo.getEmail();
                if (Strings.isNotBlank(email)) {
                    sendEmailUtil.sendMail(email, "", "????????????", "???????????????????????????AK47?????????????????????????????????");
                }
                break;
        }
        data.setCheckStatus(status);

        linkMapper.updateById(data);
        return Result.success("????????????");
    }

    @Override
    public Result<LinkLineVO> linkLine(Long userId) {
        List<Integer> pass = null;
        List<Integer> noPass = null;
        try {
            // ????????????????????????
            LinkLineDTO passLink = linkMapper.linkLine(2, userId);
            // ????????????????????????
            LinkLineDTO noPassLink = linkMapper.linkLine(0, userId);
            pass = new ArrayList<>();
            noPass = new ArrayList<>();
            setLinkLine(pass, passLink);
            setLinkLine(noPass, noPassLink);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("????????????????????????");
        }

        return Result.success(new LinkLineVO(pass, noPass, "<b>" + LocalDate.now().getYear() + "????????????????????????????????????</b>"));
    }

    /**
     * ?????????????????????
     */
    private void setLinkLine(List<Integer> list, LinkLineDTO dto) throws Exception {
        if (dto != null) {
            Class<?> clz = dto.getClass();
            // ???????????????????????????????????????Field??????
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                Method m = (Method) dto.getClass().getMethod("get" + getMethodName(field.getName()));
                Integer val = (Integer) m.invoke(dto);
                list.add(val);
            }
        } else {
            // ??????????????????????????????0
            for (int i = 0; i < 12; i++) {
                list.add(0);
            }
        }

    }

    // ??????????????????????????????????????????????????????????????????
    private static String getMethodName(String fieldName) throws Exception {
        byte[] items = fieldName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

}


