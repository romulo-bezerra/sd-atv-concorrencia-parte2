package br.edu.ifpb.sdatvconcorrenciaparte2.dao;

import br.edu.ifpb.sdatvconcorrenciaparte2.factory.ConFactoryPostgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class TimeWaitApplicationDao {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    public void insertTimeWait(int timeWait){
        try {
            Connection con = ConFactoryPostgreSQL.getConnectionPostgres();
            String sql = "INSERT INTO TimeWaitApplication (lastTimeWait) VALUES (?)";
            PreparedStatement statement = null;
            statement = con.prepareStatement(sql);
            statement.setInt(1, timeWait);
            statement.executeUpdate();
            statement.close();
            con.close();
        } catch (SQLException e) {
            log.warning("Falha ao inserir tempo de espera\n");
        }
    }

    public int getLastTimeWait(){
        int lastTimeWait = 0;
        Connection con = null;
        try {
            con = ConFactoryPostgreSQL.getConnectionPostgres();
            String sql = "SELECT lasttimewait FROM TimeWaitApplication ORDER BY id DESC LIMIT 1";
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            lastTimeWait = rs.next() ? rs.getInt(1) : lastTimeWait;
            statement.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            log.warning("Falha ao recuperar ultimo tempo de espera\n");
        }
        return lastTimeWait;
    }

    public int countRegistries(){
        int counting = 0;
        Connection con = null;
        try {
            con = ConFactoryPostgreSQL.getConnectionPostgres();
            String sql = "SELECT COUNT(*) FROM TimeWaitApplication";
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            counting = rs.next() ? rs.getInt(1) : counting;
            statement.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            log.warning("Falha ao recuperar contagem d eregistros\n");
        }
        return counting;
    }

}
