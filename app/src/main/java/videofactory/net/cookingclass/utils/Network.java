package videofactory.net.cookingclass.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Class For Network Connect.
 * 
 * 占쌥듸옙占�占싣뤄옙 permission占쏙옙 Manifest占쏙옙 占쌩곤옙占쌔억옙 占싼댐옙.<br>
 * "android.permission.ACCESS_NETWORK_STATE"<br>
 * "android.permission.ACCESS_WIFI_STATE"<br>
 * "android.permission.INTERNET"<br>
 * 
 * @author jungchae
 */

public class Network {

    public static Drawable getImage(String _strUrl) throws Exception {
        LOG.debug(">> Network::getServerImage=[" + _strUrl + "]");
        Drawable drawable = null;

        HttpURLConnection conn = null;
        try {
            URL url = new URL(_strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10 * 1000); // 30 secs
            conn.setReadTimeout(10 * 1000); // 30 secs
            conn.setUseCaches(false);
            conn.setRequestProperty("Accept-Encoding", "Gzip"); // added for
                                                                // gingerbread

            conn.connect();

            int nResCode = conn.getResponseCode();
            if (nResCode != HttpURLConnection.HTTP_OK) {
                String strResMsg = conn.getResponseMessage();
                LOG.debug("Http Response error:%d (%s)", nResCode, strResMsg);
                return null;
            }

            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            LOG.debug("width=" + bitmap.getWidth());
            LOG.debug("height=" + bitmap.getHeight());
            if (bitmap.getWidth() == 0 || bitmap.getHeight() == 0)
                return null;

            drawable = new BitmapDrawable(bitmap);

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return drawable;
    }

    public static String getData(String _url, String _message) throws Exception {
        LOG.debug(">> Network::getData url=" + _url);
        String message = null;

        HttpURLConnection conn = null;
        URL url = new URL(_url);
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10 * 1000); // 30 secs
        conn.setReadTimeout(10 * 1000); // 30 secs
        if (_message != null && "".equals(_message) == false) {
            conn.setRequestMethod("POST");
        } else {
            conn.setRequestMethod("GET");
        }
        conn.setUseCaches(false);
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Accept_Language", "ko-kr,ko;q=0;en-us;q=0,en;q=0.3");

        // conn.setRequestProperty("Accept-Encoding", "Gzip");

        // send request body
        if (_message != null && "".equals(_message) == false) {
            conn.setDoOutput(true);
            conn.connect();

            OutputStream outStream = conn.getOutputStream();
            outStream.write(_message.getBytes());
            outStream.flush();
            outStream.close();
        } else {
            conn.connect();
        }

        int nResCode = conn.getResponseCode();
        LOG.debug("# nResCode="+nResCode);
        if (nResCode != HttpURLConnection.HTTP_OK) {

            String strResMsg = conn.getResponseMessage();
            // LOG.warning("Http Response error:%d (%s)", nResCode, strResMsg);
            return null;
        }

        InputStream inputStream = conn.getInputStream();
        ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
        byte[] byteBuf = new byte[64 * 1024];

        int nSize = conn.getContentLength();

        LOG.warning("Content-length is nSize=" + nSize);
//        if (nSize < 0) {
            LOG.warning("Content-length is invalid");

            int nLen = 0;

            while (true) {
                nLen = inputStream.read(byteBuf);
                if (nLen < 0) {
                    break;
                }
                byteArrayOutStream.write(byteBuf, 0, nLen);
            }
            byteArrayOutStream.flush();
//        } else {
//            int nLen = 0, nRead = 0, nTotal = nSize;
//
//            while (nRead < nTotal) {
//                nLen = inputStream.read(byteBuf);
//                nRead += nLen;
//                byteArrayOutStream.write(byteBuf, 0, nLen);
//            }
//            byteArrayOutStream.flush();
//        }

        message = byteArrayOutStream.toString();
        byteArrayOutStream.close();

        if (conn != null) {
            conn.disconnect();
        }

        return message;
    }

    public static String sendMediaLog(String _url, String _message) throws Exception {
        LOG.debug(">> Network::sendMediaLog url=" + _url);
        String message = null;

        HttpURLConnection conn = null;
        URL url = new URL(_url);
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10 * 1000); // 30 secs
        conn.setReadTimeout(10 * 1000); // 30 secs
        if (_message != null && "".equals(_message) == false) {
            conn.setRequestMethod("POST");
        } else {
            conn.setRequestMethod("GET");
        }


        conn.setUseCaches(false);
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Accept_Language", "ko-kr,ko;q=0;en-us;q=0,en;q=0.3");
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        // conn.setRequestProperty("Accept-Encoding", "Gzip");

        // send request body
        if (_message != null && "".equals(_message) == false) {
            conn.setDoOutput(true);
            conn.connect();

            OutputStream outStream = conn.getOutputStream();
            outStream.write(_message.getBytes());
            outStream.flush();
            outStream.close();
        } else {
            conn.connect();
        }

        int nResCode = conn.getResponseCode();
        LOG.debug("# nResCode="+nResCode);
        if (nResCode != HttpURLConnection.HTTP_OK) {

            String strResMsg = conn.getResponseMessage();
            // LOG.warning("Http Response error:%d (%s)", nResCode, strResMsg);
            return null;
        }

        InputStream inputStream = conn.getInputStream();
        ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
        byte[] byteBuf = new byte[64 * 1024];

        int nSize = conn.getContentLength();

        LOG.warning("Content-length is nSize=" + nSize);
//        if (nSize < 0)
//        {
            LOG.warning("Content-length is invalid");

            int nLen = 0;

            while (true) {
                nLen = inputStream.read(byteBuf);
                if (nLen < 0) {
                    break;
                }
                byteArrayOutStream.write(byteBuf, 0, nLen);
            }
            byteArrayOutStream.flush();
//        } else {
//            int nLen = 0, nRead = 0, nTotal = nSize;
//
//            while (nRead < nTotal) {
//                nLen = inputStream.read(byteBuf);
//                nRead += nLen;
//                byteArrayOutStream.write(byteBuf, 0, nLen);
//            }
//            byteArrayOutStream.flush();
//        }

        message = byteArrayOutStream.toString();
        byteArrayOutStream.close();

        if (conn != null) {
            conn.disconnect();
        }

        return message;
    }



    public static byte[] getBinary(String _url, String _message) {
        LOG.debug(">> Network::getData(String strUrl, String strRequestMsg)");
        byte[] message = null;

        HttpURLConnection conn = null;
        try {
            URL downUrl = new URL(_url);
            conn = (HttpURLConnection) downUrl.openConnection();
            conn.setConnectTimeout(10 * 1000); // 30 secs
            conn.setReadTimeout(10 * 1000); // 30 secs
            conn.setUseCaches(false);
            conn.setRequestProperty("Accept-Encoding", "Gzip");

            // send request body
            if (_message != null) {
                conn.setDoOutput(true);
                conn.connect();

                OutputStream outStream = conn.getOutputStream();
                outStream.write(_message.getBytes());
                outStream.flush();
                outStream.close();
            } else {
                conn.connect();
            }

            int nResCode = conn.getResponseCode();
            if (nResCode != HttpURLConnection.HTTP_OK) {
                String strResMsg = conn.getResponseMessage();
                // LOG.warning("Http Response error:%d (%s)", nResCode,
                // strResMsg);
                return null;
            }

            InputStream inputStream = conn.getInputStream();
            ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
            byte[] byteBuf = new byte[64 * 1024];

            int nSize = conn.getContentLength();

            LOG.warning("Content-length is nSize=" + nSize);

//            if (nSize < 0) {
                LOG.warning("Content-length is invalid");

                int nLen = 0;

                while (true) {
                    nLen = inputStream.read(byteBuf);
                    if (nLen < 0) {
                        break;
                    }
                    byteArrayOutStream.write(byteBuf, 0, nLen);
                }
                byteArrayOutStream.flush();
//            } else {
//                int nLen = 0, nRead = 0, nTotal = nSize;
//
//                while (nRead < nTotal) {
//                    nLen = inputStream.read(byteBuf);
//                    nRead += nLen;
//                    byteArrayOutStream.write(byteBuf, 0, nLen);
//                }
//                byteArrayOutStream.flush();
//            }

            message = byteArrayOutStream.toByteArray();
            byteArrayOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return message;
    }

    public static String getDataWithSSL(String _url, String _message) throws Exception {
        LOG.debug(">> Network::getServerData(String strUrl, String strRequestMsg)");
        String message = null;

        URL downUrl = new URL(_url);
        if (_url.toLowerCase().indexOf("https://") == 0) {
            HttpsURLConnection conn = null;

            conn = (HttpsURLConnection) downUrl.openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setUseCaches(false);
            conn.setRequestProperty("Accept-Encoding", "Gzip");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Accept_Language", "ko-kr,ko;q=0;en-us;q=0,en;q=0.3");

            // send request body
            if (_message != null) {
                conn.setDoOutput(true);
                conn.connect();

                OutputStream outStream = conn.getOutputStream();
                outStream.write(_message.getBytes());
                outStream.flush();
                outStream.close();
            } else {
                conn.connect();
            }

            int nResCode = conn.getResponseCode();
            if (nResCode != HttpsURLConnection.HTTP_OK) {
                String strResMsg = conn.getResponseMessage();
                // LOG.warning("Http Response error:%d (%s)", nResCode,
                // strResMsg);
                throw new Exception("" + nResCode);
            }

            InputStream inputStream = conn.getInputStream();
            ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
            byte[] byteBuf = new byte[64 * 1024];

            int nSize = conn.getContentLength();

            LOG.warning("Content-length is nSize=" + nSize);
//            if (nSize < 0) {
                LOG.warning("Content-length is invalid");

                int nLen = 0;

                while (true) {
                    nLen = inputStream.read(byteBuf);
                    if (nLen < 0) {
                        break;
                    }
                    byteArrayOutStream.write(byteBuf, 0, nLen);
                }
                byteArrayOutStream.flush();
//            } else {
//                int nLen = 0, nRead = 0, nTotal = nSize;
//
//                while (nRead < nTotal) {
//                    nLen = inputStream.read(byteBuf);
//                    nRead += nLen;
//                    byteArrayOutStream.write(byteBuf, 0, nLen);
//                }
//                byteArrayOutStream.flush();
//            }

            message = byteArrayOutStream.toString();
            byteArrayOutStream.close();
        } else {
            message = getData(_url, _message);
        }

        return message;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            input.close();
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
