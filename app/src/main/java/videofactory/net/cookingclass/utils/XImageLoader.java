package videofactory.net.cookingclass.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by OhJungchae on 2016-10-21.
 */

public class XImageLoader {

    static Map<String, SoftReference<Bitmap>> bitmaps = new HashMap<>();

    static Executor executor;
    static Handler handler;
    static{
        executor= Executors.newFixedThreadPool(6);
        handler=new Handler(Looper.getMainLooper()){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);


                Bundle bundle=msg.getData();

                ImageView imageView=(ImageView) msg.obj;

                Bitmap bitmap=bundle.getParcelable("bitmap");

                imageView.setImageBitmap(bitmap);
            }


        };

    }
/**
 * 使用强引用
 */
//	public static void load(final ImageView imageView, final int width, final int height, final String uri) {
//		if (bitmaps.get(uri) == null) {
//
//			executor.execute(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					BitmapFactory.Options options = new Options();
//					options.inJustDecodeBounds = true;
//					BitmapFactory.decodeFile(uri, options);
//
//					int h = options.outHeight;
//					int w = options.outWidth;
//
//					int size = 1;
//
//					while (w > width || h > height) {
//						w = w / 2;
//						h = h / 2;
//						size*=2;
//					}
//
//					System.out.print("   ///size="+size);
//					options.inJustDecodeBounds = false;
//					options.inSampleSize = size;
//					options.inPreferredConfig= Bitmap.Config.RGB_565;
//					Bitmap bitmap=BitmapFactory.decodeFile(uri, options);
//					System.out.println("daxiao ="+bitmap.getAllocationByteCount());
//					System.out.println("size="+size+" ,,,bitmap width="+bitmap.getWidth()+" ..bmp height="+bitmap.getHeight());
//
//					bitmaps.put(uri, bitmap);
////					bitmaps.put(uri, new SoftReference(bitmap));
//					Message message=Message.obtain();
//
//					Bundle data=new Bundle();
//					data.putParcelable("bitmap", bitmap);
//					message.setData(data);
//					message.obj=imageView;
//					handler.sendMessage(message);
//				}
//			});
//
//
//
//
//		} else {
//			imageView.setImageBitmap(bitmaps.get(uri));
//		}
//	}


    /**
     * 使用软引用
     * @param imageView
     * @param width
     * @param height
     * @param uri
     */
    public static void load(final ImageView imageView, final int width, final int height, final String uri) {
        if (bitmaps.get(uri) == null
                ||bitmaps.get(uri).get()==null) {

            executor.execute(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(uri, options);
//
//                    int h = options.outHeight;
//                    int w = options.outWidth;
//
//                    int size = 1;
//
//                    while (w > width || h > height) {
//                        w = w / 2;
//                        h = h / 2;
//                        size*=2;
//                    }
//
//
//                    System.out.print("   ///size="+size);
//                    options.inJustDecodeBounds = false;
//                    options.inSampleSize = size;
//                    options.inPreferredConfig= Bitmap.Config.RGB_565;
//                    Bitmap bitmap=BitmapFactory.decodeFile(uri, options);
//                    System.out.println("daxiao ="+bitmap.getAllocationByteCount());
//                    System.out.println("size="+size+" ,,,bitmap width="+bitmap.getWidth()+" ..bmp height="+bitmap.getHeight());
                    //软引用

                    Bitmap bitmap = null;

                    HttpURLConnection conn = null;
                    try {
                        URL _url = new URL(uri);
                        conn = (HttpURLConnection) _url.openConnection();
                        conn.setConnectTimeout(10 * 1000); // 30 secs
                        conn.setReadTimeout(10 * 1000); // 30 secs
                        conn.setUseCaches(false);
                        conn.connect();

                        int nResCode = conn.getResponseCode();
                        if (nResCode != HttpURLConnection.HTTP_OK) {
                            String strResMsg = conn.getResponseMessage();
                            LOG.debug("Http Response error:%d (%s)", nResCode, strResMsg);
                            return ;
                        }

                        InputStream inputStream = conn.getInputStream();

                        // BitmapFactory.Options op = new BitmapFactory.Options();
                        // op.inDither = false;
                        // op.inPurgeable = true;
                        // bitmap = BitmapFactory.decodeStream(inputStream, null, op);

                        bitmap = BitmapFactory.decodeStream(inputStream);

                        if (bitmap == null) {
                            return ;
                        }

                        if (bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {

                            return ;
                        }

                        inputStream.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (conn != null) {
                            conn.disconnect();
                        }
                    }
//                    if (bitmaps.get(uri) == null ||bitmaps.get(uri).get()==null)
                    {
                        bitmaps.put(uri, new SoftReference(bitmap));
                        Message message = Message.obtain();

                        Bundle data = new Bundle();
                        data.putParcelable("bitmap", bitmap);
                        message.setData(data);
                        message.obj = imageView;
                        handler.sendMessage(message);
                    }
                }
            });




        } else {
            imageView.setImageBitmap(bitmaps.get(uri).get());
        }
    }

}
