package com.biz.model.Pmodel.basic;

/**
 * Created by lzq on 2017/5/9.
 */
public class PpersonStatistics {
    private String id                   = "";           //业务员id
    private String brandCode            = "";           //商户编码
    private String personName           = "";
    private Integer giveCount           = 0;
    private Double giveTotal            = 0.00;
    private Integer getCount            = 0;
    private Double getTotal             = 0.00;
    private Double giveGetScale         = 0.00;
    private Integer memberCount         = 0;            //会员量
    private Double serviceTotal         = 0.00;
    private Double serviceRate          = 0.00;
    private Integer posCount            = 0;
    private Integer brandCount          = 0;
    private Double coinPayRate          = 0.00;         //久零贝核销率
    private String brandName            = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(Integer giveCount) {
        this.giveCount = giveCount;
    }

    public Double getGiveTotal() {
        return giveTotal;
    }

    public void setGiveTotal(Double giveTotal) {
        this.giveTotal = giveTotal;
    }

    public Integer getGetCount() {
        return getCount;
    }

    public void setGetCount(Integer getCount) {
        this.getCount = getCount;
    }

    public Double getGetTotal() {
        return getTotal;
    }

    public void setGetTotal(Double getTotal) {
        this.getTotal = getTotal;
    }

    public Double getGiveGetScale() {
        return giveGetScale;
    }

    public void setGiveGetScale(Double giveGetScale) {
        this.giveGetScale = giveGetScale;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Double getServiceTotal() {
        return serviceTotal;
    }

    public void setServiceTotal(Double serviceTotal) {
        this.serviceTotal = serviceTotal;
    }

    public Integer getPosCount() {
        return posCount;
    }

    public void setPosCount(Integer posCount) {
        this.posCount = posCount;
    }

    public Integer getBrandCount() {
        return brandCount;
    }

    public void setBrandCount(Integer brandCount) {
        this.brandCount = brandCount;
    }

    public Double getCoinPayRate() {
        return coinPayRate;
    }

    public void setCoinPayRate(Double coinPayRate) {
        this.coinPayRate = coinPayRate;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Double getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(Double serviceRate) {
        this.serviceRate = serviceRate;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
}
