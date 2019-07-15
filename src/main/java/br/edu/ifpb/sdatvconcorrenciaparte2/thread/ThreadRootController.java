package br.edu.ifpb.sdatvconcorrenciaparte2.thread;

import br.edu.ifpb.sdatvconcorrenciaparte2.loader.Loader;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Thread monitora o controle do estado da aplicação
 * utilizando flags (bandeiras/var booleans),
 * pausando e resumindo a execução da aplicação
 * dado o controle pelo teclado
 */
public class ThreadRootController implements Runnable {

    private final Logger log;
    private Loader loader;

    public ThreadRootController(Loader loader){
        this.log = Logger.getLogger(this.getClass().getName());
        this.loader = loader;
    }

    @Override
    public void run() {
        System.out.println("Escolha uma opção a qualquer momento: ");
        System.out.println("'0' para pausar a execução da aplicação");
        System.out.println("'1' para o resume da execução da aplicação");

        while(true) {
            Scanner optionConditionScanner = new Scanner(System.in);
            if (optionConditionScanner.hasNextInt()) {
                int optionController = optionConditionScanner.nextInt();

                if ((optionController == 0)) log.info("APLICAÇÃO PAUSADA");
                else if (optionController == 1) log.info("APLICAÇÃO EM RESUMO");
                else log.info("NENHUMA AÇÃO CORRESPONDENTE PARA O COMANDO");

                loader.lockThreadRoot.lock();
                flagMonitor(optionController);
                loader.lockThreadRoot.unlock();
            }
        }
    }

    private void flagMonitor(int optionController){
        if (optionController == 0) {
            loader.isPaused = true;
        } else if (optionController == 1) {
            loader.isPaused = false;
            loader.lockCondition.signalAll();
        }
    }

}
