package uk.gov.census.ffa.storage.utils.exceptions;

@SuppressWarnings("serial")
public class StorageInputStreamException extends RuntimeException {

  public StorageInputStreamException(String message, Exception e) {
    super(message, e);
  }

}
