package springApp2.config;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import springApp2.models.Blog;

import java.io.File;

public class HibernateUtil {
    @Getter
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        SessionFactory factory = new Configuration()
                .configure(new File("src/main/resources/hibernate.cfg.xml"))
                .addAnnotatedClass(Blog.class).buildSessionFactory();
        return factory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}