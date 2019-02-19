package com.example.demo.model.param;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

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

  
