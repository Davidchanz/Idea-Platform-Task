package org.gn;

import org.gn.task.Task;

import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        Task task;
        if (args.length > 0)
            task = new Task(args[0]);
        else  task = new Task();

        task.performTask1();
        task.performTask2();
    }
}