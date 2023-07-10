package cdu.zch.system.listener.easyexcel;

import com.alibaba.excel.event.AnalysisEventListener;

/**
 * @author Zch
 * @date 2023/7/10
 **/
public abstract class MyAnalysisEventListener<T> extends AnalysisEventListener<T> {

    private String msg;

    public abstract String getMsg();

}
