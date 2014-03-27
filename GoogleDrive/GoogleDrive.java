/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoogleDrive;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class GoogleDrive {

    private static final String CLIENT_ID = "892186241167-228o9c5afo7fqrnbciabv81eghdj63f5.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "BVuUu5FU8boxFTgkSMtpJwDK";
    private static final String REDIRECT_URI = "http://localhost:8084/xml2googledrive/xml2googledriveindex.jsp";

    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private GoogleAuthorizationCodeFlow flow;
    private Drive service;

    private String codeValidation = null;
    private String authorizationUrl = null;

    public GoogleDrive() {
        this.instantiateDependencies();
    }
    
    public void listFilesByUser(String user) throws IOException{
        for (File file : this.getListFiles()) {
            if(file.getOwnerNames().contains(user)){
                System.out.println(file.getId() + " - " + file.getTitle() + " - " + file.getMimeType() + " - parent: " + file.getParents().get(0).getId());
            }
        }
    }

    public void listAllFiles() throws IOException {
        for (File file : this.getListFiles()) {
                System.out.println("All: " + file.getTitle());
        }
    }

    public void uploadFile(String pathToFile, String fileTitle, String fileDescription, String fileMimeType) {
        try {
            this.service.files().insert(
                        this.prepareFile(fileTitle, fileDescription, fileMimeType),
                        this.newFileMediaContentTemplate(pathToFile, fileMimeType)
                ).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private ArrayList<File> getListFiles() throws IOException{
        Drive.Files.List list = this.service.files().list();
        FileList fileList = list.execute();
        ArrayList<File> arrayListFiles = (ArrayList) fileList.getItems();
        return arrayListFiles;
    }

    private FileContent newFileMediaContentTemplate(String pathToFile, String fileMimeType) {
        java.io.File fileContent = new java.io.File(pathToFile);
        FileContent mediaContent = new FileContent(fileMimeType, fileContent);
        return mediaContent;
    }

    public void setCodeValidation(String code) {
        this.codeValidation = code;
    }

    private void buildService(String codeValidation) {
        try {
            GoogleTokenResponse response = this.flow.newTokenRequest(codeValidation).setRedirectUri(REDIRECT_URI).execute();
            GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
            this.service = new Drive.Builder(this.httpTransport, this.jsonFactory, credential).build();
            //return service;
        } catch (IOException ex) {
            ex.printStackTrace();
            //return null;
        }
    }

    private void instantiateDependencies() {
        this.httpTransport = new NetHttpTransport();
        this.jsonFactory = new JacksonFactory();

        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                this.httpTransport, this.jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();
        this.authorizationUrl = this.flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }

    private File prepareFile(String title, String description, String mimeType) {
        // Insert a file
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);
        return body;
    }
    
    public void test() throws IOException {
        System.out.println("Please open the following URL in your browser then type the authorization code:");
        System.out.println("  " + this.authorizationUrl);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        this.codeValidation = br.readLine();
        this.buildService(this.codeValidation);

        //this.uploadFile("/Users/pablohpsilva/Desktop/document.txt", "My Document Example", "This is an example of description", "text/plain");
        this.listAllFiles();
        this.listFilesByUser("Admissions Morgan");
    }

    public static void main(String args[]) {
        GoogleDrive drive = new GoogleDrive();
        try {
            drive.test();
        } catch (Exception ex) {
            System.out.print("");
        }
    }

}
