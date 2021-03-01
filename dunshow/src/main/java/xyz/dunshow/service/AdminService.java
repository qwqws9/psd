package xyz.dunshow.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {


    public void getEmblem() {
        String defaultUrl = "http://dnfnow.xyz/emblem?emblem_search=%EA%B7%80%EA%B2%80%EC%82%AC";
        Document doc = null;
        // table id = emblemtable
        // #emblemtable > tbody > tr:nth-child(2) > td:nth-child(1) > button
        // #emblemtable > tbody > tr:nth-child(3) > td:nth-child(1) > button
        try {
            doc = Jsoup.connect(defaultUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
            System.out.println(doc.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
