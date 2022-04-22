package com.mysql.to.elastic.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.Data;

import java.util.List;

/**
 * 逻辑分页工具包
 * @author: cactusli
 * @date: 2022.03.29
 */
@Data
public class LogicPageUtils<T> {
    private int pageNum;
    private int pageSize;
    private int startRow;
    private int endRow;
    private int pages;
    private List<T> list ;
    public LogicPageUtils(int pageNum, int pageSize, List<T> totalList) {
        this.pageNum = pageNum<=1 ? 1:pageNum;
        this.pageSize = pageSize<=1 ? 1:pageSize;
        if(CollectionUtils.isEmpty(totalList)){
            return ;
        }
        int totalCount = totalList.size();
        if(totalCount<=pageSize){
            this.pages=1;
        }else{
            if(totalCount%pageSize>0){
                this.pages = totalCount/pageSize+1;
            }else{
                this.pages = totalCount/pageSize;
            }
        }
        this.startRow = this.pageNum > 0 ? (this.pageNum - 1) * this.pageSize : 0;
        this.endRow = this.startRow + this.pageSize * (this.pageNum > 0 ? 1 : 0);
        if(endRow>totalCount)   {
            endRow=totalCount;
        }
        list = totalList.subList(this.startRow,this.endRow);
    }
}
