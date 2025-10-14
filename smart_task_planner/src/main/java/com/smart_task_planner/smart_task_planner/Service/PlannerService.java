package com.smart_task_planner.smart_task_planner.Service;

import java.util.List;


import com.smart_task_planner.smart_task_planner.Entity.Task;

public interface PlannerService {

    List<Task> getTasks(String goal);

}
