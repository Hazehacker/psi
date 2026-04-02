package com.zeroone.star.moneytransfer.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAllotExportImportService {
     byte[] exportAllotDetail(List<String> ids) throws Exception;
     boolean importAllot(MultipartFile file) throws Exception;
     byte[] exportAllot(List<String> ids) throws Exception;
}