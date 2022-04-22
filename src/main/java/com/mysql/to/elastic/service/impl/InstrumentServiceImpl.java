package com.mysql.to.elastic.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.CreateOperation;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.to.elastic.entity.Instrument;
import com.mysql.to.elastic.mapper.InstrumentMapper;
import com.mysql.to.elastic.service.InstrumentService;
import com.mysql.to.elastic.utils.PhysicsPageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: cactusli
 * @date: 2022.03.29
 */
@Slf4j
@Service
public class InstrumentServiceImpl extends ServiceImpl<InstrumentMapper, Instrument> implements InstrumentService {

    @Autowired
    private ElasticsearchClient client;

    @Autowired
    private InstrumentMapper mapper;

    private static final String INSTRUMENT_INDEX = "instrument_index";

    /**
     * 此方法没有使用
     * @return
     * @throws IOException
     */
    @Override
    public List<Instrument> selectListInstrument() throws IOException {
        List<Instrument> instruments = mapper.selectListInstrument();
        List<BulkOperation>  bulkOperations = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(instruments)) {
            for (Instrument instrument : instruments) {
                //创建批量操作的数据
                BulkOperation.Builder builder = new BulkOperation.Builder();
                CreateOperation.Builder<Instrument> instrumentBuilder = new CreateOperation.Builder<>();
                instrumentBuilder.id(String.valueOf(instrument.getId()));
                instrumentBuilder.document(instrument);
                BulkOperation build = builder.create(instrumentBuilder.build()).build();
                bulkOperations.add(build);
            }
        }
        //创建批量操作的 request
        BulkRequest.Builder builderRequest = new BulkRequest.Builder();
        builderRequest.index(INSTRUMENT_INDEX);
        builderRequest.operations(bulkOperations);
        BulkResponse bulk = client.bulk(builderRequest.build());
        //errors = false 没有错误返回
        log.info("批量添加数据是否成功：" +  bulk.errors());
        return instruments;
    }

    @Override
    public String getSuccess() throws IOException {

        List<Instrument> instruments = mapper.selectListInstrument();

        List<BulkOperation>  bulkOperations = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(instruments)) {
            for (Instrument instrument : instruments) {
                //创建批量操作的数据
                BulkOperation.Builder builder = new BulkOperation.Builder();
                CreateOperation.Builder<Instrument> instrumentBuilder = new CreateOperation.Builder<>();
                instrumentBuilder.id(String.valueOf(instrument.getId()));
                instrumentBuilder.document(instrument);
                BulkOperation build = builder.create(instrumentBuilder.build()).build();
                bulkOperations.add(build);
            }
        }
        //创建批量操作的 request
        BulkRequest.Builder builderRequest = new BulkRequest.Builder();
        builderRequest.index(INSTRUMENT_INDEX);
        builderRequest.operations(bulkOperations);
        BulkResponse bulk = client.bulk(builderRequest.build());
        //errors = false 没有错误返回
        log.info("批量添加数据是否成功：" +  bulk.errors());
        if(!bulk.errors()) {
            return "数据提交到 elastic search 成功！";
        }
        return "数据提交到 elastic search 失败！";
    }

    @Override
    public PhysicsPageUtils selectByPage(PhysicsPageUtils physicsPageUtils) {

        List<Instrument> instruments = mapper.selectByPage(physicsPageUtils.getCurrpageNum(), physicsPageUtils.getPageSize());
        Long pageCount = mapper.selectCount();
        PhysicsPageUtils p = new PhysicsPageUtils(pageCount, physicsPageUtils.getCurrpageNum());
        p.setList(instruments);;
        return p;
    }


    /**
     * 更新时判断索引是否存在
     * @param instrumentList
     * @return
     */
    public boolean isDocDoesItExist(List<Instrument> instrumentList) {

        return false;
    }
}
