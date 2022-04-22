package com.mysql.to.elastic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysql.to.elastic.entity.Instrument;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: cactusli
 * @date: 2022.03.29
 */
public interface InstrumentMapper extends BaseMapper<Instrument> {

    /**
     * 查询需要同步到 elastic search 中的仪器数据
     * @return
     */
    List<Instrument> selectListInstrument();

    /**
     * 返回分页数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Instrument>  selectByPage(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    /**
     * 查询数据的总数量
     * @return
     */
    Long selectCount();
}
