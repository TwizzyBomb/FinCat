package hesh.zone.fincat.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ChatGptSort {



    private final String URL = "";

    public String sendMessage(String examples){
        StringBuilder sb = new StringBuilder();
        sb.append("Can you please sort this csv data into categories and subcategories using the following examples:");
        sb.append(examples);
        return "";
    }
}
