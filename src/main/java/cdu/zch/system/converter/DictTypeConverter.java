package cdu.zch.system.converter;

import cdu.zch.system.model.entity.SysDictType;
import cdu.zch.system.model.form.DictTypeForm;
import cdu.zch.system.model.vo.DictTypePageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Mapper(componentModel = "spring")
public interface DictTypeConverter {

    Page<DictTypePageVO> entity2Page(Page<SysDictType> page);

    DictTypeForm entity2Form(SysDictType entity);

    SysDictType form2Entity(DictTypeForm entity);
}
