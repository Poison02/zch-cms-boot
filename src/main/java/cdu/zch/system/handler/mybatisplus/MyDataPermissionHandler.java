package cdu.zch.system.handler.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Slf4j
public class MyDataPermissionHandler implements DataPermissionHandler {
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        return null;
    }
}
