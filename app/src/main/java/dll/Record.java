package dll;

/**
 * Created by kle on 2/15/2017.
 */

public class Record {
    private  int id;
    private  String folderName;
    private  String fileName;
    private String fileDate;
    private String fileSize;
    private String fullUrl;
    private int date;

    public Record(int id, String folderName, String fileName, String fileDate, String fileSize, String fullUrl, int date){
        this.id = id;
        this.folderName = folderName;
        this.fileName = fileName;
        this.fileDate = fileDate;
        this.fileSize = fileSize;
        this.fullUrl = fullUrl;
        this.date = date;
    }

    public int getId(){
        return id;
    }

    public int getDate(){
        return date;
    }

    public String getFolderName(){
        return folderName;
    }

    public String getFileName(){
        return fileName;
    }

    public String getFileDate(){
        return fileDate;
    }

    public String getFileSize(){
        return fileSize;
    }
    public String getFullUrl(){
        return fullUrl;
    }


    @Override
    public String toString(){
        return id + "," + folderName + "," + fileName + "," + fileDate + "," + fileSize + "," + fullUrl + "," + date;
    }

}
