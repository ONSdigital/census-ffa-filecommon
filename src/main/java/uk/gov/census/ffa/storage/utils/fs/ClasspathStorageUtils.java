package uk.gov.census.ffa.storage.utils.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import uk.gov.census.ffa.storage.utils.StorageFunctions;

@Component
public class ClasspathStorageUtils  implements StorageFunctions {

  @Autowired
  private FileSystemStorageUtils fsUtils;

  @Override
  public InputStream getFileInputStream(URI uri) {
    URI realUri = getRealUri(uri);
    return fsUtils.getFileInputStream(realUri);
  }

  @Override
  public List<URI> getFilenamesInFolder(URI uri) {
    URI realUri = getRealUri(uri);
    return fsUtils.getFilenamesInFolder(realUri);
  }

  @Override
  public boolean delete(URI location) {
    URI realUri = getRealUri(location);
    return fsUtils.delete(realUri);
  }

  @Override
  public String getProtocol() {
    return "classpath";
  }

  @Override
  public void save(URI destination, InputStream sis) {
    fsUtils.save(destination, sis);
  }

  
  private URI getRealUri(URI uri) {
    String path = uri.getPath().replace("classpath:", "");
    try {
      File file = new ClassPathResource(path).getFile();
      return file.toURI();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  
  
}
