package videofactory.net.cookingclass.common;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import net.videofactory.planb.R;
import net.videofactory.planb.utils.LOG;

import java.io.File;

/**
 * Created by Hoony on 2017-03-19.
 */

public class AmazonS3UploaderTest extends AsyncTask<Void, Integer, Void> {

    Context mContext = null;
    private AmazonS3 s3;
    File file = null;
    private String mode;
    private String content;
    private String s3FileName;

    public interface AsyncResponse{
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    private INotifyChange iNotifyChange;

    public AmazonS3UploaderTest(Context context, AmazonS3 s3, File file, String mode, String s3FileName, AsyncResponse delegate) {
        this.mContext = context;
        this.file = file;
        this.s3 = s3;
        this.mode = mode;
        this.delegate = delegate;
        this.s3FileName = s3FileName;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }

    /* 사전 준비 작업 */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public Void doInBackground(Void... voids) {

        try{
            uploadFile(file, mode);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /* 진행 상황 */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    /* doInBackground 끝나면 호출 됨 */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public void uploadFile(final File file, final String mode){
        String bucket = mContext.getResources().getString(R.string.amazon_aws_bucket_name);

        if(s3 != null){
            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,  s3FileName + file.getName(), file);
                putObjectRequest.setGeneralProgressListener(new com.amazonaws.event.ProgressListener() {

                    long currentByte = 0;
                    @Override
                    public void progressChanged(com.amazonaws.event.ProgressEvent progressEvent) {
                        currentByte += progressEvent.getBytesTransferred();

                        LOG.debug("currentByte: "+currentByte);
                        Intent intent = new Intent();
                        intent.setAction("FILE_UPLOAD");

                        if(mode.equals("addVideo")){
                            // 홈으로 이동 프로그래스바 생성.
                            intent.putExtra("totalByte", file.length());
                            intent.putExtra("currentByte", currentByte);
                            mContext.sendBroadcast(intent);

                            publishProgress((int)(currentByte * 100 /file.length()));       // onProgressUpdate함수 호출

                            iNotifyChange.notifyChange(currentByte, file.length());       // home탭 progressbar 업데이트용

                        }else{
                            mContext.stopService(intent);
                        }


                    }
                });

                s3.setRegion(Region.getRegion(Regions.US_WEST_1));
                s3.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucket, s3FileName+file.getName()));
                s3.putObject(putObjectRequest); // uploadFile
                this.delegate.processFinish("");
            }catch (Exception e){
                e.printStackTrace();
//                Toast.makeText(mContext, "Upload has been failed", Toast.LENGTH_SHORT).show();
            }finally {
                s3 = null;
            }
        }
    }
}
