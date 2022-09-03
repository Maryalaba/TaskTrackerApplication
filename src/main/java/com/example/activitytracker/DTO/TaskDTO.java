package com.example.activitytracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private String title;
    private String description;
    //    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date completedAt;
}
