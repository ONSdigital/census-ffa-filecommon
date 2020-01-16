package uk.gov.census.ffa.storage.utils.exceptions;

@SuppressWarnings("serial")
public class SavingFileException extends RuntimeException {

  public SavingFileException(String folderLocation, Exception e) {
    super(folderLocation, e);
  }

}
