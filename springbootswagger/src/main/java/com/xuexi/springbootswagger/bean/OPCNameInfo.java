package com.xuexi.springbootswagger.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName User
 * @create 2020-07-24 17:39
 * @Des TODO
 */
@Data
@ApiModel(description = "点表维度信息")
public class OPCNameInfo {
    @ApiModelProperty(value = "OPC点表名称")
    private String opcname;
    @ApiModelProperty(value = "系统名称")
    private String systemname;
    @ApiModelProperty(value = "分类名称")
    private String categoryname;
    @ApiModelProperty(value = "设备名称")
    private String equipmentname;
    @ApiModelProperty(value = "opc点表值的数据类型")
    private String opctype;
    @ApiModelProperty(value = "点表功能描述")
    private String description;
    @ApiModelProperty(value = "是否为有效点表")
    private Boolean isvalid;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "值单位")
    private String opcunit;
    @ApiModelProperty(value = "点表创建时间(yyyy-MM-dd)")
    private String createtime;
    @ApiModelProperty(value = "点表失效时间(yyyy-MM-dd)")
    private String deletetime;

    public String getOpcname() {
        return opcname;
    }

    public void setOpcname(String opcname) {
        this.opcname = opcname;
    }

    public String getSystemname() {
        return systemname;
    }

    public void setSystemname(String systemname) {
        this.systemname = systemname;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getEquipmentname() {
        return equipmentname;
    }

    public void setEquipmentname(String equipmentname) {
        this.equipmentname = equipmentname;
    }

    public String getOpctype() {
        return opctype;
    }

    public void setOpctype(String opctype) {
        this.opctype = opctype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Boolean isvalid) {
        this.isvalid = isvalid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOpcunit() {
        return opcunit;
    }

    public void setOpcunit(String opcunit) {
        this.opcunit = opcunit;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getDeletetime() {
        return deletetime;
    }

    public void setDeletetime(String deletetime) {
        this.deletetime = deletetime;
    }
}
