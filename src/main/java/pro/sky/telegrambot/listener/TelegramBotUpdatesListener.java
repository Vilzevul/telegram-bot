package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import liquibase.pro.packaged.S;
import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.configuration.TelegramBotConfiguration;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import javax.swing.text.html.HTML;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
private final Long myId = 1728459383l;
    @Autowired
    private TelegramBot telegramBot;
    private final NotificationService notificationService;

    public TelegramBotUpdatesListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {

        logger.info("- Processing init() - 2");
        telegramBot.setUpdatesListener(this);
    }

    Message msg;

    @Override
    @NonNull
    public int process(List<Update> updates) throws RuntimeException {
        updates.forEach(update -> {
            logger.info("- Processing process() - 3");
            logger.info("Processing update: {} - ?", update.message().text() + ", " + update.message().chat().firstName());
            // Process your updates here
User user = update.message().from();

            msg = update.message();
            if (msg.text() == null) return;
 //           Long chat_id = msg.chat().id();
            Long chat_id = user.id();
            String chat_name = msg.chat().firstName();
            List<String> arr = new ArrayList<>();


            arr = List.of(msg.text().split(" "));


            switch (arr.get(0)) {
                case ("/start"):
                    logger.info("- Processing process() case /start - 4");
                    SendResponse response = telegramBot.execute(new SendMessage(chat_id, chat_name + ", hello!"));
SendMessage sendMessage = new SendMessage(chat_id, " \n Я могу помочь вам создавать и управлять" +
        " вашими напоминаниями, которые потом я буду присылать вам через Telegram в указанное вами время." +
        "\n Список моих команд: \n" +
        "<b>/start</b> - начать использовать бота\n" +
        "/add - добавить напоминание\n" +
        "/del ID - удалить по id\n" +
        "/list - показать список напоминаний ");
sendMessage.parseMode(ParseMode.HTML);
                    response = telegramBot.execute(sendMessage);
                    break;
                case ("/add"):
                    response = telegramBot.execute(new SendMessage(chat_id, "Формат команды: \n" +
                            "01.01.2022 20:00 Текст сообщения"));
                    break;

                case ("/list"):
                    response = telegramBot.execute(new SendMessage(chat_id, getListTime()));
                    break;
                case ("/del"):
                    delAlarm(msg.text());
                    response = telegramBot.execute(new SendMessage(chat_id, getList()));
                    break;

                default:
                    try {
                        telegramBot.execute(new SendMessage(chat_id,setAlarm(msg.text()) ));
                    }catch (RuntimeException e){
                        return ;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
            ;

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void delAlarm(String textAlarm) {
        String[] arr = textAlarm.split(" ");
        Long id;
        try {
            id = Long.valueOf(arr[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        notificationService.delNotification(id);
    }

    private String getList() {
        Collection<NotificationTask> alarmList = new ArrayList<NotificationTask>(notificationService.listNotification());
//        for (Notification_task task: alarmList) {
        //       }
        return alarmList.toString();
    }

    private String getListTime() {
        Collection<NotificationTask> alarmList = new ArrayList<NotificationTask>(notificationService.findTaskByTime());
//        for (Notification_task task: alarmList) {
        //       }
        return alarmList.toString();
    }

    private String setAlarm(String textAlarm) throws Exception{

        final String regex = "([0-9\\.\\:\\s]{16})(\\s+)(.+\\S)";
        final String string = textAlarm;

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            final String groupDateTime = matcher.group(1);
            final String groupMessage = matcher.group(3);

            try {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(groupDateTime, format);
                //              dateTime=dateTime.truncatedTo(ChronoUnit.MINUTES);
                notificationService.saveNotification(new NotificationTask(msg.chat().id(),
                        groupMessage, dateTime, LocalDate.now()));
            } catch (DateTimeException e) {
                return "Неверный формат даты";
            }
            return "Записано";
        }
        return "Ошибка";
    }

    private SendResponse sendTask() {
        SendResponse response = null;
        Collection<NotificationTask> alarmList = new ArrayList<NotificationTask>(notificationService.findTaskByTime());
        for (NotificationTask task: alarmList) {
            Long chat_id = task.getId_chat();
            String chat_msg = task.getMessage();

             response = telegramBot.execute(new SendMessage(chat_id, chat_msg));
            notificationService.delNotification(task.getId());
        }
        return response;

    }
@Scheduled(fixedDelay = 60*1000l)
    private void sendBot(){
        SendResponse response = sendTask();
        if (response != null) {
            telegramBot.execute(new SendMessage(myId,"text: "
                    + response.message().text() + " отправлен! " +response.message().from().firstName() ));
        }
    }

//    @Scheduled(fixedDelay = 5000l)
    public void runConsole() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().toString(), format);
        LocalDateTime ts = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // "2017-03-08 12:30"

        notificationService.findTaskByTime();
    }
}
