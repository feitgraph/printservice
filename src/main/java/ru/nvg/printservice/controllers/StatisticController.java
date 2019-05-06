package ru.nvg.printservice.controllers;

import com.querydsl.core.types.Predicate;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nvg.printservice.dao.JobRepository;
import ru.nvg.printservice.domain.Job;
import ru.nvg.printservice.domain.JobType;
import ru.nvg.printservice.domain.QJob;
import ru.nvg.printservice.domain.User;
import ru.nvg.printservice.qdsl.StatisticFilter;
import ru.nvg.printservice.qdsl.StatisticFilterBuilder;
import ru.nvg.printservice.services.StatisticsService;

import java.awt.*;
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(StatisticController.BASE_URL)
public class StatisticController {
    public static final String BASE_URL = "/api/v1/statistics";

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    Iterable<Job> getStatistics(@RequestParam(required=false) String user,
                                @RequestParam(required=false) String device,
                                @RequestParam(required=false) JobType type,
                                @RequestParam(required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeTo,
                                @RequestParam(required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeFrom) {
        StatisticFilter statisticFilter = new StatisticFilter();
        statisticFilter.setUser(user);
        statisticFilter.setDevice(device);
        statisticFilter.setType(type);
        statisticFilter.setTimeTo(timeTo);
        statisticFilter.setTimeFrom(timeFrom);

        StatisticFilterBuilder statisticFilterBuilder = new StatisticFilterBuilder();
        Predicate predicate = statisticFilterBuilder.build(statisticFilter);

        return statisticsService.statisticsFilter(predicate);


        //Predicate p = QJob.job.device.name.eq(device);
        //return statisticsService.statisticsFilter(predicate);
    }
}
