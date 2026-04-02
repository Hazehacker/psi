package com.zeroone.star.sale.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j4.sale.SaleReturnIdsDTO;
import com.zeroone.star.project.query.j4.sale.SaleReturnCheckQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.sale.entity.*;
import com.zeroone.star.sale.mapper.SaleReturnplusFormMapper;
import com.zeroone.star.sale.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleReturnFormplusServiceImpl extends ServiceImpl<SaleReturnplusFormMapper, SaleReturnCheckQuery> implements SaleReturnFormplusService {
    @Resource
    private LogplusService logplusService;
    @Resource
    private SaleReturnInfoplusService saleReturnInfoplusService;
    @Resource
    private SaleReturnBillplusService saleReturnBillplusService;
    @Resource
    private IRoomplusService roomplusService;
    @Resource
    private IRoomInfoplusService roomInfoplusService;
    @Resource
    private IAccountplusService accountplusService;
    @Resource
    private IAccountInfoplusService accountInfoplusService;
    @Override
    @Transactional
    public JsonVO<Integer> check(SaleReturnIdsDTO saleReturnIdsDTO) {
        List<Integer> ids = saleReturnIdsDTO.getIds();
        List<SaleReturnCheckQuery> saleReturnCheckQueries = listByIds(ids);
        if(ids==null ||ids.isEmpty()){
            return JsonVO.success(1);
        }
        if(saleReturnCheckQueries==null || saleReturnCheckQueries.isEmpty()){
            return JsonVO.create(0,400,"数据库没有该退货单");
        }
        //核对
        for (SaleReturnCheckQuery form : saleReturnCheckQueries) {
            if(form.getTotal().compareTo(BigDecimal.ZERO)<0){
                return JsonVO.create(0,400,
                        form.getNumber()+"账单的单据金额小于0");
            }
            if(form.getActual().compareTo(BigDecimal.ZERO) < 0){
                return JsonVO.create(0,400,
                        form.getNumber()+"账单的实际金额为负数");
            }
            if(form.getMoney().compareTo(BigDecimal.ZERO) < 0){
                return JsonVO.create(0,400,
                        form.getNumber()+"账单的实付金额为负数");
            }
            if(form.getMoney().compareTo(form.getActual()) > 0){
                return JsonVO.create(0,400,
                        form.getNumber()+" 实付金额不能大于实际金额");
            }
            if(form.getCost().compareTo(BigDecimal.ZERO) < 0){
                return JsonVO.create(0,400,
                        form.getNumber()+"单据费用不能为负");
            }
        }
        for (SaleReturnCheckQuery saleReturnCheckQuery : saleReturnCheckQueries) {
            saleReturnCheckQuery.setCheckIn(1);//修改核对状态，改为已核对
            String info="核对退货单"+saleReturnCheckQuery.getNumber();
            logplusService.saveLog(info);//记录日志信息
        }
        updateBatchById(saleReturnCheckQueries);//修改数据库
        return JsonVO.success(1);
    }
    @Transactional
    @Override
    public JsonVO<Integer> uncheck(SaleReturnIdsDTO saleReturnIdsDTO) {
        List<Integer> ids = saleReturnIdsDTO.getIds();
        List<SaleReturnCheckQuery> saleReturnCheckQueries = listByIds(ids);
        if(ids==null ||ids.isEmpty()){
            return JsonVO.success(1);
        }
        if(saleReturnCheckQueries==null || saleReturnCheckQueries.isEmpty()){
            return JsonVO.create(0,400,"数据库没有该退货单");
        }
        for (SaleReturnCheckQuery saleReturnCheckQuery : saleReturnCheckQueries) {
            saleReturnCheckQuery.setCheckIn(0);//修改核对状态，改为未核对
            String info="反核对退货单"+saleReturnCheckQuery.getNumber();
            logplusService.saveLog(info);//记录日志信息
        }
        updateBatchById(saleReturnCheckQueries);//更新数据库
        return JsonVO.success(1);
    }

    @Override
    @Transactional
    public JsonVO<Integer> examine(SaleReturnIdsDTO saleReturnIdsDTO) {

        List<Integer> ids = saleReturnIdsDTO.getIds();//获取退货单id集合
        List<SaleReturnCheckQuery> saleReturnExam = listByIds(ids);//查询订单
        if(ids==null ||ids.isEmpty()){
            return JsonVO.success(1);
        }
        if(saleReturnExam ==null || saleReturnExam .isEmpty()){
            return JsonVO.create(0,400,"数据库没有该退货单");
        }
        for (SaleReturnCheckQuery query : saleReturnExam) {
            BigDecimal money = query.getTotal();//获取订单总金额
            String id = query.getId();//获取退货单id
            List<SreInfo> list = saleReturnInfoplusService.query().eq("pid", id).list();//获取订单对应的详细信息
            if(list==null || list.isEmpty()){
                return JsonVO.create(0,400,"审核未通过");
            }
            BigDecimal total = list.stream()
                    .map(SreInfo::getPrice)        // 提取price字段
                    .reduce(BigDecimal.ZERO, BigDecimal::add);// 累加详细信息中的商品价格
            if(total.compareTo(money)!=0){
                return JsonVO.create(0,400,"审核未通过,金额不对");
            }
            query.setExamine(1);
            logplusService.saveLog("审核退货单"+query.getNumber());//记录操作日志
            SreBill sre = SreBill.builder().pid(id).type("Sre").source(id).time(LocalDateTime.now()).money(total).build();
            saleReturnBillplusService.save(sre);//插入退货单核销详情
            List<Room> rooms = BeanUtil.copyToList(list, Room.class);
            roomplusService.updateAndsave(rooms);//修改仓库数据
            roomInfoplusService.saveBySreInfo(list);//添加仓库详情信息
            accountplusService.update().eq("id",query.getAccount()).setSql("balance = balance - " + total).update();//减少用户余额
            accountInfoplusService.saveinfo(query.getAccount(),query.getId(),total,0);//添加账户信息
        }
        updateBatchById(saleReturnExam);//更新审核状态
        return JsonVO.success(1);
    }


    @Override
    @Transactional
    public JsonVO<Integer> unexamine(SaleReturnIdsDTO saleReturnIdsDTO) {
        List<Integer> ids = saleReturnIdsDTO.getIds();//获取退货单id集合
        List<SaleReturnCheckQuery> saleReturnExam = listByIds(ids);//查询订单
        if(ids==null ||ids.isEmpty()){
            return JsonVO.success(1);
        }
        if(saleReturnExam ==null || saleReturnExam .isEmpty()){
            return JsonVO.create(0,400,"数据库没有该退货单");
        }
        for (SaleReturnCheckQuery examquery : saleReturnExam) {
            examquery.setExamine(0);
            String id=examquery.getId();
            List<SreInfo> list = saleReturnInfoplusService.query().eq("pid", id).list();//获取订单对应的详细信息
            logplusService.saveLog("反审核退货单"+examquery.getNumber());//记录操作日志
            boolean remove = saleReturnBillplusService.remove(
                    new LambdaQueryWrapper<SreBill>().eq(SreBill::getPid, examquery.getId())
            );//删除退货单核销详情
            if(!remove){
                throw new RuntimeException("没有该退货单的核销详情");
            }
            List<Room> rooms = BeanUtil.copyToList(list, Room.class);
            boolean sucess=roomplusService.updateAndDelete(rooms);//修改仓库数据
            if(!sucess){
                throw new RuntimeException("仓库产品数量不足");
            }
            roomInfoplusService. remove(new LambdaQueryWrapper<RoomInfo>().eq(RoomInfo::getClassName, list.get(0).getPid()));//删除仓库详情信息
            accountplusService.update().eq("id",examquery.getAccount()).
                    setSql("balance = balance + " + examquery.getTotal()).update();//增加用户余额
//            accountInfoService.remove(
//                    new LambdaQueryWrapper<AccountInfo>().eq(AccountInfo::getClassName, list.get(0).getPid())
//            );//删除账户信息
            accountInfoplusService.saveinfo(examquery.getAccount(),examquery.getId(),examquery.getTotal(),1);//添加账户信息
        }
        updateBatchById(saleReturnExam);//更新审核状态,改为未审核
        return JsonVO.create(1,200,"反审核成功");
    }

}
