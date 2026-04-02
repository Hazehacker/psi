package com.zeroone.star.storemanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j2.store.TransferDetailListDTO;
import com.zeroone.star.project.dto.j2.store.TransferListDTO;
import com.zeroone.star.storemanagement.entity.SwapDO;

import java.util.ArrayList;
import java.util.List;

public interface ISwapService extends IService<SwapDO> {

    ArrayList<TransferListDTO> getTransferListDTOList(List<String> idList);

    ArrayList<ArrayList<TransferDetailListDTO>> getTransferDetailListDTOList(List<String> idList);
}
