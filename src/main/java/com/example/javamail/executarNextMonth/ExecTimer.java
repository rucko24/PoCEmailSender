package com.example.javamail.executarnextmonth;

import com.example.javamail.util.ShowData;
import com.vaadin.spring.annotation.SpringComponent;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@SpringComponent
public class ExecTimer implements ShowData {

    @Autowired
    private Scheduler scheduler;

    private TimeBean timeBean;

    public void setTimeBean(final TimeBean timeBean) {
        this.timeBean = Objects.requireNonNull(timeBean, "TimeBean sin instanciar crear builder");
    }


    public void initTask() {
        try {
            final JobDetail job = JobBuilder.
                    newJob(TimerClass.class)
                    .withIdentity("sampleJob")
                    .build();
            final Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .forJob(job)
                    .withIdentity("sampleTrigger")
                    .withSchedule(CronScheduleBuilder.cronSchedule(timeBean.toString()))
                    .build();
            scheduler.start();
            if(scheduler.checkExists(job.getKey())) {
                scheduler.deleteJob(job.getKey());
            }
            scheduler.scheduleJob(job, trigger);
            getLogger().info(getExpresionCronWithMin());
        } catch (final Exception ex) {
            getLogger().error(null,ex);
        }
    }

    public String getExpresionCronWithMin() {
        return new StringBuilder(timeBean.toString())
                .append(" ")
                .append("Expresion cron h:m:s a ")
                .append(" ")
                .append(timeBean.getHour())
                .append(":")
                .append(timeBean.getMin())
                .append(":")
                .append(timeBean.getSec())
                .toString();
    }

}
