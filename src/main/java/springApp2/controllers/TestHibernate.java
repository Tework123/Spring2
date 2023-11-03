package springApp2.controllers;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import springApp2.config.HibernateUtil;
import springApp2.models.Blog;


@Controller
@RequestMapping("/blog")
public class TestHibernate {

    @GetMapping
    public void blog() {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        session.beginTransaction();

        //Add new Employee object
//        Blog emp = new Blog();
//        emp.setEmail("demo-user@mail.com");
//        emp.setFirstName("demo");
//        emp.setLastName("user");

//        session.save(emp);

//        session.getTransaction().commit();
//        HibernateUtil.shutdown();
    }
}