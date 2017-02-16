package dll;

/**
 * Created by kle on 2/15/2017.
 */

public class Record {
    public  int id;
    private  String folderName;
    private  String fileName;
    private String fileDate;
    private String fileSize;
    private String fullUrl;
    private int date;

    public Record(){}

    public Record(int id, String folderName, String fileName, String fileDate, String fileSize, String fullUrl, int date){
        this.id = id;
        this.folderName = folderName;
        this.fileName = fileName;
        this.fileDate = fileDate;
        this.fileSize = fileSize;
        this.fullUrl = fullUrl;
        this.date = date;
    }

    public void setID(int id ) {
        this.id = id;
    }
    public int getID(){
        return id;
    }

    public void setDate(int da){this.date = da;}
    public int getDate(){
        return date;
    }
    public void setFolderName(String s){this.folderName = s;}
    public String getFolderName(){
        return folderName;
    }

    public void setFileName(String s){this.fileName = s;}
    public String getFileName(){
        return fileName;
    }

    public void setFileDate(String value){this.fileDate = value;}
    public String getFileDate(){
        return fileDate;
    }

    public void setFileSize(String value){this.fileSize = value;}
    public String getFileSize(){
        return fileSize;
    }

    public void setFullUrl(String value){this.fullUrl = value;}
    public String getFullUrl(){
        return fullUrl;
    }


    @Override
    public String toString(){
        return id + "," + folderName + "," + fileName + "," + fileDate + "," + fileSize + "," + fullUrl + "," + date;
    }

}
