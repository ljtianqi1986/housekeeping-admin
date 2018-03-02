package com.biz.model.Hmodel.basic;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by qziwm on 2017/4/6.
 */
@Entity
@Table(name="wx_wheel"
)
public class TwxWheel implements java.io.Serializable {
    private String id;
    private String name;
    private Integer level=0;
    private Integer num=0;
    private Integer type=0;
    private Double num90=0.0;
    private Double percent=0.0;
    private String imgPath;
    private Date createTime=new Date();
    @GenericGenerator(name="generator", strategy="uuid.hex")@Id
    @GeneratedValue(generator="generator")

    @Column(name="id", unique=true, nullable=false, length=50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name="level")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    @Column(name="num")
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
    @Column(name="type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    @Column(name="num90")
    public Double getNum90() {
        return num90;
    }

    public void setNum90(Double num90) {
        this.num90 = num90;
    }
    @Column(name="percent")
    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
    @Column(name="imgPath")
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    @Column(name="createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
