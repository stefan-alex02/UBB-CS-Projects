package ro.mpp2024.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.Pair;
import ro.mpp2024.domain.User;
import ro.mpp2024.domain.Word;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if ((sessionFactory==null)||(sessionFactory.isClosed()))
            sessionFactory=createNewSessionFactory();
        return sessionFactory;
    }

    private static  SessionFactory createNewSessionFactory(){
        sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Word.class)
                .addAnnotatedClass(Game.class)
                .addAnnotatedClass(Pair.class)
                .addAnnotatedClass(ro.mpp2024.domain.Configuration.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static  void closeSessionFactory(){
        if (sessionFactory!=null)
            sessionFactory.close();
    }
}
