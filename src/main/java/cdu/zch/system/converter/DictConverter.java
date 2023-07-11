package cdu.zch.system.converter;

import cdu.zch.system.model.entity.SysDict;
import cdu.zch.system.model.form.DictForm;
import cdu.zch.system.model.vo.DictPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Mapper(componentModel = "spring")
public interface DictConverter {

    Page<DictPageVO> entity2Page(Page<SysDict> page);

    DictForm entity2Form(SysDict entity);

    @InheritInverseConfiguration(name="entity2Form")
    SysDict form2Entity(DictForm entity);
}
