package cdu.zch.system.common.util;

import cdu.zch.system.listener.easyexcel.MyAnalysisEventListener;
import com.alibaba.excel.EasyExcel;

import java.io.InputStream;

/**
 * Excel 工具类
 * @author Zch
 * @date 2023/7/10
 **/
public class ExcelUtils {

    public static <T> String importExcel(InputStream inputStream, Class clazz, MyAnalysisEventListener<T> listener) {
        EasyExcel.read(inputStream, clazz, listener).sheet().doRead();
        return listener.getMsg();
    }

}
