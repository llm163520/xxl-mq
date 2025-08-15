package com.xxl.mq.admin.mapper;

import com.xxl.mq.admin.model.entity.MessageArchive;
import com.xxl.mq.admin.model.entity.MessageReport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
* MessageArchive Mapper
*
* Created by xuxueli on '2025-03-22 11:17:53'.
*/
@Mapper
public interface MessageArchiveMapper {

    /**
    * 新增
    */
    public int insert(@Param("messageArchive") MessageArchive messageArchive);

    /**
    * 删除
    */
    public int delete(@Param("ids") List<Integer> ids);

    /**
    * 更新
    */
    public int update(@Param("messageArchive") MessageArchive messageArchive);

    /**
    * Load查询
    */
    public MessageArchive load(@Param("id") int id);

    /**
     * 分页查询Data
     */
    public List<MessageArchive> pageList(@Param("offset") int offset,
                                         @Param("pagesize") int pagesize,
                                         @Param("topic") String topic,
                                         @Param("status") int status,
                                         @Param("effectTimeStart") Date effectTimeStart,
                                         @Param("effectTimeEnd") Date effectTimeEnd);

    /**
     * 分页查询Count
     */
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("topic") String topic,
                             @Param("status") int status,
                             @Param("effectTimeStart") Date effectTimeStart,
                             @Param("effectTimeEnd") Date effectTimeEnd);

    /**
     * 批量插入
     */
    public int batchInsert(@Param("messageArchiveList") List<MessageArchive> messageArchiveList);

    /**
     * 批量清除
     * @param topic
     * @param isArchive             1、true: clean with effectTimeFrom; 2、false : clean all
     * @param effectTimeFrom        isArchive = true， only > effectTimeFrom is valid
     * @param pageSize
     * @return
     */
    public int batchClean(@Param("topic") String topic,
                          @Param("isArchive") boolean isArchive,
                          @Param("effectTimeFrom") Date effectTimeFrom,
                          @Param("pageSize") int pageSize);

    /**
     * 查询报表
     */
    List<MessageReport> queryReport(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询失败总数
     *      1、failStatusList = 3、4
     *
     * @param topic
     * @param failStatusList
     * @param dateFrom
     * @param dateTo
     * @return
     */
    public int queryFailCount(@Param("topic") String topic,
                              @Param("failStatusList") List<Integer> failStatusList,
                              @Param("dateFrom") Date dateFrom,
                              @Param("dateTo") Date dateTo);

}
