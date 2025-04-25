package com.weeklyPlanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weeklyPlanner.dto.OfferDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class OfferController {

    @GetMapping("/search")
    public List<OfferDTO> searchOffers(@RequestParam String query) throws MalformedURLException {
        List<OfferDTO> offers = new ArrayList<>();

        try {
            URL url = new URL("https://api.sallinggroup.com/v1/product-suggestions/relevant-products?query=" + URLEncoder.encode(query, "UTF-8") + "&per_page=20" + "&page=5");
            String bearerToken = "SG_APIM_F869JRZ91X55CTGQRGKKN7Q8XQ0J3TD1ZCHWDMGC31746C4NE9GG";

            HttpURLConnection connection =  (HttpURLConnection)url.openConnection();

            //set the token as Authorization
            connection.setRequestProperty("Authorization", "Bearer SG_APIM_F869JRZ91X55CTGQRGKKN7Q8XQ0J3TD1ZCHWDMGC31746C4NE9GG");

            System.out.println(connection.getResponseCode());
            System.out.println("Navigating to URL: " + url);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println(content);

            SearchResultDto resultDto = new ObjectMapper().readValue(content.toString(), SearchResultDto.class);
            resultDto.getSuggestions().forEach(
                    suggestion -> offers.add(new OfferDTO(suggestion.getTitle(), suggestion.getPrice()))
            );

            System.out.println(content);

        } catch (Exception e ) {
            System.out.println(e);
        }
        return offers;
    }
}

class SearchResultDto {

    private List<ProductDto> suggestions;

    public List<ProductDto> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<ProductDto> suggestions) {
        this.suggestions = suggestions;
    }
}

class ProductDto {
    private String title;
    private String id;
    private String prod_id;
    private double price;
    private String description;
    private String link;
    private String img;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}


