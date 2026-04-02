package com.zeroone.star.homepage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.homepage.entity.Buy;
import com.zeroone.star.homepage.mapper.BuyMapper;
import com.zeroone.star.homepage.service.IBuyService;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.j1.homepage.DV;
import com.zeroone.star.project.query.j1.homepage.PurchaseDailyTotalQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购单详情 服务实现类
 * </p>
 *
 * @author zonk
 * @since 2025-11-1
 */
@Slf4j // 2. 添加 Slf4j 日志
@Service
public class BuyServiceImpl extends ServiceImpl<BuyMapper, Buy> implements IBuyService {

    @Resource
    private UserHolder userHolder;

    @Override
    public ArrayList<DV> getPurchaseOrderStatistics(PurchaseDailyTotalQuery query) {
        String cur_userID;
        String frameId;
        try {
            UserDTO currentUser = userHolder.getCurrentUser();
            cur_userID = currentUser.getId();
            frameId = currentUser.getFrameId();
        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
            return new ArrayList<>();
        }

        // 2. 从数据库获取 *有数据* 的日期
        ArrayList<DV> sqlResults = baseMapper.getPurchaseOrderStatistics(query, cur_userID, frameId);

        // 3. 将 SQL 结果转为 Map
        Map<String, BigDecimal> dataMap = sqlResults.stream()
                .collect(Collectors.toMap(DV::getDate, DV::getValue));

        // 4. 补全日期：遍历查询的每一天，填充数据
        ArrayList<DV> filledList = new ArrayList<>();

        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String dateString = date.toString();
            BigDecimal value = dataMap.getOrDefault(dateString, BigDecimal.ZERO);
            filledList.add(new DV(dateString, value));
        }

        return filledList;
    }
}
