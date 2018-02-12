package hello;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {

    @RequestMapping("/")
    public String mainPage(){
        return "main";
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="search", defaultValue="World") String search, Model model) {
        List<String> list = null;
        try {
            list = FileTextSearcher.mainFunction(search);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        model.addAttribute("list", list);
        return "answer";

    }
}
