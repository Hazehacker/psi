package com.zeroone.star.moneytransfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.moneytransfer.entity.Account;
import com.zeroone.star.moneytransfer.entity.AccountInfo;
import com.zeroone.star.moneytransfer.entity.Allot;
import com.zeroone.star.moneytransfer.entity.AllotInfo;
import com.zeroone.star.moneytransfer.mapper.AllotInfoMapper;
import com.zeroone.star.moneytransfer.mapper.AllotMapper;
import com.zeroone.star.moneytransfer.service.IAllotService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.money.transfer.*;
import com.zeroone.star.project.query.j7.money.transfer.AllotExamineQuery;
import com.zeroone.star.project.query.j7.money.transfer.AllotQuery;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 转账单 服务实现类
 * </p>
 *
 * @author hh
 * @since 2025-10-23
 */
@Service
public class AllotServiceImpl extends ServiceImpl<AllotMapper, Allot> implements IAllotService {
     @Resource
     MsAllotMapper msAllotMapper;
     @Resource
     MsAllotInfoMapper msAllotInfoMapper;
    @Resource
    AllotMapper allotMapper;

    @Resource
    AllotInfoMapper allotInfoMapper;


    @SneakyThrows
    @Override
    public PageDTO<AllotListDTO> listAllot(AllotQuery query) {
        //要联合frame,people,user，alloInfo查询
        // 构建分页查询对象
        Page<Allot> page = new Page<>(query.getPageIndex(), query.getPageSize());
        // 构建查询条件对象
        QueryWrapper<Allot> queryWrapper = new QueryWrapper<>();

        QueryWrapper<AllotInfo> queryWrapper1=new QueryWrapper<>();
        //查询条件

        //单据编号
        queryWrapper.like(!StringUtils.isEmpty(query.getNumber()),"number",query.getNumber());

        //资金账户
        //根据allotinfo找到账户对应的pid从而定位到allot的id
        //可能是转出账户也可能是转入账户
        if(!StringUtils.isEmpty(query.getAccount())){
            queryWrapper1.eq("account",query.getAccount()).or()
                    .eq("tat",query.getAccount());
            List<AllotInfo> allotInfoList=allotInfoMapper.selectList(queryWrapper1);
            Set<String> pids=new HashSet<>();//去重
            if(allotInfoList.size()!=0){
                for(AllotInfo allotInfo:allotInfoList){
                    pids.add(allotInfo.getPid());
                }
                if(pids.size()!=0){
                    queryWrapper.in("id",pids);
                }
            }
        }



        if (query.getTime() != null) {
            queryWrapper.ge("time", query.getTime());
        }

// 结束时间：如果endTime不为null，才添加 time <= endTime
        if (query.getLeaveTime() != null) {
            queryWrapper.le("time", query.getLeaveTime());
        }
        //制单人
        queryWrapper.eq(!StringUtils.isEmpty(query.getUser()),"user",query.getUser());
        //审核状态
        queryWrapper.eq(query.getExamine()!=null,"examine",query.getExamine());
        //制单人
        queryWrapper.eq(!StringUtils.isEmpty(query.getPeople()),"people",query.getPeople());
        //备注信息
        queryWrapper.like(!StringUtils.isEmpty(query.getData()),"data",query.getData());
        // 分页查询
        Page<Allot> allotPage = baseMapper.selectPage(page, queryWrapper);

        //因为涉及联表返回值不能直接映射

        List<Allot> allotList=allotPage.getRecords();
        List<AllotListDTO> dtoList=new ArrayList<>();
        for(Allot allot:allotList){
            //先把对应字段转换了
            AllotListDTO dto=msAllotMapper.allotToAllotListDto(allot);
            dto.setFrame(allotMapper.getFrameName(allot));
            dto.setPeople(allotMapper.getPeopleName(allot));
            dto.setUser(allotMapper.getUserName(allot));
            dtoList.add(dto);
        }
        Page<AllotListDTO> pageRes = new Page<>(query.getPageIndex(), query.getPageSize());
        pageRes.setTotal(dtoList.size());
        pageRes.setRecords(dtoList);
        return PageDTO.create(pageRes);

    }


    @Transactional
    @Override
    public String saveAllot(AddAllotDTO addAllotDTO) {


        Allot allot=msAllotMapper.addAllotDtoToAllot(addAllotDTO);
        int res1=baseMapper.insert(allot);
        if(res1==0){
            throw  new RuntimeException("传入参数有错，新增账单记录失败");
        }
        int res2=0;
        //获取allot_info的pid
        String pid =allot.getId();

        //遍历设置单据金额
        BigDecimal total=allot.getTotal();
       List<AddAllotInfoDTO>  allotInfoDTOList=addAllotDTO.getAllotInfoList();
       //记录allotInfo的数量
       int cnt=allotInfoDTOList.size();
       //遍历列表更新信息
        for(AddAllotInfoDTO allotInfoDTO:allotInfoDTOList){
            //计算单据金额
            total.add(allotInfoDTO.getMoney());
            AllotInfo allotInfo=msAllotInfoMapper.addAllotInfoDtoToAllotInfo(allotInfoDTO);
            //设置allotinfo的pid
            allotInfo.setPid(pid);
            //插入allotinfo
             res2+=allotInfoMapper.insert(allotInfo);

             //转入转出账户不能相同

             //根据id获取整个account对象
            Account account= allotMapper.getAccount(allotInfo.getAccount());
            //转入账户
            LocalDate timee=allot.getTime();
            Account tat= allotMapper.getAccount(allotInfo.getTat());
            account.setBalance(account.getBalance().subtract(allotInfo.getMoney()));
            tat.setBalance(tat.getBalance().add(allotInfo.getMoney()));
            account.setTime(timee);
            tat.setTime(timee);
            //更新账户信息
            allotMapper.updateAccount(account);
            allotMapper.updateAccount(tat);
            this.insertAccountInfo(account,allot.getTime(),allotInfo.getMoney(),0,"allot",allot.getId());
            this.insertAccountInfo(tat,allot.getTime(),allotInfo.getMoney(),1,"allot",allot.getId());

        }
        //设置总金额
        allot.setTotal(total);

        if(res1==1&&res2==cnt){
            return  allot.getId();
        }
        else {
            throw  new RuntimeException("账单详情插入失败");
        }
    }

    @Transactional
    @Override
    public AllotDetailDTO getAllotdetail(String id ) {

        Allot allot=baseMapper.selectById(id);
        if(allot==null)return  null;
        AllotDetailDTO allotDetailDTO=msAllotMapper.allotToAllotDetailDTO(allot);
      List<AllotInfo> list=allotMapper.getAllotInfoList(id);
      List<AllotInfoDTO> targetList=new ArrayList<>();
      for (AllotInfo allotInfo:list){
          AllotInfoDTO allotInfoDTO=msAllotInfoMapper.allotInfoToAllotInfoDto(allotInfo);
          //返回可读性数据
          allotInfoDTO.setTat(allotInfoMapper.getAccountName(allotInfo.getAccount()));
          allotInfoDTO.setAccount(allotInfoMapper.getAccountName(allotInfo.getTat()));
          targetList.add(allotInfoDTO);
      }

      allotDetailDTO.setAllotInfoDTOList(targetList);
        return allotDetailDTO ;
    }


    /**
     * 转账单批量删除
     * @param ids 转账单ID列表
     * @return 是否成功
     */
    @Override
    @Transactional
    public List<String> removeAllots(List<String> ids) {
        //检查不可操作的账单，已经审核的账单不可删除

        //检查不存在的id

        List<String> successIds=new ArrayList<>();
        List<Allot> allotList= null;
        try {
            allotList = allotMapper.allotList(ids);
        } catch (Exception e) {
            throw new RuntimeException("存在错误id，中断删除操作");
        }
        QueryWrapper<AllotInfo> queryWrapper = new QueryWrapper<>();
        for(Allot allot:allotList){
            if(allot.getExamine()==1)continue;
            // 删除转账单详询（allot_info）
            queryWrapper.eq("pid",allot.getId());
            allotInfoMapper.delete(queryWrapper);
            // 删除转账单（allot）
            allotMapper.deleteById(allot);
            successIds.add(allot.getId());
        }
        return successIds;

    }

    /**
     * 批量审核/反审核转账单
     * @param query 审核状态参数
     * @param ids 转账单ID列表
     * @return 是否成功
     */
    @Override
    @Transactional
    public List<String> examine(AllotExamineQuery query, List<String> ids) {
        List<Allot> allotList = allotMapper.allotList(ids);
        if(allotList.isEmpty()){
            throw new RuntimeException("传入数据在数据表中均不存在");
        }

        //记录操作成功的id
        List<String> successIds=new ArrayList<>();

        Integer examine=query.getExamine();
        //取出所有的allots
        try {
            for(Allot allot:allotList) {

                //不需要操作的账单
                if(allot.getExamine().equals(examine))continue;

                //设置账单状态
                allot.setExamine(examine);
                //根据allot拿到allot_info
                String pid = allot.getId();
                List<AllotInfo> allotInfoList = allotMapper.getAllotInfoList(pid);

                //检查账户操作是否有误
                boolean accountOperation=false;

                for (AllotInfo allotInfo : allotInfoList) {
                    //每次遍历开始设置为false
                    accountOperation=false;
                    Account account = allotMapper.getAccount(allotInfo.getAccount());
                    Account tat = allotMapper.getAccount(allotInfo.getTat());
                    //检查传入账户是否存在
                    if(account==null||tat==null)continue;
                    //账户存在可以操作
                    else accountOperation=true;
                    BigDecimal money = allotInfo.getMoney();

                    if (examine == 1) {//更新账户信息
                        account.setBalance(account.getBalance().subtract(money));
                        tat.setBalance(tat.getBalance().add(money));

                    } else if(examine==0) {
                        account.setBalance(account.getBalance().add(money));
                        tat.setBalance(tat.getBalance().subtract(money));
                    }
                    //更新余额信息
                    allotMapper.updateAccount(account);
                    allotMapper.updateAccount(tat);
                    //这里直接插入新的accountinfo记录
                    if(examine==1){
                        this.insertAccountInfo(account,allot.getTime(),allotInfo.getMoney(),0,"allot",allot.getId());
                        this.insertAccountInfo(tat,allot.getTime(),allotInfo.getMoney(),1,"allot",allot.getId());
                    }else if(examine==0){
                        this.insertAccountInfo(account,allot.getTime(),allotInfo.getMoney(),0,"allot",allot.getId());
                        this.insertAccountInfo(tat,allot.getTime(),allotInfo.getMoney(),1,"allot",allot.getId());
                    }


                }
                //所有操作结束后再添加id
                if(accountOperation==true){
                    successIds.add(allot.getId());
                }

            }
        } catch (Exception e) {
            //操作失败只可能是传入参数有问题

          throw  new RuntimeException("操作中断,存在不可操作数据");

        }
        return  successIds;
    }

    private void insertAccountInfo(Account account,LocalDate time,BigDecimal money, Integer direction, String type, String className) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setId(IdWorker.getIdStr()); // 雪花ID
        accountInfo.setPid(account.getId()); // 关联账户ID
        accountInfo.setType(type);
        accountInfo.setClassName(className);
        accountInfo.setMoney(money);
        accountInfo.setTime(time);
        accountInfo.setDirection(direction); // 0=支出，1=收入
        allotMapper.insertAccountInfo(accountInfo);
    }
    /**
     * 修改转账单
     * @param allotModifyDTO 转账单
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean updateAllot(AllotModifyDTO allotModifyDTO) {
        if(allotModifyDTO == null || StringUtils.isEmpty(allotModifyDTO.getId())) {
            return false;
        }
        // 删除原有的 allot_info
        QueryWrapper<AllotInfo> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("pid", allotModifyDTO.getId());
        allotInfoMapper.delete(infoQueryWrapper);

        // 更新主表 allot
        Allot allot = msAllotMapper.allotModifyDtoToAllot(allotModifyDTO);
//        BeanUtils.copyProperties(allotModifyDTO, allot);
        int updateResult = allotMapper.updateById(allot);
        if(updateResult <= 0) {
            throw  new RuntimeException("修改转账单详情失败");
        }

        // 插入新的 allot_info
        List<AllotInfoDTO> dtoList = allotModifyDTO.getAllotList();
        if(dtoList != null && !dtoList.isEmpty()) {
            for(AllotInfoDTO dto : dtoList) {
                AllotInfo info = msAllotInfoMapper.allotInfoDTOToAllotInfo(dto);
//                BeanUtils.copyProperties(dto, info);
                // 将id设置为null，以便MybatisPlus自动生成
                info.setId(null);
                // 设置与主表关联的 id
                info.setPid(allotModifyDTO.getId());
                // 如果有任何一条详情插入失败，则事务回滚，最终也会返回 false
                int inserted = allotInfoMapper.insert(info);
                if(inserted <= 0) {
                    // 抛出异常以触发事务回滚
                    throw new RuntimeException("修改转账单详情失败");
                }
            }
        }
        return true;
    }
}
