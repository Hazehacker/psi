package com.zeroone.star.storemanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j2.store.TransferDetailListDTO;
import com.zeroone.star.project.dto.j2.store.TransferListDTO;
import com.zeroone.star.storemanagement.entity.SwapDO;
import com.zeroone.star.storemanagement.mapper.SwapInfoMapper;
import com.zeroone.star.storemanagement.mapper.SwapMapper;
import com.zeroone.star.storemanagement.service.ISwapService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class SwapServiceImpl extends ServiceImpl<SwapMapper, SwapDO> implements ISwapService {

    @Resource
    SwapMapper swapMapper;

    @Resource
    SwapInfoMapper swapInfoMapper;

    @Override
    public ArrayList<TransferListDTO> getTransferListDTOList(List<String> idList) {
        return new ArrayList<>(swapMapper.getTransferListDTOList(idList));
    }

    @Override
    public ArrayList<ArrayList<TransferDetailListDTO>> getTransferDetailListDTOList(List<String> idList) {
        ArrayList<ArrayList<TransferDetailListDTO>> list = new ArrayList<>();
        for(String s:idList){
            System.out.println("id:"+s);
            //ArrayList<TransferDetailListDTO> transferDetailListDTO = swapInfoMapper.getTransferDetailListDTO(s);
            //list.add(transferDetailListDTO);
        }
        return list;
    }
}
