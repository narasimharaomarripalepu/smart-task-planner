package com.smart_task_planner.smart_task_planner.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.smart_task_planner.smart_task_planner.Entity.Task;
import com.smart_task_planner.smart_task_planner.Service.PlannerService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "http://localhost:3000") 
@RestController
public class PlannerController {

    @Autowired
    private PlannerService plannerService;

    @GetMapping("/planner")
    public List<Task> getTasks(@RequestParam String goal) {
        return plannerService.getTasks(goal);
    }
    
}
