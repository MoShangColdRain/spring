package com.example.demo.model.param;

import lombok.Builder;
import lombok.Data;

import java.util.Date;


/**
 * @author liuxiang
 *
 */
@Data
@Builder
public class DateDto {
    private Date beginDate;

    private Date nextDate;

    private Date monthFirstDate;

}
