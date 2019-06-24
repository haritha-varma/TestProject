package com.haritha.example.searchrest.dao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@ConfigurationProperties(prefix = "search")
public class SearchProperties
{
    private String paragraph ;

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    @Override
    public String toString() {
        return "SearchProperties{}";
    }
}
