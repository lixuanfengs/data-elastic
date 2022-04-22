package com.mysql.to.elastic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysql.to.elastic.entity.Instrument;
import com.mysql.to.elastic.utils.PhysicsPageUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author: cactusli
 * @date: 2022.03.29
 */
public interface InstrumentService extends IService<Instrument> {

    /* 查询需要同步到 elastic search 中的仪器数据
     * @return
     */
    List<Instrument> selectListInstrument() throws IOException;

    /**
     * 返回成功 - 失败 标识
     * @return
     */
    String getSuccess() throws IOException;

    /**
     * 返回分页数据
     * @param physicsPageUtils
     * @return
     */
    PhysicsPageUtils selectByPage(PhysicsPageUtils physicsPageUtils);
}
