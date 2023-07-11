package cdu.zch.system.mapper;

import cdu.zch.system.model.entity.SysDictType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Mapper
@Repository
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {
}
