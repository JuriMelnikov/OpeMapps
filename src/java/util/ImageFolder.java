/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ResourceBundle;

/**
 *
 * @author jvm
 */
public class ImageFolder {
     private final static ResourceBundle imageFolder = ResourceBundle.getBundle("properties.pathToImageFolder");
    public static String getImageFolder(String key){
        return imageFolder.getString(key);
    }
}
