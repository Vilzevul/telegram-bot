--liquibase formatted sql
--changeSet mybot:1
alter table notificationtask
    rename column time_send to timesend;



