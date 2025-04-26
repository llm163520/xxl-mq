package com.xxl.mq.admin.mapper;

import com.xxl.mq.admin.model.entity.Message;
import com.xxl.mq.admin.model.entity.MessageReport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
* Message Mapper
*
* Created by xuxueli on '2025-03-21 21:54:06'.
*/
@Mapper
public interface MessageMapper {

    /**
    * 新增
    */
    public int insert(@Param("message") Message message);

    /**
     * 批量新增
     */
    public int batchInsert(@Param("messageList") List<Message> messageList);

    /**
    * 删除
    */
    public int delete(@Param("ids") List<Long> ids);

    /**
    * 更新
    */
    public int update(@Param("message") Message message);

    /**
    * Load查询
    */
    public Message load(@Param("id") long id);

    /**
    * 分页查询Data
    */
	public List<Message> pageList(@Param("offset") int offset,
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
     * 根据状态查询消息
     */
    public List<Message> queryByTopicAndStatus(@Param("topic") String topic,
                                               @Param("archiveStatusList") List<Integer> archiveStatusList,
                                               @Param("pageSize") int pageSize);

    /**
     * 查询总数
     */
    public int count();

    /**
     * 查询报表
     */
    List<MessageReport> queryReport(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 批量更新状态
     */
    public int batchUpdateStatus(@Param("messageList") List<Message> messageList);

    /**
     * pull 查询消息，多个Topic分批查询
     *
     * @param topicList
     * @param pagesize
     * @return
     */
    public List<Message> pullQuery(@Param("topicList") List<String> topicList,
                                   @Param("status") int status,
                                   @Param("partitionIdFrom") int partitionIdFrom,
                                   @Param("partitionIdTo") int partitionIdTo,
                                   @Param("pagesize") int pagesize);

    /**
     * pull 修改消息状态
     *
     * @param messageIdList
     * @param instanceUuid
     * @param statuFrom
     * @param statuTo
     * @return
     */
    public int pullLock(@Param("messageIdList") List<Long> messageIdList,
                        @Param("instanceUuid") String instanceUuid,
                        @Param("statuFrom") int statuFrom,
                        @Param("statuTo")  int statuTo);

    /**
     * pull 查询消息，根据uuid
     *
     * @param messageIdList
     * @param instanceUuid
     * @param status
     * @return
     */
    public List<Message> pullQueryByUuid(@Param("messageIdList") List<Long> messageIdList,
                                         @Param("instanceUuid") String instanceUuid,
                                         @Param("status") int status);

    /**
     * retry 失败消息查询, 根据 id
     *      1、status = 失败
     *      2、retry_count_remain >0
     *      3、id in 指定范围内
     *
     * @param messageIdList
     * @return
     */
    List<Message> queryRetryDataById(@Param("messageIdList") List<Long> messageIdList,
                                     @Param("statusList") List<Integer> statusList);

    /**
     * retry 失败消息查询, 分批
     *
     * @param failStatusList
     * @param pagesize
     * @return
     */
    List<Message> queryRetryDataByPage(@Param("failStatusList") List<Integer> failStatusList,
                                       @Param("pagesize") int pagesize);

    /**
     * retry 失败消息批量修改，更新重试信息
     *      1、status 改为初始化
     *      2、retry_count_remain - 1
     *      3、effect_time 按照指定策略延迟
     *
     * @param failMessageList
     * @return
     */
    public int batchFailRetry(@Param("failMessageList") List<Message> failMessageList,
                              @Param("failStatusList") List<Integer> failStatusList,
                              @Param("newStatus") Integer newStatus,
                              @Param("consumeLog") String consumeLog);


    /**
     * stuck 卡住状态任务 修改为 失败；
     *
     * @param runningStatus
     * @param stuckTime
     * @param failStatus
     * @param pagesize
     * @return
     */
    public int updateStuck2FailWithPage(@Param("runningStatus") Integer runningStatus,
                                        @Param("stuckTime") Date stuckTime,
                                        @Param("failStatus") int failStatus,
                                        @Param("pagesize") int pagesize);

}
