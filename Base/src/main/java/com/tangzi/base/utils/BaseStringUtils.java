package com.tangzi.base.utils;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liubin on 2017/10/11.
 * String Utils
 **/
public class BaseStringUtils {

    private BaseStringUtils() {
        throw new AssertionError();
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p/>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0 || "null".equalsIgnoreCase(str));
    }

    /**
     * is null or its length is 0
     * <p/>
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }


    /**
     * get length of CharSequence
     * <p/>
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     *
     * @param str
     * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     * <p/>
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
    }

    /**
     * capitalize first letter
     * <p/>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     * <p/>
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * <p/>
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     * <li>if href is null, return ""</li>
     * <li>if not match regx, return source</li>
     * <li>return the last string that match regx</li>
     * </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     * <p/>
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return BaseStringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * <p/>
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * <p/>
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * return specific characters which substring from original string
     *
     * @param inputString
     * @param returnCount
     * @return
     */
    public static String subStrAndKeepN(String inputString, int returnCount, boolean appendEllipsis) {
        if (isBlank(inputString)) {
            return inputString;
        }
        if (inputString.length() <= returnCount) {
            return inputString;
        }
        if (appendEllipsis) {
            inputString = inputString.substring(0, returnCount) + "...";
        } else {
            inputString = inputString.substring(0, returnCount);
        }
        return inputString;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    public static String convertPhoneNumber(String phone) {
        if (BaseStringUtils.isBlank(phone)) {
            return phone;
        }
        phone = phone.trim();
        StringBuilder str = new StringBuilder();
        if (phone.length() >= 11) {
            str.append(phone.substring(0, 3));
            str.append("****");
            str.append(phone.substring(phone.length() - 4, phone.length()));
            return str.toString();
        }
        return str.toString();
    }

    public static String join(Object[] elements, CharSequence separator) {
        return join(Arrays.asList(elements), separator);
    }

    public static String join(Iterable<? extends Object> elements, CharSequence separator) {
        StringBuilder builder = new StringBuilder();

        if (elements != null) {
            Iterator<? extends Object> iter = elements.iterator();
            if (iter.hasNext()) {
                builder.append(String.valueOf(iter.next()));
                while (iter.hasNext()) {
                    builder.append(separator).append(String.valueOf(iter.next()));
                }
            }
        }

        return builder.toString();
    }

    public static String fixLastSlash(String str) {
        String res = str == null ? "/" : str.trim() + "/";
        if (res.length() > 2 && res.charAt(res.length() - 2) == '/')
            res = res.substring(0, res.length() - 1);
        return res;
    }

    public static int convertToInt(String str) throws NumberFormatException {
        int s, e;
        for (s = 0; s < str.length(); s++)
            if (Character.isDigit(str.charAt(s)))
                break;
        for (e = str.length(); e > 0; e--)
            if (Character.isDigit(str.charAt(e - 1)))
                break;
        if (e > s) {
            try {
                return Integer.parseInt(str.substring(s, e));
            } catch (NumberFormatException ex) {
                // Log.e("convertToInt", ex);
                throw new NumberFormatException();
            }
        } else {
            throw new NumberFormatException();
        }
    }

    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    public static String stripToEmpty(String str) {
        return str == null ? "" : strip(str, (String) null);
    }

    public static String strip(String str) {
        return strip(str, (String) null);
    }

    public static String strip(String str, String stripChars) {
        if (isEmpty(str)) {
            return str;
        } else {
            str = stripStart(str, stripChars);
            return stripEnd(str, stripChars);
        }
    }

    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            int start = 0;
            if (stripChars == null) {
                while (start != strLen && Character.isWhitespace(str.charAt(start))) {
                    ++start;
                }
            } else {
                if (stripChars.isEmpty()) {
                    return str;
                }

                while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
                    ++start;
                }
            }

            return str.substring(start);
        } else {
            return str;
        }
    }

    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str != null && (end = str.length()) != 0) {
            if (stripChars == null) {
                while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                    --end;
                }
            } else {
                if (stripChars.isEmpty()) {
                    return str;
                }

                while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                    --end;
                }
            }

            return str.substring(0, end);
        } else {
            return str;
        }
    }

    public static boolean isNumeric(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * @param value     显示的字符串
     * @param color     颜色
     * @param isAppendN 是否加空行
     * @param isBold    粗体
     * @return
     */
    public static SpannableStringBuilder generateString(String value, int color, boolean isAppendN, boolean isBold) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        SpannableString ss = new SpannableString(value);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        ss.setSpan(colorSpan, 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if (isBold) {
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
            ss.setSpan(styleSpan, 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        sb.append(ss);
        if (isAppendN) {
            sb.append("\n");
        }
        return sb;
    }

    public static SpannableString createDifferentSizeAmount(String money) {
        if (BaseStringUtils.isBlank(money)) {
            return new SpannableString("");
        }
        SpannableString spannableString = new SpannableString(money);
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(0.76f);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1f);
        RelativeSizeSpan sizeSpan03 = new RelativeSizeSpan(0.7f);

        if (money.length() < 2) {
            return spannableString;
        }
        int index = money.indexOf(".");
        if (index < 0) {
            index = money.length();
        }
        spannableString.setSpan(sizeSpan01, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, 1, index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (index != money.length()) {
            spannableString.setSpan(sizeSpan03, index, money.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
    }
}