package com.inspur.cmis.action;

import com.inspur.cmis.entity.CicustbasinfoEntity;
import com.inspur.cmis.entity.GccontractmainEntity;
import com.inspur.cmis.entity.GroupEntity;
import com.inspur.cmis.service.CiCustBaseService;
import com.inspur.cmis.service.GcContractService;
import com.inspur.cmis.service.GroupService;
import com.inspur.common.action.BaseAction;
import com.inspur.common.entity.JsonResult;
import com.inspur.common.entity.PaginationBean;
import com.inspur.common.util.GsonUtils;
import com.inspur.common.util.HQLHelper;
import com.inspur.common.util.IsNullUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by LiuLiHao on 2018年8月27日15:48:23
 * 描述： 合同信息
 * 作者： LiuLiHao
 */
public class GcContractAction extends BaseAction {
    private static final long serialVersionUID = 711362308004342341L;

    @Autowired
    private GcContractService gcContractService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private CiCustBaseService ciCustBaseService;
    /**
     * 列表
     * @return
     */
    public String contractInfo(){
        //分页查询
        HQLHelper hqlHelper = new HQLHelper(GccontractmainEntity.class);
        int page = 1;
        if (currentPage!=null && currentPage>0){
            page = currentPage;
        }
        //条件查询
        if (entity!=null && IsNullUtils.isNotNull(entity.getCustid())){
            hqlHelper.addWhere(" o.custid = ?",entity.getCustid());
            request.setAttribute("custId",entity.getCustid());
        }
        if (entity!=null && IsNullUtils.isNotNull(entity.getCustname())){
            hqlHelper.addWhere(" o.custid = ?",entity.getCustname());
            request.setAttribute("custName",entity.getCustname());
        }
        //设置页数
        PaginationBean pageBean = gcContractService.getPageBean(hqlHelper, page);
        request.setAttribute("pageBean",pageBean);

        return "contractInfo";
    }

    /**
     * 添加页面
     * @return
     */
    public String contractAddHtml(){
        //查询所有机构
        List<GroupEntity> list = groupService.findAllUseable();
        request.setAttribute("groups",list);

        List<CicustbasinfoEntity> infos = ciCustBaseService.findAll();
        //客户信息
        request.setAttribute("infos",infos);

        return "contractAddHtml";
    }

    /**
     * 添加
     * @return
     */
    public String contractAdd(){
        JsonResult jsonResult = new JsonResult(0,"添加失败");

        Integer custid = Integer.valueOf(entity.getCustid());
        CicustbasinfoEntity ciCustBase = ciCustBaseService.findObjectById(custid);
        if (ciCustBase!=null){
            //设置客户姓名
            entity.setCustname(ciCustBase.getCname());
        }

        gcContractService.add(entity);
        jsonResult = new JsonResult(1,"添加成功");
        result = GsonUtils.toJson(jsonResult);

        return "contractAdd";
    }



    //页数
    private Integer currentPage;
    ///////////ajax返回数据使用/////////////
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    private GccontractmainEntity entity;

    public GccontractmainEntity getEntity() {
        return entity;
    }

    public void setEntity(GccontractmainEntity entity) {
        this.entity = entity;
    }
}
