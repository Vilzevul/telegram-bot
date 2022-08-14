package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationTask saveNotification(NotificationTask notification_task) {
        return notificationRepository.save(notification_task);
    }

    public Collection<NotificationTask> listNotification() {

return notificationRepository.findAll();
    }

    public void delNotification(Long id){
        notificationRepository.deleteById(id);
    }

    public Collection<NotificationTask> findTaskByTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
 //      String groupDateTime = "07.08.2022 21:00";
//        LocalDateTime dateTimeGroup = LocalDateTime.parse(groupDateTime, format);

 //       String formattedString = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).format(format); // "2017-03-08 12:30"
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo((ChronoUnit.MINUTES));
        return notificationRepository.findAllByTimesendBefore(dateTime);




    }
}
