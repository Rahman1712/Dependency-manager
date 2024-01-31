package com.ar.util;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipBoardCreation {
	public static String clipValueFromClipboard(){
        String dependencyPOMString = Clipboard.getSystemClipboard().getString();
        System.out.println(dependencyPOMString);
        return dependencyPOMString;
    }
   
   public static void clipValue(Dependency dep){
//       String dependency = dep.getDependency();
       String groupid = dep.getGroupId();
       String artifactid = dep.getArtifactId();
       String version = dep.getVersion(); 
       String scope = dep.getScope();
       String optional = dep.getOptional();
       String description = dep.getDescription();

       boolean versionBool; 
       boolean scopeBool ;
       boolean optionalBool;
       boolean descriptionBool;
       
        if(version == null) versionBool = false;
        else versionBool = version.matches(".*\\w.*");
        if(scope == null) scopeBool = false;
        else scopeBool =  scope.matches(".*\\w.*");
        if(optional == null) optionalBool = false;
        else optionalBool = optional.matches(".*\\w.*");
        if(description == null) descriptionBool = false;
        else descriptionBool = description.matches(".*\\w.*");
        
//        System.out.println(versionBool);
        
       StringBuilder plainBuffer = new StringBuilder();
       
       if(descriptionBool) plainBuffer.append("<!-- ").append(description).append(" -->");
       plainBuffer.append("\n");
       plainBuffer.append("<dependency>");
       plainBuffer.append("\n \t");
       plainBuffer.append("<groupId>").append(groupid).append("</groupId>");
       plainBuffer.append("\n \t");
       plainBuffer.append("<artifactId>").append(artifactid).append("</artifactId>");
       if(versionBool || scopeBool || optionalBool)plainBuffer.append("\n \t");
       else plainBuffer.append("\n");
       if(versionBool) { 
           plainBuffer.append("<version>").append(version).append("</version>");
           if(scopeBool || optionalBool) plainBuffer.append("\n \t") ;
           else plainBuffer.append("\n");
       }
       if(scopeBool) {
           plainBuffer.append("<scope>").append(scope).append("</scope>");
           if(optionalBool) plainBuffer.append("\n \t");
           else plainBuffer.append("\n");
       }
       if(optionalBool) {
           plainBuffer.append("<optional>").append(optional).append("</optional>");
           plainBuffer.append("\n");
       }
       plainBuffer.append("</dependency>");
       
       System.out.println(plainBuffer);
       
       ClipboardContent clipboardContent = new ClipboardContent();
       clipboardContent.putString(plainBuffer.toString());
       // set clipboard content
       Clipboard.getSystemClipboard().setContent(clipboardContent);
       
   }    
   public static String clipValueToTextArea(Dependency dep){
//       String dependency = dep.getDependency();
       String groupid = dep.getGroupId();
       String artifactid = dep.getArtifactId();
       String version = dep.getVersion(); 
       String scope = dep.getScope();
       String optional = dep.getOptional();
       String description = dep.getDescription();

       boolean versionBool; 
       boolean scopeBool ;
       boolean optionalBool;
       boolean descriptionBool;
       
        if(version == null) versionBool = false;
        else versionBool = version.matches(".*\\w.*");
        if(scope == null) scopeBool = false;
        else scopeBool =  scope.matches(".*\\w.*");
        if(optional == null) optionalBool = false;
        else optionalBool = optional.matches(".*\\w.*");
        if(description == null) descriptionBool = false;
        else descriptionBool = description.matches(".*\\w.*");
        
//        System.out.println(versionBool);
        
       StringBuilder plainBuffer = new StringBuilder();
       
       if(descriptionBool) plainBuffer.append("<!-- ").append(description).append(" -->");
       plainBuffer.append("\n");
       plainBuffer.append("<dependency>");
       plainBuffer.append("\n \t");
       plainBuffer.append("<groupId>").append(groupid).append("</groupId>");
       plainBuffer.append("\n \t");
       plainBuffer.append("<artifactId>").append(artifactid).append("</artifactId>");
       if(versionBool || scopeBool || optionalBool)plainBuffer.append("\n \t");
       else plainBuffer.append("\n");
       if(versionBool) { 
           plainBuffer.append("<version>").append(version).append("</version>");
           if(scopeBool || optionalBool) plainBuffer.append("\n \t") ;
           else plainBuffer.append("\n");
       }
       if(scopeBool) {
           plainBuffer.append("<scope>").append(scope).append("</scope>");
           if(optionalBool) plainBuffer.append("\n \t");
           else plainBuffer.append("\n");
       }
       if(optionalBool) {
           plainBuffer.append("<optional>").append(optional).append("</optional>");
           plainBuffer.append("\n");
       }
       plainBuffer.append("</dependency>");
       
      return plainBuffer.toString();
       
   }    
   /**
* ALL ARE SAME
* 
*  else{ descriptionBool = description.equals(" ") || description.equals("") ? false : true;}
*  else{ descriptionBool = description.matches(".*\\w.*") ? false : true;}
*  else{ descriptionBool = !description.matches(".*\\w.*") ? true : false;}
*  else{ descriptionBool = description.matches(".*\\w.*");}
*/
   
}