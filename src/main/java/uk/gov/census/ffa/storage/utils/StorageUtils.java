package uk.gov.census.ffa.storage.utils;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageUtils {
  @Autowired
  private StorageProtocolResolver storageProtocolResolver;
  
  public InputStream getFileInputStream(URI location) {
    StorageFunctions utils = storageProtocolResolver.resolve(location);
    return utils.getFileInputStream(location);
  }
  
  public List<URI> getFilenamesInFolder(URI location, String prefix){
    return getFilenamesInFolder(location)
        .stream().filter(u -> FilenameUtils.getBaseName(u.toString()).startsWith(prefix)).collect(Collectors.toList());
  }
  
  public List<URI> getFilenamesInFolder(URI location){
    StorageFunctions utils = storageProtocolResolver.resolve(location);
    return utils.getFilenamesInFolder(location);
  }

  public void move(URI source, URI destination) {
    StorageFunctions sUtils = storageProtocolResolver.resolve(source);
    InputStream sis = sUtils.getFileInputStream(source);
    
    StorageFunctions dUtils = storageProtocolResolver.resolve(destination);
    dUtils.save(destination, sis);
    sUtils.delete(source);
  }
  
  public boolean delete(URI location) {
    StorageFunctions utils = storageProtocolResolver.resolve(location);
    return utils.delete(location);
  }
}
