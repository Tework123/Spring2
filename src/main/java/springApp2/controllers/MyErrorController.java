package springApp2.controllers;

import jakarta.servlet.RequestDispatcher;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // TODO: log error details here

        model.addAttribute("errorMessage",
                request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        model.addAttribute("errorStatus",
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        return "error";
    }
}