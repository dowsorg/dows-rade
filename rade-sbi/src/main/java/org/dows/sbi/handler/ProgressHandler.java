package org.dows.sbi.handler;

import cn.hutool.core.lang.Dict;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 12/3/2024 5:22 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ProgressHandler /*extends TextWebSocketHandler*/ {
    //SockJsWebSocketHandler sockJsWebSocketHandler;
    private final SimpMessagingTemplate messagingTemplate;
    private static final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private static final AtomicBoolean errorBoolean = new AtomicBoolean(false);
    Timer timer = new Timer();

    @Async
    public void start(int totalStatements, double progressIncrement,String topic,Integer delay) {
        atomicBoolean.set(false);
        pushProgress(totalStatements, progressIncrement,topic,delay);
    }

    public void sendMessage(String topic,Object data){
        messagingTemplate.convertAndSend(topic, data);
    }

    public void sendErrorMessage(String message){
        Dict dict = Dict.of("error", message);
        messagingTemplate.convertAndSend("/topic/error", dict);
    }

    private void pushProgress(int totalStatements, double progressIncrement,String topic,Integer delay)      {
        double averageTimePerStatement = estimateAverageTimePerStatement();

        timer.scheduleAtFixedRate(new TimerTask() {
            private double progress = 0;

            @Override
            public void run() {
                progress += progressIncrement;
                if (progress >= totalStatements) {
                    if(!atomicBoolean.get()) {
                        Dict dict = Dict.of("percentage", "99");
                        messagingTemplate.convertAndSend(topic, dict);
                    }
                } else {
                    if(!atomicBoolean.get()) {
                        String message = String.format("%s", (progress / totalStatements) * 100);
                        Dict dict = Dict.of("percentage", message);
//                    messagingTemplate.convertAndSend("/topic/progress", dict);
                        messagingTemplate.convertAndSend(topic, dict);
                    }
                }
                if (atomicBoolean.get()) {
                    this.cancel();
                    Dict dict = Dict.of("percentage", "100");
                    messagingTemplate.convertAndSend(topic, dict);
                }
                if (errorBoolean.get()) {
                    this.cancel();
                    Dict dict = Dict.of("percentage", "0","error", "任务执行结束，任务中断");
                    messagingTemplate.convertAndSend(topic, dict);
                }
            }
        }, 0, (long) averageTimePerStatement * delay);
    }

    private double estimateAverageTimePerStatement() {
        // 估算每条语句的平均执行时间
        // 假设每条语句平均执行时间为1秒
        return 1.0;
    }

    public void finish() {
        atomicBoolean.set(true);
    }

    public void error(String message) {
//        Dict dict = Dict.of("error", message);
//        messagingTemplate.convertAndSend("/topic/progress", dict);
        // 停止进度推送
//        atomicBoolean.set(true);
        log.error("任务执行结束，任务中断: {}", message);
        errorBoolean.set(true);
    }



//    @MessageMapping("/progress")
//    public void handleProgressMessage(Message<?> message) {
//        // 这里可以根据接收到的前端消息进行处理，暂不做具体处理
//    }
//
//    public void sendProgress(int percentage, WebSocketSession session) {
//        Map<String, Object> progressMap = new HashMap<>();
//        progressMap.put("percentage", percentage);
//        try {
//            session.sendMessage(new org.springframework.web.socket.WebSocketMessage<byte[]>() {
//                @Override
//                public byte[] getPayload() {
//                    // 将进度信息转换为字节数组，这里简单假设进度信息是字符串
//                    return progressMap.toString().getBytes();
//                }
//
//                @Override
//                public int getPayloadLength() {
//                    return 0;
//                }
//
//                @Override
//                public boolean isLast() {
//                    return true;
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        Map<String, Object> responseMap = new HashMap<>();
//        responseMap.put("message", "欢迎连接进度监控WebSocket服务");
//        session.sendMessage(new org.springframework.web.socket.WebSocketMessage<byte[]>() {
//            @Override
//            public byte[] getPayload() {
//                return responseMap.toString().getBytes();
//            }
//
//            @Override
//            public int getPayloadLength() {
//                return 0;
//            }
//
//            @Override
//            public boolean isLast() {
//                return true;
//            }
//        });
//    }
//
//    @Override
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//        // 这里可以添加处理接收到的消息的逻辑
//    }
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        // 处理传输错误
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        // 处理连接关闭
//    }

    /*@MessageMapping("/progress")
    public void handleProgressMessage(Message<?> message) {
        // 这里可以根据接收到的前端消息进行处理，本示例暂不做具体处理
        MessageHeaders headers = message.getHeaders();
        // 获取WebSocket会话对象
        WebSocketSession session = (WebSocketSession) headers.get(SimpMessageHeaderAccessor.SESSION_KEY);
        // 如果会话存在，可以进行一些操作，比如发送欢迎消息等
        if (session!= null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "欢迎连接进度监控WebSocket服务");
            Message<?> responseMessage = MessageBuilder.withPayload(responseMap)
                   .setHeader(SimpMessageHeaderAccessor.MESSAGE_TYPE, SimpMessageType.CONNECT_ACK)
                   .setHeader(SimpMessageHeaderAccessor.SESSION_KEY, session)
                   .build();
            messagingTemplate.sendToUser(session.getAttributes().get("user").toString(), "/queue/response",
                    responseMessage);
        }
    }

    public void sendProgress(int percentage) {
        Map<String, Object> progressMap = new HashMap<>();
        progressMap.setProperty("percentage", percentage);
        messagingTemplate.convertAndSend("/topic/progress", progressMap);
    }*/
}