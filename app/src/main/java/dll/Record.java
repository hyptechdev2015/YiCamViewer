package dll;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kle on 2/15/2017.
 */

public class Record implements BaseColumns {

    //private variables
    private  int id;
    private  String folderName;
    private  String fileName;
    private String fileDate;
    private String fileSize;
    private String fullUrl;
    private int date;
    private byte[] thumbnail;

    public Record(){}

    public Record(int id, String folderName, String fileName, String fileDate, String fileSize, String fullUrl, int date, byte[] image){
        this.id = id;
        this.folderName = folderName;
        this.fileName = fileName;
        this.fileDate = fileDate;
        this.fileSize = fileSize;
        this.fullUrl = fullUrl;
        this.date = date;
        this.thumbnail = image;
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

    public void setThumbnail(byte[] value){this.thumbnail = value;}
    public byte[] getThumbnail(){
        return thumbnail;
    }


    @Override
    public String toString(){
        String yesT="0";
        if(thumbnail != null)
            yesT = "1";

        return id + "," + folderName + "," + fileName + "," + fileDate + "," + fileSize + "," + fullUrl + "," + date+","+ yesT;
    }

}
