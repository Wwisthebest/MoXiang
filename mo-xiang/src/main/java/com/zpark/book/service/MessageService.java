package com.zpark.book.service;

import com.zpark.book.entity.Message;

import java.util.List;

public interface MessageService {

    /**
     * 发送留言
     * @param message
     * @return
     */
    boolean addMessage(Message message);

    /**
     * 删除留言
     * @param id
     * @return
     */
    boolean deleteMessage(Long id);

    /**
     * 获取某个留言
     * @param id
     * @return
     */
    Message getMessage(Long id);

    /**
     * 获取某个用户收到的所有留言
     * @param userId
     * @return
     */
    List<Message> getAllMyMessage(Long userId);

    /**
     * 获取某个闲置的所有留言
     * @param idleId
     * @return
     */
    List<Message> getAllIdleMessage(Long idleId);

}
