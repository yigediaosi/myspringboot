package com.net.yoga.util;

import com.net.yoga.common.constant.CommonConstant;
import com.net.yoga.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.*;

@Slf4j
public final class CommonUtil {
    private static final char[] bcdLookup = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    public static <T> Map<String, T> asMap(Object... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("args.length = " + args.length);
        }

        Map<String, T> map = new HashMap<String, T>();
        for (int i = 0; i < args.length - 1; i += 2) {
            map.put(String.valueOf(args[i]), (T) args[i + 1]);
        }
        return map;
    }

    public static String mergeUnionKey(Object... args) {
        if (args.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Object arg : args) {
            sb.append(arg).append(CommonConstant.COMMON_VERTICAL_STR);
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

    /**
     * 计算排列组合的值
     *
     * @param total
     * @param select
     * @return
     */
    public static int combine(int total, int select) {
        if (select > total) {
            return 0;
        } else if (select == total) {
            return 1;
        } else if (total == 0) {
            return 1;
        } else {
            if (select > total / 2) {
                select = total - select;
            }

            long result = 1;
            for (int i = total; i > total - select; i--) {
                result *= i;
                if (result < 0) {
                    return -1;
                }
            }
            for (int j = select; j > 0; j--) {
                result /= j;
            }
            if (result > Integer.MAX_VALUE) {
                return -1;
            }
            return (int) result;
        }
    }

    /**
     * 从n个对象中选择m个的所有排列
     *
     * @param a
     * @param m
     * @return
     */
    public static List<String[]> combine(String[] a, int m) {
        List<String[]> result = new ArrayList<String[]>();
        int n = a.length;
        int[] bs = new int[n];
        if (m > n) {
            throw new RuntimeException("Can not get " + n + " elements from " + m + " elements!");
        } else if (m == n) {
            result.add(a);
            return result;
        }

        for (int i = 0; i < n; i++) {
            bs[i] = 0;
        }
        //初始化
        for (int i = 0; i < m; i++) {
            bs[i] = 1;
        }
        boolean flag = true;
        boolean tempFlag = false;
        int pos = 0;
        int sum = 0;
        //首先找到第一个10组合，然后变成01，同时将左边所有的1移动到数组的最左边
        do {
            sum = 0;
            pos = 0;
            tempFlag = true;
            result.add(getElement(bs, a, m));

            for (int i = 0; i < n - 1; i++) {
                if (bs[i] == 1 && bs[i + 1] == 0) {
                    bs[i] = 0;
                    bs[i + 1] = 1;
                    pos = i;
                    break;
                }
            }
            //将左边的1全部移动到数组的最左边

            for (int i = 0; i < pos; i++) {
                if (bs[i] == 1) {
                    sum++;
                }
            }
            for (int i = 0; i < pos; i++) {
                if (i < sum) {
                    bs[i] = 1;
                } else {
                    bs[i] = 0;
                }
            }

            //检查是否所有的1都移动到了最右边
            for (int i = n - m; i < n; i++) {
                if (bs[i] == 0) {
                    tempFlag = false;
                    break;
                }
            }
            if (tempFlag == false) {
                flag = true;
            } else {
                flag = false;
            }

        }
        while (flag);
        result.add(getElement(bs, a, m));

        return result;
    }

    private static String[] getElement(int[] bs, String[] a, int m) {
        String[] result = new String[m];
        int pos = 0;
        for (int i = 0; i < bs.length; i++) {
            if (bs[i] == 1) {
                result[pos] = a[i];
                pos++;
            }
        }
        return result;
    }

    /**
     * 对序列号进行初始化，如果>8位则不管，如果<8位，则左补0
     *
     * @param numberToFormat
     * @return
     */
    public static String formatSequence(long numberToFormat) {
        DecimalFormat format = new DecimalFormat("00000000");
        return format.format(numberToFormat);
    }

    public static String formatSubSequence(long numberToFormat) {
        DecimalFormat format = new DecimalFormat("000000000");
        return format.format(numberToFormat);
    }

    /*
     * 将16进制字符串转换为字符数组
     */
    public static final byte[] hexStrToBytes(String s) {
        byte[] bytes;

        bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }

    /*
     * 将字符数组转换为16进制字符串
     */
    public static final String bytesToHexStr(byte[] bcd) {
        StringBuffer s = new StringBuffer(bcd.length * 2);

        for (int i = 0; i < bcd.length; i++) {
            s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
            s.append(bcdLookup[bcd[i] & 0x0f]);
        }

        return s.toString();
    }


    public static String initBeanName(String gameEn) {
        if (StringUtils.isBlank(gameEn)) {
            throw new BusinessException("initBeanName error. gameEn:" + gameEn);
        }
        char chars[] = gameEn.toCharArray();
        boolean needChange = Boolean.FALSE;
        StringBuffer beanName = new StringBuffer();
        for (char ch : chars) {
            String chStr = String.valueOf(ch);
            if (CommonConstant.COMMON_SPLIT_STR.equals(chStr)) {
                needChange = Boolean.TRUE;
                continue;
            }
            if (needChange) {
                needChange = Boolean.FALSE;
                chStr = chStr.toUpperCase();
            }
            beanName.append(chStr);
        }
        return beanName.toString();
    }

    /**
     * 将a和b中的元素组合到一个数组中，并排序
     *
     * @param a
     * @param b
     * @return
     */
    public static String[] mergeArray(String[] a, String[] b) {
        int aLength = a.length;
        int bLength = b.length;
        String[] c = new String[aLength + bLength];
        System.arraycopy(a, 0, c, 0, aLength);
        System.arraycopy(b, 0, c, aLength, bLength);
        Arrays.sort(c);
        return c;
    }

    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        List<Integer> b = a;
        System.out.print(a);
        System.out.print(b);
        a.set(0, 100);
        System.out.print(a);
        System.out.print(b);
    }

}
