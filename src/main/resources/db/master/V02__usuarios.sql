CREATE TABLE usuarios (
  login varchar(100) PRIMARY KEY UNIQUE,
  nome varchar(100) DEFAULT NULL,
  senha varchar(20) DEFAULT NULL,
  empresa varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;