package com.example.javamail.executarNextMonth;

import com.example.javamail.util.ShowData;
import com.vaadin.spring.annotation.SpringComponent;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.function.Consumer;

@SpringComponent
public class ExecTimer implements ShowData {


    private static final String EVERY_TOW_SEC = "2 * * ? * *";  // cada 2 segundos
    private static final String EVERY_SEC = "* * * ? * *"; //funciona
    private static final String EVERY_MIN = "0 * * ? * *"; //funciona

    @Autowired
    private Scheduler scheduler;

    private JobDetail job;
    private Trigger trigger;
    private TimeBean timeBean;

    public void setTimeBean(final TimeBean timeBean) {
        this.timeBean = Objects.requireNonNull(timeBean, () -> "TimeBean sin instanciar crear builder");
    }


    public void initTask() {
        try {
            job = JobBuilder.
                    newJob(TimerClass.class)
                    .withIdentity("sampleJob")
                    .build();
            trigger = TriggerBuilder
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
            show(System.out::println , getExpresionCronWithMin());
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private void show(final Consumer<String> c , final String value) {
        c.accept(value);
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
