package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name="notificationtask")
public class NotificationTask {
    @Id
    @GeneratedValue
    Long id;
    Long idchat;
    String message;
    LocalDateTime timesend;
    LocalDate timeedit;

    public NotificationTask(Long id_chat, String message, LocalDateTime time_send, LocalDate time_edit) {
        this.idchat = id_chat;
        this.message = message;
        this.timesend = time_send;
        this.timeedit = time_edit;
    }

    public NotificationTask() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask task = (NotificationTask) o;
        return Objects.equals(timesend, task.timesend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timesend);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_chat() {
        return idchat;
    }

    public void setId_chat(Long id_chat) {
        this.idchat = id_chat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime_send() {
        return timesend;
    }

    public void setTime_send(LocalDateTime time_send) {
        this.timesend = time_send;
    }

    public LocalDate getTime_edit() {
        return timeedit;
    }

    public void setTime_edit(LocalDate time_edit) {
        this.timeedit = time_edit;
    }

    @Override
    public String toString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String foramttedString = timesend.format(format);

        return id + " " + message + " " + foramttedString + "\n";
    }
}

