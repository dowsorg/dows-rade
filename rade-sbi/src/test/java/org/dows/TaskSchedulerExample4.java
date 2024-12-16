//package org.dows;
//
//import com.sun.jna.platform.win32.*;
//import com.sun.jna.ptr.IntByReference;
//
//public class TaskSchedulerExample4 {
//
//    public static void main(String[] args) {
//        try {
//            // 初始化COM库
//            Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_APARTMENTTHREADED);
//
//            // 获取任务计划程序服务
//            ITaskScheduler taskScheduler = (ITaskScheduler)ole.getCOMObject("Schedule.Service", ITaskScheduler.class);
//
//            // 获取根文件夹
//            ITaskFolder rootFolder = taskScheduler.getRootFolder();
//
//            // 创建任务定义
//            ITaskDefinition taskDefinition = rootFolder.getDefinition();
//
//            // 设置任务属性
//            taskDefinition.setRegistrationInfo("MyTask", "A task created by JNA", 0);
//
//            // 创建触发器和动作（这里需要根据实际需求来设置）
//
//            // 注册任务
//            IntByReference taskHandle = new IntByReference();
//            taskScheduler.registerTaskDefinition("MyTask", taskDefinition, 0, null, null, 0, taskHandle, null);
//
//            System.out.println("任务创建成功，句柄：" + taskHandle.getValue());
//
//            // 清理COM资源
//            Ole32.INSTANCE.CoUninitialize();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}