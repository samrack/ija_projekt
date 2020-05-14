package ija.project;

import java.time.LocalTime;

public interface TimeUpdate {
    void update(LocalTime time);

    void newTime(LocalTime time);

    void reloadSchedule(LocalTime time);
}
