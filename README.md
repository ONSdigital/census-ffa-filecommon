# census-ffa-storage-utils

This library allows you to do run the same commands on Cloud Bucket Files and System Files commands.
Useful when wanting to run Acceptance Tests locally and run them in env which require Cloud Providers.
SpringBoot does have similar functionality with Resource, but has its limitations 

The commands currently available are in uk.gov.census.ffa.storage.utils.StorageFunctions
This provides functionality for Local File System and GoogleStore Buckets

Quick starts

Project requires Springboot, probably just Springframework will do
Add this dependency to Project
Add @ComponentScan({..."uk.gov.census.ffa.commonfile.utils"})
File locations in your properties files need to be in a valid uri format with either file:// or gs://

rough example to get list of files from a folder

properties file:

google:
example.folderLocation=gs://csv-bucket-test/testfiles/

file system:
example.folderLocation=file://${home}/testfiles/


  @Value("${example.folderLocation}")
  private URI folderLocation;

  List<URI> files = fsUtils.getFilenamesInFolder(fileLocation);
        