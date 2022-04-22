package com.mysql.to.elastic.controller;

import com.mysql.to.elastic.entity.Instrument;
import com.mysql.to.elastic.service.InstrumentService;
import com.mysql.to.elastic.utils.PhysicsPageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author: cactusli
 * @date: 2022.03.29
 */
@RestController
public class InstrumentController {

    @Autowired
    private InstrumentService instrumentService;

    /**
     * 返回数据列表
     * @return
     * @throws IOException
     */
    @GetMapping("/getInstrumentList")
    public List<Instrument> getInstrumentList() throws IOException {
        return instrumentService.selectListInstrument();
    }

    /**
     * 返回成功 - 失败 标识
     * @return
     * @throws IOException
     */
    @GetMapping("/getSuccess")
    public String getSuccess() throws IOException {
        return instrumentService.getSuccess();
    }

    /**
     * 返回分页数据
     * @param physicsPageUtils
     * @return
     */
    @GetMapping("/getPagQuery")
    public PhysicsPageUtils getPagQuery(PhysicsPageUtils physicsPageUtils) {
        PhysicsPageUtils p  = instrumentService.selectByPage(physicsPageUtils);
        return p;
    }
}
