package com.itheima.mobilesafe.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.itheima.mobilesafe.db.dao.NumberAddressDao;
import com.itheima.mobilesafe.utils.objects.MobileQuery;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Catherine on 2016/9/1.
 * Soft-World Inc.
 * catherine919@soft-world.com.tw
 */
public class TelephoneUtils {
    private HttpUtils.Callback callback;
    private static String address = "查无此号";

    @Nullable
    public static String getAddressFromNum(String number) {

        switch (number.length()) {
            case 3:
                // 110
                address = "匪警号码";
                break;
            case 4:
                // 5554
                address = "模拟器";
                break;
            case 5:
                // 10086
                address = "客服号码";
                break;
            case 7:
                //
                address = "本地号码";
                break;

            case 8:
                address = "本地号码";
                break;
            case 11:
                //使用正则表达式过滤错误号码
                //https://msdn.microsoft.com/zh-cn/library/ae5bf541(v=vs.100).aspx
                /**
                 * ^ 开头
                 * 1 第一位限定1
                 * [3456] 第二位是3、4、5、6任一都行
                 * [0-9] 效果等同于 \d，适用于之后的九位数字，所以是 \d\d\d\d\d\d\d\d\d 等同于 \d{9}
                 * $ 结尾
                 *
                 * 正则式为 ^1[3456]\d{9}$
                 */
                //符合规则
                if (number.matches("^1[3456]\\d{9}$")) {
                    //手机号码
                    NumberAddressDao nad = new NumberAddressDao();
                    address = nad.queryNumber(number);
                    if (TextUtils.isEmpty(address)) {
//                        HttpTools.sendDataByGet(Settings.taoBaoGetAddressUrl,new String[]{"tel"},new String[]{number},myHandler);
                        HttpUtils.get(Settings.tenpayUrl, new String[]{"chgmobile"}, new String[]{number}, new HttpUtils.Callback() {
                            @Override
                            public void onResponse(String response) {
                                if (!TextUtils.isEmpty(response)) {
                                    try {
                                        InputStream stream = new ByteArrayInputStream(response.getBytes("GBK"));
                                        XMLPullParserHandler xmlParser = new XMLPullParserHandler();
                                        MobileQuery mobileQuery = xmlParser.parse(stream);
                                        if (mobileQuery.getRetmsg().equals("OK")) {
                                            address = mobileQuery.getCity() + mobileQuery.getSupplier();
                                        } else
                                            address = "查无此号";
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                        address = "查无此号";
                                    }
                                } else
                                    address = "查无此号";
                            }
                        });
                    }
                }
                break;
            default:
                address = "查无此号";
                break;
        }
        return address;
    }


}
