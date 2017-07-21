package com.scott.computercontrollerclient.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shijiale on 2017/7/3 0003.
 * EventPerformer 标识 event 处理的方法
 * 方法形式如下:
 * @IEventCustomer
 * class Widget {
 *
 *      @IEventPerfotmer
 *      public void onEvent(IEvent event) {
 *
 *      }
 *
 *
 *      public void needPerformer() {
 *          EventManager.getSingleton().regesiter(this);
 *      }
 *
 *      public void unNeedPerformer() {
 *          EventManager.getSingleton().unRegesiter(this);
 *      }
 * }
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IEventPerformer {
    /**
     * type 没有指定时，默认接受所有消息
     * 处理其他消息时可以自定义type类型。
     * 处理传输任务时type类型为 CommonCodes.CmdCode 中标识的type
     * 当type 为一级 type时 例如:
     * COMMAND_ADD,
     * COMMAND_DELETE,
     * COMMAND_MODIFY,
     * COMMAND_QUERY 时。
     * 标识了上述类型的方法，将处理该type下所有二级事件
     * 例如：
     * type = COMMAND_ADD,
     *
     * 则会处理如下事件:
     *  COMMAND_ADD_TASK
     *  COMMAND_ADD_TASKS
     *  COMMAND_ADD
     * @return
     */
    int type() default -1;

    /***
     * 如果标识:
     * TargetThread.MAIN_THREAD
     * 则会将事件在主线程处理。
     *
     * 如果标识 TargetThread.WORK_THREAD
     * 则在子线程处理
     * @return
     */
    TargetThread target() default TargetThread.MAIN_THREAD;
}
