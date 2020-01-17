package uk.gov.census.ffa.storage.utils.exceptions;

@SuppressWarnings("serial")
public class InvalidStorageLocationException extends RuntimeException {

  public InvalidStorageLocationException(String folderLocation, Exception e) {
    super(folderLocation, e);
  }

}
