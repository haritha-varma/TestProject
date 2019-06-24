package com.haritha.example.searchrest.model;

import java.util.List;

public class SearchRequest {

    public SearchRequest() {

    }

    private List<String> searchText;

    public List<String> getSearchText() {
        return searchText;
    }

    public void setSearchText(List<String> searchText) {
        this.searchText = searchText;
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "searchText=" + searchText +
                '}';
    }
}
