package de.hofuniversity.iws.server.data.handler;

import javax.persistence.*;

// entityManager.detach(entity);
// later
// entityManager.merge(entity);
public class HibernateUtil {

    private static final EntityManagerFactory emf = init();

    private static EntityManagerFactory init() {
        try {
            return Persistence.createEntityManagerFactory("physixLab");
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static boolean isConnectedToDB() {
        boolean successfully = false;

        try {
            EntityManager entityManager = getEntityManagerFactory()
                    .createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.getTransaction().commit();
            entityManager.close();
            successfully = true;
        } catch (Exception e) {
            System.err.println(e);
        }
        return successfully;
    }
}