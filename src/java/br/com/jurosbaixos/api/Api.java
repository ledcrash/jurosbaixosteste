/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jurosbaixos.api;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mrfil
 */
public class Api {
    private static String getUrl(int type, String hash){
        switch(type){
            case 1:
                return "https://codetest.jurosbaixos.com.br/v1/fizzbuzz";
            case 2:
                return "https://codetest.jurosbaixos.com.br/v1/fizzbuzz/reset";
            case 3: 
                return "https://codetest.jurosbaixos.com.br/v1/fizzbuzz/" + hash;    
            case 4: 
                return "https://codetest.jurosbaixos.com.br/v1/fizzbuzz/" + hash + "/canihastreasure";
            default:
                return "";
        }
    }
    
    private static String apiKey = "PauloWillGetThisDoneSwiftly!";

    public static String fizzbuzz(){
        Client client = ClientBuilder.newClient();
        Invocation.Builder invocationBuilder = client.target(getUrl(1, "")).request().header("X-API-KEY", apiKey);
        Response response = invocationBuilder.get();
        String result = response.readEntity(String.class);     
        client.close();
        return result;
    }
    
    public static String reset(){
        Client client = ClientBuilder.newClient();
        Invocation.Builder invocationBuilder = client.target(getUrl(2, "")).request().header("X-API-KEY", apiKey);
        Response response = invocationBuilder.get();
        String result = response.readEntity(String.class);
        client.close();
        return result;
    }
    
    public static String delete(String hash){
        Client client = ClientBuilder.newClient();
        Invocation.Builder invocationBuilder = client.target(getUrl(3, hash)).request().header("X-API-KEY", apiKey);
        Response response = invocationBuilder.delete();
        String result = response.readEntity(String.class);
        client.close();
        return result;
    }
    
    public static String post(String hash, String data){
        Client client = ClientBuilder.newClient();
        Invocation.Builder invocationBuilder = client.target(getUrl(3, hash)).request().header("X-API-KEY", apiKey);
        Response response = invocationBuilder.post(Entity.json(data));
        String result = response.readEntity(String.class);
        int status = response.getStatus();
        client.close();
        return result;
    }
    
    public static String getMyPrize(String hash){
        String result;
        
        try{
            Client client = ClientBuilder.newClient();
            Invocation.Builder invocationBuilder = client.target(getUrl(4, hash)).request().header("X-API-KEY", apiKey);
            Response response = invocationBuilder.get();
            
            result = response.readEntity(String.class);
            client.close();
        }
        catch(Exception e){
            result = e.getLocalizedMessage();
        }
        
        return result;
    }
}
