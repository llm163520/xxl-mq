package com.xxl.mq.admin.broker.thread;

import com.xxl.mq.admin.broker.config.BrokerBootstrap;
import com.xxl.mq.admin.model.entity.MessageReport;
import com.xxl.mq.admin.model.entity.Topic;
import com.xxl.mq.admin.util.I18nUtil;
import com.xxl.mq.core.constant.MessageStatusEnum;
import com.xxl.tool.concurrent.CyclicThread;
import com.xxl.tool.core.CollectionTool;
import com.xxl.tool.core.DateTool;
import com.xxl.tool.core.StringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ArchiveThreadHelper
 *
 * @author xuxueli
 */
public class ArchiveAndAlarmThreadHelper {
    private static final Logger logger = LoggerFactory.getLogger(ArchiveAndAlarmThreadHelper.class);

    // ---------------------- init ----------------------

    private final BrokerBootstrap brokerBootstrap;
    public ArchiveAndAlarmThreadHelper(BrokerBootstrap brokerBootstrap) {
        this.brokerBootstrap = brokerBootstrap;
    }

    // ---------------------- start / stop ----------------------

    private static final int ARCHIVE_INTERVAL = 10 * 60 * 1000;

    /**
     * start ArchiveThreadHelper (will stop with jvm)
     *
     * remark：
     *      1、archive message：          move "real-time" 2 "archive-message"
     *      2、refresh messge-report：    within 3 days
     *      3、alarm by topic：           by topic, detect new-fail-message
     *
     *      fail>0, by topic
     */
    public void start(){

        CyclicThread archiveThread = new CyclicThread("archiveThread", true, new Runnable() {
            @Override
            public void run() {

                // valid master
                if (!brokerBootstrap.getLocalCacheThreadHelper().isMasterBroker()) {
                    return;
                }

                // 1、move real-time 2 archive-message
                List<Topic> topicList = brokerBootstrap.getLocalCacheThreadHelper().findTopicAll();
                if (CollectionTool.isNotEmpty(topicList)) {
                    for (Topic topic : topicList) {
                        brokerBootstrap.getMessageService().archive(topic.getTopic(), topic.getArchiveStrategy(), 10000);
                    }
                }

                // 2、refresh daily message-report（within 3 days）
                Date startOfToday = DateTool.setStartOfDay(new Date());
                Date dateFrom = DateTool.addDays(startOfToday, -2);
                Date dateTo = DateTool.addDays(startOfToday, 1);
                List<MessageReport> messageReportList = brokerBootstrap.getMessageMapper().queryReport(dateFrom, dateTo);
                List<MessageReport> messageReportList2 = brokerBootstrap.getMessageArchiveMapper().queryReport(dateFrom, dateTo);

                Map<Date, MessageReport> reportMap = new HashMap<>();
                for (MessageReport report : messageReportList) {            // fill real-time data
                    MessageReport existingReport = reportMap.get(report.getProduceDay());
                    if (existingReport == null) {
                        reportMap.put(report.getProduceDay(), report);
                    } else {
                        existingReport.setNewCount(existingReport.getNewCount() + report.getNewCount());
                        existingReport.setRunningCount(existingReport.getRunningCount() + report.getRunningCount());
                        existingReport.setSucCount(existingReport.getSucCount() + report.getSucCount());
                        existingReport.setFailCount(existingReport.getFailCount() + report.getFailCount());
                    }
                }
                for (MessageReport report : messageReportList2) {           // fill archive data
                    MessageReport existingReport = reportMap.get(report.getProduceDay());
                    if (existingReport == null) {
                        reportMap.put(report.getProduceDay(), report);
                    } else {
                        existingReport.setNewCount(existingReport.getNewCount() + report.getNewCount());
                        existingReport.setRunningCount(existingReport.getRunningCount() + report.getRunningCount());
                        existingReport.setSucCount(existingReport.getSucCount() + report.getSucCount());
                        existingReport.setFailCount(existingReport.getFailCount() + report.getFailCount());
                    }
                }
                for (Date produceDay = dateFrom; produceDay.getTime() <= dateTo.getTime(); produceDay = DateTool.addDays(produceDay, 1)) {
                    MessageReport report = reportMap.get(produceDay);
                    if (report == null) {
                        report = new MessageReport();
                        report.setProduceDay(produceDay);
                        report.setNewCount(0);
                        report.setRunningCount(0);
                        report.setSucCount(0);

                        reportMap.put(report.getProduceDay(), report);      // fill none data
                    }
                }

                // write report
                if (CollectionTool.isNotEmpty(reportMap.values())) {
                    for (MessageReport messageReport : reportMap.values()) {
                        messageReport.setUpdateTime(new Date());
                        brokerBootstrap.getMessageReportMapper().insert(messageReport);
                    }
                }

                // 3、alarm by topic
                if (CollectionTool.isNotEmpty(topicList)) {
                    for (Topic topic : topicList) {
                        // email alarm
                        Set<String> emailList = StringTool.isNotBlank(topic.getAlarmEmail())
                                ? Arrays.stream(topic.getAlarmEmail().split(",")).filter(StringTool::isNotBlank).collect(Collectors.toSet()) :
                                null;
                        if (emailList == null) {
                            continue;
                        }

                        // filter fail count
                        Date failFrom = DateTool.addMilliseconds(new Date(), -1 * ARCHIVE_INTERVAL);
                        Date failTo = new Date();
                        List<Integer> failStatusList = Arrays.asList(MessageStatusEnum.EXECUTE_FAIL.getValue(), MessageStatusEnum.EXECUTE_TIMEOUT.getValue());

                        int failCount = brokerBootstrap.getMessageMapper().queryFailCount(topic.getTopic(), failStatusList, failFrom, failTo);
                        failCount += brokerBootstrap.getMessageArchiveMapper().queryFailCount(topic.getTopic(), failStatusList, failFrom, failTo);
                        if (failCount <= 0) {
                            continue;
                        }

                        // email cotent
                        String title = "【监控报警】" + I18nUtil.getString("admin_name_full");
                        String content = makeEmailConent(topic, failFrom, failTo, failCount);

                        // send mail
                        for (String email : emailList) {
                            try {
                                MimeMessage mimeMessage = brokerBootstrap.getMailSender().createMimeMessage();

                                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                                helper.setFrom(brokerBootstrap.getMailUsername());
                                helper.setTo(email);
                                helper.setSubject(title);
                                helper.setText(content, true);

                                brokerBootstrap.getMailSender().send(mimeMessage);
                            } catch (Exception e) {
                                logger.error(">>>>>>>>>>> xxl-mq, fail alarm email send error, topic:{}", topic.getTopic(), e);
                            }

                        }
                    }
                }

            }
        }, ARCHIVE_INTERVAL, true);
        archiveThread.start();
    }

    public void stop(){
        // do nothing
    }

    /**
     * makeEmailConent
     *
     * @return
     */
    private static String makeEmailConent(Topic topic, Date dateFrom, Date dateTo, long failCount){
        String mailBodyTemplate = "<h5> 监控告警明细：</span>" +
                "<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n" +
                "   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >" +
                "      <tr>\n" +
                "         <td width=\"20%\" >"+ "Topic" +"</td>\n" +
                "         <td width=\"25%\" >"+ "主题描述" +"</td>\n" +
                "         <td width=\"20%\" >"+ "告警时间" +"</td>\n" +
                "         <td width=\"15%\" >"+ "告警标题" +"</td>\n" +
                "         <td width=\"20%\" >"+ "告警内容" +"</td>\n" +
                "      </tr>\n" +
                "   </thead>\n" +
                "   <tbody>\n" +
                "      <tr>\n" +
                "         <td>"+ topic.getTopic() +"</td>\n" +
                "         <td>"+ topic.getDesc() +"</td>\n" +
                "         <td>"+ DateTool.formatDateTime(dateFrom) +" ~ \n"+DateTool.formatDateTime(dateTo) +"</td>\n" +
                "         <td>消费失败</td>\n" +
                "         <td>失败次数："+ failCount +"</td>\n" +
                "      </tr>\n" +
                "   </tbody>\n" +
                "</table>";

        return mailBodyTemplate;
    }

}
