package de.hhu.rhinoshareapp.controller;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainpageController {

    @Autowired
    UserRepository users;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleRepository articleRepository;

/*    @GetMapping("/")
    public String loadMainPage(Model m, Principal p) {
        ActualUserChecker.checkActualUser(m, p, users);
        return "mainpage";
    }
*/
    @GetMapping("/")
    public String viewAll(Model model, Principal p){
        ActualUserChecker.checkActualUser(model, p, userRepository);
        Iterable<Article> articles= articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "Article/viewAll";
    }

}