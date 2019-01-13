package com.example.javamail.executarnextmonth;

import com.example.javamail.util.GenericBuilder;
import com.example.javamail.util.ShowData;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringComponent
public class RunNextTask implements ShowData {

    @Autowired
    private ExecTimer execTimer;
    private ZonedDateTime zonedDateTime;
    private TimeBean timeBean;


    public void initTimeBuilder(final Tiempos tiposTiempos, final Integer valueTime, Label label) {
        try {
            switch (tiposTiempos) {
                case SEGUNDOS:
                    zonedDateTime = getZoneDateTime().plusSeconds(valueTime);
                    timeBean = getTimeBeanBuilder();
                    break;
                case MINUTOS:
                    zonedDateTime = getZoneDateTime().plusMinutes(valueTime);
                    timeBean = getTimeBeanBuilder();
                    break;
                case HORAS:
                    zonedDateTime = getZoneDateTime().plusHours(valueTime);
                    timeBean = getTimeBeanBuilder();
                    break;
                case MESES:
                    zonedDateTime = getZoneDateTime().plusMonths(valueTime);
                    timeBean = getTimeBeanBuilder();
                    break;
            }
        }catch (Exception ex){
            getLogger().error(null,ex);
        }
        execTimer.setTimeBean(timeBean);
        label.setValue("Expresión cron "+timeBean.toString());
        execTimer.initTask();
    }

    /**
     * Metodo de uso opcional
     */
    public void initTimeBeanMonth() {
        final ZoneId zoneId = ZoneId.of("Europe/Madrid");
        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(),zoneId).plusMonths(1);
        //para crear una tarea mensual el builder es valido asi segun cronmaker

        final TimeBean timeBean = GenericBuilder.of(TimeBean::new)
                .with(TimeBean::setDay,zonedDateTime.getDayOfMonth())
                .with(TimeBean::setMonth,zonedDateTime.getMonthValue())
                .build();

        execTimer.setTimeBean(timeBean);
        execTimer.initTask();
    }

    private ZonedDateTime getZoneDateTime() {
        return ZonedDateTime.ofInstant(Instant.now(),ZoneId.of("Europe/Madrid"));
    }

    private TimeBean getTimeBeanBuilder() {
        return GenericBuilder.of(TimeBean::new)
                .with(TimeBean::setSec,this.zonedDateTime.getSecond())
                .with(TimeBean::setMin,this.zonedDateTime.getMinute())
                .with(TimeBean::setHour,this.zonedDateTime.getHour())
                .with(TimeBean::setDay,this.zonedDateTime.getDayOfMonth())
                .with(TimeBean::setMonth,this.zonedDateTime.getMonthValue())
                .build();
    }
}
