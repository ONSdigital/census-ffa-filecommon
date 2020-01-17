package uk.gov.census.ffa.storage.utils.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import uk.gov.census.ffa.storage.utils.StorageFunctions;
import uk.gov.census.ffa.storage.utils.exceptions.StorageInputStreamException;
import uk.gov.census.ffa.storage.utils.exceptions.SavingFileException;

@Component
public class FileSystemStorageUtils implements StorageFunctions {

  @Override
  public InputStream getFileInputStream(URI uri) {
    try {
      return new FileInputStream(new File(uri));
    } catch (FileNotFoundException e) {
      throw new StorageInputStreamException("Problem getting InputStream for " + uri, e);
    }
  }

  @Override
  public List<URI> getFilenamesInFolder(URI uri) {
    var file = new File(uri);
    var filenames = new ArrayList<URI>();

    if (file.isDirectory()) {
      File[] listFiles = file.listFiles();
      if (listFiles != null) {
        for (File f : listFiles) {
          filenames.add(f.toURI());
        }
      }
    }
    return filenames;
  }

  @Override
  public boolean delete(URI location) {
    return (new File(location)).delete();
  }

  @Override
  public String getProtocol() {
    return "file";
  }

  @Override
  public void save(URI destination, InputStream sis) {
    try {
      File targetFile = new File(destination);
      FileUtils.copyInputStreamToFile(sis, targetFile);
    } catch (IOException e) {
      throw new SavingFileException("Problem Saving File " + destination, e);
    }
  }
}
