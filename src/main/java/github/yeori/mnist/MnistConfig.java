/*-
 * #%L
 * JMnistDB
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2016 yeori
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package github.yeori.mnist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import github.yeori.mnist.util.Util;

public class MnistConfig {

    final public static String ATT_PATH_TRAINING_LABEL = "path.training.label";
    final public static String ATT_PATH_TRAINING_IMG = "path.training.img";
    final public static String ATT_PATH_TEST_LABEL = "path.test.label";
    final public static String ATT_PATH_TEST_IMG = "path.test.img";

    final public static String ATT_OUT_DIR = "out.dir";
    final public static String ATT_OUT_MODE = "out.mode";
    
    final public static String ATT_OUT_TRAINING_PREFIX = "out.training.prefix";
    
    final public static String ATT_OUT_TEST_PREFIX = "out.test.prefix";
    
    private static List<String> KEYS = Arrays.asList(
            ATT_PATH_TRAINING_LABEL,
            ATT_PATH_TRAINING_IMG,
            ATT_PATH_TEST_LABEL,
            ATT_PATH_TEST_IMG,
            ATT_OUT_DIR,
            ATT_OUT_MODE,
            ATT_OUT_TRAINING_PREFIX,
            ATT_OUT_TEST_PREFIX
            );
    
    private Map<String, Object> configMap = new HashMap<>();
    
    public MnistConfig( String configFilePath) {
        
        File f= new File ( configFilePath);
        
        try( FileInputStream fis =new FileInputStream(f)) {
            load ( fis );
        } catch ( FileNotFoundException e ) {
            throw new MnistException("check the configuration file path: %s", f.getPath());
        } catch (IOException e) {
            throw new MnistException(e, "io exception occured");
        }
    }
    
    public MnistConfig(){}

    public MnistConfig(InputStream configSource) {
        try {
            load(configSource);
        } catch (IOException e) {
            throw new MnistException(e, "io exception from constructor input");
        }
    }

    private void load(InputStream in ) throws IOException {
        
        Scanner sc = new Scanner(in);
        
        String [] params ;
        while ( sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if ( isEmptyOrComment ( line )) {
                continue;
            }
            params = Arrays.stream(line.split("="))
                           .map(s -> s.trim())
                           .toArray(n -> new String[n]);
            
            if ( params.length != 2) {
                throw new MnistException("check 'key=value' format : %s", line );
            }
            
            if ( !KEYS.contains(params[0]) ) {
                throw new MnistException("unknown property %s", params[0]);
            }
            
            process ( params[0], params[1]);
        }
        
        // processing default if not exist
        if ( configMap.get(ATT_OUT_TRAINING_PREFIX) == null) {
            configMap.put(ATT_OUT_TRAINING_PREFIX, "tr");
        }
        
        if ( configMap.get(ATT_OUT_TEST_PREFIX) == null ) {
            configMap.put(ATT_OUT_TEST_PREFIX, "test");
            
        }
        
    }

    private void process(String key, String value) {
        switch (key) {
        case ATT_PATH_TRAINING_LABEL:
        case ATT_PATH_TRAINING_IMG:
        case ATT_PATH_TEST_LABEL:
        case ATT_PATH_TEST_IMG:
            setInputFile(key, value);
            break;
        case ATT_OUT_DIR :
            outputDir(key, value);
            break;
        case ATT_OUT_MODE :
            outmode(key, value);
        case ATT_OUT_TRAINING_PREFIX :
            value = Util.replaceIf ( value, "", "tr-");
            configMap.put( key, value );
            break;
        case ATT_OUT_TEST_PREFIX :
            value = Util.replaceIf ( value, "", "test-");
            configMap.put( key, value );
            break;
            
        default:
            throw new RuntimeException ( String.format("what is this? %s=%s", key, value));
        }
    }

    private boolean isEmptyOrComment(String line) {
        return line.length() == 0 || line.startsWith("#") ;
    }
    
    public void setTrainingLabel( String path ) {
        setInputFile(ATT_PATH_TRAINING_LABEL, path);
    }
    
    public void setTrainingImage ( String path ) {
        setInputFile(ATT_PATH_TRAINING_IMG, path);
    }
    
    public void setTestLabel ( String path ) {
        setInputFile(ATT_PATH_TRAINING_IMG, path);
    }
    
    public void setTestImage ( String path ) {
        setInputFile(ATT_PATH_TEST_IMG, path);
    }
    
    private void setInputFile ( String key, String path ) {
        Util.should_be_valid( String.format("empty for null path value: [%s]", path), path);
        path = path.trim();
        File f = new File ( path );
        Util.should_exist( String.format(  "no such file: '%s' for key '%s'", path, key), f );
        Util.should_be_file( String.format("not a file: '%s' for key : '%s'", path, key), f );
        configMap.put(key, f);
    }
    
    public void setOutputDir ( String outputDir ) {
        outputDir(ATT_OUT_DIR, outputDir);
    }
    
    private void outputDir ( String key, String dirPath ) {
        Util.should_not_null( String.format("null output path value: [%s]", dirPath), dirPath);
        
        dirPath = dirPath.trim();
        File outDir = new File ( dirPath );
        Util.should_exist( String.format(  "no such dir: '%s' for key '%s'", dirPath, key), outDir );
        Util.should_be_dir( String.format("not a directory: '%s' for key : '%s'", dirPath,key), outDir );
        configMap.put(key, outDir);
    }
    
    /**
     * 
     * @return one of "raw", "png", "jpg"
     */
    public String getOutMode() {
        return (String) configMap.get(ATT_OUT_MODE);
    }
    
    /**
     * should be one of "raw", "png", "jpg"
     * @param mode
     */
    public void setOutMode ( String mode) {
        outmode(ATT_OUT_MODE, mode);
    }
    
    private void outmode ( String key, String mode) {
        mode = mode.trim().toLowerCase();
        Util.should_be_oneOf(
                String.format("'%s' should be one of %s, but %s", key, Arrays.asList("", "raw", "png", "jpg"), mode),
                mode, "raw", "png", "jpg") ;
        if ( "".equals(mode) ) {
            mode = "raw";
        }
        configMap.put(key, mode);
    }
    
    public void setTrainingPrefix ( String prefix) {
        Util.should_not_null("null prefix not allowed", prefix);
        configMap.put(ATT_OUT_TRAINING_PREFIX, prefix);
    }
    public void setTestPrefix ( String prefix) {
        Util.should_not_null("null prefix not allowed", prefix);
        configMap.put(ATT_OUT_TEST_PREFIX, prefix);
    }

    public File getTrainingLabel() {
        return (File) configMap.get(ATT_PATH_TRAINING_LABEL);
    }
    
    public File getTrainingImage() {
        return (File) configMap.get(ATT_PATH_TRAINING_IMG);
    }

    public File getTestLabel() {
        return (File) configMap.get(ATT_PATH_TEST_LABEL);
    }

    public File getTestImage() {
        return (File) configMap.get(ATT_PATH_TEST_IMG);
    }

    public File getOutDir() {
        return (File) configMap.get(ATT_OUT_DIR);
    }

    public String getTrainingPrefix() {
        return (String) configMap.get(ATT_OUT_TRAINING_PREFIX);
    }
    
    public String getTestPrefix() {
        return (String) configMap.get(ATT_OUT_TEST_PREFIX);
    }

    
}
