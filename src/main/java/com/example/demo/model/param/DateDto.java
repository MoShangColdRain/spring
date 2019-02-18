package com.example.demo.model.param;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
public class DateDto {
    private Date beginDate;

    private Date nextDate;

    private Date monthFirstDate;

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

    public Date getMonthFirstDate() {
        return monthFirstDate;
    }

    public void setMonthFirstDate(Date monthFirstDate) {
        this.monthFirstDate = monthFirstDate;
    }
}
