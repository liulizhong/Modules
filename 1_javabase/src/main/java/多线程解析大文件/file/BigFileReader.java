package 多线程解析大文件.file;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by renhongqiang on 2019-07-02 14:09
 */
public class BigFileReader {
    private int threadPoolSize;                  //线程池数
    private Charset charset;                      //Char集合Set
    private int bufferSize;                      //缓冲大小Int
    private IFileHandle handle;                   // handle(String line)接口
    private ExecutorService executorService;     // 线程池
    private long fileLength;                      //文件 字节大小
    private RandomAccessFile rAccessFile;         //一款功能较丰富的文本编辑工具，升级版的File
    private Set<StartEndPair> startEndPairs;      //StartEndPair集合Set
    private CyclicBarrier cyclicBarrier;          //线程餐厅，作用就是会让所有线程都等待完成后才会继续下一步行动。
    private AtomicLong counter = new AtomicLong(0);    //原子类，作用是对长整形进行原子操作。因为：32位操作系统中，64位的Long和Double会被当做两个分离的32位进行操作，不具原子性。

    /**
     * 构造器，初始化所有属性，除了cyclicBarrier
     *
     * @param file
     * @param handle
     * @param charset
     * @param bufferSize
     * @param threadPoolSize
     */
    public BigFileReader(File file, IFileHandle handle, Charset charset, int bufferSize, int threadPoolSize) {
        this.fileLength = file.length();
        this.handle = handle;
        this.charset = charset;
        this.bufferSize = bufferSize;
        this.threadPoolSize = threadPoolSize;
        try {
            this.rAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        startEndPairs = new HashSet<StartEndPair>();
    }

    /**
     * 主函数运行
     */
    public void start() {
        long everySize = this.fileLength / this.threadPoolSize;
        try {
            calculateStartEnd(0, everySize);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final long startTime = System.currentTimeMillis();
        cyclicBarrier = new CyclicBarrier(startEndPairs.size(), new Runnable() {
            public void run() {
                System.out.println("use time: " + (System.currentTimeMillis() - startTime));
                System.out.println("all line: " + counter.get());
                BigFileReader.this.shutdown();
            }
        });
        for (StartEndPair pair : startEndPairs) {
            System.out.println("分配分片：" + pair);
            this.executorService.execute(new SliceReaderTask(pair));
        }
    }

    /**
     * 此方法将添加startEndPairs元素，从0开始，每个线程分的文件大小大致相等【有处理整行的逻辑】
     *
     * @param start
     * @param size
     * @throws IOException
     */
    private void calculateStartEnd(long start, long size) throws IOException {
        if (start > fileLength - 1) {
            return;
        }
        StartEndPair pair = new StartEndPair();
        pair.start = start;
        long endPosition = start + size - 1;
        if (endPosition >= fileLength - 1) {
            pair.end = fileLength - 1;
            startEndPairs.add(pair);
            return;
        }

        rAccessFile.seek(endPosition);
        byte tmp = (byte) rAccessFile.read();
        while (tmp != '\n' && tmp != '\r') {
            endPosition++;
            if (endPosition >= fileLength - 1) {
                endPosition = fileLength - 1;
                break;
            }
            rAccessFile.seek(endPosition);
            tmp = (byte) rAccessFile.read();
        }
        pair.end = endPosition;
        startEndPairs.add(pair);

        calculateStartEnd(endPosition + 1, size);

    }

    /**
     * 关闭rAccessFile和executorService
     */
    public void shutdown() {
        try {
            this.rAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.executorService.shutdown();
    }

    /**
     * 调用一次便counter递增1，并运行一次this.handle.handle(line)改变handle的值;
     *
     * @param bytes
     * @throws UnsupportedEncodingException
     */
    private void handle(byte[] bytes) throws UnsupportedEncodingException {
        String line = null;
        if (this.charset == null) {
            line = new String(bytes);
        } else {
            line = new String(bytes, charset);
        }
        if (line != null && !"".equals(line)) {
            this.handle.handle(line);
            counter.incrementAndGet();
        }
    }

    /**
     * 内部Bean类[start、end]
     */
    private static class StartEndPair {
        public long start;
        public long end;

        @Override
        public String toString() {
            return "star=" + start + ";end=" + end;
        }
    }

    /**
     * 内部线程Runnable类
     */
    private class SliceReaderTask implements Runnable {
        private long start;
        private long sliceSize;
        private byte[] readBuff;

        public SliceReaderTask(StartEndPair pair) {
            this.start = pair.start;
            this.sliceSize = pair.end - pair.start + 1;
            this.readBuff = new byte[bufferSize];
        }

        @Override
        public void run() {
            try {
                MappedByteBuffer mapBuffer = rAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, start, this.sliceSize);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                for (int offset = 0; offset < sliceSize; offset += bufferSize) {
                    int readLength;
                    if (offset + bufferSize <= sliceSize) {
                        readLength = bufferSize;
                    } else {
                        readLength = (int) (sliceSize - offset);
                    }
                    mapBuffer.get(readBuff, 0, readLength);
                    for (int i = 0; i < readLength; i++) {
                        byte tmp = readBuff[i];
                        //碰到换行符
                        if (tmp == '\n' || tmp == '\r') {
                            handle(bos.toByteArray());
                            bos.reset();
                        } else {
                            bos.write(tmp);
                        }
                    }
                }
                if (bos.size() > 0) {
                    handle(bos.toByteArray());
                }
                cyclicBarrier.await();//测试性能用
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 静态内部类，作用是为创建主类对象
     */
    public static class Builder {
        private int threadSize = 1;
        private Charset charset;
        private int bufferSize = 1024 * 1024;
        private IFileHandle handle;
        private File file;

        // 构造器初始化
        public Builder(String file, IFileHandle handle) {
            this.file = new File(file);
            if (!this.file.exists())
                throw new IllegalArgumentException("文件不存在！");
            this.handle = handle;
        }

        // 线程池数设置【默认1】
        public Builder threadPoolSize(int size) {
            if (size <= 0) {
                throw new IllegalArgumentException("线程池参数必须为大于0的整数");
            }
            this.threadSize = size;
            return this;
        }

        // 初始化charset
        public Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        // 初始化缓冲Buffer大小
        public Builder bufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        //
        public BigFileReader build() {
            return new BigFileReader(this.file, this.handle, this.charset, this.bufferSize, this.threadSize);
        }
    }

}
