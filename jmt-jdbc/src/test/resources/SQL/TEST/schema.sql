
CREATE TABLE Users (
  id mediumint(8) unsigned NOT NULL auto_increment,
  Name varchar(255) default NULL,
  EMail varchar(255) default NULL,
  Phone varchar(100) default NULL,
  Fax varchar(100) default NULL,
  Postal varchar(10) default NULL,
  Country varchar(100) default NULL,
  Region varchar(50) default NULL,
  City varchar(255),
  Street varchar(255) default NULL,
  Company varchar(255),
  Birthday varchar(255),
  PRIMARY KEY (id)
) AUTO_INCREMENT=1;