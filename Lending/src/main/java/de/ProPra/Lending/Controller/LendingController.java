package de.ProPra.Lending.Controller;

import de.ProPra.Lending.Dataaccess.PostProccessor;
import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.LendingRepresentation;
import de.ProPra.Lending.Dataaccess.Repositories.PersonRepository;
import de.ProPra.Lending.Dataaccess.Repositories.RequestRepository;
import de.ProPra.Lending.Dataaccess.RequestRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.HashMap;

@Controller
public class LendingController {

    private LendingRepository lendings;
    private PersonRepository persons;
    private ArticleRepository articles;
    private RequestRepository requests;

    @Autowired
    public LendingController(LendingRepository lendings, PersonRepository persons, ArticleRepository articles, RequestRepository requests){
        this.lendings = lendings;
        this.articles = articles;
        this.persons = persons;
        this.requests = requests;
    }

    @GetMapping("/lendings/{lendID}")
        public String LendingPage(Model model, @PathVariable final long lendID) {
            LendingRepresentation filledLendings = new LendingRepresentation(lendings,persons,articles, requests);
            model.addAttribute("lendings", filledLendings.FillLendings(lendID));
            return "overviewLendings";
        }

    @GetMapping("/borrows/{borrowID}")
    public String BorrowPage(Model model, @PathVariable final long borrowID) {
        LendingRepresentation filledLendings = new LendingRepresentation(lendings,persons,articles,requests);
        model.addAttribute("lendings", filledLendings.FillBorrows(borrowID));
        return "overviewBorrows";
    }

    @GetMapping("/{id}")
    public String Overview(Model model, @PathVariable final long id) {
        RequestRepresentation filledRequests = new RequestRepresentation(persons, articles, requests, id);
        model.addAttribute("id", id);
        model.addAttribute("requests", filledRequests.FillRequest());
        return "overview";
    }

    @PostMapping("/{id}")
    public String PostOverview(Model model,@PathVariable final long id, @RequestBody String postBody) {
        System.out.println("ich gehe rein");
        RequestRepresentation filledRequests = new RequestRepresentation(persons, articles, requests, id);
        HashMap<String, String> postBodyParas = PostProccessor.SplitString(postBody);
        PostProccessor.CheckDecision(postBodyParas, lendings, articles, requests);
        model.addAttribute("id", id);
        model.addAttribute("requests", filledRequests.FillRequest());
        return "overview";
    }

    @GetMapping("/lendingRequest")
        public String LendingRequest(Model model, @RequestParam("requesterID") long requesterID, @RequestParam("articleID") long articleID){
        model.addAttribute("requesterID",requesterID);
        model.addAttribute("articleID", articleID);
        return "lendingRequest";
    }
    @PostMapping("/inquiry") //TODO: timewindow is important!
    public String inquiry(Model model, @RequestBody String postBody) {
        HashMap<String, String> postBodyParas = PostProccessor.SplitString(postBody);
        PostProccessor.CreateNewEntryRequest(postBodyParas, requests);
        model.addAttribute("id", postBodyParas.get("requesterID"));
        return "inquiry";
    }
}
