CREATE TABLE Usuario (
  id VARCHAR PRIMARY KEY,
	nome VARCHAR(255),
	atualizando BOOLEAN,
	deletando BOOLEAN
);

CREATE TABLE TimeWaitApplication (
  id SERIAL PRIMARY KEY,
  lastTimeWait INT
);
