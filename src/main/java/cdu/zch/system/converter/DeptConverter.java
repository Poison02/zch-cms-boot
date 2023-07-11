package cdu.zch.system.converter;

import cdu.zch.system.model.entity.SysDept;
import cdu.zch.system.model.form.DeptForm;
import cdu.zch.system.model.vo.DeptVO;
import org.mapstruct.Mapper;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Mapper(componentModel = "spring")
public interface DeptConverter {

    DeptForm entity2Form(SysDept entity);

    DeptVO entity2Vo(SysDept entity);

    SysDept form2Entity(DeptForm deptForm);

}
