package com.example.javamail.executarNextMonth;

import com.example.javamail.util.ShowData;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.EnumSet;

enum Tiempos {

    SEGUNDOS("SEGUNDOS"),
    MINUTOS("MINUTOS"),
    HORAS("HORAS"),
    MESES("MESES");

    private String tiempos;

    Tiempos(final String tiempos) {
        this.tiempos = tiempos;
    }
    public String getTiempos() {
        return tiempos;
    }
    public static Tiempos getTiemposValues(final String tiempos) {
        return EnumSet.allOf(Tiempos.class)
                .stream()
                .filter(obj -> obj.getTiempos().equals(tiempos))
                .findFirst()
                .get();
    }
}

@SpringComponent
public class RunNextTask implements ShowData {

    @Autowired
    private ExecTimer execTimer;
    private ZonedDateTime zonedDateTime;
    private String tiemposDeTiempos;
    private Integer valueTime;
    private TimeBean timeBean;
    /**
     * Send 1min later
     * se ejecuta sola una vez
     * si se desea ejecutar repetidamente tocara modificar la expresion cron
     */

    public void initTimeBuilder(final String tiposDeTiempos, final Integer valueTime, Label label) {
        this.tiemposDeTiempos = tiposDeTiempos;
        this.valueTime = valueTime;
        try {
            final Tiempos tiempos = Tiempos.getTiemposValues(tiposDeTiempos);
            switch (tiempos) {
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
        }catch (Exception ex){ex.printStackTrace();}
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
        final TimeBean timeBean = new TimeBean.Builder()
                .withDay(zonedDateTime.getDayOfMonth())//el dia es requerido
                .withMonth(zonedDateTime.getMonthValue()) //el mes es requrido
                .build();
        execTimer.setTimeBean(timeBean);
        execTimer.initTask();
    }

    private ZonedDateTime getZoneDateTime() {
        return ZonedDateTime.ofInstant(Instant.now(),ZoneId.of("Europe/Madrid"));
    }

    private TimeBean getTimeBeanBuilder() {
        final TimeBean timeBean = new TimeBean.Builder()
                .withSec(this.zonedDateTime.getSecond())
                .withMin(this.zonedDateTime.getMinute())
                .withHour(this.zonedDateTime.getHour())
                .withDay(this.zonedDateTime.getDayOfMonth())//el dia es requerido
                .withMonth(this.zonedDateTime.getMonthValue()) //el mes es requrido
                .build();
        return timeBean;
    }
}
