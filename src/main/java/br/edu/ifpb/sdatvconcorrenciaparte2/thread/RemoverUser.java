package br.edu.ifpb.sdatvconcorrenciaparte2.thread;

import br.edu.ifpb.sdatvconcorrenciaparte2.dao.UsuarioDao;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

public class RemoverUser implements Runnable {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private UsuarioDao usuarioDao;
    private ArrayBlockingQueue<String> queueDelete;

    public RemoverUser(ArrayBlockingQueue<String> queueDelete) {
        this.queueDelete = queueDelete;
        this.usuarioDao = new UsuarioDao();
        new Thread(this, "RemoverUser").start();
    }

    @Override
    public void run() {
        deleteUser();
    }

    private void deleteUser() {
        try {
            String userIdDeleteLiberado = queueDelete.take();
            usuarioDao.delete(userIdDeleteLiberado);
            log.info("Usuário deletado");
        } catch (InterruptedException e) {
            log.warning(Thread.currentThread().getName() + "Thread interrompida");
        }
    }

}
