package pl.kedziora.emilek.roomies.utils;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import pl.kedziora.emilek.json.objects.data.SingleAnnouncementData;
import pl.kedziora.emilek.json.objects.enums.Interval;
import pl.kedziora.emilek.roomies.database.objects.Announcement;
import pl.kedziora.emilek.roomies.database.objects.EventEntry;

import javax.annotation.Nullable;
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

    public static int addTask(ScheduledFuture<?> task) {
        int taskKey = task.hashCode();
        schedulersMap.put(taskKey, task);
        return taskKey;
    }

    public static List<SingleAnnouncementData> generateAnnouncementData(List<Announcement> announcements) {
        return Lists.newArrayList(
                Collections2.transform(announcements, new Function<Announcement, SingleAnnouncementData>() {
                    @Override
                    public SingleAnnouncementData apply(@Nullable Announcement announcement) {
                        String userName = null;
                        if (!announcement.getAnonymous()) {
                            userName = announcement.getUser().getName();
                        }
                        return new SingleAnnouncementData(announcement.getId(), announcement.getTitle(), userName,
                                announcement.getContent(), announcement.getUser().getId());
                    }
                })
        );
    }

}
