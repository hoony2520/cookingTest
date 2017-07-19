package videofactory.net.cookingclass.common;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import net.videofactory.planb.utils.LOG;

import java.io.File;

/**
 * Created by Utae on 2016-07-13.
 */
public abstract class AmazonS3Uploader {

    private Context context;
    private final File uploadFile ;
    private int mode;

    public static final int MODE_VIDEO_UPLOAD = 0;      //video
    public static final int MODE_PROFILE_IMG_UPLOAD = 1;        //profile
    public static final int MODE_THUMBNAIL_IMG_UPLOAD = 2;      //thumbnail
    public static final int MODE_COVER_IMG_UPLOAD = 3;          //cover

    private String s3FilePath;
    private String bucket;

    public AmazonS3Uploader(Context context, int mode, File uploadFile) {
        this.context = context;
        this.mode = mode;
        this.uploadFile = uploadFile;
    }

    public AmazonS3Uploader(Context context, int mode, String filePath) {
        this.context = context;
        this.mode = mode;
        this.uploadFile = new File(filePath);
    }

    public void uploadS3(){
        s3FilePath = Utilities.transTimeFormatFromTimeMillis(System.currentTimeMillis(),"yyyy/MM/dd/") + UserInfo.getUserAndroidId(context) + Long.toString(System.currentTimeMillis());
        LOG.debug(s3FilePath);
        switch (mode){
            case MODE_VIDEO_UPLOAD :
                bucket = "project-audi/video";      // 영상
                break;

            case MODE_PROFILE_IMG_UPLOAD:
                bucket = "project-audi/profile";    // 프로필 이미지
                break;

            case MODE_THUMBNAIL_IMG_UPLOAD:
                bucket = "project-audi/image";      //이미지 썸네일
                break;

            case MODE_COVER_IMG_UPLOAD:
                bucket = "project-audi/coverImg";   // 커버 이미지
                break;

        }



        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(

                context,
//                "us-east-1:e4dbf036-7fe6-4a50-9883-82b220f6cab4", // Identity Pool ID
//                Regions.US_EAST_1 // Region
                "us-west-2:0d56c178-cd80-4fde-9f20-9795752d7976",   // Identity Pool ID

                Regions.US_WEST_1   // Region
        );

        // Create an S3 client
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_WEST_1));

        TransferUtility transferUtility = new TransferUtility(s3, context);
//        uploadFile = new File("");
        LOG.debug(uploadFile);
        TransferObserver observer = transferUtility.upload(
                bucket,     /* The bucket to upload to */
                s3FilePath,    /* The key for the uploaded object */
                uploadFile        /* The file where the data to upload exists */
        );

        LOG.debug("url: "+observer.getAbsoluteFilePath());

        observer.setTransferListener(new TransferListener() {


            @Override
            public void onStateChanged(int id, TransferState state) {
                Utilities.logD("Test", "id : " + Integer.toString(id) + "////state : " + state.toString());
                if(state.toString().equals(TransferState.COMPLETED.toString())){
                    String s3Url = "https://d2w1dgw1e9awhj.cloudfront.net/";
                    switch (mode){
                        case MODE_PROFILE_IMG_UPLOAD :
                            s3Url += "profile/";
                            break;

                        case MODE_VIDEO_UPLOAD :
                            s3Url += "video/";
                            break;

                        case MODE_THUMBNAIL_IMG_UPLOAD:
                            s3Url += "image/";
                            break;

                        case MODE_COVER_IMG_UPLOAD:
                            s3Url += "coverimg/";
                            break;

                    }
                    s3Url += s3FilePath;
                    onUploadFinish(s3Url);

                    LOG.debug(s3Url);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Utilities.logD("Test", "id : " + Integer.toString(id) + "////bytesCurrent / bytesTotal : " + Long.toString(bytesCurrent) + " / " + Long.toString(bytesTotal));
            }

            @Override
            public void onError(int id, Exception ex) {
                Utilities.logD("Test", "id : " + Integer.toString(id) + "////Exception : " + ex.toString());
            }
        });
    }

    protected abstract void onUploadFinish(String s3Url);
}
