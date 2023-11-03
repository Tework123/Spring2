package springApp2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
//import springApp2.config.HibernateUtil;
//import springApp2.config.HibernateUtil;
import springApp2.config.HibernateUtil;
import springApp2.models.Blog;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();


            Blog blog = new Blog();
            blog.setFirstname("dan");
            blog.setEmail("email");
            blog.setLastname("ddd");

            session.beginTransaction();

            session.persist(blog);

            session.getTransaction().commit();
            session.close();
            System.out.println(blog);
//        int id = 6;
//            session.beginTransaction();

//        List<Blog> blogs = session.createQuery("from Blog where id = 5 ").getResultList();
//        System.out.println(blogs);

//            Blog blog = session.get(Blog.class, 5);
//            System.out.println(blog);
//            session.remove(blog);
//            blog.setLastname("HHEEELLO");
//            session.createQuery("delete Blog where id = 3").executeUpdate();

//            session.getTransaction().commit();
//            session.close();

        } finally {
//            HibernateUtil.shutdown();

        }

    }
}
