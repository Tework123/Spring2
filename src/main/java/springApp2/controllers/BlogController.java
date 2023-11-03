package springApp2.controllers;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springApp2.config.HibernateUtil;
import springApp2.models.Blog;


@Controller
public class BlogController {
    @Value("${db}")
    private String db;

    @GetMapping()
    public String index() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();

            Blog blog = session.get(Blog.class, 5);
            System.out.println(blog);


            session.getTransaction().commit();
            session.close();

        } finally {
            HibernateUtil.shutdown();

        }


        System.out.println(db);
        return "blog/index";

    }
}
