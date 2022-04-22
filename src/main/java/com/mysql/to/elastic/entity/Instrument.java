package com.mysql.to.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: cactusli
 * @date: 2022.03.29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Instrument {
    private Integer id;
    private String cname;
    private String ename;
    private String subjects;
    private String insName;
    private String instrBelongsType;
    private String instrBelongsId;
    private String instrBelongsName;
    private String instrCategory;
    private String insMainType;
    private String insMiddleType;
    private String insSubType;
    private Double worth;
    private String nation;
    private String manuFacturer;
    private String beginDate;
    private String instrVersion;
    private String technical;
    private String functioni;
    private Double runMachine;
    private String serviceContent;
    private String achieveMent;
    private String image;
    private String requireMent;
    private String fee;
    private String serviceUrl;
    private String province;
    private String city;
    private String country;
    private String location;
    private String postalCode;
    private String contact;
    private String phone;
    private String email;
    private String address;
    private String lat;
    private String lng;
    private String instruType;
    private String instrSource;
}
