package uk.gov.census.ffa.storage.utils;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

public interface StorageFunctions {

  InputStream getFileInputStream(URI uri);

  List<URI> getFilenamesInFolder(URI uri);

  boolean delete(URI location);

  String getProtocol();

  void save(URI destination, InputStream sis);


}
