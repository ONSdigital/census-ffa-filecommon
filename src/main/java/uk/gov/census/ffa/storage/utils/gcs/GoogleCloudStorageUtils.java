package uk.gov.census.ffa.storage.utils.gcs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.api.gax.paging.Page;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import uk.gov.census.ffa.storage.utils.StorageFunctions;
import uk.gov.census.ffa.storage.utils.exceptions.SavingFileException;

@Component
public class GoogleCloudStorageUtils implements StorageFunctions {

  private static Storage storage = null;

  static {
    storage = StorageOptions.getDefaultInstance().getService();
  }
  
  @Override
  public InputStream getFileInputStream(URI uri) {
    Bucket bucket = retrieveBucket(uri);
    String path = uri.getRawPath().substring(1);
    Blob blob = bucket.get(path);
    
    ReadChannel reader = blob.reader();
    InputStream inputStream = Channels.newInputStream(reader);
    return inputStream;
  }

  @Override
  public List<URI> getFilenamesInFolder(URI uri) {
    Bucket bucket = retrieveBucket(uri);
    String path = uri.getRawPath().substring(1);

    Page<Blob> blobPage = bucket.list(Storage.BlobListOption.prefix(path));
    var uriList = new ArrayList<URI>();
    blobPage.getValues().forEach(b -> uriList.add(createURI(b)));
    return uriList;
  }

  @Override
  public boolean delete(URI location) {
    Bucket bucket = retrieveBucket(location);
    String path = location.getRawPath().substring(1);
    BlobId source = BlobId.of(bucket.getName(), path);
    return storage.delete(source);
  }

  @Override
  public String getProtocol() {
    return "gs";
  }

  @Override
  public void save(URI destination, InputStream sis) {
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      byte[] readBuf = new byte[4096];
      while (sis.available() > 0) {
        int bytesRead = sis.read(readBuf);
        os.write(readBuf, 0, bytesRead);
      }
      
      Bucket bucket = retrieveBucket(destination);
      String path = destination.getRawPath().substring(1);
      storage.create(
          BlobInfo
              .newBuilder(bucket.getName(), path)
              .setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))))
              .build(),
          os.toByteArray());
    } catch (IOException e) {
      throw new SavingFileException("Problem Saving File " + destination, e);
    }  }

  private URI createURI(BlobInfo bi) {
    return URI.create("gs://" + bi.getBucket() + "/" + bi.getName());
  }

  private Bucket retrieveBucket(URI uri) {
    String bucketName = uri.getHost();
    Bucket bucket = storage.get(bucketName);
    return bucket;
  }
  
}
