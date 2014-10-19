create database if not exists `calendar`;
use `calendar`;

drop table if exists `calendar_users`;
create table `calendar_users` (
    `id` int(32) unsigned not null auto_increment,
    `email` varchar(255) not null unique,
    `password` varchar(255) not null,
    `name` varchar(255) not null,
    PRIMARY KEY (`id`)    
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

drop table if exists `events`;
create table `events` (
    `id` int(32) unsigned not null auto_increment,
    `when` timestamp not null,
    `summary` varchar(255) not null,
    `description` varchar(500) not null,
    `owner` int(32) unsigned not null,
    `num_likes` int(32) unsigned not null,
    `event_level` tinyint not null,   
    PRIMARY KEY (`id`),
    KEY `fk_events_owner` (`owner`),
    CONSTRAINT `constraints_events_owner` FOREIGN KEY (`owner`) REFERENCES `calendar_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

drop table if exists `event_attendees`;
create table `events_attendees` (
    `id` int(32) unsigned not null auto_increment,
    `event_id` int(32) unsigned not null,
    `attendee` int(32) unsigned not null,
    PRIMARY KEY (`id`),
    KEY `fk_events_id` (`event_id`),
    KEY `fk_events_attendee` (`attendee`),
    CONSTRAINT `constraints_events_id` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `constraints_events_attendee` FOREIGN KEY (`attendee`) REFERENCES `calendar_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;