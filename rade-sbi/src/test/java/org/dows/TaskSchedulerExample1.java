//package org.dows;
//
//import com.sun.jna.*;
//import com.sun.jna.platform.win32.*;
//import com.sun.jna.ptr.IntByReference;
//
//public class TaskSchedulerExample1 {
//
//    public interface ITaskService extends COMInterface {
//        ITaskService INSTANCE = WinNT.INSTANCE.getInterface(ITaskService.class);
//
//        int HrInit();
//        int HrRegisterTask(String pPath, String pRegistrationInfo, int flags, String pUserId, String pPassword, int logonType, ITaskDefinition pDefinition, IntByReference pTaskHandle, int pSecurityDescriptor);
//    }
//
//    public static void main(String[] args) {
//        try {
//            // 初始化COM库
//            Ole32.INSTANCE.CoInitializeEx(Pointer.NULL, Ole32.COINIT_MULTITHREADED);
//
//            // 创建任务服务
//            ITaskService taskService = ITaskService.INSTANCE;
//
//            // 初始化任务服务
//            int hr = taskService.HrInit();
//            if (hr != 0) {
//                throw new Win32Exception(hr);
//            }
//
//            // 创建任务定义
//            ITaskDefinition taskDefinition = null; // 这里需要创建一个任务定义
//
//            // 注册任务
//            IntByReference taskHandle = new IntByReference();
//            hr = taskService.HrRegisterTask("MyTask.xml", "Task Description", WinNT.TASK_CREATE_OR_UPDATE, null, null, WinNT.LOGON32_LOGON_INTERACTIVE_TOKEN, taskDefinition, taskHandle, 0);
//            if (hr != 0) {
//                throw new Win32Exception(hr);
//            }
//
//            System.out.println("任务已创建，句柄：" + taskHandle.getValue());
//
//            // 清理COM资源
//            Ole32.INSTANCE.CoUninitialize();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}