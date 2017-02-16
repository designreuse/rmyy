/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.system.scheduling.generate.quartz;

/**
 * 调度器监听接口
 * 在管理调度器时实现此接口
 *
 * @author 吴智俊
 */
public interface SchedulerListenerable {

    public void jobFinalized(Long jobId);
}
