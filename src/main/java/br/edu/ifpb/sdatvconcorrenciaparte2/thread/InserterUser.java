package br.edu.ifpb.sdatvconcorrenciaparte2.thread;

import br.edu.ifpb.sdatvconcorrenciaparte2.dao.UsuarioDao;
import br.edu.ifpb.sdatvconcorrenciaparte2.domain.Usuario;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

public class InserterUser implements Runnable {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private UsuarioDao usuarioDao;
    private Usuario usuario;
    private ArrayBlockingQueue<String> queueInsert;
    private ArrayBlockingQueue<String> queueUpdate;

    public InserterUser(Usuario usuario, ArrayBlockingQueue<String> queueInsert, ArrayBlockingQueue<String> queueUpdate) {
        this.usuario = usuario;
        this.queueInsert = queueInsert;
        this.queueUpdate = queueUpdate;
        this.usuarioDao = new UsuarioDao();
        new Thread(this, "InserterUser").start();
    }

    @Override
    public void run() {
        insertUser();
    }

    private void insertUser() {
        try {
            String userIdInsertLiberado = queueInsert.take();
            usuarioDao.insert(usuario);
            queueUpdate.put(userIdInsertLiberado);
            log.info("Usuário inserido");
        } catch (InterruptedException e) {
            log.warning(Thread.currentThread().getName() + "Thread interrompida");
        }
    }

}
