package com.zeroone.star.fund.service.impl;

import com.zeroone.star.fund.entity.Receipt;
import com.zeroone.star.fund.entity.ReceiptDetail;
import com.zeroone.star.fund.mapper.ReceiptDetailMapper;
import com.zeroone.star.fund.mapper.ReceiptMapper;
import com.zeroone.star.project.dto.j4.fund.ReceiptAddDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptSettlementDetailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundReceiptServiceImplTest {

    @Mock
    private ReceiptMapper receiptMapper;

    @Mock
    private ReceiptDetailMapper receiptDetailMapper;

    @InjectMocks
    private FundReceiptServiceImpl fundReceiptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addReceipt() {
        // 创建测试数据
        ReceiptAddDTO receiptAddDTO = new ReceiptAddDTO();
        receiptAddDTO.setCustomer("customer1");
        receiptAddDTO.setTime(LocalDateTime.now());
        receiptAddDTO.setNumber("REC001");
        receiptAddDTO.setTotal(new BigDecimal("1000.00"));
        // 修复 List.of() 兼容性问题
        List<String> files = new ArrayList<>();
        files.add("file1.txt");
        files.add("file2.txt");
        receiptAddDTO.setFile(files);
        receiptAddDTO.setData("备注信息");
        receiptAddDTO.setUser("user1");
        receiptAddDTO.setFrame("frame1");

        // 创建结算明细
        List<ReceiptSettlementDetailDTO> infoList = new ArrayList<>();
        ReceiptSettlementDetailDTO detail1 = new ReceiptSettlementDetailDTO();
        detail1.setAccount("account1");
        detail1.setAmount(new BigDecimal("500.00"));
        detail1.setSettle("settle1");
        detail1.setData("detail1备注");
        infoList.add(detail1);

        ReceiptSettlementDetailDTO detail2 = new ReceiptSettlementDetailDTO();
        detail2.setAccount("account2");
        detail2.setAmount(new BigDecimal("500.00"));
        detail2.setSettle("settle2");
        detail2.setData("detail2备注");
        infoList.add(detail2);

        receiptAddDTO.setInfo(infoList);

        // 模拟数据库操作返回值
        when(receiptMapper.insert(any(Receipt.class))).thenReturn(1);
        when(receiptDetailMapper.insert(any(ReceiptDetail.class))).thenReturn(1);

        // 执行测试方法
        fundReceiptService.addReceipt(receiptAddDTO);

        // 验证主表插入
        verify(receiptMapper, times(1)).insert(any(Receipt.class));
        
        // 验证主表数据正确性
        ArgumentCaptor<Receipt> receiptCaptor = ArgumentCaptor.forClass(Receipt.class);
        verify(receiptMapper).insert(receiptCaptor.capture());
        Receipt capturedReceipt = receiptCaptor.getValue();
        assertEquals("customer1", capturedReceipt.getCustomer());
        assertEquals("REC001", capturedReceipt.getNumber());
        assertEquals(new BigDecimal("1000.00"), capturedReceipt.getTotal());
        assertEquals("user1", capturedReceipt.getUser());
        assertEquals(0, capturedReceipt.getExamine()); // 未审核
        assertEquals(0, capturedReceipt.getNucleus()); // 未核销

        // 验证明细表插入
        verify(receiptDetailMapper, times(2)).insert(any(ReceiptDetail.class));
        
        // 验证明细表数据正确性
        ArgumentCaptor<ReceiptDetail> detailCaptor = ArgumentCaptor.forClass(ReceiptDetail.class);
        verify(receiptDetailMapper, times(2)).insert(detailCaptor.capture());
        List<ReceiptDetail> capturedDetails = detailCaptor.getAllValues();
        assertEquals(2, capturedDetails.size());
        assertEquals("account1", capturedDetails.get(0).getAccount());
        assertEquals(new BigDecimal("500.00"), capturedDetails.get(0).getMoney());
        assertEquals("account2", capturedDetails.get(1).getAccount());
        assertEquals(new BigDecimal("500.00"), capturedDetails.get(1).getMoney());
    }
}