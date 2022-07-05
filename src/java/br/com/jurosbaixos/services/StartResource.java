/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package br.com.jurosbaixos.services;

import br.com.jurosbaixos.api.Api;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;

/**
 * REST Web Service
 *
 * @author mrfil
 */
@Path("start")
public class StartResource {

    @Context
    private UriInfo context;

    public StartResource() {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reset")
    public String reset() {
        Api.reset();
        return "{\"messagem\":\"OK\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        String response =  Api.fizzbuzz();
        
        if(response.contains("message") || response.contains("errors")){
            response =  Api.fizzbuzz();
        }
        else{
            
            JSONArray array = new JSONArray(response);
            String result = "[";
            for(int i=0; i < array.length(); i++){
                int number = array.optInt(i);
                if(number % 5 == 0 && number % 3 == 0){
                    //fizzbuzz
                    result += "\"fizzbuzz\",";
                }
                else if(number % 5 == 0){
                    //buzz
                    result += "\"buzz\",";
                }
                else if(number % 3 == 0){
                    //fzz
                    result += "\"fizz\",";
                }
                else{
                    //just the number
                    result += "\"" + number + "\",";
                }
            }
            
            result = result.substring(0, result.length() - 1);
            result += "]";
            
            String path = System.getProperty("jboss.home.url");
            File f = new File(path, "lista.txt");
            try {
                Writer out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
            } catch (UnsupportedEncodingException | FileNotFoundException ex) {
                Logger.getLogger(StartResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String hash = encode(result);
            String newAnswer = Api.post(hash, result);
            response += "\r\n" + newAnswer;
            
            String prize = Api.getMyPrize(hash);
            
            while(prize.contains("message")){
                Api.delete(hash);
                getJson();
                response += "\r\n" + result;
            }
            
            response += "\r\n" + prize;
        }
        
        return response;
    }
    
    private String encode(String data){
        String result = "";
        
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(data.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : encoded) sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
    
            result = sb.toString();
        }
        catch(NoSuchAlgorithmException nsae){
            System.out.println("Encode Error: " + nsae.getLocalizedMessage());
        }
        
        return result;
    }
}
