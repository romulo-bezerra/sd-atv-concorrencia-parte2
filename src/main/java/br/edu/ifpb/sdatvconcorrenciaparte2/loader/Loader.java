package br.edu.ifpb.sdatvconcorrenciaparte2.loader;

import br.edu.ifpb.sdatvconcorrenciaparte2.dao.TimeWaitApplicationDao;
import br.edu.ifpb.sdatvconcorrenciaparte2.domain.Usuario;
import br.edu.ifpb.sdatvconcorrenciaparte2.factory.UserIdFactory;
import br.edu.ifpb.sdatvconcorrenciaparte2.thread.InserterUser;
import br.edu.ifpb.sdatvconcorrenciaparte2.thread.RemoverUser;
import br.edu.ifpb.sdatvconcorrenciaparte2.thread.ThreadRootController;
import br.edu.ifpb.sdatvconcorrenciaparte2.thread.UpdaterUser;

import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Loader {

    private final Logger log;
    private final ArrayBlockingQueue<String> queueInsert; //cria uma fila com bloqueios para inserção
    private final ArrayBlockingQueue<String> queueUpdate; //cria uma fila com bloqueios para atualização
    private final ArrayBlockingQueue<String> queueDelete; //cria uma fila com bloqueios para remoção
    private UserIdFactory userIdFactory; //fábrica de ids

    public Lock lockThreadRoot;
    public Condition lockCondition;
    public boolean isPaused = false;

    public Loader() throws UnknownHostException {
        this.queueInsert = new ArrayBlockingQueue<String>(1000);
        this.queueUpdate = new ArrayBlockingQueue<String>(1000);
        this.queueDelete = new ArrayBlockingQueue<String>(1000);
        this.userIdFactory = new UserIdFactory();
        this.log = Logger.getLogger(this.getClass().getName());

        this.lockThreadRoot =  new ReentrantLock();
        this.lockCondition = lockThreadRoot.newCondition();
    }

    public static void main(String[] args) throws UnknownHostException {
        //Analisa tempo com 10 threads
        long initialTimeWith10Threads = System.currentTimeMillis();
        new Loader().run(10);
        long finalTimeWith10Threads = System.currentTimeMillis();
        System.out.println("Tempo de duração em milissegundos com 10 Threads: "
        + (finalTimeWith10Threads - initialTimeWith10Threads));

        //Analisa tempo com 100 threads
//        long initialTimeWith100Threads = System.currentTimeMillis();
//        new Loader().run(100);
//        long finalTimeWith100Threads = System.currentTimeMillis();
//        System.out.println("Tempo de duração em milissegundos com 100 Threads: "
//                + (finalTimeWith100Threads - initialTimeWith100Threads));

        //Analisa tempo com 1000 threads
//        long initialTimeWith1000Threads = System.currentTimeMillis();
//        new Loader().run(1000);
//        long finalTimeWith1000Threads = System.currentTimeMillis();
//        System.out.println("Tempo de duração em milissegundos com 1000 Threads: "
//        + (finalTimeWith1000Threads - initialTimeWith1000Threads));
    }

    public void run(int qntThreads) {
        //esperando tempo aleatórios de ate 4999 ms para a execução da aplicação
        timeWaitInitializationApplicationRandom();

        ThreadRootController threadRootController = new ThreadRootController(this);
        new Thread(threadRootController).start();

        for (int i=1; i<=qntThreads; i++){

            if (i > 3){
                System.out.println("\nEscolha uma opção a qualquer momento.");
                System.out.println("'0' para PAUSAR o processamento");
                System.out.println("'1' para RESUMIR o processamento\n");
            }

            try {
                verifyConditionWait();

                //tempo de espera para facilitar a visualização do
                //controle do estado de execução do processamento
                Thread.sleep(3000);

                Usuario usuario = new Usuario();
                usuario.setId(userIdFactory.createId().toString()); //setando id gerado por timestamp
                usuario.setNome("Bartolomeu");

                queueInsert.put(usuario.getId()); //id para a fila
                new InserterUser(usuario, queueInsert, queueUpdate); //thread responsável por inserir
                new UpdaterUser(queueUpdate, queueDelete); //thread responsável por atualizar
                new RemoverUser(queueDelete); //thread responsável por deletar
            } catch (InterruptedException e) {
                log.warning(Thread.currentThread().getName() + "Thread interrompida");
            }
        }
    }

    /**
     * Método verifica a condição de espera controlada pela
     * thread ThreadRootController e libera o adquirinte de bloqueio
     * para resumo da thread Main
     */
    private void verifyConditionWait(){
        try {
            lockThreadRoot.lock();
            while(isPaused) lockCondition.await();
            lockThreadRoot.unlock();
        } catch (InterruptedException e) {
            log.warning(Thread.currentThread().getName() + "Thread interrompida\n" + e.getMessage());
        }
    }

    /**
     * Metodo garante que cada instancia inicialize em tempos distintos.
     * O porquê disso se dá pelo fato de o id de usuário ser um timestamp;
     * sendo assim, duas instancias inicializando no mesmo instante feria
     * a integridade de chave primária e ocasionaria um erro de chave duplicata
     */
    private void timeWaitInitializationApplicationRandom() {
        try {
            int timeWaitApplication, lastTimeWaitApplication;
            int countingRegistriesTimeWaitApplication, countingLaterRegistriesTimeWaitApplication;

            do {
                Random timeInitializationApplicationRadom = new Random();
                timeWaitApplication = timeInitializationApplicationRadom.nextInt(5000);

                TimeWaitApplicationDao timeWaitApplicationDao = new TimeWaitApplicationDao();
                timeWaitApplicationDao.insertTimeWait(timeWaitApplication);
                countingRegistriesTimeWaitApplication = timeWaitApplicationDao.countRegistries();

                Thread.sleep(timeWaitApplication);

                countingLaterRegistriesTimeWaitApplication = timeWaitApplicationDao.countRegistries();
                lastTimeWaitApplication = timeWaitApplicationDao.getLastTimeWait();

            } while (lastTimeWaitApplication == timeWaitApplication &&
                    countingRegistriesTimeWaitApplication != countingLaterRegistriesTimeWaitApplication);

        } catch (InterruptedException e) {
            log.warning(Thread.currentThread().getName() + "Thread interrpompida");
        }
    }

}
