package com.meiyuan.catering.core.util;

/**
 * @Author MeiTao
 * @Date 2020/7/16 0016 9:04
 * @Description 简单描述 : 根据汉字获取首字母
 * @Since version-1.2.0
 */
public class HypyUtil {
    /**
     * 简体中文的编码范围从B0A1（45217）一直到F7FE（63486）
     */
    private static int BEGIN = 45217;
    private static int END = 63486;
    /** 字母总数*/
    private static int LETTER_NUMBER = 26;
    /**
     * 按照声母表示，这个表是在GB2312中的出现的第一个汉字，也就是说“啊”是代表首字母a的第一个汉字
     * i, u, v都不做声母, 自定规则跟随前面的字母
     */
    private static char[] chartable = { '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈',
            '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌',
            '塌', '挖', '昔', '压', '匝', };

    /**
     * 二十六个字母区间对应二十七个端点，GB2312码汉字区间十进制表示
     */
    private static int[] table = new int[27];

    /**
     * 对应首字母区间表
     */
    private static char[] initialtable = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            't', 't', 'w', 'x', 'y', 'z', };

    // 初始化
    static {
        for (int i = 0; i < LETTER_NUMBER; i++) {
            // 得到GB2312码的首字母区间端点表，十进制。
            table[i] = gbValue(chartable[i]);
        }
        // 区间表结尾
        table[26] = END;
    }

    // ------------------------public方法区------------------------
    /**
     * 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串 最重要的一个方法，思路如下：一个个字符读入、判断、输出
     */
    public static String cn2py(String sourceStr) {
        String result = "";
        int strLength = sourceStr.length();
        int i;
        try {
            for (i = 0; i < strLength; i++) {
                result += char2Initial(sourceStr.charAt(i));
            }
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    // ------------------------private方法区------------------------
    /**
     * 输入字符,得到他的声母,英文字母返回对应的大写字母,其他非简体汉字返回 '0'
     *
     */
    private static char char2Initial(char ch) {
        char a = 'a',z = 'z';
        char bigA = 'A',bigZ = 'Z';
        // 对英文字母的处理：小写字母转换为大写，大写的直接返回
        if (ch >= a && ch <= z){
            return (char) (ch - a + bigA);
        }
        if (ch >= bigA && ch <= bigZ){
            return ch;
        }


        /**
         * 汉字转换首字母
         * 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内
         * 若不是，则直接返回，若是，则在码表内的进行判断
         */
        int gb = gbValue(ch);

        // 在码表区间之前，直接返回
        if ((gb < BEGIN) || (gb > END)){
            return ch;
        }

        int i;
        // 判断匹配码表区间，匹配到就break,判断区间形如“[,)”
        for (i = 0; i < LETTER_NUMBER; i++) {
                if ((gb >= table[i]) && (gb < table[i+1])){
                    break;
                }
        }
        //补上GB2312区间最右端
        if (gb==END) {
            i=25;
        }
        // 在码表区间中，返回首字母
        return initialtable[i];
    }

    /**
     * 取出汉字的编码 cn 汉字
     */
    private static int gbValue(char ch) {// 将一个汉字（GB2312）转换为十进制表示。
        // 汉子位数
        int digit = 2;
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < digit){return 0;}
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }
}