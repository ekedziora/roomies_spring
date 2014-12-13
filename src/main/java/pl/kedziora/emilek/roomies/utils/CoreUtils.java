package pl.kedziora.emilek.roomies.utils;

import com.google.api.client.util.Maps;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import pl.kedziora.emilek.json.objects.enums.Interval;
import pl.kedziora.emilek.roomies.database.objects.EventEntry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class CoreUtils {

    public static Map<Integer, ScheduledFuture<?>> schedulersMap = Maps.newHashMap();

    public static Period createCyclicEventPeriod(LocalDate start, LocalDate end, Interval intervalType, Integer intervalNumber) {
        Period period = new Period(start, end);

        switch (intervalType) {
            case DAYS:
                period = period.plus(Period.days(intervalNumber));
                break;
            case WEEKS:
                period = period.plus(Period.weeks(intervalNumber));
                break;
            case MONTHS:
                period = period.plus(Period.months(intervalNumber));
                break;
            case YEARS:
                period = period.plus(Period.years(intervalNumber));
                break;
        }

        return period;
    }

    public static <T> void moveElementToFirstPlace(List<T> list, T element) {
        list.remove(element);
        list.add(0, element);
    }

    public static void handleSchedulersOnEntryDelete(List<EventEntry> deletedEntries) {
        for(EventEntry entry : deletedEntries) {
            deleteEndEntryScheduler(entry);
            deleteConfirmationCheckScheduler(entry);
        }
    }

    public static void deleteEndEntryScheduler(EventEntry entry) {
        Integer endEntrySchedulerKey = entry.getEndEntrySchedulerKey();

        if (endEntrySchedulerKey != null && schedulersMap.containsKey(endEntrySchedulerKey)) {
            ScheduledFuture<?> task = schedulersMap.get(endEntrySchedulerKey);
            task.cancel(true);
            schedulersMap.remove(endEntrySchedulerKey);
            entry.setEndEntrySchedulerKey(null);
        }
    }

    public static void deleteConfirmationCheckScheduler(EventEntry entry) {
        Integer confirmationCheckSchedulerKey = entry.getConfirmationCheckSchedulerKey();

        if (confirmationCheckSchedulerKey != null && schedulersMap.containsKey(confirmationCheckSchedulerKey)) {
            ScheduledFuture<?> task = schedulersMap.get(confirmationCheckSchedulerKey);
            task.cancel(true);
            schedulersMap.remove(confirmationCheckSchedulerKey);
            entry.setConfirmationCheckSchedulerKey(null);
        }
    }

    public static int addTask(ScheduledFuture<?> task) {
        int taskKey = task.hashCode();
        schedulersMap.put(taskKey, task);
        return taskKey;
    }

}
