# sd-atv-concorrencia-parte2
Atvidade de Concorrência na Manipulação de Dados

### Solução
A classe Loader (executora) possui uma flag, atributo boolean, 
que simboliza o estado de execução da aplicação (__pausada__ e __resumida__). 
Essa flag é monitorada pela ThreadRootController que controla o estado 
da aplicação juntamente com o objeto ReentrantLock e Condition, seguindo 
a estratégia de lock para garantir a sincronia/isolamento das threads 
entre N aplicações, tornando a controladora __mutex__.
Na classe controladora há um notificador de condição 'signalAll' que
acordam as threads que esperam por essa condição para tornarem-se 
elegíveis para adquirir o Lock.
A classe executora percebe que o estado da flag foi mudado para 'pausado'
pelo usuário (ações de input no teclado) e então executa um 'await' na 
iteração que chama a execução das threads de CRUD até que seja mudado 
a propriedade no teclado pelo usuário.


#### Passos para Execução
 > 1. Rode src/main/docker/docker-compose.yml com `docker-compose up -d` 
 para criar e inicializar o banco PostgreSQL
 > 2. Banco inicializado e configurado, rode os arquivos `sh` 
 na raiz do projeto com o comando `sh execute-instance01.sh` 
 e `sh execute-instance02.sh` para construir o projeto com 
 maven assemby e executar as instancias.
