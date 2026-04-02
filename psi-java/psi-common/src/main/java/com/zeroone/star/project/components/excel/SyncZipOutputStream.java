package com.zeroone.star.project.components.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 同步压缩输出流
 * 不同业务场景需要不同的压缩格式，调用方无需关心底层实现，都使用 write() 方法
 * 支持 GZIP 和 ZIP 两种压缩模式，边写边压缩，无需临时文件
 */
public class SyncZipOutputStream extends OutputStream {

    private final OutputStream delegate;
    private final ZipOutputStream zipOut;
    private final GZIPOutputStream gzipOut;
    private boolean isZipEntryOpen = false;

    /**
     * 创建 GZIP 压缩输出流
     */
    public static SyncZipOutputStream gzip(OutputStream out) throws IOException {
        return new SyncZipOutputStream(out, new GZIPOutputStream(out));
    }

    /**
     * 创建 ZIP 压缩输出流
     */
    public static SyncZipOutputStream zip(OutputStream out) {
        return new SyncZipOutputStream(out, new ZipOutputStream(out));
    }

    // ZIP 模式构造函数
    private SyncZipOutputStream(OutputStream out, ZipOutputStream zos) {
        this.delegate = out;
        this.zipOut = zos;
        this.gzipOut = null;
    }

    // GZIP 模式构造函数
    private SyncZipOutputStream(OutputStream out, GZIPOutputStream gzos) {
        this.delegate = out;
        this.gzipOut = gzos;
        this.zipOut = null;
    }

    /**
     * 开始一个新的 ZIP 条目（仅 ZIP 模式需要）
     */
    public void putNextEntry(String entryName) {
        if (zipOut == null) {
            throw new IllegalStateException("putNextEntry is only available in ZIP mode");
        }
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
        if (zipOut != null) {
            zipOut.write(b);
        } else {
            gzipOut.write(b);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (zipOut != null) {
            zipOut.write(b);
        } else {
            gzipOut.write(b);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (zipOut != null) {
            zipOut.write(b, off, len);
        } else {
            gzipOut.write(b, off, len);
        }
    }

    @Override
    public void flush() throws IOException {
        if (zipOut != null) {
            zipOut.flush();
        } else {
            gzipOut.flush();
        }
    }

    @Override
    public void close() throws IOException {
        if (isZipEntryOpen && zipOut != null) {
            zipOut.closeEntry();
            isZipEntryOpen = false;
        }
        if (zipOut != null) {
            zipOut.finish();
        }
        if (gzipOut != null) {
            gzipOut.finish();
        }
        // 不关闭 delegate，由调用方管理
    }
}