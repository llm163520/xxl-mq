package com.xxl.mq.admin.model.adaptor;

import com.xxl.mq.admin.model.dto.MessageArchiveDTO;
import com.xxl.mq.admin.model.dto.MessageDTO;
import com.xxl.mq.admin.model.entity.Message;
import com.xxl.mq.admin.model.entity.MessageArchive;
import com.xxl.tool.core.DateTool;

public class MessageAdaptor {

    public static Message adaptor(MessageDTO messageDTO) {
        Message message = new Message();
        message.setId(messageDTO.getId());
        message.setTopic(messageDTO.getTopic());
        message.setPartitionId(messageDTO.getPartitionId());
        message.setData(messageDTO.getData());
        message.setBizId(messageDTO.getBizId());
        message.setStatus(messageDTO.getStatus());
        message.setEffectTime(DateTool.parseDateTime(messageDTO.getEffectTime()));
        message.setRetryCountRemain(messageDTO.getRetryCountRemain());
        message.setConsumeLog(messageDTO.getConsumeLog());
        message.setConsumeInstanceUuid(messageDTO.getConsumeInstanceUuid());
        message.setAddTime(DateTool.parseDateTime(messageDTO.getAddTime()));
        message.setUpdateTime(DateTool.parseDateTime(messageDTO.getUpdateTime()));
        return message;
    }

    public static MessageDTO adaptor(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setTopic(message.getTopic());
        messageDTO.setPartitionId(message.getPartitionId());
        messageDTO.setData(message.getData());
        messageDTO.setBizId(message.getBizId());
        messageDTO.setStatus(message.getStatus());
        messageDTO.setEffectTime(DateTool.formatDateTime(message.getEffectTime()));
        message.setRetryCountRemain(messageDTO.getRetryCountRemain());
        messageDTO.setConsumeLog(message.getConsumeLog());
        messageDTO.setConsumeInstanceUuid(message.getConsumeInstanceUuid());
        messageDTO.setAddTime(DateTool.formatDateTime(message.getAddTime()));
        messageDTO.setUpdateTime(DateTool.formatDateTime(message.getUpdateTime()));
        return messageDTO;
    }

    // --------------------------------- for archive ----------------------

    public static MessageArchive adaptorToArchive(Message message) {
        MessageArchive messageArchive = new MessageArchive();
        messageArchive.setId(message.getId());
        messageArchive.setTopic(message.getTopic());
        messageArchive.setPartitionId(message.getPartitionId());
        messageArchive.setData(message.getData());
        messageArchive.setBizId(message.getBizId());
        messageArchive.setStatus(message.getStatus());
        messageArchive.setEffectTime(message.getEffectTime());
        message.setRetryCountRemain(message.getRetryCountRemain());
        messageArchive.setConsumeLog(message.getConsumeLog());
        messageArchive.setConsumeInstanceUuid(message.getConsumeInstanceUuid());
        messageArchive.setAddTime(message.getAddTime());
        messageArchive.setUpdateTime(message.getUpdateTime());
        return messageArchive;
    }

    public static MessageArchiveDTO adaptorToArchiveDto(MessageArchive messageArchive) {
        MessageArchiveDTO messageArchiveDTO = new MessageArchiveDTO();
        messageArchiveDTO.setId(messageArchive.getId());
        messageArchiveDTO.setTopic(messageArchive.getTopic());
        messageArchiveDTO.setPartitionId(messageArchive.getPartitionId());
        messageArchiveDTO.setData(messageArchive.getData());
        messageArchiveDTO.setBizId(messageArchive.getBizId());
        messageArchiveDTO.setStatus(messageArchive.getStatus());
        messageArchiveDTO.setEffectTime(DateTool.formatDateTime(messageArchive.getEffectTime()));
        messageArchiveDTO.setRetryCountRemain(messageArchive.getRetryCountRemain());
        messageArchiveDTO.setConsumeLog(messageArchive.getConsumeLog());
        messageArchiveDTO.setConsumeInstanceUuid(messageArchive.getConsumeInstanceUuid());
        messageArchiveDTO.setAddTime(DateTool.formatDateTime(messageArchive.getAddTime()));
        messageArchiveDTO.setUpdateTime(DateTool.formatDateTime(messageArchive.getUpdateTime()));
        return messageArchiveDTO;
    }

}
