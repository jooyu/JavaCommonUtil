package org.yujoo.commom.util;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author 
 */
public class ShareCodeUtil {

    /** 鑷畾涔夎繘鍒�(0,1娌℃湁鍔犲叆,瀹规槗涓巓,l娣锋穯) */
    private static final char[] r=new char[]{'F', 'L', 'G', 'W', '5', 'X', 'C', '3', '9', 'Z', 'M', '6', '7', 'Y', 'R', 'T', '2', 'H', 'S', '8', 'D', 'V', 'E', 'J', '4', 'K', 'Q', 'P', 'U', 'A', 'N', 'B'};

    /** 杩涘埗闀垮害 */
    private static final int binLen=r.length;
    
    private final static Logger logger = (Logger) LoggerFactory.getLogger(ShareCodeUtil.class);
    
//    private static final long startNumber = 1048576L;
    private static final long startNumber = 0L;


    /**
     * 
     * @param id ID
     * @return 闅忔満鐮�
     */
    public static String idToCode(long id,long costomStartNumber) {
    	if(costomStartNumber<0){
    		costomStartNumber = startNumber;
    	}
    	id += costomStartNumber;
        char[] buf=new char[32];
        int charPos=32;

        while((id / binLen) > 0) {
            int ind=(int)(id % binLen);
            // System.out.println(num + "-->" + ind);
            buf[--charPos]=r[ind];
            id /= binLen;
        }
        buf[--charPos]=r[(int)(id % binLen)];
        // System.out.println(num + "-->" + num % binLen);
        String str=new String(buf, charPos, (32 - charPos));
        return str.toUpperCase();
    }
    public static String idToCode(long idL){
    	return idToCode(idL,-1L);
    }
    public static String idToCode(String id){
    	long idL = Long.parseLong(id);
    	return idToCode(idL,-1L);
    }
    public static String idToCode(String id,long costomStartNumber){
    	long idL = Long.parseLong(id);
    	return idToCode(idL,costomStartNumber);
    }
    

    public static long codeToId(String code) {
    	code = code.toUpperCase();
        char chs[]=code.toCharArray();
        long res=0L;
        for(int i=0; i < chs.length; i++) {
            int ind=0;
            for(int j=0; j < binLen; j++) {
                if(chs[i] == r[j]) {
                    ind=j;
                    break;
                }
            }
            if(i > 0) {
                res=res * binLen + ind;
            } else {
                res=ind;
            }
//             logger.debug(ind + "-->" + res);
             
        }
        res -= startNumber;
        return res;
    }

}