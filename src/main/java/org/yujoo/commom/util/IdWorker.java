package org.yujoo.commom.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tweeter鐨剆nowflake 绉绘鍒癑ava:
 *   (a) id鏋勬垚: 42浣嶇殑鏃堕棿鍓嶇紑 + 10浣嶇殑鑺傜偣鏍囪瘑 + 12浣嶇殑sequence閬垮厤骞跺彂鐨勬暟瀛�(12浣嶄笉澶熺敤鏃跺己鍒跺緱鍒版柊鐨勬椂闂村墠缂�)
 *       娉ㄦ剰杩欓噷杩涜浜嗗皬鏀瑰姩: snowkflake鏄�5浣嶇殑datacenter鍔�5浣嶇殑鏈哄櫒id; 杩欓噷鍙樻垚浣跨敤10浣嶇殑鏈哄櫒id
 *   (b) 瀵圭郴缁熸椂闂寸殑渚濊禆鎬ч潪甯稿己锛岄渶鍏抽棴ntp鐨勬椂闂村悓姝ュ姛鑳姐�傚綋妫�娴嬪埌ntp鏃堕棿璋冩暣鍚庯紝灏嗕細鎷掔粷鍒嗛厤id
 */

public class IdWorker {

    private final static Logger logger = LoggerFactory.getLogger(IdWorker.class);

    private final long workerId;
    private final long epoch = 1403854494756L;   // 鏃堕棿璧峰鏍囪鐐癸紝浣滀负鍩哄噯锛屼竴鑸彇绯荤粺鐨勬渶杩戞椂闂�
    private final long workerIdBits = 10L;      // 鏈哄櫒鏍囪瘑浣嶆暟
    private final long maxWorkerId = -1L ^ -1L << this.workerIdBits;// 鏈哄櫒ID鏈�澶у��: 1023
    private long sequence = 0L;                   // 0锛屽苟鍙戞帶鍒�
    private final long sequenceBits = 12L;      //姣鍐呰嚜澧炰綅

    private final long workerIdShift = this.sequenceBits;                             // 12
    private final long timestampLeftShift = this.sequenceBits + this.workerIdBits;// 22
    private final long sequenceMask = -1L ^ -1L << this.sequenceBits;                 // 4095,111111111111,12浣�
    private long lastTimestamp = -1L;

    private IdWorker(long workerId) {
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() throws Exception {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) { // 濡傛灉涓婁竴涓猼imestamp涓庢柊浜х敓鐨勭浉绛夛紝鍒檚equence鍔犱竴(0-4095寰幆); 瀵规柊鐨則imestamp锛宻equence浠�0寮�濮�
            this.sequence = this.sequence + 1 & this.sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);// 閲嶆柊鐢熸垚timestamp
            }
        } else {
            this.sequence = 0;
        }

        if (timestamp < this.lastTimestamp) {
            logger.error(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp - timestamp)));
            throw new Exception(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp - timestamp)));
        }

        this.lastTimestamp = timestamp;
        return timestamp - this.epoch << this.timestampLeftShift | this.workerId << this.workerIdShift | this.sequence;
    }

    private static IdWorker flowIdWorker = new IdWorker(1);
    public static IdWorker getFlowIdWorkerInstance() {
        return flowIdWorker;
    }



    /**
     * 绛夊緟涓嬩竴涓绉掔殑鍒版潵, 淇濊瘉杩斿洖鐨勬绉掓暟鍦ㄥ弬鏁發astTimestamp涔嬪悗
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    /**
     * 鑾峰緱绯荤粺褰撳墠姣鏁�
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(timeGen());

        IdWorker idWorker = IdWorker.getFlowIdWorkerInstance();
        // System.out.println(Long.toBinaryString(idWorker.nextId()));
        System.out.println(idWorker.nextId());
        System.out.println(idWorker.nextId());
    }

}