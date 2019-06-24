package com.haritha.example.searchrest.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.haritha.example.searchrest.dao.SearchProperties;
import com.haritha.example.searchrest.model.SearchRequest;
import com.haritha.example.searchrest.model.SearchResponse;

@RestController
public class SearchController
{
    @Autowired
    private SearchProperties searchProperties;
    private static final HttpHeaders RESPONSE_HEADERS;
    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);
    static {
        LOG.debug("Entering static block to add ResponseHeaders..");
        RESPONSE_HEADERS = new HttpHeaders();
        RESPONSE_HEADERS.set("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
    }

    @PostMapping(path="/search",consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> getCount(@RequestBody SearchRequest searchRequest )
    {
        SearchResponse searchResponse=null;
        String paragraph=searchProperties.getParagraph();
        String[] WordsList = paragraph.split(" ");
        List<String> searchText = searchRequest.getSearchText();
        Map<String, Integer> map=new HashMap<>();
        if(null !=searchText && searchText.size()>0){
            for (String wrd:searchText) {
               for(String word:WordsList){
                    if(word.equals(wrd)) {
                        if(map.get(word)!=null){
                            map.put(word,map.get(word)+1);
                        }else{
                            map.put(word,1);
                        }
                    }
                }
               if(map.get(wrd)==null)
                   map.put(wrd,0);
            }
            searchResponse=new SearchResponse();
            List<Map<String, Integer>> counts=new ArrayList<>();

            for(String key:map.keySet()){
               Map<String,Integer> count= new HashMap<String,Integer>();
                count.put(key,map.get(key));
                counts.add(count);
            }

            searchResponse.setCounts(counts);
            return new ResponseEntity<>(searchResponse, RESPONSE_HEADERS, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No searching words found.", RESPONSE_HEADERS, HttpStatus.OK);
        }
    }
    @GetMapping(value = "/top/{number}", produces = "text/csv")
    public ResponseEntity<?> generateReport(@PathVariable(value = "number") int topCount) {
        LOG.debug("top count{}",topCount);

        String paragraph= searchProperties.getParagraph();
        String[] WordsList = paragraph.split(" ");
        Map<String,Integer> map=new HashMap<>();
        try {
            for (String word :WordsList) {
                if(map.get(word)!=null){
                    map.put(word,map.get(word)+1);

                }else{
                    map.put(word,1);
                }
            }
            File file = new File("report.csv");
            Set<Map.Entry<String, Integer>> set = map.entrySet();

            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
                    set);

            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

                @Override
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {

                    return o2.getValue().compareTo(o1.getValue());
                }

            });
            LOG.debug("top count list {}",list.subList(0, topCount));
            List<Map.Entry<String, Integer>> requiredList = list.subList(0, topCount);
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            String message = "";
            for (Map.Entry<String, Integer> mapdtails:requiredList) {

                message+=mapdtails.getKey()+" | "+mapdtails.getValue()+"\n";
            }
            writer.append(message);
            writer.close();


            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=report.csv")
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(new FileSystemResource(file));

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to generate report: report", ex);
        }
    }  

}
