package org.dows.core.message;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 7/22/2024 2:30 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface MessageHandler<T extends Message> {

    /**
     * 处理消息
     *
     * @param message
     */
    void handle(T message);

}
