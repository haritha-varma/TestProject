package com.haritha.example.searchrest.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class SearchResponse {

    private List<Map<String,Integer>> counts;

    @Override
    public String toString() {
        return "SearchResponse{" +
                "counts=" + counts +
                '}';
    }

    public List<Map<String, Integer>> getCounts() {
        return counts;
    }

    public void setCounts(List<Map<String, Integer>> counts) {
        this.counts = counts;
    }
}
