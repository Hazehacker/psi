package com.zeroone.star.project.components.excel;

import java.io.Serializable;

/**
 * 导出结果封装
 * 包含导出数据和监控指标
 */
public class ExportResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 导出数据行数 */
    private long rowCount;

    /** 导出耗时（毫秒） */
    private long elapsedMs;

    /** 原始文件大小（字节） */
    private long originalSize;

    /** 压缩后文件大小（字节） */
    private long compressedSize;

    /** 峰值内存占用（字节） */
    private long peakMemory;

    /** 是否启用压缩 */
    private boolean compressed;

    public ExportResult() {
    }

    public ExportResult(long rowCount, long elapsedMs) {
        this.rowCount = rowCount;
        this.elapsedMs = elapsedMs;
    }

    // Getters and Setters
    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }

    public long getElapsedMs() {
        return elapsedMs;
    }

    public void setElapsedMs(long elapsedMs) {
        this.elapsedMs = elapsedMs;
    }

    public long getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(long originalSize) {
        this.originalSize = originalSize;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public long getPeakMemory() {
        return peakMemory;
    }

    public void setPeakMemory(long peakMemory) {
        this.peakMemory = peakMemory;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    /**
     * 获取压缩率
     */
    public double getCompressionRatio() {
        if (compressedSize == 0) return 0;
        return 1.0 - (double) compressedSize / originalSize;
    }

    @Override
    public String toString() {
        return String.format(
            "ExportResult{rowCount=%d, elapsedMs=%d, originalSize=%d, compressedSize=%d, compressionRatio=%.2f%%}",
            rowCount, elapsedMs, originalSize, compressedSize, getCompressionRatio() * 100
        );
    }
}