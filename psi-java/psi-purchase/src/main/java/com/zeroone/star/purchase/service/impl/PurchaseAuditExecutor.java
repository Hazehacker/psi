package com.zeroone.star.purchase.service.impl;

import com.zeroone.star.purchase.DO.BuyDO;
import com.zeroone.star.purchase.DO.BuyInfoDO;
import com.zeroone.star.purchase.mapper.AccountMapper;
import com.zeroone.star.purchase.mapper.BuyInfoMapper;
import com.zeroone.star.purchase.mapper.BuyMapper;
import com.zeroone.star.purchase.mapper.RoomMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 采购单单条审核事务执行器
 *
 * <p>负责在独立事务内完成单条采购单的审核/反审核全部 DB 操作，
 * 由 {@link BuyServiceImpl#examineStatus} 在持有 Redisson 分布式锁期间调用。
 *
 * <p>事务在本方法返回时提交，调用方在 finally 块中释放锁，
 * 保证其他线程获锁后读到已提交状态，彻底消除并发重复审核风险。
 */
@Slf4j
@Component
public class PurchaseAuditExecutor {

    @Resource
    private BuyMapper buyMapper;

    @Resource
    private BuyInfoMapper buyInfoMapper;

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private AccountMapper accountMapper;

    /**
     * 执行单条采购单的审核或反审核
     *
     * @param noteId 采购单 ID
     * @param target 目标审核状态：1=审核，0=反审核
     * @return {@code true} — DB 已发生变更；{@code false} — 幂等跳过或数据异常
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean execute(String noteId, int target) {
        // 1. 加载主记录
        BuyDO buyDO = buyMapper.selectById(noteId);
        if (buyDO == null) {
            log.warn("[审核] 采购单 [{}] 不存在，跳过", noteId);
            return false;
        }

        // 2. 幂等检查：目标状态与当前一致则无需变更
        if (buyDO.getExamine() == target) {
            log.info("[审核] 采购单 [{}] 当前状态已为 {}，无需变更", noteId, target);
            return false;
        }

        // 3. 加载明细（审核/反审核均需同步库存）
        List<BuyInfoDO> infos = buyInfoMapper.selectByPid(noteId);
        if (infos == null || infos.isEmpty()) {
            log.warn("[审核] 采购单 [{}] 无明细，跳过", noteId);
            return false;
        }

        // 4. 同步库存
        for (BuyInfoDO info : infos) {
            BigDecimal delta = info.getNums();
            if (target == 0) {
                // 反审核：还原库存
                roomMapper.updateNumsByWarehouseAndGoods(info.getWarehouse(), info.getGoods(), delta);
            } else {
                // 审核：核减库存
                roomMapper.updateNumsByWarehouseAndGoods(info.getWarehouse(), info.getGoods(), delta.negate());
            }
        }

        // 5. 同步资金账户
        if (target == 0) {
            // 反审核：还原资金
            accountMapper.updateAccountByName(buyDO.getAccount(), buyDO.getMoney().negate());
        } else {
            // 审核：扣减资金
            accountMapper.updateAccountByName(buyDO.getAccount(), buyDO.getMoney());
        }

        // 6. 更新审核状态（最后写，作为"提交"标志）
        BuyDO patch = new BuyDO();
        patch.setId(noteId);
        patch.setExamine(target);
        buyMapper.updateById(patch);

        log.info("[审核] 采购单 [{}] {} 成功", noteId, target == 1 ? "审核" : "反审核");
        return true;
    }
}
