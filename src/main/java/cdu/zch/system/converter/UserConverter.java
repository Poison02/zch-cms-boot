package cdu.zch.system.converter;

import cdu.zch.system.model.bo.UserBO;
import cdu.zch.system.model.bo.UserFormBO;
import cdu.zch.system.model.entity.SysUser;
import cdu.zch.system.model.form.UserForm;
import cdu.zch.system.model.vo.UserImportVO;
import cdu.zch.system.model.vo.UserInfoVO;
import cdu.zch.system.model.vo.UserPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
/**
 * 用户对象转换器
 * @author Zch
 * @date 2023/7/10
 **/
@Mapper(componentModel = "spring")
public interface UserConverter {

    @Mappings({
            @Mapping(target = "genderLabel", expression = "java(cdu.zch.system.common.base.IBaseEnum.getLabelByValue(bo.getGender(), cdu.zch.system.common.enums.GenderEnum.class))")
    })
    UserPageVO bo2Vo(UserBO bo);

    Page<UserPageVO> bo2Vo(Page<UserBO> bo);

    UserForm bo2Form(UserFormBO bo);

    UserForm entity2Form(SysUser entity);

    @InheritInverseConfiguration(name = "entity2Form")
    SysUser form2Entity(UserForm entity);

    @Mappings({
            @Mapping(target = "userId", source = "id")
    })
    UserInfoVO entity2UserInfoVo(SysUser entity);

    SysUser importVo2Entity(UserImportVO vo);

}
