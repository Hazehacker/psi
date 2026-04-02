package com.zeroone.star.homepage.service.impl;

import com.zeroone.star.homepage.mapper.SummaryMapper;
import com.zeroone.star.homepage.service.ISummaryService;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.j1.homepage.SummaryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;

@Slf4j
@Service
public class SummaryServiceImpl implements ISummaryService {

    @Resource
    private SummaryMapper summaryMapper;

    @Resource
    private UserHolder userHolder;

    /**
     * 获取汇总信息
     * @return 包含多个SummaryDTO的列表，每个DTO代表一项汇总数据
     */
    @Override
    public ArrayList<SummaryDTO> getSummaryInfo() {
        // 声明变量，但此处在try块外声明，是为了避免在catch块或后续逻辑中无法访问
        String cur_userID;
        String framePID;
        try {
            UserDTO currentUser = userHolder.getCurrentUser();
            if (currentUser == null) {
                log.warn("无法获取当前登录用户信息");
                return new ArrayList<>();
            }
            cur_userID = currentUser.getId();
            framePID = currentUser.getFrameId();
        } catch (Exception e) {
            log.error("获取当前用户信息时发生异常", e);
            return new ArrayList<>();
        }


        // 1. 依次调用Mapper方法查询所有原始数据
        Long totalGoods = summaryMapper.getTotalGoodsCount();
        BigDecimal totalStock = summaryMapper.getTotalStockSum();
        Long totalCustomers = summaryMapper.getTotalCustomersCount(framePID);
        Long totalSuppliers = summaryMapper.getTotalSuppliersCount(framePID);
        Long stockWarnings = summaryMapper.getStockWarningCount();
        Long expiryWarnings = summaryMapper.getExpiryWarningCount();

        // 2. 创建一个列表来存储所有 SummaryDTO 对象
        ArrayList<SummaryDTO> resultList = new ArrayList<>();

        // 3. 将每一项数据分别封装成一个 SummaryDTO 对象并添加到列表中
        // 注意：DTO 中的 value 是 Integer, 需要进行类型转换
        // **已根据你的要求更新了 'to' 字段的值（路由）**
        resultList.add(new SummaryDTO("商品总数", totalGoods.intValue(), "/product-manage"));
        resultList.add(new SummaryDTO("库存总数", totalStock.intValue(), "/inventory-query"));
        resultList.add(new SummaryDTO("客户总数", totalCustomers.intValue(), "/customer-manage"));
        resultList.add(new SummaryDTO("供应商总数", totalSuppliers.intValue(), "/supplier-manage"));
        resultList.add(new SummaryDTO("库存预警", stockWarnings.intValue(), "/inventory-query"));
        resultList.add(new SummaryDTO("保质期预警", expiryWarnings.intValue(), "/batch-query"));

        // 4. 返回最终的列表
        return resultList;
    }
}