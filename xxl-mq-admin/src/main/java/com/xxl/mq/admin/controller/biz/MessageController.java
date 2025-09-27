package com.xxl.mq.admin.controller.biz;

import com.xxl.mq.admin.constant.enums.ArchiveStrategyEnum;
import com.xxl.mq.admin.util.I18nUtil;
import com.xxl.mq.core.constant.MessageStatusEnum;
import com.xxl.mq.admin.model.dto.MessageDTO;
import com.xxl.mq.admin.model.entity.Application;
import com.xxl.mq.admin.service.ApplicationService;
import com.xxl.mq.admin.service.MessageService;
import com.xxl.sso.core.annotation.XxlSso;
import com.xxl.tool.core.DateTool;
import com.xxl.tool.core.StringTool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import com.xxl.tool.response.Response;
import com.xxl.tool.response.PageModel;

import static com.xxl.mq.admin.controller.biz.MessageArchiveController.findPermissionApplication;

/**
* Message Controller
*
* Created by xuxueli on '2025-03-21 21:54:06'.
*/
@Controller
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;
    @Resource
    private ApplicationService applicationService;

    /**
    * 页面
    */
    @RequestMapping
    @XxlSso
    public String index(Model model, HttpServletRequest request, String topic) {

        // Enum
        model.addAttribute("MessageStatusEnum", MessageStatusEnum.values());
        model.addAttribute("ArchiveStrategyEnum", ArchiveStrategyEnum.values());

        // appname
        List<Application> applicationList = findPermissionApplication(request, applicationService);
        model.addAttribute("applicationList", applicationList);

        // param
        model.addAttribute("topic", topic);

        return "biz/message";
    }

    /**
    * 分页查询
    */
    @RequestMapping("/pageList")
    @ResponseBody
    @XxlSso
    public Response<PageModel<MessageDTO>> pageList(@RequestParam(required = false, defaultValue = "0") int offset,
                                                    @RequestParam(required = false, defaultValue = "10") int pagesize,
                                                    @RequestParam(required = false) String topic,
                                                    @RequestParam(required = false) int status,
                                                    @RequestParam(required = false) String filterTime) {

        // parse param
        Date effectTimeStart = null;
        Date effectTimeEnd = null;
        if (StringTool.isNotBlank(filterTime)) {
            List<String> tempArr = StringTool.split(filterTime, " - ");
            if (tempArr.size() == 2) {
                effectTimeStart = DateTool.parseDateTime(tempArr.get(0));
                effectTimeEnd = DateTool.parseDateTime(tempArr.get(1));
            }
        }

        // valid
        if (StringTool.isBlank(topic)) {
            return Response.ofFail(I18nUtil.getString("system_please_input") +"Topic");
        }

        // page query
        PageModel<MessageDTO> pageModel = messageService.pageList(offset, pagesize, topic, status, effectTimeStart, effectTimeEnd);
        return Response.ofSuccess(pageModel);
    }

    /**
    * Load查询
    */
    /*@RequestMapping("/load")
    @ResponseBody
    @XxlSso
    public Response<Message> load(int id){
        return messageService.load(id);
    }*/

    /**
    * 新增
    */
    @RequestMapping("/insert")
    @ResponseBody
    @XxlSso
    public Response<String> insert(MessageDTO messageDTO){
        return messageService.insert(messageDTO);
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    @ResponseBody
    @XxlSso
    public Response<String> delete(@RequestParam("ids[]") List<Long> ids){
        return messageService.delete(ids);
    }

    /**
    * 更新
    */
    @RequestMapping("/update")
    @ResponseBody
    @XxlSso
    public Response<String> update(MessageDTO messageDTO){
        return messageService.update(messageDTO);
    }

    /**
     * 更新
     */
    @RequestMapping("/archive")
    @ResponseBody
    @XxlSso
    public Response<String> archive(String topic, Integer archiveStrategy){
        return messageService.archive(topic, archiveStrategy, 1000);
    }

}
