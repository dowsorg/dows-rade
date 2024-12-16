//package org.dows;
//
//import com.sun.jna.platform.win32.Ole32;
//import com.sun.jna.platform.win32.OleAuto;
//import com.sun.jna.platform.win32.WinDef.VARIANT;
//
//public class TaskSchedulerExample3 {
//
//    public interface ITaskScheduler extends OleAuto.IDispatch {
//        ITaskFolder getRootFolder();
//    }
//
//    public interface ITaskFolder extends OleAuto.IDispatch {
//        ITaskDefinition getDefinition();
//        ITask createTask(String path, int flags);
//    }
//
//    public interface ITaskDefinition extends OleAuto.IDispatch {
//        void setRegistrationInfo(String name, String description);
//        void setTrigger(ITaskTrigger trigger);
//        void setAction(ITaskAction action);
//    }
//
//    public interface ITaskTrigger extends OleAuto.IDispatch {
//        // Define trigger properties and methods
//    }
//
//    public interface ITaskAction extends OleAuto.IDispatch {
//        // Define action properties and methods
//    }
//
//    public static void main(String[] args) {
//        try {
//            Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_APARTMENTTHREADED);
//
//            ITaskScheduler taskScheduler = (ITaskScheduler) OleAuto.INSTANCE.createDispatch("Schedule.Service", ITaskScheduler.class);
//            ITaskFolder rootFolder = taskScheduler.getRootFolder();
//
//            ITask task = rootFolder.createTask("MyTask", 0); // 0 = TASK_CREATE
//            ITaskDefinition taskDefinition = task.getDefinition();
//
//            // Set task properties (e.g., description, triggers, actions)
//            taskDefinition.setRegistrationInfo("MyTask", "A task created by JNA");
//
//            // Release COM resources
//            Ole32.INSTANCE.CoUninitialize();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}