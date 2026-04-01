package com.zeroone.star.project.components.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 同步压缩输出流
 * 支持 GZIP 和 ZIP 两种压缩模式，边写边压缩，无需临时文件
 */
public class SyncZipOutputStream extends OutputStream {

    private final OutputStream delegate;
    private final ZipOutputStream zipOut;
    private boolean isZipEntryOpen = false;

    /**
     * 创建 GZIP 压缩输出流
     */
    public static SyncZipOutputStream gzip(OutputStream out) throws IOException {
        return new SyncZipOutputStream(out, new GZIPOutputStream(out), false);
    }

    /**
     * 创建 ZIP 压缩输出流
     */
    public static SyncZipOutputStream zip(OutputStream out) {
        ZipOutputStream zos = new ZipOutputStream(out);
        return new SyncZipOutputStream(out, zos, true);
    }

    private SyncZipOutputStream(OutputStream out, OutputStream inner, boolean isZip) {
        this.delegate = out;
        this.zipOut = (ZipOutputStream) inner;
    }

    /**
     * 开始一个新的 ZIP 条目（仅 ZIP 模式需要）
     */
    public void putNextEntry(String entryName) {
        try {
            if (isZipEntryOpen) {
                zipOut.closeEntry();
            }
            zipOut.putNextEntry(new ZipEntry(entryName));
            isZipEntryOpen = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create zip entry: " + entryName, e);
        }
    }

    @Override
    public void write(int b) throws IOException {
        zipOut.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        zipOut.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        zipOut.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        zipOut.flush();
    }

    @Override
    public void close() throws IOException {
        if (isZipEntryOpen) {
            zipOut.closeEntry();
            isZipEntryOpen = false;
        }
        zipOut.finish();
        // 不关闭 delegate，由调用方管理
    }
}